package it.polimi.ingsw.IntelliCranio.server.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.models.market.CardMarket;

import java.util.ArrayList;

public class BuyDevCard implements Action{

    private int row, col;
    private CardMarket market;

    /**
     * Constructor
     * <p>
     *     Gets all the necessary parameter from json strings.
     * </p>
     * @param jsonArgs
     */
    public BuyDevCard(ArrayList<String> jsonArgs) throws InvalidArgumentsException {

        Gson gson = new Gson();

        row = gson.fromJson(jsonArgs.get(0), new TypeToken<Integer>(){}.getType());
        col = gson.fromJson(jsonArgs.get(1), new TypeToken<Integer>(){}.getType());
        market = gson.fromJson(jsonArgs.get(2), new TypeToken<CardMarket>(){}.getType());

    }

    @Override
    public ArrayList<String> playAction(Game manager) throws InvalidArgumentsException {
        throw new UnsupportedOperationException();
    }
}
