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

import java.util.ArrayList;
import java.util.stream.Stream;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static it.polimi.ingsw.IntelliCranio.server.ability.Ability.AbilityType.*;

public class ManageWarehouseAction implements ActionI{

    private Game game;
    private boolean fromDefault;

    public ManageWarehouseAction(boolean fromDefault) {
        this.fromDefault = fromDefault;
    }

    @Override
    public ActionI execute(Game game, Packet packet) throws InvalidArgumentsException {
        this.game = game;

        if(packet == null || packet.getInstructionCode() == null) throw new InvalidArgumentsException(CODE_NULL);

        if(packet.getInstructionCode() == SWAP_LINES) return swapLines(packet.getArgs());
        if(packet.getInstructionCode() == ADD_FROM_EXTRA) return addFromExtra(packet.getArgs());
        if(packet.getInstructionCode() == REMOVE_FROM_DEPOT) return removeFromDepot(packet.getArgs());
        if(packet.getInstructionCode() == DEPOT_TO_CARD) return depotToCard(packet.getArgs());
        if(packet.getInstructionCode() == EXTRA_TO_CARD) return extraToCard(packet.getArgs());
        if(packet.getInstructionCode() == CANCEL) return cancel();
        if(packet.getInstructionCode() == CONFIRM) return confirm();

        throw new InvalidArgumentsException(CODE_NOT_ALLOWED); //Code in packet is not allowed in this state
    }

    private ActionI swapLines(ArrayList<String> args) throws InvalidArgumentsException {

        int first, second; //Expected arguments for this operation

        //region Conversion of args from packet

        Gson gson = new Gson();

        if(args.size() < 2) throw new InvalidArgumentsException(NOT_ENOUGH_ARGS);
        if(args.size() > 2) throw new InvalidArgumentsException(TOO_MANY_ARGS);

        try{
            first = gson.fromJson(args.get(0), Integer.class);
            second = gson.fromJson(args.get(1), Integer.class);
        } catch (Exception e) {
            throw new InvalidArgumentsException(TYPE_MISMATCH);
        }

        //endregion

        //I get here if there are no problems with arguments conversion

        Warehouse playerWh = game.getCurrentPlayer().getWarehouse();

        //region Argument validation

        //Same Line Condition
        if(first == second) throw new InvalidArgumentsException(SELECTION_INVALID);

        //Negative Line Condition
        if(first < 0 || second < 0) throw new InvalidArgumentsException(SELECTION_INVALID);

        //Over Size Condition
        if(first >= playerWh.getDepot().length || second >= playerWh.getDepot().length)
            throw new InvalidArgumentsException(SELECTION_INVALID);
        //endregion

        //I get here if the argument is valid

        //region Execute operation

        ArrayList<Resource> playerExtra = game.getCurrentPlayer().getExtraRes();

        playerWh.swapLines(first, second, playerExtra);
        return null; //No need to change state

        //endregion
    }

    private ActionI addFromExtra(ArrayList<String> args) throws InvalidArgumentsException {

        Resource resource; //First Expected argument
        int depotLine; //Second Expected argument

        //region Conversion of args from packet

        Gson gson = new Gson();

        if(args.size() < 2) throw new InvalidArgumentsException(NOT_ENOUGH_ARGS);
        if(args.size() > 2) throw new InvalidArgumentsException(TOO_MANY_ARGS);

        try {
            resource = gson.fromJson(args.get(0), Resource.class);
            depotLine = gson.fromJson(args.get(1), Integer.class);
        }catch (Exception e) {
            throw new InvalidArgumentsException(TYPE_MISMATCH);
        }
        //endregion

        //I get here if there are no problems with arguments conversion

        Player player = game.getCurrentPlayer();
        Warehouse playerWh = game.getCurrentPlayer().getWarehouse();

        //region Argument validation

        //Null Condition
        if(resource == null) throw new InvalidArgumentsException(NULL_ARG);

        //Negative Line Condition
        if(depotLine < 0) throw new InvalidArgumentsException(SELECTION_INVALID);

        //Over Size Condition
        if(depotLine >= playerWh.getDepot().length) throw new InvalidArgumentsException(SELECTION_INVALID);

        //Extra Empty Condition
        if(!player.hasExtra())
            throw new InvalidArgumentsException(SELECTION_INVALID);

        //Resource Not in Extra Condition (blank or faith CAN NOT stay in extra resources)
        if(!player.hasExtra(resource.getType()))
            throw new InvalidArgumentsException(SELECTION_INVALID);

        //Not Same Resource Condition
        if(playerWh.getType(depotLine) != resource.getType())
            throw new InvalidArgumentsException(SELECTION_INVALID);

        //Depot Line Full Condition
        if(playerWh.isFull(depotLine))
            throw new InvalidArgumentsException(SELECTION_INVALID);

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        playerWh.addFromExtra(depotLine, resource, player);

        return null; //No need to change state

        //endregion
    }

    private ActionI removeFromDepot(ArrayList<String> args) throws InvalidArgumentsException {

        int depotLine; //Expected argument

        //region Conversion of args from packet

        Gson gson = new Gson();

        if(args.size() == 0) throw new InvalidArgumentsException(NOT_ENOUGH_ARGS);
        if(args.size() > 1) throw new InvalidArgumentsException(TOO_MANY_ARGS);

        try {
            depotLine = gson.fromJson(args.get(0), Integer.class);
        } catch (Exception e) {
            throw new InvalidArgumentsException(TYPE_MISMATCH);
        }
        //endregion

        //I get here if there are no problems with arguments conversion

        Warehouse playerWh = game.getCurrentPlayer().getWarehouse();

        //region Argument validation

        //Negative line Condition
        if(depotLine < 0) throw new InvalidArgumentsException(SELECTION_INVALID);

        //Over Size Condition
        if(depotLine >= playerWh.getDepot().length)
            throw new InvalidArgumentsException(SELECTION_INVALID);

        //Empty Condition
        if(playerWh.isEmpty(depotLine))
            throw new InvalidArgumentsException(SELECTION_INVALID);

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        Player player = game.getCurrentPlayer();

        playerWh.removeToExtra(depotLine, player);

        return null; //No need to change state

        //endregion
    }

    private ActionI depotToCard(ArrayList<String> args) throws InvalidArgumentsException {

        int depotLine; //Expected first argument

        //region Conversion of args from packet

        Gson gson = new Gson();

        if(args.size() == 0) throw new InvalidArgumentsException(NOT_ENOUGH_ARGS);
        if(args.size() > 1) throw new InvalidArgumentsException(TOO_MANY_ARGS);

        try {
            depotLine = gson.fromJson(args.get(0), Integer.class);
        }catch (Exception e) {
            throw new InvalidArgumentsException(TYPE_MISMATCH);
        }
        //endregion

        //I get here if there are no problems with arguments conversion

        Player player = game.getCurrentPlayer();
        Warehouse playerWh = game.getCurrentPlayer().getWarehouse();
        Resource resource; //Resource to get is depot is not empty
        LeadCard card; //The card to put the resource

        //region Argument validation

        //Invalid State Condition
        if(fromDefault) throw new InvalidArgumentsException(STATE_INVALID);

        //Depot Line Empty Condition
        if(playerWh.isEmpty(depotLine))
            throw new InvalidArgumentsException(SELECTION_INVALID);

        resource = playerWh.getDepot()[depotLine];

        //Card Not in Hand Condition
        if(!player.hasLeader(DEPOT, resource.getType()))
            throw new InvalidArgumentsException(SELECTION_INVALID);

        //Card Inactive Condition
        card = player.getLeader(DEPOT, resource.getType());

        if(!card.isActive())
            throw new InvalidArgumentsException(SELECTION_INVALID);

        //Card Depot Full
        if(((DepotAbility)card.getSpecialAbility()).isFull())
            throw new InvalidArgumentsException(SELECTION_INVALID);


        //endregion

        //I get here if the argument is valid

        //region Execute operation

        //Add a resource to the card
        DepotAbility depotAbility = (DepotAbility) card.getSpecialAbility();
        depotAbility.addResource(); //Resource type is unique for every card, no need to specify what to add

        //Remove the resource from selected depot line
        playerWh.removeAmount(depotLine, 1);

        return null; //No need to change state

        //endregion
    }

    private ActionI extraToCard(ArrayList<String> args) throws InvalidArgumentsException {

        Resource resource; //Expected argument

        //region Conversion of args from packet

        Gson gson = new Gson();

        if(args.size() == 0) throw new InvalidArgumentsException(NOT_ENOUGH_ARGS);
        if(args.size() > 1) throw new InvalidArgumentsException(TOO_MANY_ARGS);

        try {
            resource = gson.fromJson(args.get(0), Resource.class);
        }catch (Exception e) {
            throw new InvalidArgumentsException(TYPE_MISMATCH);
        }
        //endregion

        //I get here if there are no problems with arguments conversion

        Player player = game.getCurrentPlayer();
        LeadCard card;

        //region Argument validation
        Stream<LeadCard> playerLeaders = game.getCurrentPlayer().getLeaders().stream();

        //Invalid State Condition
        if(fromDefault) throw new InvalidArgumentsException(STATE_INVALID);

        //Resource Not in Extra Condition
        if(!player.hasExtra(resource.getType()))
            throw new InvalidArgumentsException(SELECTION_INVALID);

        //Card Not in Hand Condition

        if(!player.hasLeader(DEPOT, resource.getType()))
            throw new InvalidArgumentsException(SELECTION_INVALID);

        //Card Inactive Condition
        card = player.getLeader(DEPOT, resource.getType());

        if(!card.isActive())
            throw new InvalidArgumentsException(SELECTION_INVALID);

        //Card Depot is full
        if(((DepotAbility)card.getSpecialAbility()).isFull())
            throw new InvalidArgumentsException(SELECTION_INVALID);

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        //Add a resource to depot card
        DepotAbility depotAbility = (DepotAbility) card.getSpecialAbility();
        depotAbility.addResource();

        //Remove resource from extra res
        player.removeExtra(resource.getType(), 1);

        return null; //No need to change state

        //endregion
    }

    private ActionI cancel() throws InvalidArgumentsException {

        //region Conversion of args from packet
        //endregion

        //I get here if there are no problems with arguments conversion

        //region Argument validation
        //endregion

        //I get here if the argument is valid

        //region Execute operation

        if(fromDefault)
            return new DefaultAction(); //If i came in this state from default, go back to a default state
        else {
            //Todo: reset of model state
            return null; //No need to change state
        }

        //endregion
    }

    private ActionI confirm() throws InvalidArgumentsException {

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

        return new DefaultAction(); //Go to this state

        //endregion
    }
}
