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
import java.util.Collections;
import java.util.Dictionary;
import java.util.stream.Collectors;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;

public class GameManager implements Runnable {

    private Game game;

    private Action action = new Action();
    private boolean newGame;

    private NetworkManagerI network;


    public GameManager(boolean newGame, ArrayList<Pair<String, SocketHandler>> playerConnections) {
        this.newGame = newGame;

        ArrayList<String> nicknames = new ArrayList<>();
        nicknames.addAll(playerConnections.stream().map(Pair::getKey).collect(Collectors.toList()));

        game = new Game(nicknames);

        network = new SocketManager(playerConnections);
    }

    /*Idea: Static because can be called anytime in the game without reference to this object
    Interrupt all the inputs from client and display the results and the winner.
    Disconnect from the clients
    Clear the backup memory from disk
    Terminate the execution of this thread
     */
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

        //Game lifecycle
        while (true) {

            //region Setup turn
            Packet gamePacket = new Packet(GAME, null, new ArrayList<>(Arrays.asList(game)));
            network.sendAll(gamePacket);

            Player currentPlayer = game.getCurrentPlayer();

            String message = "---> It's " + currentPlayer.getNickname() + "'s turn";
            network.sendAll(new Packet(COMMUNICATION, null, new ArrayList<>(Arrays.asList(message))));

            //Force client to set the correct scene
            Packet startPacket = new Packet(currentPlayer.getLastAction(), null, new ArrayList<>());
            network.send(currentPlayer.getNickname(), startPacket);

            action.restoreState(currentPlayer.getLastAction());

            //endregion

            boolean hurryUp = false;

            // Turn cycle
            while (true) {

                // Check if the turn has changed
                if (!game.getCurrentPlayer().equals(currentPlayer))
                    break;

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
                    } else {
                        Packet sendPacket = new Packet(COMMUNICATION, null, new ArrayList<>(Arrays.asList(Net.HURRYUP_MSG)));
                        network.send(currentPlayer.getNickname(), sendPacket);
                        hurryUp = true;
                    }

                } catch (IOException e) {
                    game.changeTurn(); //Change turn upon disconnection
                }

                // If no packet has been received, for any reason, don't do the action
                if (packet == null)
                    continue; //Goto Turn cycle

                hurryUp = false;

                // Do the action
                if(packet.getInstructionCode() != END_TURN) {
                    try {
                        action.execute(game, packet); //Execute the content of the packet received

                        String ackMessage = "Action Accepted";
                        Packet ackMessagePacket = new Packet(COMMUNICATION, null, new ArrayList<>(Arrays.asList(ackMessage)));
                        network.send(currentPlayer.getNickname(), ackMessagePacket);

                        // Send next action to the player
                        if (!game.endTurn)
                            network.send(currentPlayer.getNickname(), new Packet(action.getActionCode(), ACK, new ArrayList<>()));

                        //Send updated game
                        gamePacket = new Packet(GAME, null, new ArrayList<>(Arrays.asList(game)));
                        network.sendAll(gamePacket);

                    } catch (InvalidArgumentsException e) {

                        ArrayList<Object> errorArgs = new ArrayList<>();
                        errorArgs.add(e.getErrorMessage());

                        network.send(currentPlayer.getNickname(), new Packet(COMMUNICATION, e.getCode(), errorArgs));
                        //Resend action to reset client scene
                        network.send(currentPlayer.getNickname(), new Packet(action.getActionCode(), ACK, new ArrayList<>()));

                    }
                } else {
                    //Player decided to end its turn
                    endTurn(currentPlayer);
                }

                //Check if action was an automatic ending turn
                if(game.endTurn)
                    endTurn(currentPlayer);

            }
        }

    }

    private void endTurn (Player currentPlayer) {
        network.send(currentPlayer.getNickname(), new Packet(IDLE, null, null));
        game.changeTurn();
    }
}
