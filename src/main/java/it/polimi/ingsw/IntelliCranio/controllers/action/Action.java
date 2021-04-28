package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;

public class Action {

    private ActionState actionState;

    public void execute(Game game, Packet packet) throws InvalidArgumentsException {
        actionState.execute(game, packet);
    }

    public void setActionState(ActionState state) {
        actionState = state;
    }
}
