package it.polimi.ingsw.IntelliCranio.server.action;

import it.polimi.ingsw.IntelliCranio.server.GameManager;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;

import java.util.ArrayList;

public interface Action {

    public ArrayList<String> playAction(GameManager manager) throws InvalidArgumentsException;

}
