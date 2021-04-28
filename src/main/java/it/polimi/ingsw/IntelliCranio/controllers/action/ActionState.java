package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;

public abstract class ActionState {

    protected Action action;

    public ActionState(Action action) {
        this.action = action;
    }

    public abstract void execute(Game game, Packet packet) throws InvalidArgumentsException;


}
