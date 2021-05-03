package it.polimi.ingsw.IntelliCranio.controllers.action;

import com.google.gson.Gson;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.Save;

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

        if(packet == null || packet.getInstructionCode() == null) {
            InvalidArgumentsException e = new InvalidArgumentsException(CODE_NULL);
            String errorMessage = "OOOPS, something went wrong! No action received";
            e.setErrorMessage(errorMessage);
            throw e;
        }

        switch (packet.getInstructionCode()) {
            case CHOOSE_RES: chooseResource(packet.getArgs()); return;
            default:
                InvalidArgumentsException e = new InvalidArgumentsException(CODE_NOT_ALLOWED);
                String errorMessage = "OOOPS, something went wrong! Action invalid in current state";
                e.setErrorMessage(errorMessage);
                throw e; //Code in packet is not allowed in this state
        }
    }

    private void chooseResource(ArrayList<Object> args) throws InvalidArgumentsException {

        Resource resource; //Expected argument for this operation

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
        } catch (Exception ex) {
            InvalidArgumentsException e = new InvalidArgumentsException(TYPE_MISMATCH);

            String errorMessage = "OOOPS, something went wrong! Server received an element invalid for this action";
            errorMessage += "\nElement expected: Resource";

            e.setErrorMessage(errorMessage);

            throw e;
        }
        //endregion

        //I get here if there are no problems with arguments conversion

        //region Argument validation

        //Null Condition
        if(resource == null) {
            InvalidArgumentsException e = new InvalidArgumentsException(NULL_ARG);

            String errorMessage = "OOOPS, something went wrong! Server received a null element";
            errorMessage += "\nElement expected: Resource";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Blank or Faith Condition
        if(resource.getType() == BLANK || resource.getType() == FAITH) {
            InvalidArgumentsException e = new InvalidArgumentsException(SELECTION_INVALID);

            String errorMessage = "OOOPS, something went wrong! Received an invalid type for the resource";
            errorMessage += "\nType Received: " + resource.getType().toString();

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //Invalid amount Condition
        if(resource.getAmount() != 1) {
            InvalidArgumentsException e = new InvalidArgumentsException(VALUE_INVALID);

            String errorMessage = "OOOPS, something went wrong! Selected amount is invalid";
            errorMessage += "\nAmount Received: " + resource.getAmount();
            errorMessage += "\nAmount Expected: 1";

            e.setErrorMessage(errorMessage);

            throw e;
        }

        //endregion

        //I get here if the argument is valid

        //region Execute operation

        Player player = game.getCurrentPlayer();

        int allowedAmount = game.getInitRes(game.getCurrentPlayerIndex());
        int allowedFaith = game.getInitFaith(game.getCurrentPlayerIndex());

        //At this stage, extra res are not unified. Every element is a unit

        //If player already has the right amount of resources, the game shouldn't be in this state. Return the new state then.
        if(player.extraAmount() == allowedAmount && player.getFaithPosition() == allowedFaith) {
            action.setActionState(new ManageWarehouse_ActionState(action, false), MNG_WARE);
            player.setLastAction(MNG_WARE);
        }

        //Add the selected resource the player extras
        player.addExtra(resource);

        if(player.extraAmount() == allowedAmount) {
            //Add initial amount of faith (no need to check for position)
            for (int i = 0; i < allowedFaith; ++i)
                player.addFaith();

            action.setActionState(new ManageWarehouse_ActionState(action, false), MNG_WARE);
            player.setLastAction(MNG_WARE);
        }

        Save.saveGame(game);

        //endregion
    }
}
