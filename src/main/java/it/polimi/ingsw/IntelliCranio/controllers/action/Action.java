package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;

import static it.polimi.ingsw.IntelliCranio.network.Packet.*;

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

    public InstructionCode getActionCode(){
        return actionCode;
    }
}
