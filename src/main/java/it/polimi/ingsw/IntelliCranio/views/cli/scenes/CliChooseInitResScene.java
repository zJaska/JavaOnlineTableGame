package it.polimi.ingsw.IntelliCranio.views.cli.scenes;

import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.*;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.CliUtil;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.CHOOSE_INIT_RES;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.CHOOSE_RES;

public class CliChooseInitResScene implements CliScene {

    public void displayOptions() {
        System.out.println("Choose an initial resource.");
        CliIdleScene.showResourceTypes();
    }

    public Pair<InstructionCode, ArrayList<Object>> createData(ArrayList<String> input) throws InvalidArgumentsException {
        if (input.size() > 1)
            throw new InvalidArgumentsException("ERRROR: you must input only one argument");

        ResourceType type = CliUtil.checkResourceType(input.get(0));

        return new Pair<>(
                CHOOSE_RES,
                new ArrayList<>(Arrays.asList(new Resource(type, 1)))
        );
    }
}
