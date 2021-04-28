package it.polimi.ingsw.IntelliCranio.views;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.views.cli.Cli;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.util.Lists.toList;
import static it.polimi.ingsw.IntelliCranio.util.Lists.toObjectList;

public class DummyView extends Cli {
    private int count = 0;

    private String[] inputs;

    public DummyView(String[] in) {
        inputs = in;
    }

    @Override
    public Pair<InstructionCode, ArrayList<Object>> getInput() {
        if (count < inputs.length) {
            System.out.println(inputs[count]);
            return new Pair<InstructionCode, ArrayList<Object>>(null, toObjectList(inputs[count++]));
        }

        return null;
    }
}
