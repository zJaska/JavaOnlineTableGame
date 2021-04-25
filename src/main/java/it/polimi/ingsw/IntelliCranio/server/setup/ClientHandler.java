package it.polimi.ingsw.IntelliCranio.server.setup;

import it.polimi.ingsw.IntelliCranio.network.SocketHandler;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import javafx.util.Pair;

import java.net.Socket;
import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.network.Packet.ErrorCode.ACK;
import static it.polimi.ingsw.IntelliCranio.network.Packet.ErrorCode.NICKNAME_TAKEN;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.CHOOSE_NICKNAME;

public class ClientHandler implements Runnable {
    private static ArrayList<Pair<String,Socket>> firstPlayers = new ArrayList<>();
    private static ArrayList<Pair<String,Socket>> waitingPlayers = new ArrayList<>();

    Socket socket;

    ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        SocketHandler socketHandler = new SocketHandler(socket);

        // Start communication: choose nickname

        boolean chosen;
        String nickname;

        socketHandler.send(new Packet(CHOOSE_NICKNAME, null,null));
        do {
            chosen = false;
            nickname = socketHandler.receive().getArgs().get(0);

            String finalNickname = nickname;
            if (firstPlayers.stream().filter(x -> x.getKey().equals(finalNickname)).count() >= 1 ||
                    waitingPlayers.stream().filter(x -> x.getKey().equals(finalNickname)).count() >= 1) {
                chosen = true;
                socketHandler.send(new Packet(CHOOSE_NICKNAME, NICKNAME_TAKEN,null));
            }
        } while (chosen);
        socketHandler.send(new Packet(CHOOSE_NICKNAME, ACK,null));

        System.out.println("Player connected: " + nickname);

        if (3 * firstPlayers.size() <= waitingPlayers.size()) {
            firstPlayers.add(new Pair<String, Socket>(nickname, socket));
            //socketHandler.send(new Packet(CHOOSE_NUMBER_PLAYERS,null,null));
        }
        else
            waitingPlayers.add(new Pair<String, Socket>(nickname,socket));
    }
}
