package it.polimi.ingsw.IntelliCranio.client;

import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.network.Packet.*;
import it.polimi.ingsw.IntelliCranio.network.SocketHandler;
import it.polimi.ingsw.IntelliCranio.views.View;
import javafx.util.Pair;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

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
