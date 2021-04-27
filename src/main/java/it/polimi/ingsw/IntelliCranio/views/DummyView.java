package it.polimi.ingsw.IntelliCranio.views;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.views.cli.Cli;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.util.Lists.toList;

public class DummyView extends Cli {
    private int count = 0;

    private String[][] inputs;

    public DummyView(String[][] in) {
        inputs = in;
    }

    @Override
    public Pair<InstructionCode, ArrayList<String>> getInput() {
        if (count < inputs.length) {
            Arrays.stream(inputs[count]).forEach(x -> {System.out.print(x + " ");});
            System.out.println();
            return new Pair<InstructionCode, ArrayList<String>>(null, toList(inputs[count++]));
        }

        return null;
    }
}
