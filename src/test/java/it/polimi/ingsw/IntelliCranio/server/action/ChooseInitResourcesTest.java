package it.polimi.ingsw.IntelliCranio.server.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.IntelliCranio.server.GameManager;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.server.resource.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ChooseInitResourcesTest {

    @Test
    void playAction() throws InvalidArgumentsException {
        GameManager manager = new GameManager();
        Action action;
        Executable executable;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        ArrayList<String> args = new ArrayList<>(); //args is empty
        ArrayList<Resource> testSelection = new ArrayList<>(); //test selection is empty

        //region Test when args list is empty
        executable = new Executable() {
            Action action = new ChooseInitResources(args); //args is empty
            @Override
            public void execute() throws Throwable {
                action.playAction(manager);
            }
        };

        assertThrows(InvalidArgumentsException.class, executable);
        //endregion

        //region Test when args has an empty list serialized in json
        args.add(gson.toJson(testSelection)); //args has one element but from empty list

        executable = new Executable() {
            Action action = new ChooseInitResources(args); //args is empty
            @Override
            public void execute() throws Throwable {
                action.playAction(manager);
            }
        };

        action = new ChooseInitResources(args);
        assertDoesNotThrow(executable);
        assertNull(action.playAction(manager));
        //endregion

        //region Test when selection has 1 item but expected is 0
        testSelection.add(new Resource(FinalResource.ResourceType.STONE, 1));
        args.clear();
        args.add(gson.toJson(testSelection)); //Selection has 1 item

        executable = new Executable() {
            Action action = new ChooseInitResources(args); //args is empty
            @Override
            public void execute() throws Throwable {
                action.playAction(manager);
            }
        };

        assertThrows(InvalidArgumentsException.class, executable);

        //endregion

    }
}