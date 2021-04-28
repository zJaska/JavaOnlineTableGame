package it.polimi.ingsw.IntelliCranio.controllers.action;

import com.google.gson.Gson;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;

import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;

public class DiscardInitLeaders_ActionState extends ActionState {

    private Game game;

    public DiscardInitLeaders_ActionState(Action action) {
        super(action);
    }

    @Override
    public void execute(Game game, Packet packet) throws InvalidArgumentsException {
        this.game = game;

        if(packet == null || packet.getInstructionCode() == null) throw new InvalidArgumentsException(CODE_NULL);

        if(packet.getInstructionCode() == DISCARD_LEAD) discard(packet.getArgs());

        throw new InvalidArgumentsException(CODE_NOT_ALLOWED); //Code in packet is not allowed in this state
    }

    private void discard(ArrayList<Object> args) throws InvalidArgumentsException {

        LeadCard card; //Expected argument for this operation

        //region Conversion of args from packet

        if(args.size() == 0) throw new InvalidArgumentsException(NOT_ENOUGH_ARGS);
        if(args.size() > 1) throw new InvalidArgumentsException(TOO_MANY_ARGS);

        try {
            card = (LeadCard) args.get(0);
        } catch (Exception e) {
            throw new InvalidArgumentsException(TYPE_MISMATCH);
        }
        //endregion

        //I get here if there are no problems with arguments conversion

        Player player = game.getCurrentPlayer();

        //region Input validation

        //Null Condition
        if(card == null) throw new InvalidArgumentsException(NULL_ARG);

        //Not in hand Condition
        if(!player.hasLeader(card))
            throw new InvalidArgumentsException(SELECTION_INVALID);

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        //If player already has only two cards, the game shouldn't be in this state. Return the new state then.
        if(player.getLeaders().size() == 2)
            action.setActionState(new ChooseInitResources_ActionState(action));

        //Remove the selected card from player
        player.removeLeader(card);

        if(player.getLeaders().size() == 2)
            action.setActionState(new ChooseInitResources_ActionState(action));

        //endregion

    }
}
