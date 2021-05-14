package it.polimi.ingsw.IntelliCranio.util;

import it.polimi.ingsw.IntelliCranio.client.MainClient;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static java.lang.Integer.parseInt;

public class CliUtil {

    private static final ArrayList<String> containers = new ArrayList<>(Arrays.asList("warehouse", "strongbox", "card"));

    public static int checkInt(String input, int min, int max) throws InvalidArgumentsException {
        int number = 0;
        try {
            number = parseInt(input);
        } catch (NumberFormatException e) {
            throw new InvalidArgumentsException("ERROR: you must input a number");
        }

        if (number < min || number > max)
            throw new InvalidArgumentsException("ERROR: you must input a number between " + min + " and " + max);

        return number;
    }

    public static ResourceType checkResourceType(String input) throws InvalidArgumentsException {
        if (Arrays.stream(ResourceType.values())
                .filter(x -> x != FAITH && x != BLANK)
                .noneMatch(type -> type.toString().equals(input.trim().toUpperCase())))
            throw new InvalidArgumentsException("ERROR: you must input one of the listed resources");

        return ResourceType.valueOf(input.trim().toUpperCase());
    }

    public static Pair<InstructionCode, ArrayList<Object>> resFromCard(ArrayList<String> input) throws InvalidArgumentsException {
        if (input.size() == 1) {
            System.out.println("Choose the index (1,2) of the leader card where you want to take the resource from.");
            return null;
        }
        if (input.size() != 2)
            throw new InvalidArgumentsException("ERROR: you must input only 1 arguments");

        int amount = CliUtil.checkInt(input.get(1),1,MainClient.game.getCurrentPlayer().getLeaders().size());

        return new Pair<>(
                RES_FROM_CARD,
                new ArrayList<>(Arrays.asList(MainClient.game.getCurrentPlayer().getLeaders().get(amount - 1)))
        );
    }

    public static Pair<InstructionCode, ArrayList<Object>> resFromStrongbox(ArrayList<String> input) throws InvalidArgumentsException {
        if (input.size() == 1) {
            System.out.println("Choose the resource type that you want to use from the strongbox");
            return null;
        }
        if (input.size() != 2)
            throw new InvalidArgumentsException("ERROR: you must input only 1 arguments");

        ResourceType resType = CliUtil.checkResourceType(input.get(1));

        return new Pair<>(
                RES_FROM_STRONG,
                new ArrayList<>(Arrays.asList(new Resource(resType, 1)))
        );
    }

    public static Pair<InstructionCode, ArrayList<Object>> resFromWarehouse(ArrayList<String> input) throws InvalidArgumentsException {
        if (input.size() == 1) {
            System.out.println("Choose the line of the warehouse where you want to take the resource from");
            return null;
        }
        if (input.size() != 2)
            throw new InvalidArgumentsException("ERROR: you must input only 1 arguments");

        int amount = CliUtil.checkInt(input.get(1),1,3);

        return new Pair<>(
                RES_FROM_DEPOT,
                new ArrayList<>(Arrays.asList(amount-1))
        );
    }

    /**
     *
     * @param input This array must be formatted like this: 'command' 'player'. This method is thought to
     *              be used in the CliIdleScene.
     */
    public static Player checkPlayerHelpCommands(ArrayList<String> input, Game game) throws InvalidArgumentsException {
        if (input.size() == 1)
            return game.getPlayer(MainClient.nickname);
        else {
            if (input.size() != 2)
                throw new InvalidArgumentsException("ERROR: you must only input the nickname of the player");

            Player player = game.getPlayer(input.get(1));
            if (player == null)
                throw new InvalidArgumentsException("ERROR: player doesn't exist");
            return player;
        }
    }
}
