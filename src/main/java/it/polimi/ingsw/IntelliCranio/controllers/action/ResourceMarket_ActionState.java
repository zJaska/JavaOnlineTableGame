package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.market.ResourceMarket;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.ability.Ability;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.Checks;
import it.polimi.ingsw.IntelliCranio.util.Save;

import java.util.ArrayList;
import java.util.Objects;

import static it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;

public class ResourceMarket_ActionState extends ActionState {

    private Game game;

    private boolean hasSelected;
    private Resource blanks = new Resource(BLANK, 0);

    public ResourceMarket_ActionState(Action action) {
        super(action);
    }

    @Override
    public void execute(Game game, Packet packet) throws InvalidArgumentsException {
        this.game = game;

        if (packet == null || packet.getInstructionCode() == null || packet.getArgs() == null) throw new InvalidArgumentsException(CODE_NULL);

        switch (packet.getInstructionCode()) {
            case SELECT_ROW: selectRow(packet.getArgs()); return;
            case SELECT_COLUMN: selectCol(packet.getArgs()); return;
            case CHOOSE_RES: chooseResource(packet.getArgs()); return;
            case CANCEL: cancel(); return;
            case CONFIRM: confirm(); return;
        }


        throw new InvalidArgumentsException(CODE_NOT_ALLOWED); //Code in packet is not allowed in this state


    }

    private void selectRow(ArrayList<Object> args) throws InvalidArgumentsException {

        int row; //Expected argument

        //region Conversion of args from packet

        //Amount args check
        Checks.argsAmount(args, 1);

        try {

            row = (int)args.get(0);

        } catch (Exception ex) {
            InvalidArgumentsException e = new InvalidArgumentsException(TYPE_MISMATCH);

            String errorMessage = "OOOPS, something went wrong! Server received an element invalid for this action";
            errorMessage += "\nElement expected: Integer Number";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //endregion

        //I get here if there are no problems with arguments conversion

        ResourceMarket rm = game.getResourceMarket();

        //region Argument validation

        //Already selected something
        Checks.hasSelected(hasSelected);

        //Row Over size Condition
        Checks.overSizeLine(row, rm.ROWS);

        //Negative Row Condition
        Checks.negativeLine(row);

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        Player player = game.getCurrentPlayer();

        ArrayList<FinalResource> marketRes = rm.selectRow(row);

        //Add faith to player for each faith taken from market
        marketRes.stream().filter(res -> res.getType().equals(FAITH)).forEach(faith -> player.addFaith());

        //If player has ANY card of type RESOURCE, add blank resources
        if(player.hasLeader(Ability.AbilityType.RESOURCE))
            //Add all blanks present in selected row to blanks list
            marketRes.stream().filter(res -> res.getType().equals(BLANK)).forEach(blank -> blanks.addAmount(blank.getAmount()));

        //Add all resources different from BLANK or FAITH to player extra res
        marketRes.stream()
                .filter(res -> (!res.getType().equals(FAITH) || !res.getType().equals(BLANK)))
                .forEach(res -> player.addExtra(res.getType(), res.getAmount()));

        hasSelected = true;

        //endregion
    }

    private void selectCol(ArrayList<Object> args) throws InvalidArgumentsException {

        int col; //Expected argument

        //region Conversion of args from packet

        //Amount args check
        Checks.argsAmount(args, 1);

        try {

            col = (int)args.get(0);

        } catch (Exception ex) {
            InvalidArgumentsException e = new InvalidArgumentsException(TYPE_MISMATCH);

            String errorMessage = "OOOPS, something went wrong! Server received an element invalid for this action";
            errorMessage += "\nElement expected: Integer Number";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //endregion

        //I get here if there are no problems with arguments conversion

        ResourceMarket rm = game.getResourceMarket();

        //region Argument validation

        //Already selected something
        Checks.hasSelected(hasSelected);

        //Row Over size Condition
        Checks.overSizeLine(col, rm.COLUMNS);

        //Negative Row Condition
        Checks.negativeLine(col);

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        Player player = game.getCurrentPlayer();

        ArrayList<FinalResource> marketRes = rm.selectColumn(col);

        //Add faith to player for each faith taken from market
        marketRes.stream().filter(res -> res.getType().equals(FAITH)).forEach(faith -> player.addFaith());

        //If player has ANY card of type RESOURCE, add blank resources
        if(player.hasLeader(Ability.AbilityType.RESOURCE))
            //Add all blanks present in selected row to blanks list
            marketRes.stream().filter(res -> res.getType().equals(BLANK)).forEach(blank -> blanks.addAmount(blank.getAmount()));

        //Add all resources different from BLANK or FAITH to player extra res
        marketRes.stream()
                .filter(res -> (!res.getType().equals(FAITH) || !res.getType().equals(BLANK)))
                .forEach(res -> player.addExtra(res.getType(), res.getAmount()));

        hasSelected = true;

        //endregion
    }

    private void chooseResource(ArrayList<Object> args) throws InvalidArgumentsException {

        Resource resource; //Expected arg for this action

        //region Conversion of args from packet

        //Amount args check
        Checks.argsAmount(args, 1);

        try {

            resource = (Resource) args.get(0);

        } catch (Exception ex) {
            InvalidArgumentsException e = new InvalidArgumentsException(TYPE_MISMATCH);

            String errorMessage = "OOOPS, something went wrong! Server received an element invalid for this action";
            errorMessage += "\nElement expected: Resource";

            e.setErrorMessage(errorMessage);

            throw e;
        }
        //endregion

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
        Checks.notInHand(player, Ability.AbilityType.SALE, resource.getType());

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

        action.setActionState(new Default_ActionState(action), DEFAULT);

        //endregion
    }
}
