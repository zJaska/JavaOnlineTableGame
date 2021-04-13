package it.polimi.ingsw.IntelliCranio.server.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.server.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.models.player.Player;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class DiscardInitLeaders implements Action{

    private ArrayList<LeadCard> selection;

    /**
     * Gets all the necessary parameter from json strings.
     * @param jsonArgs List of all the LeadCard to discard
     */
    public DiscardInitLeaders(ArrayList<String> jsonArgs) throws InvalidArgumentsException {

        Gson gson = new Gson();

        if (jsonArgs.size() > 0)

            try {
                selection = gson.fromJson(jsonArgs.get(0), new TypeToken<ArrayList<LeadCard>>(){}.getType());
            } catch (Exception e) {
                throw new InvalidArgumentsException(InstructionCode.DISCARD_INIT_LEADERS);
            }

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
     * @return
     */
    @Override
    public ArrayList<String> playAction(Game manager) throws InvalidArgumentsException {

        argumentValidation(manager);

        //I get here if there are no problems with input args

        Player currentPlayer = manager.getCurrentPlayer();

        selection.forEach(selected -> {
            //Get the card that match the selected one by ID
            LeadCard cardToDiscard = currentPlayer.getLeaders().stream()
                    .filter(card -> card.getID().equals(selected.getID()))
                    .findFirst().get();

            //Remove the card from the list
            currentPlayer.getLeaders().remove(cardToDiscard);
        });

        return null;
    }

    private void argumentValidation(Game manager) throws InvalidArgumentsException {

        //NonNull Condition
        if(selection == null)
            throw new InvalidArgumentsException(InstructionCode.DISCARD_INIT_LEADERS);

        //Invalid Amount Condition
        if(selection.size() != 2)
            throw new InvalidArgumentsException(InstructionCode.DISCARD_INIT_LEADERS);

        //Duplicated Condition
        if(selection.get(0).getID().equals(selection.get(1).getID())) //IDs Match
            throw new InvalidArgumentsException(InstructionCode.DISCARD_INIT_LEADERS);

        //Not in player hand Condition
        Player currentPlayer = manager.getCurrentPlayer();
        AtomicBoolean error = new AtomicBoolean(false);

        selection.forEach(selected -> {
            if(currentPlayer.getLeaders().stream().noneMatch(card -> card.getID().equals(selected.getID())))
                error.set(true);
        });

        if(error.get())
            throw new InvalidArgumentsException(InstructionCode.DISCARD_INIT_LEADERS);
    }
}
