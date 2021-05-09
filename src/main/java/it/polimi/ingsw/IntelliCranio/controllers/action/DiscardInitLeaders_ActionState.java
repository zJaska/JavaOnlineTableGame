package it.polimi.ingsw.IntelliCranio.controllers.action;

import com.google.gson.Gson;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.Checks;
import it.polimi.ingsw.IntelliCranio.util.Save;

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

        if(packet == null || packet.getInstructionCode() == null || packet.getArgs() == null) {
            InvalidArgumentsException e = new InvalidArgumentsException(CODE_NULL);
            String errorMessage = "OOOPS, something went wrong! No action received";
            e.setErrorMessage(errorMessage);
            throw e;
        }

        switch (packet.getInstructionCode()) {
            case DISCARD_LEAD: discard(packet.getArgs()); return;
            default:
                InvalidArgumentsException e = new InvalidArgumentsException(CODE_NOT_ALLOWED);
                String errorMessage = "OOOPS, something went wrong! Action invalid in current state";
                e.setErrorMessage(errorMessage);
                throw e; //Code in packet is not allowed in this state
        }
    }

    private void discard(ArrayList<Object> args) throws InvalidArgumentsException {

        LeadCard card; //Expected argument for this operation

        //region Conversion of args from packet

        Checks.argsAmount(args, 1);

        try {
            card = (LeadCard) args.get(0);
        } catch (Exception ex) {
            InvalidArgumentsException e = new InvalidArgumentsException(TYPE_MISMATCH);

            String errorMessage = "OOOPS, something went wrong! Server received an element invalid for this action";
            errorMessage += "\nElement expected: Leader Card";

            e.setErrorMessage(errorMessage);

            throw e;
        }
        //endregion

        //I get here if there are no problems with arguments conversion

        Player player = game.getCurrentPlayer();

        //region Input validation

        //Null Condition
        Checks.nullElement(card, "Leader Card");

        //Not in hand Condition
        Checks.notInHand(player, card);

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        //If player already has only two cards, the game shouldn't be in this state. Return the new state then.
        if(player.getLeaders().size() == 2) {

            //Check for automatic end turn
            if (game.getInitRes(game.getCurrentPlayerIndex()) == 0) {
                game.endTurn = true;
                player.setLastAction(DEFAULT);
            } else {
                action.setActionState(new ChooseInitResources_ActionState(action), CHOOSE_INIT_RES);
                player.setLastAction(CHOOSE_INIT_RES);
            }

            return;
        }

        //Remove the selected card from player
        player.removeLeader(card);

        if(player.getLeaders().size() == 2) {

            //Check for automatic end turn
            if (game.getInitRes(game.getCurrentPlayerIndex()) == 0) {
                game.endTurn = true;
                player.setLastAction(DEFAULT);
            } else {
                action.setActionState(new ChooseInitResources_ActionState(action), CHOOSE_INIT_RES);
                player.setLastAction(CHOOSE_INIT_RES);
            }
        }


        Save.saveGame(game); //Save the state of the game

        //endregion

    }
}
