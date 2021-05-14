package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.client.MainClient;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType;
import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.CliUtil;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;

public class CliResourceMarketScene implements CliScene {

    public void displayOptions() {
        System.out.println("RESOURCE MARKET ACTIONS:");
        System.out.println("-) selectRow <number>");
        System.out.println("-) selectColumn <number>");
        System.out.println("-) chooseResource <ResourceType>");
        System.out.println("-) confirm");
        System.out.println("-) cancel");
        CliIdleScene.showResourceMarket();
    }

    private Pair<InstructionCode, ArrayList<Object>> selectRowColumn(String type, ArrayList<String> input) throws InvalidArgumentsException {
        Game game = MainClient.game;

        if (input.size() == 1) {
            System.out.println("Select the " + type + " from which the resources will be taken");
            return null;
        }

        if (input.size() != 2)
            throw new InvalidArgumentsException("Select the " + type + " from which the resources will be taken");

        int max = (type == "row") ? game.getResourceMarket().ROWS : game.getResourceMarket().COLUMNS;
        int num = CliUtil.checkInt(input.get(1),1, max);

        return new Pair<>(
                (type == "row") ? SELECT_ROW : SELECT_COLUMN,
                new ArrayList<>(Arrays.asList(num-1))
        );
    }

    private Pair<InstructionCode, ArrayList<Object>> chooseResource(ArrayList<String> input) throws InvalidArgumentsException {
        if (input.size() == 1) {
            System.out.println("Select the resource that you want to get from the blank marbles (active leader card needed)");
            return null;
        }

        if (input.size() != 2)
            throw new InvalidArgumentsException("ERROR: you must input only 1 resource type");

        ResourceType type = CliUtil.checkResourceType(input.get(1));

        return new Pair<>(CHOOSE_RES, new ArrayList<>(Arrays.asList(type)));
    }

    public Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) throws InvalidArgumentsException {

        switch (input.get(0)) {
            case "selectRow":
                return selectRowColumn("row", input);
            case "selectColumn":
                return selectRowColumn("column", input);
            case "chooseResource":
                return chooseResource(input);
            case "confirm":
                return new Pair<>(CONFIRM, null);
            case "cancel":
                return new Pair<>(CANCEL, new ArrayList<>());
            default:
                throw new InvalidArgumentsException("Command not found");
        }
    }
}
