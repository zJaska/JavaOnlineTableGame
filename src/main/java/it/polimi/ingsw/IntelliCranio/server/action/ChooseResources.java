package it.polimi.ingsw.IntelliCranio.server.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.server.GameManager;
import it.polimi.ingsw.IntelliCranio.server.Packet;
import it.polimi.ingsw.IntelliCranio.server.resource.Resource;

import java.util.ArrayList;

public class ChooseResources implements Action{

    private int amount;
    private ArrayList<Resource> selection;

    /**
     * Constructor
     * <p>
     *     Gets all the necessary parameter from json strings.
     * </p>
     * @param jsonArgs
     */
    public ChooseResources(ArrayList<String> jsonArgs) {

        Gson gson = new Gson();

        amount = gson.fromJson(jsonArgs.get(0), new TypeToken<Integer>(){}.getType());
        selection = gson.fromJson(jsonArgs.get(1), new TypeToken<ArrayList<Resource>>(){}.getType());
    }

    @Override
    public void playAction(GameManager manager, Packet packet) {
        throw new UnsupportedOperationException();
    }
}
