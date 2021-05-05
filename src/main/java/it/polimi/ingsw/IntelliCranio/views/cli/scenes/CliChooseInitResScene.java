package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.*;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.CHOOSE_INIT_RES;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.CHOOSE_RES;

public class CliChooseInitResScene implements CliScene {

    public void displayOptions() {
        System.out.print("Choose an initial resource. You can type:");
        Arrays.stream(ResourceType.values())
                .filter(x -> !Arrays.asList(FAITH, BLANK).contains(x))
                .forEach(x -> System.out.print(x + " / "));
        System.out.println();
    }

    public Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) throws InvalidArgumentsException {
        if (input.size() > 1)
            throw new InvalidArgumentsException("ERRROR: you must input only one argument");

        if (Arrays.stream(values()).noneMatch(type -> type.toString().equals(input.get(0).trim().toUpperCase())))
            throw new InvalidArgumentsException("ERROR: you must input one of the listed resources");

        return new Pair<>(
                CHOOSE_RES,
                new ArrayList<>(Arrays.asList(new Resource(ResourceType.valueOf(input.get(0).trim().toUpperCase()), 1)))
        );
    }
}
