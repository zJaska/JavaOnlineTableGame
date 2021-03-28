package it.polimi.ingsw.IntelliCranio;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.IntelliCranio.server.GameManager;
import it.polimi.ingsw.IntelliCranio.server.action.ChooseInitResources;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.server.player.Player;
import it.polimi.ingsw.IntelliCranio.server.player.Warehouse;
import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.server.resource.Resource;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        GameManager manager = new GameManager();
        manager.addPlayer(new Player(new Warehouse(3)), 0);

        Resource[] temp = new Resource[3];
        ArrayList<Resource> res = new ArrayList<>();

        temp[0] = new Resource(FinalResource.ResourceType.STONE, 2);
        temp[1] = new Resource(FinalResource.ResourceType.COIN, 2);
        res.add(new Resource(FinalResource.ResourceType.STONE, 2));
        res.add(new Resource(FinalResource.ResourceType.COIN, 2));

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<String> arg = new ArrayList<>();
        arg.add(gson.toJson(res));
        arg.add(gson.toJson(temp));

        ChooseInitResources action = new ChooseInitResources(arg);
        try {
            action.playAction(manager);
        } catch (InvalidArgumentsException e) {
            e.printStackTrace();
        }


/*
        try {
            CardGenerator.generateLeadCardFile(manager.generateCards());
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        manager.createLeaderCards("src/main/resources/leadcards_config.json", true);
    }
}
