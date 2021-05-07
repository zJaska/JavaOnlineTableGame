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

        if(packet == null || packet.getInstructionCode() == null) {
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
        if(args.size() == 0) {
            InvalidArgumentsException e = new InvalidArgumentsException(NOT_ENOUGH_ARGS);

            String errorMessage = "OOOPS, something went wrong! Server received less arguments than expected";
            errorMessage += "\nArguments received: " + args.size();
            errorMessage += "\nArguments expected: 1";

            e.setErrorMessage(errorMessage);

            throw e;
        }
        if(args.size() > 1) {
            InvalidArgumentsException e = new InvalidArgumentsException(TOO_MANY_ARGS);

            String errorMessage = "OOOPS, something went wrong! Server received more arguments than expected";
            errorMessage += "\nArguments received: " + args.size();
            errorMessage += "\nArguments expected: 1";

            e.setErrorMessage(errorMessage);

            throw e;
        }

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
        if(card == null) {
            InvalidArgumentsException e = new InvalidArgumentsException(NULL_ARG);

            String errorMessage = "OOOPS, something went wrong! Server received a null element";
            errorMessage += "\nElement expected: Leader Card";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Not in Hand Condition
        if(!player.hasLeader(card)) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! Server received a card you don't have";

            e.setErrorMessage(errorMessage);

            throw e;
        }

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

        if(error.get()) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
            String errorMessage = "OOOPS, something went wrong! You don't have enough resources to activate this card";
            e.setErrorMessage(errorMessage);
            throw e;
        }

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

        if(error.get()) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);
            String errorMessage = "OOOPS, something went wrong! You don't have enough development cards to activate this card";
            e.setErrorMessage(errorMessage);
            throw e;
        }

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
        if(args.size() == 0) {
            InvalidArgumentsException e = new InvalidArgumentsException(NOT_ENOUGH_ARGS);

            String errorMessage = "OOOPS, something went wrong! Server received less arguments than expected";
            errorMessage += "\nArguments received: " + args.size();
            errorMessage += "\nArguments expected: 1";

            e.setErrorMessage(errorMessage);

            throw e;
        }
        if(args.size() > 1) {
            InvalidArgumentsException e = new InvalidArgumentsException(TOO_MANY_ARGS);

            String errorMessage = "OOOPS, something went wrong! Server received more arguments than expected";
            errorMessage += "\nArguments received: " + args.size();
            errorMessage += "\nArguments expected: 1";

            e.setErrorMessage(errorMessage);

            throw e;
        }

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
        if(card == null) {
            InvalidArgumentsException e = new InvalidArgumentsException(NULL_ARG);

            String errorMessage = "OOOPS, something went wrong! Server received a null element";
            errorMessage += "\nElement expected: Leader Card";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Not in Hand
        if(!player.hasLeader(card)) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! Server received a card you don't have";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Already Active
        if(player.getLeader(card).isActive()) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! The card seems to be active";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        player.removeLeader(card);
        player.addFaith();

        Save.saveGame(game);

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
        action.setActionState(new ManageWarehouse_ActionState(action, DEFAULT), MNG_WARE);
        game.getCurrentPlayer().setLastAction(MNG_WARE);
        Save.saveGame(game);
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
        game.getCurrentPlayer().setLastAction(CARD_MARKET);
        Save.saveGame(game);
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
        game.getCurrentPlayer().setLastAction(RES_MARKET);
        Save.saveGame(game);
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
        game.getCurrentPlayer().setLastAction(ACT_PROD);
        Save.saveGame(game);
        //endregion
    }

}
