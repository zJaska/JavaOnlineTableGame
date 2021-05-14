package it.polimi.ingsw.IntelliCranio.util;

import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.network.SocketHandler;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.COMMUNICATION;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.DIE;

public class Net {

    public static final String TIMEOUT_MSG = "You took too much to answer, disconnecting";
    public static final String HURRYUP_MSG = "HURRY UP! You are running out of time!";
    public static final String ACK_MSG = "Action accepted!";

    public static Packet createPacketFromInput(Pair<Packet.InstructionCode, ArrayList<Object>> input) {
        ArrayList<Object> args = new ArrayList<>();
        if (input.getValue() != null)
            args = input.getValue();
        return new Packet(input.getKey(), null, args);
    }

    public static void disconnectPlayer(SocketHandler socketHandler, String msg) {
        socketHandler.send(new Packet(COMMUNICATION, null, new ArrayList<Object>(Arrays.asList(msg))));
        socketHandler.send(new Packet(DIE, null, null));
    }
}
