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
import java.util.concurrent.Semaphore;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.IDLE;

public class RunningNetwork implements Runnable {

    private static SocketHandler socketHandler;
    private static View view;

    private static Semaphore dataReady = new Semaphore(0);

    private static ArrayList<Packet> packets = new ArrayList<>();
    private static Packet input;

    public RunningNetwork(SocketHandler socketHandler, View view) {
        this.socketHandler = socketHandler;
        this.view = view;
    }

    public void run() {
        while (true) {
            input = receivePacket();

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
            case PING:
                return null;
            case COMMUNICATION:
                view.showCommunication((String) pack.getArgs().get(0));
                return null;
            case GAME:
                MainClient.game = (Game) pack.getArgs().get(0);
                System.out.println((String)pack.getArgs().get(1)); //Print associated message
                return null;
            case IDLE:
                view.setScene(IDLE);
                return null;
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
