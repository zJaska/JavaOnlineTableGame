package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.market.ResourceMarket;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.Checks;
import it.polimi.ingsw.IntelliCranio.util.Conversions;
import it.polimi.ingsw.IntelliCranio.util.Save;

import java.util.ArrayList;
import java.util.Objects;

import static it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static it.polimi.ingsw.IntelliCranio.models.ability.Ability.AbilityType.*;

public class ResourceMarket_ActionState extends ActionState {

    private boolean hasSelected;
    private Resource blanks = new Resource(BLANK, 0);

    public ResourceMarket_ActionState(Action action) {
        super(action);
    }

    @Override
    public void execute(Game game, Packet packet) throws InvalidArgumentsException {
        this.game = game;

        Checks.packetCheck(packet);

        switch (packet.getInstructionCode()) {
            case SELECT_ROW: selectRow(packet.getArgs()); return;
            case SELECT_COLUMN: selectCol(packet.getArgs()); return;
            case CHOOSE_RES: chooseResource(packet.getArgs()); return;
            case CANCEL: cancel(); return;
            case CONFIRM: confirm(); return;
            default:
                InvalidArgumentsException e = new InvalidArgumentsException(CODE_NOT_ALLOWED);
                String errorMessage = "OOOPS, something went wrong! Action invalid in current state";
                e.setErrorMessage(errorMessage);
                throw e; //Code in packet is not allowed in this state
        }
    }

    private void selectRow(ArrayList<Object> args) throws InvalidArgumentsException {

        int row; //Expected argument

        //Amount args check
        Checks.argsAmount(args, 1);

        row = Conversions.getInteger(args, 0);


        //I get here if there are no problems with arguments conversion

        ResourceMarket rm = game.getResourceMarket();

        //region Argument validation

        //Already selected something
        Checks.hasSelected(hasSelected);

        //Row Over size Condition
        Checks.overSizeLine(row, rm.ROWS);

        //Negative Row Condition
        Checks.negativeValue(row);

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        Player player = game.getCurrentPlayer();

        ArrayList<FinalResource> marketRes = rm.selectRow(row);

        //Add faith to player for each faith taken from market
        marketRes.stream().filter(res -> res.getType().equals(FAITH)).forEach(faith -> game.addCurrentPlayerFaith());

        //If player has ANY card of type RESOURCE and is active, add blank resources
        if(player.getLeaders().stream().anyMatch(lead -> (lead.getAbilityType() == RESOURCE && lead.isActive())))
            //Add all blanks present in selected row to blanks list
            marketRes.stream().filter(res -> res.getType().equals(BLANK)).forEach(blank -> blanks.addAmount(blank.getAmount()));

        //Add all resources different from BLANK or FAITH to player extra res
        marketRes.stream()
                .filter(res -> (!res.getType().equals(FAITH) && !res.getType().equals(BLANK)))
                .forEach(res -> player.addExtra(res.getType(), res.getAmount()));

        hasSelected = true;

        //endregion
    }

    private void selectCol(ArrayList<Object> args) throws InvalidArgumentsException {

        int col; //Expected argument

        //Amount args check
        Checks.argsAmount(args, 1);

        col = Conversions.getInteger(args, 0);


        //I get here if there are no problems with arguments conversion

        ResourceMarket rm = game.getResourceMarket();

        //region Argument validation

        //Already selected something
        Checks.hasSelected(hasSelected);

        //Row Over size Condition
        Checks.overSizeLine(col, rm.COLUMNS);

        //Negative Row Condition
        Checks.negativeValue(col);

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        Player player = game.getCurrentPlayer();

        ArrayList<FinalResource> marketRes = rm.selectColumn(col);

        //Add faith to player for each faith taken from market
        marketRes.stream().filter(res -> res.getType().equals(FAITH)).forEach(faith -> game.addCurrentPlayerFaith());

        //If player has ANY card of type RESOURCE and isActive, add blank resources
        if(player.getLeaders().stream().anyMatch(lead -> (lead.getAbilityType() == RESOURCE && lead.isActive())))
            //Add all blanks present in selected row to blanks list
            marketRes.stream().filter(res -> res.getType().equals(BLANK)).forEach(blank -> blanks.addAmount(blank.getAmount()));

        //Add all resources different from BLANK or FAITH to player extra res
        marketRes.stream()
                .filter(res -> (!res.getType().equals(FAITH) && !res.getType().equals(BLANK)))
                .forEach(res -> player.addExtra(res.getType(), res.getAmount()));

        hasSelected = true;

        //endregion
    }

    private void chooseResource(ArrayList<Object> args) throws InvalidArgumentsException {

        Resource resource; //Expected arg for this action

        //Amount args check
        Checks.argsAmount(args, 1);

        resource = Conversions.getResource(args, 0);


        //I get here if there are no problems with arguments conversion

        Player player = game.getCurrentPlayer();

        //region Argument validation

        //Non null Condition
        Checks.nullElement(resource, "Resource");
        Checks.nullElement(resource.getType(), "A type of resource");

        //Check blanks from row/col selection
        Checks.noBlanks(blanks);

        //Blank or Faith Selected Condition
        Checks.blankOrFaith(resource);

        //Player doesn't have correct card
        Checks.notInHand(player, RESOURCE, resource.getType());

        //Card is not active
        LeadCard card = player.getLeader(RESOURCE, resource.getType());
        Checks.cardInactive(card);

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        //Add selected resource to player extras
        player.addExtra(resource.getType(), 1);

        //Remove one blank
        blanks.removeAmount(1);

        //endregion
    }

    private void confirm() throws InvalidArgumentsException{

        //region Argument validation

        //Nothing selected condition
        Checks.notSelected(hasSelected);

        //Has blanks Condition
        Checks.hasBlanks(blanks);

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        Player player = game.getCurrentPlayer();

        player.hasPlayed = true;

        action.setActionState(new ManageWarehouse_ActionState(action, RES_MARKET), MNG_WARE);

        //NO SAVE THERE

        //endregion
    }

    private void cancel() {
        //region Execute operation

        game.loadGame(Objects.requireNonNull(Save.loadGame(game.getUuid())));

        game.getCurrentPlayer().setLastAction(DEFAULT);
        action.setActionState(new Default_ActionState(action), DEFAULT);

        //endregion
    }
}
