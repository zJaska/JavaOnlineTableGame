package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;

import static it.polimi.ingsw.IntelliCranio.network.Packet.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.DEFAULT;

public class Action {

    private ActionState actionState;
    private InstructionCode actionCode;

    /**
     * Calls the execute method of its current ActionState
     * @param game The game to update
     * @param packet The packet from client
     * @throws InvalidArgumentsException
     */
    public void execute(Game game, Packet packet) throws InvalidArgumentsException {
        actionState.execute(game, packet);
    }

    /**
     * Change the state of this object
     * @param actionState The state to set
     * @param actionCode The code of the new state
     */
    public void setActionState(ActionState actionState, InstructionCode actionCode) {
        this.actionState = actionState;
        this.actionCode = actionCode;
    }

    /**
     * Restore the state of a player if it was a init one,
     * sets the default state otherwise
     * @param actionCode The action code from a player
     */
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
