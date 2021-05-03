package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import javafx.util.Pair;

import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.CHOOSE_NICKNAME;

public class CliNicknameScene implements CliScene {

    public void displayOptions() {
        System.out.println("Select your nickname: ");
    }

    public String checkSyntax(ArrayList<String> input) {
        if (input.size() != 1)
            return "ERROR, you must input only one word: ";

        return "";
    }

    public Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) {
        return new Pair<InstructionCode, ArrayList<Object>> (CHOOSE_NICKNAME, new ArrayList<Object>(input));
    }
}
