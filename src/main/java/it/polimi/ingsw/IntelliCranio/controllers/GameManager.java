package it.polimi.ingsw.IntelliCranio.controllers;

import it.polimi.ingsw.IntelliCranio.controllers.action.Action;
import it.polimi.ingsw.IntelliCranio.controllers.action.ActionState;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class GameManager {

    private Action action;

    /*Idea: Static because can be called anytime in the game without reference to this object
    Interrupt all the inputs from client and display the results and the winner.
    Disconnect from the clients
    Clear the backup memory from disk
    Terminate the execution of this thread
     */
    public static void endingGame() {
        throw new NotImplementedException();
    }
}
