package it.polimi.ingsw.IntelliCranio.views;

import it.polimi.ingsw.IntelliCranio.network.Packet.Response;
import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import javafx.util.Pair;

import java.util.ArrayList;

public interface View {
    Pair<InstructionCode,ArrayList<Object>> getInput();
    void setScene(InstructionCode code);
    void displayError(Response response);
    void showCommunication(String msg);
}
