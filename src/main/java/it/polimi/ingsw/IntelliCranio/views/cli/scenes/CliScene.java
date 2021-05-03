package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import javafx.util.Pair;

import java.util.ArrayList;

public interface CliScene {

    void displayOptions();

    String checkSyntax(ArrayList<String> input);

    Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input);
}
