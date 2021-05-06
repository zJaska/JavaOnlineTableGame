package it.polimi.ingsw.IntelliCranio.util;

import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;

import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType.*;
import static java.lang.Integer.parseInt;

public class CliUtil {

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
        if (Arrays.stream(values())
                .filter(x -> x != FAITH && x != BLANK)
                .noneMatch(type -> type.toString().equals(input.trim().toUpperCase())))
            throw new InvalidArgumentsException("ERROR: you must input one of the listed resources");

        return ResourceType.valueOf(input.trim().toUpperCase());
    }
}
