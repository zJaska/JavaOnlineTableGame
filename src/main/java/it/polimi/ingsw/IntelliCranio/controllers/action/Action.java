package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;

import static it.polimi.ingsw.IntelliCranio.network.Packet.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.DEFAULT;

public class Action {

    private ActionState actionState;
    private InstructionCode actionCode;

    public void execute(Game game, Packet packet) throws InvalidArgumentsException {
        actionState.execute(game, packet);
    }

    public void setActionState(ActionState actionState, InstructionCode actionCode) {
        this.actionState = actionState;
        this.actionCode = actionCode;
    }

    public void restoreState(InstructionCode actionCode) {

        switch (actionCode) {
            case DISCARD_INIT_LEAD:
                actionState = new DiscardInitLeaders_ActionState(this);
                this.actionCode = actionCode;
                break;
            case CHOOSE_INIT_RES:
                actionState = new ChooseInitResources_ActionState(this);
                this.actionCode = actionCode;
                break;
            default:
                actionState = new Default_ActionState(this);
                this.actionCode = DEFAULT;
        }
    }

    public InstructionCode getActionCode(){
        return actionCode;
    }
}
