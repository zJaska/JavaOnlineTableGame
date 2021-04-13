package it.polimi.ingsw.IntelliCranio.views;

import it.polimi.ingsw.IntelliCranio.server.Packet;
import it.polimi.ingsw.IntelliCranio.server.Packet.InstructionCode;

public interface View {

    String getInput();

    void setScene(InstructionCode code);

}
