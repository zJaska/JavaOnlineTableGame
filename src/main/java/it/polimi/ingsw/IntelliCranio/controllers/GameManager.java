package it.polimi.ingsw.IntelliCranio.controllers;

import it.polimi.ingsw.IntelliCranio.controllers.action.*;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.network.*;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.server.setup.MainServer;
import it.polimi.ingsw.IntelliCranio.util.Net;
import it.polimi.ingsw.IntelliCranio.util.Save;
import javafx.util.Pair;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static it.polimi.ingsw.IntelliCranio.util.Net.ACK_MSG;

public class GameManager implements Runnable {

    private Game game;
    private Action action = new Action();

    private NetworkManagerI network;
    private ConcurrentHashMap<String, Boolean> onlinePlayers = new ConcurrentHashMap<>();

    public GameManager(UUID uuid, ArrayList<Pair<String, SocketHandler>> onlinePlayerConnections) {
        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.addAll(onlinePlayerConnections.stream().map(Pair::getKey).collect(Collectors.toList()));

        network = new SocketManager(onlinePlayerConnections);

        if (uuid == null) {
            game = new Game(nicknames);
            onlinePlayerConnections.forEach(pair -> onlinePlayers.put(pair.getKey(), true));
        }
        else {
            game = Save.loadGame(uuid);
            game.getPlayers().forEach(player -> onlinePlayers.put(player.getNickname(), false));
            game.endTurn(false);
        }
    }

    public void reconnectPlayer(String nickname, SocketHandler socketHandler) {
        network.connect(nickname, socketHandler);

        network.send(nickname, new Packet(GAME, null, new ArrayList<>(Arrays.asList(game))));
        network.send(nickname, new Packet(COMMUNICATION, null, new ArrayList<>(Arrays.asList("Joining the running game..."))));
        network.send(nickname, new Packet(IDLE, null, null));

        onlinePlayers.put(nickname, true);
    }

    public boolean isOnline(String nickname) {
        return onlinePlayers.get(nickname);
    }

    public UUID getUUID() { return game.getUuid(); }

    public void run() {
        playGame();
        endGame();
    }

    private void playGame() {

        Thread checkDisconnected = new Thread(() -> {
            while (true) {
                try { Thread.sleep(2000); } catch (Exception e) {}
                onlinePlayers.keySet().forEach(nick -> {
                    if (onlinePlayers.get(nick) && PingingDevice.isDisconnected(((SocketManager)network).getSocketHandler(nick)))
                        onlinePlayers.put(nick, false);
                });
            }
        });
        checkDisconnected.start();

        //Game lifecycle
        while (true) {

            while (onlinePlayers.values().stream().filter(bool -> bool).count() <= 1 && !game.isSinglePlayer())
                try { Thread.sleep(1000); } catch (Exception e) {}
            while (onlinePlayers.values().stream().filter(bool -> bool).count() <= 0 && game.isSinglePlayer())
                try { Thread.sleep(1000); } catch (Exception e) {}

            //Check for game ended and first player reached
            if(game.isGameEnded() && game.getCurrentPlayerIndex() == 0)
                break; //Exit from main game loop and show results

            if (!onlinePlayers.get(game.getCurrentPlayer().getNickname())) {
                game.changeTurn();
                continue;
            }

            //region Setup turn
            Packet gamePacket = new Packet(GAME, null, new ArrayList<>(Arrays.asList(game)));
            network.sendAll(gamePacket);

            network.sendAll(new Packet(IDLE, null,null));

            Player currentPlayer = game.getCurrentPlayer();

            String message = "It's " + currentPlayer.getNickname() + "'s turn";
            network.sendAll(new Packet(COMMUNICATION, null, new ArrayList<>(Arrays.asList(message))));

            //Force client to set the correct scene
            action.restoreState(currentPlayer.getLastAction());

            network.send(currentPlayer.getNickname(), new Packet(action.getActionCode(), null, null));

            currentPlayer.hasPlayed = false;

            //endregion

            boolean hurryUp = false;

            // Turn cycle
            while (true) {

                // Check if the turn has changed
                if(game.isTurnEnded()) {
                    endTurn(currentPlayer);
                    break;
                }


                // Update game objects in all clients, to ensure consistency of data and to make the game
                // people see changes in others' turns


                Packet packet = null;

                // Receive the packet
                try {
                    packet = network.receive(game.getCurrentPlayer().getNickname());
                } catch (SocketTimeoutException e) {

                    if (hurryUp) {
                        Packet sendPacket = new Packet(COMMUNICATION, null, new ArrayList<>(Arrays.asList(Net.TIMEOUT_MSG)));
                        network.send(currentPlayer.getNickname(), sendPacket);
                        network.disconnect(currentPlayer.getNickname());
                        game.changeTurn();
                        onlinePlayers.put(currentPlayer.getNickname(), false);
                    } else {
                        Packet sendPacket = new Packet(COMMUNICATION, null, new ArrayList<>(Arrays.asList(Net.HURRYUP_MSG)));
                        network.send(currentPlayer.getNickname(), sendPacket);
                        hurryUp = true;
                    }

                } catch (IOException e) {
                    game.endTurn(true); //Change turn upon disconnection
                    onlinePlayers.put(currentPlayer.getNickname(), false);
                }

                // If no packet has been received, for any reason, don't do the action
                if (packet == null)
                    continue; //Goto Turn cycle

                hurryUp = false;

                // Do the action
                try {
                    action.execute(game, packet); //Execute the content of the packet received

                    //Send updated game
                    gamePacket = new Packet(GAME, null, new ArrayList<>(Arrays.asList(game)));
                    network.sendAll(gamePacket);

                    Packet ackMessagePacket = new Packet(COMMUNICATION, null, new ArrayList<>(Arrays.asList(ACK_MSG)));
                    network.send(currentPlayer.getNickname(), ackMessagePacket);

                    // Send next action to the player
                    if (!game.isTurnEnded())
                        network.send(currentPlayer.getNickname(), new Packet(action.getActionCode(), ACK, new ArrayList<>()));

                } catch (InvalidArgumentsException e) {

                    ArrayList<Object> errorArgs = new ArrayList<>();
                    errorArgs.add(e.getErrorMessage());

                    network.send(currentPlayer.getNickname(), new Packet(COMMUNICATION, e.getCode(), errorArgs));
                    //Resend action to reset client scene
                    network.send(currentPlayer.getNickname(), new Packet(action.getActionCode(), null, new ArrayList<>()));

                }

            }
        }

        checkDisconnected.stop();
    }

    private void endTurn (Player currentPlayer) {
        network.send(currentPlayer.getNickname(), new Packet(IDLE, null, null));
        game.changeTurn();
    }


    private void endGame() {
        network.sendAll(new Packet(GAME_ENDED, null, null));

        //Delete game data
        MainServer.forgetManager(getUUID());
    }

}
