package it.polimi.ingsw.IntelliCranio.server.action;

import it.polimi.ingsw.IntelliCranio.server.GameManager;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;

import java.util.ArrayList;

public class ManageWarehouse implements Action{

    /**
     * Constructor
     * <p>
     *     Gets all the necessary parameter from json strings.
     * </p>
     * @param jsonArgs
     */
    public ManageWarehouse(ArrayList<String> jsonArgs) {
    }

    @Override
    public void playAction(GameManager manager) throws InvalidArgumentsException {
        throw new UnsupportedOperationException();
    }
}
