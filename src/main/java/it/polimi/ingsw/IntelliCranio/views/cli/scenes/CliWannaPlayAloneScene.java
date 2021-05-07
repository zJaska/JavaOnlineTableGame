package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import javafx.util.Pair;

import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;

public class CliWannaPlayAloneScene implements CliScene {

    public void displayOptions() { System.out.println("Do you wanna play alone? (yes/no)"); }

    public Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) throws InvalidArgumentsException {
        if (input.size() != 1)
            throw new InvalidArgumentsException("ERROR: say yes or no!");

        switch (input.get(0)) {
            case "yes":
                return new Pair<>(ALONE, null);
            case "no":
                return new Pair<>(MULTIPLAYER, null);
            default:
                throw new InvalidArgumentsException("ERROR: say yes or no!");
        }
    }
}
