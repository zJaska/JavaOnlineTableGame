package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;

public class DefaultAction implements ActionI{

    @Override
    public ActionI execute(Game game, Packet packet) throws InvalidArgumentsException {
        return null;
    }
}
