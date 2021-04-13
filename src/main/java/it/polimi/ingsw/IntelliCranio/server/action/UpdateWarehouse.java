package it.polimi.ingsw.IntelliCranio.server.action;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.server.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.server.Utility;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.models.player.Player;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

import static com.cedarsoftware.util.DeepEquals.deepEquals;

public class UpdateWarehouse implements Action{

    private Resource[] clientDepot;
    private ArrayList<Resource> clientExtraResources;

    /**
     * Creates a new action. Get the depot configuration and the left resources from client.
     * <p>
     *     Gets all the necessary parameter from json strings.
     * </p>
     * @param jsonArgs args[0] is the depot configuration received from client.
     *                 args[1] is the list of extra resources (can be empty if the warehouse has only been changed)
     */
    public UpdateWarehouse(ArrayList<String> jsonArgs) throws InvalidArgumentsException {
        Gson gson = new Gson();

        //There has to be at least 2 arguments
        if(jsonArgs.size() > 1) {
            clientDepot = gson.fromJson(jsonArgs.get(0), new TypeToken<Resource[]>(){}.getType());
            clientExtraResources = gson.fromJson(jsonArgs.get(1), new TypeToken<ArrayList<Resource>>(){}.getType());
        }
    }

    /**
     * Check that the resources provided by the client and the server data match.
     * If so, Warehouse.update method is called and the depot is updated.
     * Every other player in order gains faith for every resource discarded.
     *
     * @param manager
     * @return null
     * @throws InvalidArgumentsException
     */
    @Override
    public ArrayList<String> playAction(Game manager) throws InvalidArgumentsException {

        argumentValidation(manager);

        //I get here if data is correct

        int discardedAmount = manager.getCurrentPlayer().getWarehouse().update(clientDepot, clientExtraResources);

        if(discardedAmount == -1){
            //Todo: Handle error
        } else {
            Player currentPlayer = manager.getCurrentPlayer();
            ArrayList<Player> players = manager.getPlayers();

            for(int i = 0; i < discardedAmount; ++i) {
                players.stream()
                        .filter(player -> !player.equals(currentPlayer))
                        .forEach(manager::addPlayerFaith); //ERROR: Method not implemented yet
            }
        }

        return null;
    }

    private void argumentValidation(Game manager) throws InvalidArgumentsException {

        //NonNull Condition
        if(clientDepot == null || clientExtraResources == null)
            throw new InvalidArgumentsException(InstructionCode.UPDATE_WAREHOUSE);

        //Depot from client has BLANK or FAITH Condition
        if(Arrays.stream(clientDepot)
                .filter(Objects::nonNull)
                .anyMatch(res -> (res.getType() == ResourceType.BLANK) || res.getType() == ResourceType.FAITH))
            throw new InvalidArgumentsException(InstructionCode.UPDATE_WAREHOUSE);

        //Resources from client has BLANK or FAITH Condition
        if(clientExtraResources.stream()
                .filter(Objects::nonNull)
                .anyMatch(res -> (res.getType() == ResourceType.BLANK) || res.getType() == ResourceType.FAITH))
            throw new InvalidArgumentsException(InstructionCode.UPDATE_WAREHOUSE);

        ArrayList<Resource> clientResources = new ArrayList<>(); //To store both depot and extra resources from client
        ArrayList<Resource> serverResources = new ArrayList<>(); //To store both depot and extra resources from server

        //Add the client resources from depot and extra (extra can be empty but nonNull)
        Arrays.stream(clientDepot).filter(Objects::nonNull).forEach(clientResources::add);
        clientResources.addAll(clientExtraResources);

        //Add the server resources from depot and extra (extra can be empty but nonNull)
        Resource[] serverDepot = manager.getCurrentPlayer().getWarehouse().getDepot();
        ArrayList<Resource> lastActionResources = new ArrayList<>(); //Example: Resources from TakeResources
        ArrayList<String> lastActionReturnArgs = manager.getLastActionReturnArgs();

        if(lastActionReturnArgs != null) {

            Gson gson = new Gson();

            try {
                lastActionResources = gson.fromJson(lastActionReturnArgs.get(0),
                        new TypeToken<ArrayList<Resource>>(){}.getType());
            } catch (Exception e) {
                throw new InvalidArgumentsException(InstructionCode.UPDATE_WAREHOUSE);
            }
        }

        //Add all the items
        Arrays.stream(serverDepot).filter(Objects::nonNull).forEach(serverResources::add);
        serverResources.addAll(lastActionResources);

        //Check if server and client resources match
        clientResources = Utility.unifyResourceAmounts(clientResources);
        serverResources = Utility.unifyResourceAmounts(serverResources);

        clientResources.sort(Comparator.comparing(FinalResource::getType));
        serverResources.sort(Comparator.comparing(FinalResource::getType));

        //Server and client does not match Condition
        if(!deepEquals(clientResources, serverResources))
            throw new InvalidArgumentsException(InstructionCode.UPDATE_WAREHOUSE);

    }
}
