package it.polimi.ingsw.IntelliCranio.server.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.server.GameManager;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.server.market.ResourceMarket;

import java.util.ArrayList;

public class TakeResources implements Action{

    private char code; //Identify row or col
    private int selected; //Identify the number of row/col
    private String input;

    /**
     * Gets a string representing which row or col is selected.
     * <p>
     *     Gets all the necessary parameter from json strings.
     * </p>
     * @param jsonArgs The string representing row/col and its number
     */
    public TakeResources(ArrayList<String> jsonArgs) {

        Gson gson = new Gson();

        if(jsonArgs.size() > 0)
            input = gson.fromJson(jsonArgs.get(1), new TypeToken<String>(){}.getType());
    }

    @Override
    public ArrayList<String> playAction(GameManager manager) throws InvalidArgumentsException {

        argumentValidation(manager);

        //I get here if there are no problems with input arguments

        throw new UnsupportedOperationException();
    }

    private void argumentValidation(GameManager manager) {

    }
}
