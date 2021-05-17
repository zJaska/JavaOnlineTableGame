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
    private static ConcurrentHashMap<String, UUID> nickname_uuid = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<UUID, GameManager> uuid_manager = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);

        nickname_uuid = new ConcurrentHashMap<String, UUID>(Save.getDatabase("database.json", Save.playerDatabaseType));

        while (true) {
            try {
                new Thread(new ClientHandler(serverSocket.accept())).start();
            }
            catch (IOException e) {
                System.err.println("A client failed to be accepted");
            }
        }
    }

    public static void startManager (ArrayList<Pair<String,SocketHandler>> players) {
        GameManager manager = new GameManager(null, players);
        players.forEach(pair -> nickname_uuid.put(pair.getKey(), manager.getUUID()));
        uuid_manager.put(manager.getUUID(), manager);
        new Thread(manager).start();

        Save.saveDatabase(new HashMap<>(nickname_uuid), "database.json");
    }

    public static void restartManager(String nickname) {
        if (isManagerRunning(nickname))
            return;

        UUID uuid = nickname_uuid.get(nickname);

        GameManager manager = new GameManager(uuid, new ArrayList<>(Arrays.asList(new Pair<>(nickname, null))));
        uuid_manager.put(uuid, manager);
        new Thread(manager).start();
    }

    public static Vector<String> getAllPlayers() {
        return new Vector<>(Collections.list(nickname_uuid.keys()));
    }

    public static void reconnectPlayer(String nickname, SocketHandler socketHandler) {
        uuid_manager.get(nickname_uuid.get(nickname)).reconnectPlayer(nickname, socketHandler);
    }

    public static void forgetManager(UUID uuid) {
        uuid_manager.remove(uuid);

        ArrayList<String> players = new ArrayList<>();
        nickname_uuid.forEach((key, value) -> {
            if (value.equals(uuid))
                players.add(key);
        });
        players.forEach(nick -> nickname_uuid.remove(nick));

        Save.saveDatabase(new HashMap<>(nickname_uuid), "database.json");
        Save.deleteGameData(uuid);
    }

    public static boolean isManagerRunning(String nickname) {
        return uuid_manager.get(nickname_uuid.get(nickname)) != null;
    }

    public static boolean isPlayerOnline(String nickname) {
        if (uuid_manager.get(nickname_uuid.get(nickname)) == null)
            return false;
        return uuid_manager.get(nickname_uuid.get(nickname)).isOnline(nickname);
    }
}
