package it.polimi.ingsw.IntelliCranio.server.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.server.GameManager;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;

import java.util.ArrayList;

public class ActivateProduction implements Action{

    private ArrayList<FinalResource> warehouseCost;
    private ArrayList<FinalResource> strongboxCost;
    private ArrayList<FinalResource> receivedResources;

    /**
     * Constructor
     * <p>
     *     Gets all the necessary parameter from json strings.
     * </p>
     * @param jsonArgs
     */
    public ActivateProduction(ArrayList<String> jsonArgs) {

        Gson gson = new Gson();

        warehouseCost = gson.fromJson(jsonArgs.get(0), new TypeToken<ArrayList<FinalResource>>(){}.getType());
        strongboxCost = gson.fromJson(jsonArgs.get(1), new TypeToken<ArrayList<FinalResource>>(){}.getType());
        receivedResources = gson.fromJson(jsonArgs.get(2), new TypeToken<ArrayList<FinalResource>>(){}.getType());

    }

    @Override
    public void playAction(GameManager manager) throws InvalidArgumentsException {
        throw new UnsupportedOperationException();
    }
}
