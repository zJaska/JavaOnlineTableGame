package it.polimi.ingsw.IntelliCranio.server.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;

import java.util.ArrayList;

public interface Action {

    ArrayList<String> playAction(Game manager) throws InvalidArgumentsException;

}
