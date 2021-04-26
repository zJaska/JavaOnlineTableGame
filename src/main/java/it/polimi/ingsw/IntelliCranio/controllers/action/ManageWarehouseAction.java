package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;

public class ManageWarehouseAction implements ActionI{

    private Game game;
    private boolean fromDefault;

    public ManageWarehouseAction(boolean fromDefault) {
        this.fromDefault = fromDefault;
    }

    @Override
    public ActionI execute(Game game, Packet packet) throws InvalidArgumentsException {
        return null;
    }
}
