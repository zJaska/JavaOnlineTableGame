package it.polimi.ingsw.IntelliCranio.controllers.action;

import com.google.gson.Gson;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;

import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;

public class DiscardInitLeadersAction implements ActionI {

    private Game game;

    @Override
    public ActionI execute(Game game, Packet packet) throws InvalidArgumentsException {
        this.game = game;

        if(packet == null || packet.getInstructionCode() == null) throw new InvalidArgumentsException(CODE_NULL);
        if(packet.getInstructionCode() == DISCARD_LEAD) return discard(packet.getArgs());

        throw new InvalidArgumentsException(CODE_NOT_ALLOWED); //Code in packet is not allowed in this state
    }

    private ActionI discard(ArrayList<String> args) throws InvalidArgumentsException {

        LeadCard card; //Expected argument for this operation

        //region Conversion of args from packet
        Gson gson = new Gson();

        if(args.size() == 0) throw new InvalidArgumentsException(NOT_ENOUGH_ARGS);
        if(args.size() > 1) throw new InvalidArgumentsException(TOO_MANY_ARGS);

        try {
            card = gson.fromJson((args.get(0)), LeadCard.class);
        } catch (Exception e) {
            throw new InvalidArgumentsException(TYPE_MISMATCH);
        }
        //endregion

        //I get here if there are no problems with arguments conversion

        ArrayList<LeadCard> playerCards = game.getCurrentPlayer().getLeaders();

        //region Input validation

        //Null Condition
        if(card == null) throw new InvalidArgumentsException(NULL_ARG);

        //Not in hand Condition
        if(playerCards.stream().noneMatch(pCard -> pCard.getID().equals(card.getID())))
            throw new InvalidArgumentsException(NOT_IN_HAND);

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        //If player already has only two cards, the game shouldn't be in this state. Return the new state then.
        if(playerCards.size() == 2) return new ChooseInitResourcesAction();

        //Remove the selected card from player
        playerCards.removeIf(pCard -> pCard.getID().equals(card.getID()));

        //Check for return type
        if(playerCards.size() > 2)
            return null; //No need to change state or reset the current one
        else
            return new ChooseInitResourcesAction(); //Return the new state to go to

        //endregion

    }
}
