package it.polimi.ingsw.IntelliCranio.controllers;

import it.polimi.ingsw.IntelliCranio.controllers.action.Action;
import it.polimi.ingsw.IntelliCranio.controllers.action.ActionState;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.network.NetworkManagerI;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.network.SocketHandler;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.Net;
import javafx.util.Pair;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;

public class GameManager implements Runnable {

    private Game game;

    private Action action;
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

        while (true) {

            //Setup turno

            Player currentPlayer = game.getCurrentPlayer();

            network.sendAll(new Packet(COMMUNICATION, null, new ArrayList<>(Arrays.asList("---> It's " + currentPlayer.getNickname() + "'s turn"))));

            Packet startPacket = new Packet(currentPlayer.getLastAction(), null, new ArrayList<>());
            network.send(currentPlayer.getNickname(), startPacket);

            boolean hurryUp = false;

            while (true) {

                // Check if the turn has changed
                if (game.getCurrentPlayer() != currentPlayer)
                    break;

                // Update game objects in all clients, to ensure consistency of data and to make the game
                // people see changes in others' turns
                network.sendAll(new Packet(GAME, null, new ArrayList<>(Arrays.asList(game))));

                Packet packet = null;

                // Receive the packet
                try {
                    packet = network.receive(game.getCurrentPlayer().getNickname());
                } catch (SocketTimeoutException e) {

                    if (hurryUp) {
                        network.send(currentPlayer.getNickname(), new Packet(COMMUNICATION, null, new ArrayList<Object>(Arrays.asList(Net.TIMEOUT_MSG))));
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

                        network.send(currentPlayer.getNickname(), new Packet(action.getActionCode(), ACK, new ArrayList<>()));

                    } catch (InvalidArgumentsException e) {

                        ArrayList<Object> errorArgs = new ArrayList<>();
                        errorArgs.add(e.getErrorMessage());

                        network.send(currentPlayer.getNickname(), new Packet(COMMUNICATION, e.getCode(), errorArgs));
                    }
                } else {

                    //Ending turn operations

                    network.send(currentPlayer.getNickname(), new Packet(IDLE, null, null));
                    game.changeTurn();
                }
            }
        }

    }
}
