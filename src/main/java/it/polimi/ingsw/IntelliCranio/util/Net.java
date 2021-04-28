package it.polimi.ingsw.IntelliCranio.util;

import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.network.SocketHandler;
import javafx.util.Pair;

import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.COMMUNICATION;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.DIE;
import static it.polimi.ingsw.IntelliCranio.util.Lists.toList;

public class Net {

    public static final String TIMEOUT_MSG = "You took too much to answer, disconnecting";

    public static Packet createPacketFromInput(Pair<Packet.InstructionCode, ArrayList<Object>> input) {
        return new Packet(input.getKey(), null, input.getValue());
    }

    public static void disconnectPlayer(SocketHandler socketHandler, String msg) {
        socketHandler.send(new Packet(COMMUNICATION, null, toList(msg)));
        socketHandler.send(new Packet(DIE, null, null));
    }
}
