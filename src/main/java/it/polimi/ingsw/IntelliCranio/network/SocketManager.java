package it.polimi.ingsw.IntelliCranio.network;

import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

public class SocketManager implements NetworkManagerI {

    private TreeMap<String, SocketHandler> players;

    /**
     * The constructor trasforms 'players' in a dictionary.
     * This class lets you call SocketHandler methods for the given player indicated in String 'name'.
     *
     * @param players The list of pairs 'String,SocketHandler' used in waitingRoom
     */

    public SocketManager(ArrayList<Pair<String,SocketHandler>> players) {
        players.forEach(x -> {
            this.players.put(x.getKey(),x.getValue());
        });
    }

    public void send(String name, Packet packet) {
        try {
            players.get(name).send(packet);
        } catch (NullPointerException e) {
            System.out.println("Cannot send to " + name);
        }

    }

    public Packet receive(String name) {
        try {
            return players.get(name).receive();
        } catch (NullPointerException e) {
            System.out.println("Cannot receive from " + name);
        } catch (IOException e) {
            System.out.println("Player that encountered the exception: " + name);
        }

        return null;
    }

    /**
     * Flushes the buffers of each player.
     */
    public void clear() {

    }
}
