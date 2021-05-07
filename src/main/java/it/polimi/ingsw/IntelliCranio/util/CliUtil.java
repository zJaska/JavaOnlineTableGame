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

    public static Pair<InstructionCode, ArrayList<Object>> resFrom(ArrayList<String> input) throws InvalidArgumentsException {
        if (input.size() == 1) {
            System.out.println("Choose the resource type and the amount that you want to use, from 'warehouse', 'strongbox' or 'card'");
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
