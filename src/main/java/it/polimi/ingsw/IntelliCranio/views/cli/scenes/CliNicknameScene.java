package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import javafx.util.Pair;

import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.CHOOSE_NICKNAME;

public class CliNicknameScene implements CliScene {

    public void displayOptions() {
        System.out.println("Select your nickname: ");
    }

    public Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) throws InvalidArgumentsException {
        if (input.size() != 1)
            throw new InvalidArgumentsException("ERROR, you must input only one word: ");

        return new Pair<> (CHOOSE_NICKNAME, new ArrayList<>(input));
    }
}
