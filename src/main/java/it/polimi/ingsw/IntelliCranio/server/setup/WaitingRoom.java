package it.polimi.ingsw.IntelliCranio.server.setup;

import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.network.SocketHandler;
import javafx.util.Pair;

import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.util.Lists.toList;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.COMMUNICATION;

public class WaitingRoom {
    private int size;
    private ArrayList<Pair<String,SocketHandler>> players = new ArrayList<>();
    private int sleepTime = 1000;

    private static int numLobbies = 0;
    public static int getNumLobbies() {
        return numLobbies;
    }

    public WaitingRoom(int size, Pair<String,SocketHandler> firstPlayer) {
        this.size = size;
        players.add(firstPlayer);
        firstPlayer.getValue().send(new Packet(COMMUNICATION, null, toList(new String[] {"Added to a lobby, waiting for other players..."})));
    }

    public void run() {
        numLobbies++;

        while (players.size() < size) {
            ClientHandler.acquirePlayerReady();
            Pair<String,SocketHandler> newPlayer = ClientHandler.popWaitingPlayer();

            players.add(newPlayer);
            newPlayer.getValue().send(new Packet(COMMUNICATION, null, toList(new String[] {"Added to a lobby, waiting for other players..."})));
        }

        numLobbies--;

        startGame();
    }

    public void startGame() {
        System.out.print("Starting game with: ");
        players.forEach( x -> { System.out.print(x.getKey() + " "); } );
        System.out.println();

        // Telling clients the game is starting

        players.forEach(x -> {
            x.getValue().send(new Packet(COMMUNICATION, null, toList(new String[] {"Starting game..."})));
        });

        // Create and start the gameManager
    }
}