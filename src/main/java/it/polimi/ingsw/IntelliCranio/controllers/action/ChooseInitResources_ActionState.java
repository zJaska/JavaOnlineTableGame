package it.polimi.ingsw.IntelliCranio.controllers.action;

import com.google.gson.Gson;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;

import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;

public class ChooseInitResources_ActionState extends ActionState {

    private Game game;

    public ChooseInitResources_ActionState(Action action){
        super(action);
    }

    @Override
    public void execute(Game game, Packet packet) throws InvalidArgumentsException {
        this.game = game;

        if(packet == null || packet.getInstructionCode() == null) throw new InvalidArgumentsException(CODE_NULL);

        if(packet.getInstructionCode() == CHOOSE_RES) chooseResource(packet.getArgs());

        throw new InvalidArgumentsException(CODE_NOT_ALLOWED); //Code in packet is not allowed in this state
    }

    private void chooseResource(ArrayList<Object> args) throws InvalidArgumentsException {

        Resource resource; //Expected argument for this operation

        //region Conversion of args from packet

        if(args.size() == 0) throw new InvalidArgumentsException(NOT_ENOUGH_ARGS);
        if(args.size() > 1) throw new InvalidArgumentsException(TOO_MANY_ARGS);

        try {
            resource = (Resource) args.get(0);
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
        int allowedFaith = game.getInitFaith(game.getCurrentPlayerIndex());
        Player player = game.getCurrentPlayer();

        //At this stage, extra res are not unified. Every element is a unit

        //If player already has the right amount of resources, the game shouldn't be in this state. Return the new state then.
        if(player.extraAmount() == allowedAmount && player.getFaithPosition() == allowedFaith)
            action.setActionState(new ManageWarehouse_ActionState(action, false), MNG_WARE);

        //Add the selected resource the player extras
        player.addExtra(resource);

        if(player.extraAmount() == allowedAmount) {
            //Add initial amount of faith (no need to check for position)
            for (int i = 0; i < allowedFaith; ++i)
                player.addFaith();

            action.setActionState(new ManageWarehouse_ActionState(action, false), MNG_WARE);
        }

        //endregion
    }
}