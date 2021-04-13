package it.polimi.ingsw.IntelliCranio.server.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.models.resource.CardResource;

import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.server.Packet.InstructionCode.*;

public class PlayLeader implements Action{

    private LeadCard card;

    /**
     * Create the new action, get the card the client want to activate.
     * <p>
     *     Gets all the necessary parameter from json strings.
     * </p>
     * @param jsonArgs A single arg with the card to activate
     * @throws InvalidArgumentsException
     */
    public PlayLeader(ArrayList<String> jsonArgs) throws InvalidArgumentsException {

        Gson gson = new Gson();

        try {
            card = gson.fromJson(jsonArgs.get(0), new TypeToken<LeadCard>(){}.getType());
        } catch (Exception e) {
            throw new InvalidArgumentsException(PLAY_LEADER);
        }
    }

    /**
     * Check the requisites for the card, activate the card if everything is correct,
     * throw InvalidArgumentException otherwise.
     * @param manager
     * @return null
     * @throws InvalidArgumentsException
     */
    @Override
    public ArrayList<String> playAction(Game manager) throws InvalidArgumentsException {

        argumentValidation(manager);

        //I get here if everything is ok

        //Activate the selected card
        ArrayList<LeadCard> playerLeaders = manager.getCurrentPlayer().getLeaders();
        playerLeaders.stream()
                .filter(leader -> leader.getID().equals(card.getID()))
                .findFirst().get()
                .activateCard();

        throw new UnsupportedOperationException();
    }

    private void argumentValidation(Game manager) throws InvalidArgumentsException {

        //NonNull Condition
        if(card == null)
            throw new InvalidArgumentsException(PLAY_LEADER);

        //Not in hand Condition
        if(manager.getCurrentPlayer().getLeaders().stream()
                .noneMatch(leader -> leader.getID().equals(card.getID())))
            throw new InvalidArgumentsException(PLAY_LEADER);

        //Already active Condition
        if(manager.getCurrentPlayer().getLeaders().stream()
                .anyMatch(leader -> (leader.getID().equals(card.getID()) && leader.isActive())))
            throw new InvalidArgumentsException(PLAY_LEADER);

        //Invalid Requirement Condition

        //region Card Requirements
        if(card.getCardRequirements() != null) {
            ArrayList<CardResource> cardRequirements = card.getCardRequirements();
            ArrayList<DevCard> playerCards = manager.getCurrentPlayer().getAllDevCards();

            for (CardResource requirement : cardRequirements) {
                //Case: Amount of card with any level
                if (requirement.getMinLevelRequired() == 0) {
                    if (playerCards.stream()
                            .filter(card -> card.getType() == requirement.getType())
                            .count() < requirement.getAmount())
                        throw new InvalidArgumentsException(PLAY_LEADER);
                }

                //Case: Min Level required
                if(requirement.getMinLevelRequired() > 0) {

                }

                throw new InvalidArgumentsException(PLAY_LEADER);
            }
        }
        //endregion
    }
}
