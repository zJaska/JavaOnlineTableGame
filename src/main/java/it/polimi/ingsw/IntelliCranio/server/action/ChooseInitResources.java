package it.polimi.ingsw.IntelliCranio.server.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.server.GameManager;
import it.polimi.ingsw.IntelliCranio.server.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.server.player.Warehouse;
import it.polimi.ingsw.IntelliCranio.server.resource.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.cedarsoftware.util.DeepEquals.deepEquals;

public class ChooseInitResources implements Action{

    private ArrayList<Resource> selection; //The resources the player choosed
    private Resource[] tempDepot; //The depot configuration of the client

    /**
     * Gets all the necessary parameter from json strings.
     *
     * @param jsonArgs Two elements containing the list of resources the player choosed
     *                and the depot configuration.
     */
    public ChooseInitResources(ArrayList<String> jsonArgs) {

        Gson gson = new Gson();

        selection = gson.fromJson(jsonArgs.get(0), new TypeToken<ArrayList<Resource>>(){}.getType());
        tempDepot = gson.fromJson(jsonArgs.get(1), new TypeToken<Resource[]>(){}.getType());
    }

    @Override
    public void playAction(GameManager manager) throws InvalidArgumentsException {

        //region Error Handling

        AtomicBoolean error = new AtomicBoolean(false);

        //Check if depot contains all the elements in selection (can have more then selection)
        selection.forEach(res -> {
            if(Arrays.stream(tempDepot).filter(Objects::nonNull).noneMatch(dep -> {
                return dep.getType() == res.getType() && dep.getAmount() == res.getAmount();
            }))
                error.set(true);
        });

        if(error.get())
            throw new InvalidArgumentsException(Packet.InstructionCode.CHOOSE_INIT_RES);

        //Check if selection contains all the elements in depot
        Arrays.stream(tempDepot).filter(Objects::nonNull).forEach(dep -> {
            if(selection.stream().noneMatch(res -> {
                return dep.getType() == res.getType() && dep.getAmount() == res.getAmount();
            }))
                error.set(true);
        });

        if(error.get())
            throw new InvalidArgumentsException(Packet.InstructionCode.CHOOSE_INIT_RES);


        //The amount of resources i expect
        int correctAmount = manager.getInitRes(manager.getCurrentPlayerIndex());

        //The amount of resources provided by the client arguments
        int actualAmount = selection.stream().mapToInt(Resource::getAmount).sum();

        if(actualAmount != correctAmount)
            throw new InvalidArgumentsException(Packet.InstructionCode.CHOOSE_INIT_RES);
        //endregion

        //I get here if no problems occur

        //Try update the warehouse, get -1 if error occurs
        Warehouse warehouse = manager.getCurrentPlayer().getWarehouse();
        if(warehouse.update(tempDepot, null) == -1) {
            //Todo: Update error handling
        }
    }
}
