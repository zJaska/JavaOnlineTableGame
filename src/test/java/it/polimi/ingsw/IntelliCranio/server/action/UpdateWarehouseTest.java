package it.polimi.ingsw.IntelliCranio.server.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.server.GameManager;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.server.player.Player;
import it.polimi.ingsw.IntelliCranio.server.player.Warehouse;
import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.server.resource.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UpdateWarehouseTest {

    @Test
    void playAction() throws InvalidArgumentsException {
        Action action;
        Executable executable;
        GameManager manager = new GameManager();
        Player player = new Player();
        Warehouse serverWarehouse = new Warehouse(3);
        Resource[] serverDepot = new Resource[3];

        Gson toGson = new GsonBuilder().setPrettyPrinting().create();
        Gson fromGson = new Gson();

        serverWarehouse.update(serverDepot, new ArrayList<>());
        player.setWarehouse(serverWarehouse);
        manager.addPlayer(player, 0);

        Resource[] clientDepot;
        ArrayList<Resource> clientExtraResources;

        //region Test when server depot is empty and is passed a empty depot and empty extra resources
        clientDepot = new Resource[3]; //New Depot
        clientExtraResources = new ArrayList<>();
        ArrayList<String> argsTest1 = new ArrayList<>();
        argsTest1.add(toGson.toJson(clientDepot));
        argsTest1.add(toGson.toJson(clientExtraResources));

        action = new UpdateWarehouse(argsTest1);
        ArrayList<String> returnArgsTest1 = action.playAction(manager);
        String argTest1 = returnArgsTest1.get(0);
        int retTest1 = fromGson.fromJson(argTest1, new TypeToken<Integer>(){}.getType());
        assertEquals(0, retTest1);
        //endregion

        //region Server has 1 element in depot and is passed an empty depot and empty resources
        serverDepot[0] = new Resource(FinalResource.ResourceType.STONE, 1);
        serverWarehouse.update(serverDepot, new ArrayList<>());

        ArrayList<String> argsTest2 = new ArrayList<>();
        argsTest2.add(toGson.toJson(clientDepot));
        argsTest2.add(toGson.toJson(clientExtraResources));

        executable = new Executable() {
            @Override
            public void execute() throws Throwable {
                Action action = new UpdateWarehouse(argsTest2); //args is empty
                    action.playAction(manager);
            }
        };

        action = new UpdateWarehouse(argsTest1);
        String retArgTest2 = returnArgsTest1.get(0);
        int retTest2 = fromGson.fromJson(retArgTest2, new TypeToken<Integer>(){}.getType());
        assertThrows(InvalidArgumentsException.class, executable);
        //endregion

        //region Test using ChooseInitResources
        Action initResAction;
        ArrayList<Resource> selectedRes = new ArrayList<>();
        selectedRes.add(new Resource(FinalResource.ResourceType.SHIELD, 1));
        selectedRes.add(new Resource(FinalResource.ResourceType.SERVANT, 1));
        ArrayList<String> args = new ArrayList<>();
        args.add(toGson.toJson(selectedRes));

        initResAction = new ChooseInitResources(args);
        ArrayList<String> returnTest3 = initResAction.playAction(manager);
        manager.setLastActionReturnArgs(returnTest3);

        clientDepot[0] = new Resource(FinalResource.ResourceType.STONE, 1);

        ArrayList<String> test3Args = new ArrayList<>();
        test3Args.add(toGson.toJson(clientDepot));
        test3Args.add(toGson.toJson(selectedRes));

        action = new UpdateWarehouse(test3Args);
        action.playAction(manager);
    }
}