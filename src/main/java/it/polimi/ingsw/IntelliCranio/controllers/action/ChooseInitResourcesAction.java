package it.polimi.ingsw.IntelliCranio.controllers.action;

import com.google.gson.Gson;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;

import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.ErrorCode.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;

public class ChooseInitResourcesAction implements ActionI{

    private Game game;

    @Override
    public ActionI execute(Game game, Packet packet) throws InvalidArgumentsException {
        this.game = game;

        if(packet == null || packet.getInstructionCode() == null) throw new InvalidArgumentsException(CODE_NULL);
        if(packet.getInstructionCode() == CHOOSE_RES) return chooseResource(packet.getArgs());

        throw new InvalidArgumentsException(CODE_NOT_ALLOWED); //Code in packet is not allowed in this state
    }

    private ActionI chooseResource(ArrayList<String> args) throws InvalidArgumentsException {

        Resource resource; //Expected argument for this operation

        //region Conversion of args from packet

        Gson gson = new Gson();

        if(args.size() == 0) throw new InvalidArgumentsException(NOT_ENOUGH_ARGS);
        if(args.size() > 1) throw new InvalidArgumentsException(TOO_MANY_ARGS);

        try {
            resource = gson.fromJson((args.get(0)), Resource.class);
        } catch (Exception e) {
            throw new InvalidArgumentsException(TYPE_MISMATCH);
        }
        //endregion

        //I get here if there are no problems with arguments conversion

        //region Argument validation

        //Null Condition
        if(resource == null) throw new InvalidArgumentsException(NULL_ARG);

        //Blank or Faith Condition
        if(resource.getType() == BLANK || resource.getType() == FAITH)
            throw new InvalidArgumentsException(SELECTION_INVALID);

        //Excessive amount Condition
        if(resource.getAmount() > 1) throw new InvalidArgumentsException(VALUE_INVALID);

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        int allowedAmount = game.getInitRes(game.getCurrentPlayerIndex());
        ArrayList<Resource> playerResources = game.getCurrentPlayer().getExtraRes();

        //If player already has the right amount of resources, the game shouldn't be in this state. Return the new state then.
        if(playerResources.size() == allowedAmount) return new ManageWarehouseAction(false);

        //Add the selected resource the player extras
        playerResources.add(resource);

        //Check for return type
        if(playerResources.size() < allowedAmount)
            return null; //No need to change state
        else
            return new ManageWarehouseAction(false);

        //endregion
    }
}
