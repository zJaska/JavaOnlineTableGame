package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.network.Packet.Response;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.CHOOSE_NICKNAME;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static java.lang.Integer.parseInt;

public class CliChooseNumberPlayersScene implements CliScene {
    public void displayOptions() {
        System.out.println("Choose the number of players of the party (2 to 4): ");
    }

    public void displayError (Response response) {
        if (response == null || response == ACK)
            return;

        switch (response) {
            case OUT_OF_BOUNDS:
                System.out.println("ERROR, choose between 2 and 4: ");
                break;
            case BAD_ARGUMENTS_NUMBER:
                System.out.println("ERROR, only 1 argument expected");
                break;
            case NOT_A_NUMBER:
                System.out.println("ERROR, you must input a number: ");
                break;
            default:
                System.out.println("Choose the number of players of the party (2 to 4): (an error has occurred)");
                break;
        }
    }

    public Response isSyntaxCorrect(ArrayList<String> input) {
        if (input.size() != 1)
            return Response.BAD_ARGUMENTS_NUMBER;

        try {
            int num = parseInt(input.get(0));
            if (num >= 2 && num <= 4)
                return ACK;
            else
                return Response.OUT_OF_BOUNDS;
        } catch (NumberFormatException e) { }

        return Response.NOT_A_NUMBER;
    }

    public Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) {
        return new Pair<InstructionCode, ArrayList<Object>> (CHOOSE_NICKNAME, new ArrayList<Object>(input));
    }
}
