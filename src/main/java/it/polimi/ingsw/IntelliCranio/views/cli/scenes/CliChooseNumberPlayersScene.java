package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import javafx.util.Pair;

import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.CHOOSE_NICKNAME;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static java.lang.Integer.parseInt;

public class CliChooseNumberPlayersScene implements CliScene {
    public void displayOptions() {
        System.out.println("Choose the number of players of the party (2 to 4): ");
    }

    public String checkSyntax(ArrayList<String> input) {
        if (input.size() != 1)
            return "ERROR, only 1 argument expected";

        try {
            int num = parseInt(input.get(0));
            if (num >= 2 && num <= 4)
                return "";
            else
                return "ERROR, choose between 2 and 4: ";
        } catch (NumberFormatException e) { }

        return "ERROR, you must input a number: ";
    }

    public Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) {
        return new Pair<InstructionCode, ArrayList<Object>> (CHOOSE_NICKNAME, new ArrayList<Object>(input));
    }
}
