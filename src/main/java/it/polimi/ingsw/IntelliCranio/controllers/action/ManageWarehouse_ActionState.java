package it.polimi.ingsw.IntelliCranio.controllers.action;

import com.google.gson.Gson;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.models.player.Warehouse;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.ability.DepotAbility;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.Save;

import java.util.ArrayList;
import java.util.stream.Stream;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static it.polimi.ingsw.IntelliCranio.server.ability.Ability.AbilityType.*;

public class ManageWarehouse_ActionState extends ActionState {

    private Game game;
    private boolean fromDefault;

    public ManageWarehouse_ActionState(Action action, boolean fromDefault) {
        super(action);
        this.fromDefault = fromDefault;
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
            case SWAP_LINES: swapLines(packet.getArgs()); return;
            case ADD_FROM_EXTRA: addFromExtra(packet.getArgs()); return;
            case REMOVE_FROM_DEPOT: removeFromDepot(packet.getArgs()); return;
            case DEPOT_TO_CARD: depotToCard(packet.getArgs()); return;
            case EXTRA_TO_CARD: extraToCard(packet.getArgs()); return;
            case CANCEL: cancel(); return;
            case CONFIRM: confirm(); return;
            default:
                InvalidArgumentsException e = new InvalidArgumentsException(CODE_NOT_ALLOWED);
                String errorMessage = "OOOPS, something went wrong! Action invalid in current state";
                e.setErrorMessage(errorMessage);
                throw e; //Code in packet is not allowed in this state
        }
    }

    private void swapLines(ArrayList<Object> args) throws InvalidArgumentsException {

        int first, second; //Expected arguments for this operation

        //region Conversion of args from packet

        if(args.size() < 2) {
            InvalidArgumentsException e = new InvalidArgumentsException(NOT_ENOUGH_ARGS);

            String errorMessage = "OOOPS, something went wrong! Server received less arguments than expected";
            errorMessage += "\nArguments received: " + args.size();
            errorMessage += "\nArguments expected: 2";

            e.setErrorMessage(errorMessage);

            throw e;
        }
        if(args.size() > 2) {
            InvalidArgumentsException e = new InvalidArgumentsException(TOO_MANY_ARGS);

            String errorMessage = "OOOPS, something went wrong! Server received more arguments than expected";
            errorMessage += "\nArguments received: " + args.size();
            errorMessage += "\nArguments expected: 2";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        try{
            first = (int)args.get(0);
            second = (int)args.get(1);
        } catch (Exception ex) {
            InvalidArgumentsException e = new InvalidArgumentsException(TYPE_MISMATCH);

            String errorMessage = "OOOPS, something went wrong! Server received some elements invalid for this action";
            errorMessage += "\nElements expected: Integer Numbers";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //endregion

        //I get here if there are no problems with arguments conversion

        Warehouse playerWh = game.getCurrentPlayer().getWarehouse();

        //region Argument validation

        //Same Line Condition
        if(first == second) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! Selected lines are the same";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Negative Line Condition
        if(first < 0 || second < 0) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! One or more lines selected are negative";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Over Size Condition
        if(first >= playerWh.getDepot().length || second >= playerWh.getDepot().length) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! One or more lines selected exceed depot size";

            e.setErrorMessage(errorMessage);

            throw e;
        }
        //endregion

        //I get here if the argument is valid

        //region Execute operation

        ArrayList<Resource> playerExtra = game.getCurrentPlayer().getExtraRes();

        playerWh.swapLines(first, second, playerExtra);

        //NO SAVE FOR THIS ACTION

        //endregion
    }

    private void addFromExtra(ArrayList<Object> args) throws InvalidArgumentsException {

        Resource resource; //First Expected argument
        int depotLine; //Second Expected argument

        //region Conversion of args from packet

        if(args.size() < 2) {
            InvalidArgumentsException e = new InvalidArgumentsException(NOT_ENOUGH_ARGS);

            String errorMessage = "OOOPS, something went wrong! Server received less arguments than expected";
            errorMessage += "\nArguments received: " + args.size();
            errorMessage += "\nArguments expected: 2";

            e.setErrorMessage(errorMessage);

            throw e;
        }
        if(args.size() > 2) {
            InvalidArgumentsException e = new InvalidArgumentsException(TOO_MANY_ARGS);

            String errorMessage = "OOOPS, something went wrong! Server received more arguments than expected";
            errorMessage += "\nArguments received: " + args.size();
            errorMessage += "\nArguments expected: 2";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        try {
            resource = (Resource) args.get(0);
            depotLine = (int) args.get(1);
        }catch (Exception ex) {
            InvalidArgumentsException e = new InvalidArgumentsException(TYPE_MISMATCH);

            String errorMessage = "OOOPS, something went wrong! Server received some elements invalid for this action";
            errorMessage += "\nFirst Element Expected: Resource";
            errorMessage += "\nSecond Element Expected: Integer Number";

            e.setErrorMessage(errorMessage);

            throw e;
        }
        //endregion

        //I get here if there are no problems with arguments conversion

        Player player = game.getCurrentPlayer();
        Warehouse playerWh = game.getCurrentPlayer().getWarehouse();

        //region Argument validation

        //Null Condition
        if(resource == null) {
            InvalidArgumentsException e = new InvalidArgumentsException(NULL_ARG);

            String errorMessage = "OOOPS, something went wrong! Server received a null element";
            errorMessage += "\nElement expected: Leader Card";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Negative Line Condition
        if(depotLine < 0) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! Selected depot line is negative";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Over Size Condition
        if(depotLine >= playerWh.getDepot().length) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! Selected line is over depot size";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Extra Empty Condition
        if(!player.hasExtra()) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! Your extra resources slot is empty";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Resource Not in Extra Condition (blank or faith CAN NOT stay in extra resources)
        if(!player.hasExtra(resource.getType())) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! The type selected is not present in your extra resources slot";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Not Same Resource Condition
        if(playerWh.getType(depotLine) != resource.getType()) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! The type to add is not the same as the one in selected depot line";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Depot Line Full Condition
        if(playerWh.isFull(depotLine)) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! The selected depot line is already full";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        playerWh.addFromExtra(depotLine, resource, player);

        //NO SAVE FOR THIS

        //endregion
    }

    private void removeFromDepot(ArrayList<Object> args) throws InvalidArgumentsException {

        int depotLine; //Expected argument

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
            depotLine = (int) args.get(0);
        } catch (Exception ex) {
            InvalidArgumentsException e = new InvalidArgumentsException(TYPE_MISMATCH);

            String errorMessage = "OOOPS, something went wrong! Server received an element invalid for this action";
            errorMessage += "\nElement expected: Integer Number";

            e.setErrorMessage(errorMessage);

            throw e;
        }
        //endregion

        //I get here if there are no problems with arguments conversion

        Warehouse playerWh = game.getCurrentPlayer().getWarehouse();

        //region Argument validation

        //Negative line Condition
        if(depotLine < 0) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! Selected depot line is negative";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Over Size Condition
        if(depotLine >= playerWh.getDepot().length) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! Selected depot line is over the depot size";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Empty Condition
        if(playerWh.isEmpty(depotLine)) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! Selected depot line is empty";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        Player player = game.getCurrentPlayer();
        playerWh.removeToExtra(depotLine, player);

        //NO SAVE FOR THIS

        //endregion
    }

    private void depotToCard(ArrayList<Object> args) throws InvalidArgumentsException {

        int depotLine; //Expected first argument

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
            depotLine = (int) args.get(0);
        }catch (Exception ex) {
            InvalidArgumentsException e = new InvalidArgumentsException(TYPE_MISMATCH);

            String errorMessage = "OOOPS, something went wrong! Server received an element invalid for this action";
            errorMessage += "\nElement expected: Integer Number";

            e.setErrorMessage(errorMessage);

            throw e;
        }
        //endregion

        //I get here if there are no problems with arguments conversion

        Player player = game.getCurrentPlayer();
        Warehouse playerWh = game.getCurrentPlayer().getWarehouse();
        Resource resource; //Resource to get is depot is not empty
        LeadCard card; //The card to put the resource

        //region Argument validation

        //Invalid State Condition
        if(fromDefault) {
            InvalidArgumentsException e = new InvalidArgumentsException(STATE_INVALID);

            String errorMessage = "OOOPS, something went wrong! Adding resource to card is not allowed in this state";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Depot Line Empty Condition
        if(playerWh.isEmpty(depotLine)) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! Selected depot line is empty";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        resource = playerWh.getDepot()[depotLine];

        //Card Not in Hand Condition
        if(!player.hasLeader(DEPOT, resource.getType())) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! None of your cards meet the resource requirement";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Card Inactive Condition
        card = player.getLeader(DEPOT, resource.getType());

        if(!card.isActive()) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! Your card is not active";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Card Depot Full
        if(((DepotAbility)card.getSpecialAbility()).isFull()) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! The depot of the card is already full";

            e.setErrorMessage(errorMessage);

            throw e;
        }


        //endregion

        //I get here if the argument is valid

        //region Execute operation

        //Add a resource to the card
        DepotAbility depotAbility = (DepotAbility) card.getSpecialAbility();
        depotAbility.addResource(); //Resource type is unique for every card, no need to specify what to add

        //Remove the resource from selected depot line
        playerWh.removeAmount(depotLine, 1);

        //NO SAVE FOR THIS

        //endregion
    }

    private void extraToCard(ArrayList<Object> args) throws InvalidArgumentsException {

        Resource resource; //Expected argument

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
            resource = (Resource) args.get(0);
        }catch (Exception ex) {
            InvalidArgumentsException e = new InvalidArgumentsException(TYPE_MISMATCH);

            String errorMessage = "OOOPS, something went wrong! Server received an element invalid for this action";
            errorMessage += "\nElement expected: Resource";

            e.setErrorMessage(errorMessage);

            throw e;
        }
        //endregion

        //I get here if there are no problems with arguments conversion

        Player player = game.getCurrentPlayer();
        LeadCard card;

        //region Argument validation
        Stream<LeadCard> playerLeaders = game.getCurrentPlayer().getLeaders().stream();

        //Invalid State Condition
        if(fromDefault) {
            InvalidArgumentsException e = new InvalidArgumentsException(STATE_INVALID);

            String errorMessage = "OOOPS, something went wrong! Adding resource to card is not allowed in this state";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Extra Empty Condition
        if(!player.hasExtra()) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! Your extra resources slot is empty";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Resource Not in Extra Condition
        if(!player.hasExtra(resource.getType())) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! Selected resource is not present in extra resources slot";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Card Not in Hand Condition

        if(!player.hasLeader(DEPOT, resource.getType())) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! None of your cards meet the selected resource requirement";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Card Inactive Condition
        card = player.getLeader(DEPOT, resource.getType());

        if(!card.isActive()) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! The card is not active";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Card Depot is full
        if(((DepotAbility)card.getSpecialAbility()).isFull()) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! The depot of the card is already full";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        //Add a resource to depot card
        DepotAbility depotAbility = (DepotAbility) card.getSpecialAbility();
        depotAbility.addResource();

        //Remove resource from extra res
        player.removeExtra(resource.getType(), 1);

        //NO SAVE FOR THIS

        //endregion
    }

    private void cancel() throws InvalidArgumentsException {

        //region Conversion of args from packet
        //endregion

        //I get here if there are no problems with arguments conversion

        //region Argument validation
        //endregion

        //I get here if the argument is valid

        //region Execute operation

        //Reset the game status
        game = Save.loadGame(game.getUuid());

        if(fromDefault) {
            //If i came in this state from default, go back to a default state
            action.setActionState(new Default_ActionState(action), DEFAULT);
            game.getCurrentPlayer().setLastAction(DEFAULT); //This change affect the model
            Save.saveGame(game); //Save this small change
        }

        //endregion
    }

    private void confirm() throws InvalidArgumentsException {

        //region Conversion of args from packet
        //endregion

        //I get here if there are no problems with arguments conversion

        //region Argument validation
        //endregion

        //I get here if the argument is valid

        //region Execute operation

        //Add faith to every other player other than current one
        game.addFaithToAll(game.getCurrentPlayer().extraAmount());

        //Apply changes

        action.setActionState(new Default_ActionState(action), DEFAULT); //Go to this state
        game.getCurrentPlayer().setLastAction(DEFAULT);

        //SAVE THE NEW STATUS
        Save.saveGame(game);

        //endregion
    }
}
