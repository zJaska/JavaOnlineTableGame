package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.models.player.Warehouse;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.models.ability.DepotAbility;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.Checks;
import it.polimi.ingsw.IntelliCranio.util.Conversions;
import it.polimi.ingsw.IntelliCranio.util.Lists;
import it.polimi.ingsw.IntelliCranio.util.Save;

import java.util.ArrayList;
import java.util.stream.Stream;

import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static it.polimi.ingsw.IntelliCranio.models.ability.Ability.AbilityType.*;

public class ManageWarehouse_ActionState extends ActionState {

    private Packet.InstructionCode prevState;

    public ManageWarehouse_ActionState(Action action, Packet.InstructionCode prevState) {
        super(action);
        this.prevState = prevState;
    }

    /**
     * Execute the action relative to the code received with packet.
     * Actions for this state:
     * SWAP_LINES: Swap two lines in depot
     * ADD_FROM_EXTRA: Add a resource from player extra to a depot line
     * REMOVE_FROM_DEPOT: Remove 1 resource from a depot line and put it in player extra
     * DEPOT_TO_CARD: Remove 1 resource from a depot line and put it in a depot leader card
     * EXTRA_TO_CARD: Remove 1 resource from player extra and put it in a depot leader card
     * CANCEL: Cancel all the changes and go back to initial config.
     * CONFIRM: Save all the changes and return to the default state
     * @param game The game to update
     * @param packet Packet received from client
     * @throws InvalidArgumentsException
     */
    @Override
    public void execute(Game game, Packet packet) throws InvalidArgumentsException {
        this.game = game;

        Checks.packetCheck(packet);

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

        Checks.argsAmount(args, 2);

        first = Conversions.getInteger(args, 0);
        second = Conversions.getInteger(args, 1);

        //I get here if there are no problems with arguments conversion

        Warehouse playerWh = game.getCurrentPlayer().getWarehouse();

        //region Argument validation

        //Same Line Condition
        Checks.sameLine(first, second);

        //Negative Line Condition
        Checks.negativeValue(first);
        Checks.negativeValue(second);

        //Over Size Condition
        Checks.overSizeLine(first, playerWh.getDepot().length);
        Checks.overSizeLine(second, playerWh.getDepot().length);
        
        //endregion

        //I get here if the argument is valid

        //region Execute operation

        ArrayList<Resource> playerExtra = game.getCurrentPlayer().getExtraRes();

        //Add all the surplus from swap
        playerExtra.addAll(playerWh.swapLines(first, second));

        playerExtra = Lists.unifyResourceAmounts(playerExtra); //Unify all the extra res for future usage

        //NO SAVE FOR THIS ACTION

        //endregion
    }

    private void addFromExtra(ArrayList<Object> args) throws InvalidArgumentsException {

        Resource resource; //First Expected argument
        int depotLine; //Second Expected argument

        Checks.argsAmount(args, 2);

        resource = Conversions.getResource(args, 0);
        depotLine = Conversions.getInteger(args, 1);

        //I get here if there are no problems with arguments conversion

        Player player = game.getCurrentPlayer();
        Warehouse playerWh = game.getCurrentPlayer().getWarehouse();

        //region Argument validation

        //Null Condition
        Checks.nullElement(resource, "Resource");
        Checks.nullElement(resource.getType(), "Type of Resource");

        //Negative Line Condition
        Checks.negativeValue(depotLine);

        //Over Size Condition
        Checks.overSizeLine(depotLine, playerWh.getDepot().length);

        //Extra Empty Condition
        Checks.extraEmpty(player);

        //Resource Not in Extra Condition (blank or faith CAN NOT stay in extra resources)
        Checks.notInExtra(player, resource.getType());

        //Not Same Resource Condition (if WH is null, action is VALID)
        Checks.notSameResource(playerWh.getType(depotLine), resource.getType(), true);

        //Resource present in a different line
        Checks.resInDifferentLine(playerWh, depotLine, resource);

        //Depot Line Full Condition
        Checks.depotFull(playerWh, depotLine);

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        //Add a single amount of selected resource in selected depot and remove it from extra
        playerWh.add(depotLine, resource);
        player.removeExtra(resource.getType(), 1);

        //NO SAVE FOR THIS

        //endregion
    }

    private void removeFromDepot(ArrayList<Object> args) throws InvalidArgumentsException {

        int depotLine; //Expected argument

        Checks.argsAmount(args, 1);

        depotLine = Conversions.getInteger(args, 0);

        //I get here if there are no problems with arguments conversion

        Warehouse playerWh = game.getCurrentPlayer().getWarehouse();

        //region Argument validation

        //Negative line Condition
        Checks.negativeValue(depotLine);

        //Over Size Condition
        Checks.overSizeLine(depotLine, playerWh.getDepot().length);

        //Empty Condition
        Checks.depotEmpty(playerWh, depotLine);

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        //Remove a single amount from depot and add it to extra resources

        Resource res = playerWh.getDepot()[depotLine];
        Player player = game.getCurrentPlayer();

        playerWh.remove(depotLine);
        player.addExtra(res.getType(), 1);

        //NO SAVE FOR THIS

        //endregion
    }

    private void depotToCard(ArrayList<Object> args) throws InvalidArgumentsException {

        int depotLine; //Expected first argument

        Checks.argsAmount(args, 1);

        depotLine = Conversions.getInteger(args, 0);

        //I get here if there are no problems with arguments conversion

        Player player = game.getCurrentPlayer();
        Warehouse playerWh = game.getCurrentPlayer().getWarehouse();
        Resource resource; //Resource to get is depot is not empty
        LeadCard card; //The card to put the resource

        //region Argument validation

        //Invalid State Condition
        Checks.invalidState(prevState, CHOOSE_INIT_RES);
        Checks.invalidState(prevState, DEFAULT);

        //Depot Line Empty Condition
        Checks.depotEmpty(playerWh, depotLine);

        resource = playerWh.getDepot()[depotLine];

        //Card Not in Hand Condition
        Checks.notInHand(player, DEPOT, resource.getType());

        card = player.getLeader(DEPOT, resource.getType());

        //Card Inactive Condition
        Checks.cardInactive(card);

        //Card Depot Full Condition
        Checks.cardDepotFull(card);

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        //Add a resource to the card
        DepotAbility depotAbility = (DepotAbility) card.getSpecialAbility();
        depotAbility.addResource(); //Resource type is unique for every card, no need to specify what to add

        //Remove the resource from selected depot line
        playerWh.remove(depotLine);

        //NO SAVE FOR THIS

        //endregion
    }

    private void extraToCard(ArrayList<Object> args) throws InvalidArgumentsException {

        Resource resource; //Expected argument

        Checks.argsAmount(args, 1);

        resource = Conversions.getResource(args, 0);

        //I get here if there are no problems with arguments conversion

        Player player = game.getCurrentPlayer();
        LeadCard card;

        //region Argument validation
        Stream<LeadCard> playerLeaders = game.getCurrentPlayer().getLeaders().stream();

        //Invalid State Condition
        Checks.invalidState(prevState, CHOOSE_INIT_RES);
        Checks.invalidState(prevState, DEFAULT);

        //Extra Empty Condition
        Checks.extraEmpty(player);

        //Resource Not in Extra Condition
        Checks.notInExtra(player, resource.getType());

        //Card Not in Hand Condition
        Checks.notInHand(player, DEPOT, resource.getType());

        card = player.getLeader(DEPOT, resource.getType());

        //Card Inactive Condition
        Checks.cardInactive(card);

        //Card Depot is full
        Checks.cardDepotFull(card);

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

        //region Execute operation

        //Reset the game status
        game.loadGame(Save.loadGame(game.getUuid()));

        if(prevState == DEFAULT) {
            //If i came in this state from default, go back to a default state,
            //else stay in this action state by doing nothing
            action.setActionState(new Default_ActionState(action), DEFAULT);
            game.getCurrentPlayer().setLastAction(DEFAULT); //This change affect the model
            Save.saveGame(game); //Save this small change
        }

        //endregion
    }

    private void confirm() throws InvalidArgumentsException {

        //region Execute operation

        //Add faith to every other player other than current one
        game.addFaithToOthers(game.getCurrentPlayer().extraAmount());
        game.getCurrentPlayer().resetExtra();

        //Apply changes

        if(prevState == CHOOSE_INIT_RES) {
            game.getCurrentPlayer().setLastAction(DEFAULT);
            game.endTurn(true);
        } else {
            action.setActionState(new Default_ActionState(action), DEFAULT); //Go to this state
            game.getCurrentPlayer().setLastAction(DEFAULT);
        }

        //SAVE THE NEW STATUS
        Save.saveGame(game);

        //endregion
    }
}
