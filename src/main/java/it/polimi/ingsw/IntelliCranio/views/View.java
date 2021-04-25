package it.polimi.ingsw.IntelliCranio.views;

import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.network.Packet.ErrorCode;
import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;

public interface View {

    Packet setScene(InstructionCode code, boolean display, ErrorCode option);

}
