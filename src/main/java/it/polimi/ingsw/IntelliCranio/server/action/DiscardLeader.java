package it.polimi.ingsw.IntelliCranio.server.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.server.GameManager;
import it.polimi.ingsw.IntelliCranio.server.Packet;
import it.polimi.ingsw.IntelliCranio.server.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.server.player.Player;

import java.util.ArrayList;

public class DiscardLeader implements Action{

    private LeadCard card;

    /**
     * Gets the card to discard as json argument
     * <p>
     *     Gets all the necessary parameter from json strings.
     * </p>
     * @param jsonArgs
     */
    public DiscardLeader(ArrayList<String> jsonArgs) {

        Gson gson = new Gson();

        if(jsonArgs.size() > 0)
            card = gson.fromJson(jsonArgs.get(0), new TypeToken<LeadCard>(){}.getType());
    }

    @Override
    public ArrayList<String> playAction(GameManager manager) throws InvalidArgumentsException {

        argumentValidation(manager);

        //I get here if there are no problems with input

        Player currentPlayer = manager.getCurrentPlayer();

        //Remove matching ID card
        currentPlayer.getLeaders().removeIf(leader -> {
            return leader.getID().equals(card.getID());
        });

        manager.addPlayerFaith(currentPlayer, 1); //ERROR: Method not implemented yet

        return null;
    }

    private void argumentValidation(GameManager manager) throws InvalidArgumentsException {

        //NonNull Condition
        if(card == null)
            throw new InvalidArgumentsException(Packet.InstructionCode.DISCARD_LEADER);

        //Check if the selected card is actually in player hand
        ArrayList<LeadCard> playerLeaders = manager.getCurrentPlayer().getLeaders();

        if(playerLeaders.stream().noneMatch(leader -> {
            return card.getID().equals(leader.getID());
        }))
            throw new InvalidArgumentsException(Packet.InstructionCode.DISCARD_LEADER);

    }
}
