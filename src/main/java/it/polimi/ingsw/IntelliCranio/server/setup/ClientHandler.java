package it.polimi.ingsw.IntelliCranio.server.setup;

import it.polimi.ingsw.IntelliCranio.network.PingingDevice;
import it.polimi.ingsw.IntelliCranio.network.SocketHandler;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import javafx.util.Pair;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static java.lang.Integer.parseInt;

public class ClientHandler implements Runnable {
    private static ArrayList<Pair<String,SocketHandler>> firstPlayers = new ArrayList<>();
    private static ArrayList<Pair<String,SocketHandler>> waitingPlayers = new ArrayList<>();
    private static ArrayList<String> allPlayers = new ArrayList<>();

    private SocketHandler socketHandler;

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
                nickname = socketHandler.receive().getArgs().get(0);
                String finalNickname = nickname;

                if (allPlayers.stream().anyMatch(x -> x.equals(finalNickname))) {
                    chosen = true;
                    socketHandler.send(new Packet(CHOOSE_NICKNAME, NICKNAME_TAKEN,null));
                }
            } catch (IOException e) { return; }
        } while (chosen);

        allPlayers.add(nickname);

        socketHandler.send(new Packet(CHOOSE_NICKNAME, ACK,null));

        System.out.println("Player connected: " + nickname);

        // Setting up pinging device for the client

        new Thread(new PingingDevice(socketHandler)).start();


        // Adding the player to a lobby

        Pair<String,SocketHandler> playerPair = new Pair<String, SocketHandler>(nickname, socketHandler);

        if (3 * firstPlayers.size() <= waitingPlayers.size())
            firstPlayersCycle(playerPair);
        else
            waitingPlayers.add(playerPair);
    }

    /**
     * This method asks to the first player the number of players and creates the lobby.
     * In case the player takes too much to respond, it will ask another one.
     */

    private void firstPlayersCycle(Pair<String,SocketHandler> firstPlayer) {
        firstPlayers.add(firstPlayer);

        socketHandler.send(new Packet(CHOOSE_NUMBER_PLAYERS, null,null));
        try {

            int numPlayers = parseInt(socketHandler.receive().getArgs().get(0));

            System.out.println("Received size");
            socketHandler.send(new Packet(CHOOSE_NUMBER_PLAYERS, ACK,null));

            new WaitingRoom(numPlayers, firstPlayer).run();

        } catch (SocketTimeoutException e) {

            if (waitingPlayers.size() >= 1) {
                firstPlayers.remove(firstPlayer);
                Pair<String,SocketHandler> waitingPlayer = waitingPlayers.get(0);
                waitingPlayers.remove(0);
                firstPlayersCycle(waitingPlayer);
            } else {
                firstPlayersCycle(firstPlayer);
            }

        }
        catch (IOException e) { return; }
    }
}
