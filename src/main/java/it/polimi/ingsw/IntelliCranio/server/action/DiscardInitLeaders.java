package it.polimi.ingsw.IntelliCranio.server.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.server.GameManager;
import it.polimi.ingsw.IntelliCranio.server.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.server.player.Player;

import java.util.ArrayList;

public class DiscardInitLeaders implements Action{

    private ArrayList<LeadCard> selection;

    /**
     * Constructor
     * <p>
     *     Gets all the necessary parameter from json strings.
     * </p>
     * @param jsonArgs
     */
    public DiscardInitLeaders(ArrayList<String> jsonArgs) {

        Gson gson = new Gson();

        selection = gson.fromJson(jsonArgs.get(0), new TypeToken<ArrayList<LeadCard>>(){}.getType());

    }

    @Override
    public void playAction(GameManager manager) {

        Player currentPlayer = manager.getCurrentPlayer();

        selection.forEach(card -> {
            currentPlayer.getLeaders().remove(card);
        });

    }
}
