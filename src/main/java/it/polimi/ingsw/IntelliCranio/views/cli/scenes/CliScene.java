package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import javafx.util.Pair;

import java.util.ArrayList;

public interface CliScene {

    void displayOptions();
    Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) throws InvalidArgumentsException;
}
