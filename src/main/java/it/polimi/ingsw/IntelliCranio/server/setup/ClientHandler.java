package it.polimi.ingsw.IntelliCranio.server.setup;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.network.PingingDevice;
import it.polimi.ingsw.IntelliCranio.network.SocketHandler;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import javafx.util.Pair;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.util.Net.TIMEOUT_MSG;
import static it.polimi.ingsw.IntelliCranio.util.Net.disconnectPlayer;
import static java.lang.Integer.parseInt;

public class ClientHandler implements Runnable {

    private SocketHandler socketHandler;

    // Vector is used because it's thread-safe
    private static Vector<Pair<String,SocketHandler>> waitingPlayers = new Vector<>();
    private static Vector<String> playersNames = new Vector<>();
    private static ArrayList<Game> currentGames = new ArrayList<>();

    private static int numFirstPlayers = 0;

    private static Semaphore playerReady = new Semaphore(0);
    public static void acquirePlayerReady() {
        try { playerReady.acquire(); }
        catch (InterruptedException e) { }
    }

    ClientHandler(Socket socket) throws IOException {
        socketHandler = new SocketHandler(socket);
    }

    /**
     * This method returns and removes from the list 'waitingPlayers' a player.
     * @return The player that has been waiting for the longest time
     */

    public static Pair<String,SocketHandler> popWaitingPlayer() {
        if (waitingPlayers.size() > 0) {
            Pair<String,SocketHandler> player = waitingPlayers.get(0);
            waitingPlayers.remove(0);
            return player;
        }

        return null;
    }

    public void run() {

        // Start communication: choose nickname

        boolean chosen;
        String nickname = "";

        socketHandler.send(new Packet(CHOOSE_NICKNAME, null,null));
        do {
            chosen = false;

            try {
                nickname = (String) socketHandler.receive().getArgs().get(0);
                String finalNickname = nickname;

                if (playersNames.stream().anyMatch(x -> x.equals(finalNickname))) {
                    chosen = true;
                    socketHandler.send(new Packet(CHOOSE_NICKNAME, NICKNAME_TAKEN,null));
                    socketHandler.send(new Packet(COMMUNICATION, null,new ArrayList<>(Arrays.asList("Nickname already taken, choose another one"))));
                }
            } catch (IOException e) {
                disconnectPlayer(socketHandler,TIMEOUT_MSG);
                return;
            }
        } while (chosen);

        playersNames.add(nickname);

        socketHandler.send(new Packet(NICKNAME, null, new ArrayList<>(Arrays.asList(nickname))));

        System.out.println("Player connected: " + nickname);

        // Setting up pinging device for the client

        new Thread(new PingingDevice(socketHandler)).start();


        // Adding the player to a lobby

        Pair<String,SocketHandler> playerPair = new Pair<String, SocketHandler>(nickname, socketHandler);

        if (3 * numFirstPlayers <= waitingPlayers.size() && WaitingRoom.getNumLobbies() <= 0)
            firstPlayersCycle(playerPair);
        else {
            waitingPlayers.add(playerPair);
            playerReady.release();
        }
    }

    /**
     * This method asks to the first player the number of players and creates the lobby.
     * In case the player takes too much to respond, it will ask another one.
     */

    private void firstPlayersCycle(Pair<String,SocketHandler> firstPlayer) {
        numFirstPlayers++;

        firstPlayer.getValue().send(new Packet(CHOOSE_NUMBER_PLAYERS, null,null));
        try {

            int numPlayers = parseInt((String) firstPlayer.getValue().receive().getArgs().get(0));

            System.out.println("Received size");
            //firstPlayer.getValue().send(new Packet(CHOOSE_NUMBER_PLAYERS, ACK,null));

            numFirstPlayers--;
            new WaitingRoom(numPlayers, firstPlayer).run();

        } catch (SocketTimeoutException e) {

            numFirstPlayers--;
            playersNames.remove(firstPlayer.getKey());
            disconnectPlayer(firstPlayer.getValue(),TIMEOUT_MSG);

            if (waitingPlayers.size() >= 1) {
                Pair<String,SocketHandler> waitingPlayer = waitingPlayers.get(0);
                waitingPlayers.remove(0);
                waitingPlayers.add(firstPlayer);
                firstPlayersCycle(waitingPlayer);
            }

        }
        catch (IOException e) {
            numFirstPlayers--;
            return;
        }
    }
}
