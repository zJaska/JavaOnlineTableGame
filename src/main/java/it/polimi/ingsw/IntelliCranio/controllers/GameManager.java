package it.polimi.ingsw.IntelliCranio.controllers;

import it.polimi.ingsw.IntelliCranio.controllers.action.Action;
import it.polimi.ingsw.IntelliCranio.controllers.action.ActionState;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.network.NetworkManagerI;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.network.SocketHandler;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import javafx.util.Pair;

import java.util.ArrayList;
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

            Packet startPacket = new Packet(currentPlayer.getLastAction(), null, new ArrayList<>());

            //Prende un pacchetto
            //Esegue l'azione
            //Risponde al player
            while (true) {
                //Todo: Cambiare col giusto modo di ricevere il pacchetto
                Packet packet = network.receive(game.getCurrentPlayer().getNickname());

                if(packet.getInstructionCode() != END_TURN) {
                    try {
                        action.execute(game, packet);

                        Packet ackPacket = new Packet(action.getActionCode(), ACK, new ArrayList<>());
                        //pacchetto GAME (?)

                        //Send del pacchetto

                    } catch (InvalidArgumentsException e) {

                        ArrayList<Object> errorArgs = new ArrayList<>();
                        errorArgs.add(e.getErrorMessage());

                        Packet errorPacket = new Packet(COMMUNICATION, e.getCode(), errorArgs);

                        //Send del pacchetto
                    }
                }else {

                    //Ending operations
                    Packet idlePacket = new Packet(IDLE, null, new ArrayList<>());

                    //Send

                    //Cambio turno
                    game.changeTurn();

                    break;

                }
            }
        }

    }
}
