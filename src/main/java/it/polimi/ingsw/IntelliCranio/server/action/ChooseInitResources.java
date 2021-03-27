package it.polimi.ingsw.IntelliCranio.server.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.server.GameManager;
import it.polimi.ingsw.IntelliCranio.server.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.server.resource.Resource;

import java.util.ArrayList;

public class ChooseInitResources implements Action{

    private ArrayList<Resource> selection; //The resources the player has choosed

    /**
     * Gets all the necessary parameter from json strings.
     *
     * @param jsonArgs A single element containing the list of resources the player  choosed
     */
    public ChooseInitResources(ArrayList<String> jsonArgs) {

        Gson gson = new Gson();

        selection = gson.fromJson(jsonArgs.get(0), new TypeToken<ArrayList<Resource>>(){}.getType());
    }

    @Override
    public void playAction(GameManager manager) throws InvalidArgumentsException {

        //region Error Handling
        //The amount of resources i expect
        int correctAmount = manager.getInitRes(manager.getCurrentPlayerIndex());

        //The amount of resources provided by the client arguments
        int actualAmount = selection.stream().mapToInt(Resource::getAmount).sum();

        if(actualAmount != correctAmount)
            throw new InvalidArgumentsException(Packet.InstructionCode.CHOOSE_INIT_RES);
        //endregion




        throw new UnsupportedOperationException();
    }
}
