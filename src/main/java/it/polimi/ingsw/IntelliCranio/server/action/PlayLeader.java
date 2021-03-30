package it.polimi.ingsw.IntelliCranio.server.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.server.GameManager;
import it.polimi.ingsw.IntelliCranio.server.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;

import java.util.ArrayList;

public class PlayLeader implements Action{

    private LeadCard card;

    /**
     * Constructor
     * <p>
     *     Gets all the necessary parameter from json strings.
     * </p>
     * @param jsonArgs
     */
    public PlayLeader(ArrayList<String> jsonArgs) throws InvalidArgumentsException {

        Gson gson = new Gson();

        card = gson.fromJson(jsonArgs.get(0), new TypeToken<LeadCard>(){}.getType());
    }

    @Override
    public ArrayList<String> playAction(GameManager manager) throws InvalidArgumentsException {
        throw new UnsupportedOperationException();
    }
}
