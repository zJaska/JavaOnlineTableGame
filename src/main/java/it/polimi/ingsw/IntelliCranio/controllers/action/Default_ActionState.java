package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.models.resource.CardResource;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.Lists;

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

        if(packet == null || packet.getInstructionCode() == null) throw new InvalidArgumentsException(CODE_NULL);

        if(packet.getInstructionCode() == PLAY_LEADER) playLeader(packet.getArgs());
        if(packet.getInstructionCode() == DISCARD_LEAD) discardLeader(packet.getArgs());
        if(packet.getInstructionCode() == MNG_WARE) manageWarehouse();
        if(packet.getInstructionCode() == CARD_MARKET) cardMarket();
        if(packet.getInstructionCode() == RES_MARKET) resourceMarket();

        throw new InvalidArgumentsException(CODE_NOT_ALLOWED); //Code in packet is not allowed in this state
    }

    private void playLeader(ArrayList<Object> args) throws InvalidArgumentsException {

        LeadCard card; //The argument expected

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

        //region Argument validation

        //Null Condition
        if(card == null) throw new InvalidArgumentsException(NULL_ARG);

        //Not in Hand Condition
        if(!player.hasLeader(card)) throw new InvalidArgumentsException(SELECTION_INVALID);

        AtomicBoolean error = new AtomicBoolean(false);

        //Resource Requirements Condition
        ArrayList<Resource> playerResources = player.getAllResources();
        ArrayList<FinalResource> resourceRequirements = player.getLeader(card).getResourceRequirements();

        if(resourceRequirements != null)
            resourceRequirements.forEach(resReq -> {

                //No match of type and amount
                if(playerResources.stream().noneMatch(pRes ->
                        (pRes.getType() == resReq.getType() && pRes.getAmount() >= resReq.getAmount())))
                    error.set(true);

            });

        if(error.get()) throw new InvalidArgumentsException(SELECTION_INVALID);

        //Card Requirements Condition
        ArrayList<CardResource> playerDevCards = Lists.toCardResource(player.getAllDevCards());
        ArrayList<CardResource> cardRequirements = player.getLeader(card).getCardRequirements();

        cardRequirements.forEach(cReq -> {
            //If not 0, the lead card has a specific level requirement
            if(cReq.getLevel() != 0) {
                //No specific match of type and level
                if(playerDevCards.stream().noneMatch(pDev ->
                        (pDev.getType() == cReq.getType() && pDev.getLevel() == cReq.getLevel())))
                    error.set(true);
            }
            else {
                int typeAmount = playerDevCards.stream().filter(pDev -> pDev.getType() == cReq.getType())
                        .map(CardResource::getAmount).reduce(Integer::sum).get();

                if(typeAmount < cReq.getAmount())
                    error.set(true);
            }
        });

        if(error.get()) throw new InvalidArgumentsException(SELECTION_INVALID);

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        player.getLeader(card).activateCard();

        //endregion
    }

    private void discardLeader(ArrayList<Object> args) throws InvalidArgumentsException {

        LeadCard card; //Expected argument

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

        //region Argument validation

        //Null Condition
        if(card == null) throw new InvalidArgumentsException(NULL_ARG);

        //Not in Hand
        if(!player.hasLeader(card)) throw new InvalidArgumentsException(SELECTION_INVALID);

        //Already Active
        if(player.getLeader(card).isActive()) throw new InvalidArgumentsException(SELECTION_INVALID);

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        player.removeLeader(card);

        //endregion
    }

    private void manageWarehouse() {

        //region Conversion of args from packet
        //endregion

        //I get here if there are no problems with arguments conversion

        //region Argument validation
        //endregion

        //I get here if the argument is valid

        //region Execute operation
        action.setActionState(new ManageWarehouse_ActionState(action, true), MNG_WARE);
        //endregion
    }

    private void cardMarket() {

        //region Conversion of args from packet
        //endregion

        //I get here if there are no problems with arguments conversion

        //region Argument validation
        //endregion

        //I get here if the argument is valid

        //region Execute operation
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);
        //endregion
    }

    private void resourceMarket() {

        //region Conversion of args from packet
        //endregion

        //I get here if there are no problems with arguments conversion

        //region Argument validation
        //endregion

        //I get here if the argument is valid

        //region Execute operation
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        //endregion
    }

    private void activateProduction() {

        //region Conversion of args from packet
        //endregion

        //I get here if there are no problems with arguments conversion

        //region Argument validation
        //endregion

        //I get here if the argument is valid

        //region Execute operation
        action.setActionState(new ActivateProduction_ActionState(action), ACT_PROD);
        //endregion
    }

}
