package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.CliUtil;
import javafx.util.Pair;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType.BLANK;
import static it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType.FAITH;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static java.lang.Integer.parseInt;

public class CliManageWarehouseScene implements CliScene {

    public void displayOptions() {
        System.out.println("MANAGE WAREHOUSE: ");
        System.out.println("-) swaplines <number> <number>");
        System.out.println("-) addFromExtra <resourceType> <number>");
        System.out.println("-) removeFromWarehouse <number>");
        System.out.println("-) warehouseToCard <number>");
        System.out.println("-) extraToCard <number>");
        System.out.println("-) cancel");
        System.out.println("-) confirm");
        CliIdleScene.showWarehouse();
    }

    private Pair<InstructionCode, ArrayList<Object>> swaplines(ArrayList<String> input) throws InvalidArgumentsException {
        if (input.size() == 1) {
            System.out.println("Choose 2 lines to swap (from 1 to 3)");
            return null;
        }
        if (input.size() != 3)
            throw new InvalidArgumentsException("ERROR: you must input 2 numbers");

        int n1 = CliUtil.checkInt(input.get(1),1,3);
        int n2 = CliUtil.checkInt(input.get(2),1,3);

        return new Pair<>(SWAP_LINES, new ArrayList<>(Arrays.asList(n1-1,n2-1)));
    }

    private Pair<InstructionCode, ArrayList<Object>> addFromExtra(ArrayList<String> input) throws InvalidArgumentsException {
        if (input.size() == 1) {
            System.out.println("Choose the type of resource from the extra pool and the line of the warehouse where you want to put it");
            return null;
        }
        if (input.size() != 3)
            throw new InvalidArgumentsException("ERROR: you must input a resource type and a number");

        ResourceType type = CliUtil.checkResourceType(input.get(1));
        int n = CliUtil.checkInt(input.get(2),1,3);

        return new Pair<>(ADD_FROM_EXTRA, new ArrayList<>(Arrays.asList(
                new Resource(type,1),
                n-1
        )));
    }

    private Pair<InstructionCode, ArrayList<Object>> removeFromWarehouse(ArrayList<String> input) throws InvalidArgumentsException {
        if (input.size() == 1) {
            System.out.println("Choose the line of the warehouse from which you want to put a resource in the extra pool");
            return null;
        }
        if (input.size() != 2)
            throw new IllegalArgumentException("ERROR: you must input only 1 number");

        int num = CliUtil.checkInt(input.get(1),1,3);

        return new Pair<>(REMOVE_FROM_DEPOT, new ArrayList<>(Arrays.asList(num-1)));
    }

    private Pair<InstructionCode, ArrayList<Object>> warehouseToCard(ArrayList<String> input) throws InvalidArgumentsException {
        if (input.size() == 1) {
            System.out.println("Choose the line of the warehouse from which you want to put a resource in the relative leader card");
            return null;
        }
        if (input.size() != 2)
            throw new IllegalArgumentException("ERROR: you must input only 1 number");

        int num = CliUtil.checkInt(input.get(1),1,3);

        return new Pair<>(DEPOT_TO_CARD, new ArrayList<>(Arrays.asList(num-1)));
    }

    private Pair<InstructionCode, ArrayList<Object>> extraToCard(ArrayList<String> input) throws InvalidArgumentsException {
        if (input.size() == 1) {
            System.out.println("Choose the resource type that you want to put in the leader card");
            return null;
        }
        if (input.size() != 2)
            throw new IllegalArgumentException("ERROR: you must input 1 resource");

        ResourceType type = CliUtil.checkResourceType(input.get(1));

        return new Pair<>(EXTRA_TO_CARD, new ArrayList<>(Arrays.asList(new Resource(type,1))));
    }

    public Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) throws InvalidArgumentsException {
        switch (input.get(0)) {
            case "swaplines":
                return swaplines(input);
            case "addFromExtra":
                return addFromExtra(input);
            case "removeFromWarehouse":
                return removeFromWarehouse(input);
            case "warehouseToCard":
                return warehouseToCard(input);
            case "extraToCard":
                return extraToCard(input);
            case "confirm":
                return new Pair<>(CONFIRM, null);
            case "cancel":
                return new Pair<>(CANCEL, null);
            default:
                throw new InvalidArgumentsException("ERROR: command not found");
        }
    }
}
