package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Integer.parseInt;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;

public class CliDefaultScene implements CliScene {

    // WARNING: the order of codes in the array must be equal to the order of the actions in the menu
    ArrayList<InstructionCode> instructionCodes = new ArrayList<>(Arrays.asList(
            PLAY_LEADER, DISCARD_LEAD, MNG_WARE, CARD_MARKET, RES_MARKET, ACT_PROD
    ));

    public void displayOptions() {
        System.out.println("Choose an action to take: ");
        System.out.println("1) PLAY a LEADER card");
        System.out.println("2) DISCARD a LEADER card");
        System.out.println("3) Manage your WAREHOUSE");
        System.out.println("4) Go to the CARD MARKET");
        System.out.println("5) Go to the RESOURCE MARKET");
        System.out.println("6) Activate the PRODUCTION");
    }

    public Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) throws InvalidArgumentsException {
        if (input.size() > 1)
            throw new InvalidArgumentsException("ERROR: you must input only 1 argument");

        int number = 0;
        try { number = parseInt(input.get(0)); }
        catch (NumberFormatException e) {
            throw new InvalidArgumentsException("ERROR: you must input a number");
        }

        if (number < 1 || number > 6)
            throw new InvalidArgumentsException("ERROR: you must input a number between 1 and 6");

        return new Pair<>(instructionCodes.get(parseInt(input.get(0)) - 1), null);
    }
}
