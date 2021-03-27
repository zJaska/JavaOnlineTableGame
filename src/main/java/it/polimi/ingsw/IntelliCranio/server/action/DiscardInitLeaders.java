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
     * Gets all the necessary parameter from json strings.
     * @param jsonArgs List of all the LeadCard to discard
     */
    public DiscardInitLeaders(ArrayList<String> jsonArgs) {

        Gson gson = new Gson();


        selection = gson.fromJson(jsonArgs.get(0), new TypeToken<ArrayList<LeadCard>>(){}.getType());

    }

    @Override
    public void playAction(GameManager manager) {

        Player currentPlayer = manager.getCurrentPlayer();

        selection.forEach(selected -> {
            LeadCard cardToDiscard = currentPlayer.getLeaders().stream().filter(card -> {
                return card.getID().equals(selected.getID());
            }).findFirst().get();

            currentPlayer.getLeaders().remove(cardToDiscard);
        });

    }
}
