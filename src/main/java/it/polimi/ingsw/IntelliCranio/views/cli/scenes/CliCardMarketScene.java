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

    public void displayOptions() {
        System.out.println("CARD MARKET ACTIONS: ");
        System.out.println("-) selectCard <number> <number>");
        System.out.println("-) resFromCard <number>");
        System.out.println("-) resFromStrongbox <resourceType>");
        System.out.println("-) resFromWarehouse <number>");
        System.out.println("-) selectSlot <number>");
        System.out.println("-) cancel");
        System.out.println("-) confirm");
        CliIdleScene.showCardMarket();
    }

    private Pair<InstructionCode, ArrayList<Object>> selectCard(ArrayList<String> input) throws InvalidArgumentsException {
        Game game = MainClient.getGame();

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
            case "resFromCard":
                return CliUtil.resFromCard(input);
            case "resFromStrongbox":
                return CliUtil.resFromStrongbox(input);
            case "resFromWarehouse":
                return CliUtil.resFromWarehouse(input);
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
