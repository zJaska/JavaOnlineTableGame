package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.client.MainClient;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;

import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.CliUtil;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;

public class CliActivateProductionScene implements CliScene {

    public void displayOptions() {
        System.out.println("ACTIVATE PRODUCTION:");
        System.out.println("-) selectSlot <number>");
        System.out.println("-) selectCard <number>");
        System.out.println("-) resFromCard <number>");
        System.out.println("-) resFromStrongbox <resourceType>");
        System.out.println("-) resFromWarehouse <number>");
        System.out.println("-) chooseResource <resourceType>");
        System.out.println("-) cancel");
        System.out.println("-) confirm");
        CliIdleScene.showDevCards();
    }

    private Pair<InstructionCode, ArrayList<Object>> selectSlot(ArrayList<String> input) throws InvalidArgumentsException {
        if (input.size() == 1) {
            System.out.println("Select the slot of the development card you want to use (or 0 if you want to use the base production)");
            return null;
        }
        if (input.size() != 2)
            throw new InvalidArgumentsException("ERROR: you must input only the number of the slot");

        int num = CliUtil.checkInt(input.get(1),0, MainClient.getGame().getCurrentPlayer().getFirstDevCards().length);

        return new Pair<>(
                SELECT_SLOT,
                new ArrayList<>(Arrays.asList(num)));
    }

    private Pair<InstructionCode, ArrayList<Object>> selectCard(ArrayList<String> input) throws InvalidArgumentsException {
        if (input.size() == 1) {
            System.out.println("Select the index of the leader card (starting from one) that you want to use. It must have a production ability.");
            return null;
        }
        if (input.size() != 2)
            throw new InvalidArgumentsException("ERROR: you must input only the number of the leader card");

        ArrayList<LeadCard> leaders = MainClient.getGame().getCurrentPlayer().getLeaders();
        int num = CliUtil.checkInt(input.get(1), 1, leaders.size());
        LeadCard lead = leaders.get(num-1);

        return new Pair<>(
                SELECT_CARD,
                new ArrayList<>(Arrays.asList(lead)));
    }

    private Pair<InstructionCode, ArrayList<Object>> chooseResource(ArrayList<String> input) throws InvalidArgumentsException {
        if (input.size() == 1) {
            System.out.println("Choose the resource type of the products of the base production or the leader card. You must do this once for every resource.");
            return null;
        }
        if (input.size() != 2)
            throw new InvalidArgumentsException("ERROR: you must input only the resource type");

        ResourceType type = CliUtil.checkResourceType(input.get(1));

        return new Pair<>(
                CHOOSE_RES,
                new ArrayList<>(Arrays.asList(new Resource(type,1))));
    }

    public Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) throws InvalidArgumentsException {
        switch (input.get(0)) {
            case "selectSlot":
                return selectSlot(input);
            case "selectCard":
                return selectCard(input);
            case "resFromCard":
                return CliUtil.resFromCard(input);
            case "resFromStrongbox":
                return CliUtil.resFromStrongbox(input);
            case "resFromWarehouse":
                return CliUtil.resFromWarehouse(input);
            case "chooseResource":
                return chooseResource(input);
            case "cancel":
                return new Pair<>(CANCEL, new ArrayList<>());
            case "confirm":
                return new Pair<>(CONFIRM, new ArrayList<>());
            default:
                throw new InvalidArgumentsException("Command not found");
        }
    }
}
