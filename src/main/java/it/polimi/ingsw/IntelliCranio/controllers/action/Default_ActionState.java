package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.models.resource.CardResource;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.Checks;
import it.polimi.ingsw.IntelliCranio.util.Lists;
import it.polimi.ingsw.IntelliCranio.util.Save;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;

public class Default_ActionState extends ActionState {

    private Game game;

    public Default_ActionState(Action action) {
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
            case PLAY_LEADER: playLeader(packet.getArgs()); return;
            case DISCARD_LEAD: discardLeader(packet.getArgs()); return;
            case MNG_WARE: manageWarehouse(); return;
            case CARD_MARKET: cardMarket(); return;
            case RES_MARKET: resourceMarket(); return;
            case ACT_PROD: activateProduction(); return;
            default:
                InvalidArgumentsException e = new InvalidArgumentsException(CODE_NOT_ALLOWED);
                String errorMessage = "OOOPS, something went wrong! Action invalid in current state";
                e.setErrorMessage(errorMessage);
                throw e; //Code in packet is not allowed in this state
        }
    }

    private void playLeader(ArrayList<Object> args) throws InvalidArgumentsException {

        LeadCard card; //The argument expected

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

        //region Argument validation

        //Null Condition
        Checks.nullElement(card, "Leader Card");

        //Not in Hand Condition
        Checks.notInHand(player, card);


        //Resource Requirements Condition
        Checks.resourceRequirements(player, card);

        //Card Requirements Condition
        Checks.cardRequirements(player, card);

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        player.getLeader(card).activateCard();
        Save.saveGame(game);

        //endregion
    }

    private void discardLeader(ArrayList<Object> args) throws InvalidArgumentsException {

        LeadCard card; //Expected argument

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

        //region Argument validation

        //Null Condition
        Checks.nullElement(card, "Leader Card");

        //Not in Hand
        Checks.notInHand(player, card);

        //Already Active
        Checks.cardActive(player, card);

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        player.removeLeader(card);
        player.addFaith();

        Save.saveGame(game);

        //endregion
    }

    private void manageWarehouse() {

        //region Execute operation
        action.setActionState(new ManageWarehouse_ActionState(action, DEFAULT), MNG_WARE);
        game.getCurrentPlayer().setLastAction(MNG_WARE);
        Save.saveGame(game);
        //endregion
    }

    private void cardMarket() throws InvalidArgumentsException {

        Player currentPlayer = game.getCurrentPlayer();

        Checks.hasPlayed(currentPlayer); //Throw exception if player already did a turn action

        //region Execute operation
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);
        currentPlayer.setLastAction(CARD_MARKET);
        Save.saveGame(game);
        //endregion
    }

    private void resourceMarket() throws InvalidArgumentsException {

        Player currentPlayer = game.getCurrentPlayer();

        Checks.hasPlayed(currentPlayer); //Throw exception if player already did a turn action

        //region Execute operation
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        game.getCurrentPlayer().setLastAction(RES_MARKET);
        Save.saveGame(game);
        //endregion
    }

    private void activateProduction() throws InvalidArgumentsException {

        Player currentPlayer = game.getCurrentPlayer();

        Checks.hasPlayed(currentPlayer); //Throw exception if player already did a turn action

        //region Execute operation
        action.setActionState(new ActivateProduction_ActionState(action), ACT_PROD);
        game.getCurrentPlayer().setLastAction(ACT_PROD);
        Save.saveGame(game);
        //endregion
    }

}
