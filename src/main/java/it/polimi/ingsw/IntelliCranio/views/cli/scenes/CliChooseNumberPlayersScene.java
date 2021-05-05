package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import javafx.util.Pair;

import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.CHOOSE_NICKNAME;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static java.lang.Integer.parseInt;

public class CliChooseNumberPlayersScene implements CliScene {
    public void displayOptions() {
        System.out.println("Choose the number of players of the party (2 to 4): ");
    }

    public Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) throws InvalidArgumentsException {
        if (input.size() != 1)
            throw new InvalidArgumentsException("ERROR, only 1 argument expected");

        int num = 0;
        try {
            num = parseInt(input.get(0));
        } catch (NumberFormatException e) {
            throw new InvalidArgumentsException("ERROR, you must input a number: ");
        }

        if (num < 2 || num > 4)
            throw new InvalidArgumentsException("ERROR, choose between 2 and 4: ");

        return new Pair<InstructionCode, ArrayList<Object>> (CHOOSE_NICKNAME, new ArrayList<Object>(input));
    }
}
