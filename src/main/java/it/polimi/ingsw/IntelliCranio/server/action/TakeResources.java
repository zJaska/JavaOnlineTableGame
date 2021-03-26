package it.polimi.ingsw.IntelliCranio.server.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.server.GameManager;
import it.polimi.ingsw.IntelliCranio.server.Packet;
import it.polimi.ingsw.IntelliCranio.server.market.ResourceMarket;

import java.util.ArrayList;

public class TakeResources implements Action{

    private ResourceMarket market;
    private String code; //Identify row or col number "c1", "r2", ...

    /**
     * Constructor
     * <p>
     *     Gets all the necessary parameter from json strings.
     * </p>
     * @param jsonArgs
     */
    public TakeResources(ArrayList<String> jsonArgs) {

        Gson gson = new Gson();

        market = gson.fromJson(jsonArgs.get(0), new TypeToken<ResourceMarket>(){}.getType());
        code = gson.fromJson(jsonArgs.get(1), new TypeToken<String>(){}.getType());
    }

    @Override
    public void playAction(GameManager manager, Packet packet) {
        throw new UnsupportedOperationException();
    }
}
