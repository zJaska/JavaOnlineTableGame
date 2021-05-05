package it.polimi.ingsw.IntelliCranio.network;

import javafx.util.Pair;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.TreeMap;

public class SocketManager implements NetworkManagerI {

    private TreeMap<String, SocketHandler> players = new TreeMap<>();

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
            System.out.println("(NPE) Cannot send to " + name);
        }
    }

    public void sendAll(Packet packet) {
        for (SocketHandler x : players.values()) {
            x.send(packet);
        };
    }

    public Packet receive(String name) throws IOException {
        try {
            return players.get(name).receive();
        } catch (NullPointerException e) {
            System.out.println("Cannot receive from " + name + " (NullPointerException)");
        } catch (SocketTimeoutException e) {
            System.out.println("Player: " + name);
            throw new SocketTimeoutException();
        } catch (IOException e) {
            System.out.println("Player: " + name);
            throw new IOException();
        }

        return null;
    }

    /**
     * Flushes the buffers of each player.
     */
    public void clear() {
        players.values().forEach(x -> x.clear());
    }

    public void disconnect (String name) {
        players.get(name).close();
    }
}
