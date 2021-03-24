package it.polimi.ingsw.IntelliCranio.server.action;

import it.polimi.ingsw.IntelliCranio.server.GameManager;
import it.polimi.ingsw.IntelliCranio.server.Packet;

public interface Action {

    public void playAction(GameManager manager, Packet packet);

}
