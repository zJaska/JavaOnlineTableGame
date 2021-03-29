package it.polimi.ingsw.IntelliCranio.server.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.server.GameManager;
import it.polimi.ingsw.IntelliCranio.server.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.server.player.Warehouse;
import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.server.resource.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.cedarsoftware.util.DeepEquals.deepEquals;

public class ChooseInitResources implements Action {

    private ArrayList<Resource> selection; //The resources the player choosed

    /**
     * This action validate the resources choosed by the player at the start of the game.
     *
     * @param jsonArgs The list of resources the player choosed.
     */
    public ChooseInitResources(ArrayList<String> jsonArgs) {

        Gson gson = new Gson();

        if(jsonArgs.size() > 0)
            selection = gson.fromJson(jsonArgs.get(0), new TypeToken<ArrayList<Resource>>(){}.getType());
    }

    /**
     * Check input consistency, throw InvalidArgumentException if something is wrong
     *
     * @param manager
     * @return The selected resources converted back to json string
     * @throws InvalidArgumentsException If input args does not match server data
     */
    @Override
    public ArrayList<String> playAction(GameManager manager) throws InvalidArgumentsException {

        argumentValidation(manager);

        //I get here if no problems occur

        if(selection.size() > 0) {
            ArrayList<String> returnArgs = new ArrayList<>();

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            returnArgs.add(gson.toJson(selection));

            return returnArgs;
        } else
            return null;
    }

    //Check the client input with server data
    private void argumentValidation(GameManager manager) throws InvalidArgumentsException {

        //NonNull Condition
        if(selection == null)
            throw new InvalidArgumentsException(Packet.InstructionCode.CHOOSE_INIT_RES);

        //Search if at least one element is faith or blank
        if(selection.stream().anyMatch(res -> {
            return res.getType() == FinalResource.ResourceType.FAITH || res.getType() == FinalResource.ResourceType.BLANK;
        }))
            throw new InvalidArgumentsException(Packet.InstructionCode.CHOOSE_INIT_RES);

        //The amount of resources i expect
        int correctAmount = manager.getInitRes(manager.getCurrentPlayerIndex());

        //The amount of resources provided by the client arguments
        int actualAmount = selection.stream().mapToInt(Resource::getAmount).sum();

        if (actualAmount != correctAmount)
            throw new InvalidArgumentsException(Packet.InstructionCode.CHOOSE_INIT_RES);
    }
}
