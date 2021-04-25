package it.polimi.ingsw.IntelliCranio.server.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;

import java.util.ArrayList;

import static com.cedarsoftware.util.DeepEquals.deepEquals;

public class ChooseInitResources implements Action {

    private ArrayList<Resource> selection; //The resources the player choosed

    /**
     * Create the new Action and get the necessary parameters from json.
     *
     * @param jsonArgs The list of resources the player choosed.
     */
    public ChooseInitResources(ArrayList<String> jsonArgs) throws InvalidArgumentsException {

        Gson gson = new Gson();

        if(jsonArgs.size() > 0)
            try{
                selection = gson.fromJson(jsonArgs.get(0), new TypeToken<ArrayList<Resource>>(){}.getType());
            } catch (Exception e) {
                throw new InvalidArgumentsException(InstructionCode.CHOOSE_INIT_RES);
            }
    }

    /**
     * Check input consistency, throw InvalidArgumentException if something is wrong.
     * Return the selected resources to use for other action checks.
     *
     * @param manager
     * @return The selected resources converted back to json string
     * @throws InvalidArgumentsException If input args does not match server data
     */
    @Override
    public ArrayList<String> playAction(Game manager) throws InvalidArgumentsException {

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
    private void argumentValidation(Game manager) throws InvalidArgumentsException {

        //NonNull Condition
        if(selection == null)
            throw new InvalidArgumentsException(InstructionCode.CHOOSE_INIT_RES);

        //Faith or Blank Condition
        if(selection.stream().anyMatch(res -> {
            return res.getType() == ResourceType.FAITH || res.getType() == ResourceType.BLANK;
        }))
            throw new InvalidArgumentsException(InstructionCode.CHOOSE_INIT_RES);

        //Invalid Amount Condition
        //The amount of resources i expect
        int correctAmount = manager.getInitRes(manager.getCurrentPlayerIndex());

        //The amount of resources provided by the client arguments
        int actualAmount = selection.stream().mapToInt(Resource::getAmount).sum();

        if (actualAmount != correctAmount)
            throw new InvalidArgumentsException(InstructionCode.CHOOSE_INIT_RES);
    }
}
