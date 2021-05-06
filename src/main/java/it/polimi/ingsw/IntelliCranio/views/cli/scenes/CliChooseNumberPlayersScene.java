package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.CliUtil;
import javafx.util.Pair;

import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.CHOOSE_NICKNAME;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.CHOOSE_NUMBER_PLAYERS;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static java.lang.Integer.parseInt;

public class CliChooseNumberPlayersScene implements CliScene {
    public void displayOptions() {
        System.out.println("Choose the number of players of the party (2 to 4): ");
    }

    public Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) throws InvalidArgumentsException {
        if (input.size() != 1)
            throw new InvalidArgumentsException("ERROR, only 1 argument expected");

        int num = CliUtil.checkInt(input.get(0),2,4);

        return new Pair<InstructionCode, ArrayList<Object>> (CHOOSE_NUMBER_PLAYERS, new ArrayList<Object>(input));
    }
}
