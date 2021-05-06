package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.client.MainClient;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.CliUtil;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;

public class CliCardMarketScene implements CliScene {

    private ArrayList<String> containers = new ArrayList<>(Arrays.asList("warehouse", "strongbox", "card"));

    public void displayOptions() {
        System.out.println("CARD MARKET ACTIONS: ");
        System.out.println("-) selectCard <number> <number>");
        System.out.println("-) putRes <container> <resourceType> <number>");
        System.out.println("-) selectSlot <number>");
        System.out.println("-) cancel");
        System.out.println("-) confirm");
    }

    private Pair<InstructionCode, ArrayList<Object>> selectCard(ArrayList<String> input) throws InvalidArgumentsException {
        Game game = MainClient.game;

        if (input.size() == 1) {
            System.out.println("Choose the indexes (row, column) of the card you want to buy");
            return null;
        }
        if (input.size() != 3)
            throw new InvalidArgumentsException("ERROR: you must input only 2 arguments");

        int n1 = CliUtil.checkInt(input.get(1), 1, game.getCardMarket().rows);
        int n2 = CliUtil.checkInt(input.get(2), 1, game.getCardMarket().cols);

        return new Pair<>(
                SELECT_CARD,
                new ArrayList<>(Arrays.asList(n1 - 1, n2 - 1))
        );
    }

    private Pair<InstructionCode, ArrayList<Object>> resFrom(ArrayList<String> input) throws InvalidArgumentsException {
        if (input.size() == 1) {
            System.out.println("Choose the resource type and the amount that you want to use to buy the card from 'warehouse', 'strongbox' or 'card'");
            return null;
        }
        if (input.size() != 4)
            throw new InvalidArgumentsException("ERROR: you must input only 3 arguments");

        if (!containers.contains(input.get(1)))
            throw new InvalidArgumentsException("ERROR: container must be one of the following: " +
                    containers.stream().reduce("", (x,y) -> x + " " + y));

        ResourceType resType = CliUtil.checkResourceType(input.get(2));
        int amount = CliUtil.checkInt(input.get(3),1,1000);

        return new Pair<>(
                (input.get(1) == "warehouse") ? RES_FROM_DEPOT : (input.get(1) == "strongbox") ? RES_FROM_STRONG : RES_FROM_CARD,
                new ArrayList<>(Arrays.asList(new Resource(resType, amount)))
        );
    }

    private Pair<InstructionCode, ArrayList<Object>> selectSlot(ArrayList<String> input) throws InvalidArgumentsException {

        if (input.size() == 1) {
            System.out.println("Choose the slot where you want to put the development card");
            return null;
        }
        if (input.size() != 2)
            throw new InvalidArgumentsException("ERROR: you must input only 1 number");

        int num = CliUtil.checkInt(input.get(1),1, 3);

        return new Pair<>(
                SELECT_SLOT,
                new ArrayList<>(Arrays.asList(num - 1))
        );
    }

    public Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) throws InvalidArgumentsException {

        switch (input.get(0)) {
            case "selectCard":
                return selectCard(input);
            case "putRes":
                return resFrom(input);
            case "selectSlot":
                return selectSlot(input);
            case "confirm":
                return new Pair<>(CONFIRM, null);
            case "cancel":
                return new Pair<>(CANCEL, null);
            default:
                throw new InvalidArgumentsException("Command not found");
        }
    }
}
