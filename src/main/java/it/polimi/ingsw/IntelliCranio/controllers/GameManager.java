package it.polimi.ingsw.IntelliCranio.controllers;

import it.polimi.ingsw.IntelliCranio.controllers.action.*;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.network.NetworkManagerI;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.network.SocketHandler;
import it.polimi.ingsw.IntelliCranio.network.SocketManager;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.Net;
import javafx.util.Pair;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.stream.Collectors;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;

public class GameManager implements Runnable {

    private Game game;

    private Action action = new Action();
    private boolean newGame;

    private NetworkManagerI network;

    /*Idea: Static because can be called anytime in the game without reference to this object
    Interrupt all the inputs from client and display the results and the winner.
    Disconnect from the clients
    Clear the backup memory from disk
    Terminate the execution of this thread
     */

    public GameManager(boolean newGame, ArrayList<Pair<String, SocketHandler>> playerConnections) {
        this.newGame = newGame;

        game = new Game(playerConnections.stream().map(x -> x.getKey()).collect(Collectors.toCollection(ArrayList::new)));

        network = new SocketManager(playerConnections);
    }

    public static void endingGame() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void run() {
        playGame();
    }

    private void gameSetup() {

    }

    private void playGame() {

        network.sendAll(new Packet(GAME, null, new ArrayList<>(Arrays.asList(game))));

        while (true) {

            //Setup turno

            Player currentPlayer = game.getCurrentPlayer();

            network.sendAll(new Packet(COMMUNICATION, null, new ArrayList<>(Arrays.asList("---> It's " + currentPlayer.getNickname() + "'s turn"))));

            Packet startPacket = new Packet(currentPlayer.getLastAction(), null, new ArrayList<>());
            network.send(currentPlayer.getNickname(), startPacket);

            // Set the action state based on the player's last action
            ActionState state = new Default_ActionState(action);
            if (currentPlayer.getLastAction() == DISCARD_INIT_LEAD)
                state = new DiscardInitLeaders_ActionState(action);
            if (currentPlayer.getLastAction() == CHOOSE_INIT_RES)
                state = new ChooseInitResources_ActionState(action);

            action.setActionState(state, currentPlayer.getLastAction());

            boolean hurryUp = false;

            // Turn cycle
            while (true) {

                // Check if the turn has changed
                if (game.getCurrentPlayer() != currentPlayer)
                    break;

                // Update game objects in all clients, to ensure consistency of data and to make the game
                // people see changes in others' turns


                Packet packet = null;

                // Receive the packet
                try {
                    packet = network.receive(game.getCurrentPlayer().getNickname());
                } catch (SocketTimeoutException e) {

                    if (hurryUp) {
                        network.send(currentPlayer.getNickname(), new Packet(COMMUNICATION, null, new ArrayList<Object>(Arrays.asList(Net.TIMEOUT_MSG))));
                        network.disconnect(currentPlayer.getNickname());
                        game.changeTurn();
                    } else {
                        network.send(currentPlayer.getNickname(), new Packet(COMMUNICATION, null, new ArrayList<Object>(Arrays.asList(Net.HURRYUP_MSG))));
                        hurryUp = true;
                    }

                } catch (IOException e) {
                    game.changeTurn();
                }

                // If no packet has been received, for any reaason, don't do the action
                if (packet == null)
                    continue;

                hurryUp = false;

                // Do the action
                if(packet.getInstructionCode() != END_TURN) {
                    try {
                        action.execute(game, packet);

                        if (action.getActionCode() == END_TURN) {
                            endTurn(currentPlayer);
                        } else
                            network.send(currentPlayer.getNickname(), new Packet(action.getActionCode(), ACK, new ArrayList<>()));

                    } catch (InvalidArgumentsException e) {

                        ArrayList<Object> errorArgs = new ArrayList<>();
                        errorArgs.add(e.getErrorMessage());

                        network.send(currentPlayer.getNickname(), new Packet(COMMUNICATION, e.getCode(), errorArgs));
                    }

                } else {
                   endTurn(currentPlayer);
                }

                network.sendAll(new Packet(GAME, null, new ArrayList<>(Arrays.asList(game))));
            }
        }

    }

    private void endTurn (Player currentPlayer) {
        network.send(currentPlayer.getNickname(), new Packet(IDLE, null, null));
        game.changeTurn();
    }
}
