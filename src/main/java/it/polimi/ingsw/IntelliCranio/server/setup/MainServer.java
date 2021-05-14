package it.polimi.ingsw.IntelliCranio.server.setup;

import it.polimi.ingsw.IntelliCranio.controllers.GameManager;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.network.SocketHandler;
import it.polimi.ingsw.IntelliCranio.util.Save;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MainServer {
    private static final int PORT = 1051;
    private static ConcurrentHashMap<String, GameManager> currentGames = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        /*
        File folder = new File(Save.pathPrefix);
        for (File file : folder.listFiles()) {
            ArrayList<Pair<String, SocketHandler>> players = new ArrayList<>();
            UUID uuid = UUID.fromString(file.getName());
            Save.loadGame(uuid).getPlayers().forEach(x -> players.add(new Pair<>(x.getNickname(), null)));

            startManager(uuid, players);
        }*/

        while (true) {
            try {
                new Thread(new ClientHandler(serverSocket.accept())).start();
            }
            catch (IOException e) {
                System.err.println("A client failed to be accepted");
            }
        }
    }

    public static void startManager (UUID uuid, ArrayList<Pair<String,SocketHandler>> players) {
        GameManager manager = new GameManager(uuid, players);
        players.forEach(x -> currentGames.put(x.getKey(), manager));
        new Thread(manager).start();
    }

    public static Vector<String> getAllPlayers() {
        return new Vector<>(Collections.list(currentGames.keys()));
    }

    public static GameManager getManager(String nickname) {
        return currentGames.get(nickname);
    }

    public static void forgetManager(UUID uuid, ArrayList<String> players) {
        players.forEach(x -> currentGames.remove(x));
    }
}
