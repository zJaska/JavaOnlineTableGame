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
import it.polimi.ingsw.IntelliCranio.util.Conversions;
import it.polimi.ingsw.IntelliCranio.util.Lists;
import it.polimi.ingsw.IntelliCranio.util.Save;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;

public class Default_ActionState extends ActionState {

    public Default_ActionState(Action action) {
        super(action);
    }

    /**
     * Execute the action relative to the code received with packet.
     * Actions for this state:
     * PLAY_LEADER: Activate a leader card
     * DISCARD_LEAD: Discard a leader and gain 1 faith
     * MNG_WARE: Enter the manage warehouse state
     * CARD_MARKET: Enter the card market state
     * RES_MARKET: Enter the resource market state
     * ACT_PROD: Enter the activate production state
     * END_TURN: Calls its super implementation
     * @param game The game to update
     * @param packet Packet received from client
     * @throws InvalidArgumentsException
     */
    @Override
    public void execute(Game game, Packet packet) throws InvalidArgumentsException {
        this.game = game;

        Checks.packetCheck(packet);

        switch (packet.getInstructionCode()) {
            case PLAY_LEADER: playLeader(packet.getArgs()); return;
            case DISCARD_LEAD: discardLeader(packet.getArgs()); return;
            case MNG_WARE: manageWarehouse(); return;
            case CARD_MARKET: cardMarket(); return;
            case RES_MARKET: resourceMarket(); return;
            case ACT_PROD: activateProduction(); return;
            case END_TURN: endTurn(); return;
            default:
                InvalidArgumentsException e = new InvalidArgumentsException(CODE_NOT_ALLOWED);
                String errorMessage = "OOOPS, something went wrong! Action invalid in current state";
                e.setErrorMessage(errorMessage);
                throw e; //Code in packet is not allowed in this state
        }
    }


    private void playLeader(ArrayList<Object> args) throws InvalidArgumentsException {

        LeadCard card; //The argument expected

        Checks.argsAmount(args, 1);

        card = Conversions.getLeader(args, 0);

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

        Checks.argsAmount(args, 1);

        card = Conversions.getLeader(args, 0);

        //I get here if there are no problems with arguments conversion

        Player player = game.getCurrentPlayer();

        //region Argument validation

        //Null Condition
        Checks.nullElement(card, "Leader Card");

        //Not in Hand
        Checks.notInHand(player, card);

        //Already Active
        Checks.cardActive(player.getLeader(card));

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        player.removeLeader(card);
        game.addCurrentPlayerFaith();

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
