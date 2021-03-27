package it.polimi.ingsw.IntelliCranio.server.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.server.GameManager;
import it.polimi.ingsw.IntelliCranio.server.Packet;
import it.polimi.ingsw.IntelliCranio.server.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
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

    /**
     * Remove two leader cards from the player list.
     * <summary>
     *     Check consistency of client inputs and signal any error using an exception
     *     passing the code of the action as parameter.
     *     <p>
     *         If input is correct, the two leader cards selected by the client are removed
     *         from the player list of cards.
     *     </p>
     * </summary>
     * @param manager
     * @throws InvalidArgumentsException Takes the action code as parameter to signal
     * the game manager what operation went wrong
     */
    @Override
    public void playAction(GameManager manager) throws InvalidArgumentsException {

        //Error Handling
        if(selection.size() != 2)
            throw new InvalidArgumentsException(Packet.InstructionCode.DISCARD_INIT_LEADERS);

        Player currentPlayer = manager.getCurrentPlayer();

        selection.forEach(selected -> {
            //Get the card that match the selected one by ID
            LeadCard cardToDiscard = currentPlayer.getLeaders().stream().filter(card -> {
                return card.getID().equals(selected.getID());
            }).findFirst().get();

            //Remove the card from the list
            currentPlayer.getLeaders().remove(cardToDiscard);
        });

    }
}
