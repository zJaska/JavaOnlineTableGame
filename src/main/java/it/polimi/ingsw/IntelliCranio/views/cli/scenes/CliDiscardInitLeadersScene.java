package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.network.Packet.*;
import javafx.util.Pair;

import java.util.ArrayList;

public class CliDiscardInitLeadersScene implements CliScene {

    public void displayOptions() {
        System.out.println("Discard one of you Leader cards: ");

    }

    public String checkSyntax(ArrayList<String> input) {
        return "NONOONO";
    }

    public Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) {
        return null;
    }
}
