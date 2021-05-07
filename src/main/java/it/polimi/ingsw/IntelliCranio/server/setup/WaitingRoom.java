package it.polimi.ingsw.IntelliCranio.server.setup;

import it.polimi.ingsw.IntelliCranio.controllers.GameManager;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.network.SocketHandler;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

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
        firstPlayer.getValue().send(new Packet(COMMUNICATION, null, new ArrayList<>(Arrays.asList("Added to a lobby, waiting for other players..."))));
    }

    public void run() {
        numLobbies++;

        while (players.size() < size) {
            ClientHandler.acquirePlayerReady();
            Pair<String,SocketHandler> newPlayer = ClientHandler.popWaitingPlayer();

            players.add(newPlayer);
            newPlayer.getValue().send(new Packet(COMMUNICATION, null, new ArrayList<>(Arrays.asList("Added to a lobby, waiting for other players..."))));
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
            x.getValue().send(new Packet(COMMUNICATION, null, new ArrayList<>(Arrays.asList("Starting game..."))));
        });

        ClientHandler.removePlayerNames(players.stream().map(Pair::getKey).collect(Collectors.toList()));
        MainServer.startManager(null, players);
    }
}
