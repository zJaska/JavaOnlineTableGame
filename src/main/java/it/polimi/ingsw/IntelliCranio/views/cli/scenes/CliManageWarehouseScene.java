package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static java.lang.Integer.parseInt;

public class CliManageWarehouseScene implements CliScene {

    public void displayOptions() {
        System.out.println("MANAGE WAREHOUSE: ");
        System.out.println("- swaplines <number> <number>");
        System.out.println("- addFromExtra");
        System.out.println("- removeToExtra");
        System.out.println("- addToCard");
        System.out.println("- cancel");
        System.out.println("- confirm");
    }

    private Pair<InstructionCode, ArrayList<Object>> swaplines(ArrayList<String> input) throws InvalidArgumentsException {
        if (input.size() != 3)
            throw new InvalidArgumentsException("ERROR: you must input 2 numbers");

        int n1, n2;
        try {
            n1 = parseInt(input.get(1));
            n2 = parseInt(input.get(2));
        } catch (NumberFormatException e) {
            throw new InvalidArgumentsException("ERROR: you must input numbers");
        }

        if (n1 < 1 || n1 > 3 || n2 < 1 || n2 > 3)
            throw new InvalidArgumentsException("ERROR: you must input numbers between 1 and 3");

        return new Pair<>(SWAP_LINES, new ArrayList<>(Arrays.asList(n1,n2)));
    }

    private Pair<InstructionCode, ArrayList<Object>> addFromExtra(ArrayList<String> input) throws InvalidArgumentsException {
        return null;
    }

    private Pair<InstructionCode, ArrayList<Object>> removeToExtra(ArrayList<String> input) throws InvalidArgumentsException {
        return null;
    }

    private Pair<InstructionCode, ArrayList<Object>> addToCard(ArrayList<String> input) throws InvalidArgumentsException {
        return null;
    }

    public Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) throws InvalidArgumentsException {
        switch (input.get(0)) {
            case "swaplines":
                return swaplines(input);
            case "addFromExtra":
                return addFromExtra(input);
            case "removeToExtra":
                return removeToExtra(input);
            case "4":
                return addToCard(input);
            case "5":
                return new Pair<>(CONFIRM, null);
            case "6":
                return new Pair<>(CANCEL, null);
            default:
                throw new InvalidArgumentsException("ERROR: command not found");
        }
    }
}
