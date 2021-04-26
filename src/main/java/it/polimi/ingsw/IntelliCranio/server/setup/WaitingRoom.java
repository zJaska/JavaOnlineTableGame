package it.polimi.ingsw.IntelliCranio.server.setup;

import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.network.SocketHandler;
import javafx.util.Pair;

import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.Utility.toList;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.COMMUNICATION;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.ACK;

public class WaitingRoom {
    private int size;
    private ArrayList<Pair<String,SocketHandler>> players = new ArrayList<>();
    private int sleepTime = 1000;

    public WaitingRoom(int size, Pair<String,SocketHandler> firstPlayer) {
        this.size = size;
        players.add(firstPlayer);
    }

    public void run() {
        while (players.size() < size) {
            Pair<String,SocketHandler> newPlayer = ClientHandler.popWaitingPlayer();
            if (newPlayer == null) {
                try { Thread.sleep(sleepTime); }
                catch (InterruptedException e) {  }
            } else
                players.add(newPlayer);
        }

        startGame();
    }

    public void startGame() {
        System.out.print("Starting game with: ");
        players.forEach( x -> { System.out.print(x.getKey() + " "); } );
        System.out.println();

        players.forEach(x -> {
            x.getValue().send(new Packet(COMMUNICATION, null, toList(new String[] {"Starting game..."})));
        });

        // Create and start the gameManager
    }
}
