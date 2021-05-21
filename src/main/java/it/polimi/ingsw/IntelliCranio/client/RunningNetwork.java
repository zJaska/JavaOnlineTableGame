package it.polimi.ingsw.IntelliCranio.client;

import it.polimi.ingsw.IntelliCranio.models.FaithTrack;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.market.CardMarket;
import it.polimi.ingsw.IntelliCranio.models.market.ResourceMarket;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.network.Packet.*;
import it.polimi.ingsw.IntelliCranio.network.SocketHandler;
import it.polimi.ingsw.IntelliCranio.views.View;
import javafx.util.Pair;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.Semaphore;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;

public class RunningNetwork implements Runnable {

    private static SocketHandler socketHandler;
    private static View view;

    private static Semaphore dataReady = new Semaphore(0);

    private static Vector<Packet> packets = new Vector<>();

    public RunningNetwork(SocketHandler socketHandler, View view) {
        this.socketHandler = socketHandler;
        this.view = view;
    }

    public void run() {
        while (true) {
            Packet input = receivePacket();

            if (input != null) {
                packets.add(input);
                dataReady.release();
            }
        }
    }

    private static Packet receivePacket() {
        Packet pack = null;

        try { pack = socketHandler.receive(); }
        catch (IOException e) { System.exit(-100); }

        switch (pack.getInstructionCode()) {
            case NICKNAME:
                MainClient.setNickname((String) pack.getArgs().get(0));
                return null;
            case PING:
                return null;
            case COMMUNICATION:
                view.showCommunication((String) pack.getArgs().get(0));
                return null;
            case GAME:
                MainClient.setGame((Game) pack.getArgs().get(0));
                view.gameChanged();
                return null;
            case IDLE:
                view.setScene(IDLE);
                return null;
            case GAME_ENDED:
                view.setScene(GAME_ENDED);
                Thread.currentThread().stop();
            case DIE:
                System.exit(-101);
            default:
                return pack;
        }
    }

    public static Packet getData() {
        try { dataReady.acquire(); }
        catch (InterruptedException e) {}

        Packet tmp = packets.get(0);
        packets.remove(0);

        return tmp;
    }
}
