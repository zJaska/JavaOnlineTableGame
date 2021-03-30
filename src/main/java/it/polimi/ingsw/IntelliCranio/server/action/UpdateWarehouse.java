package it.polimi.ingsw.IntelliCranio.server.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.server.GameManager;
import it.polimi.ingsw.IntelliCranio.server.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.server.player.Player;
import it.polimi.ingsw.IntelliCranio.server.resource.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class UpdateWarehouse implements Action{

    private Resource[] clientDepot;
    private ArrayList<Resource> clientExtraResources;

    /**
     * Creates a new instance of this class.
     * <p>
     *     Gets all the necessary parameter from json strings.
     * </p>
     * @param jsonArgs args[0] is the depot configuration received from client.
     *                 args[1] is the list of extra resources (can be empty if the warehouse has only been changed)
     */
    public UpdateWarehouse(ArrayList<String> jsonArgs) {
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
     *
     * @param manager
     * @return null
     * @throws InvalidArgumentsException
     */
    @Override
    public ArrayList<String> playAction(GameManager manager) throws InvalidArgumentsException {

        argumentValidation(manager);

        //I get here if data is correct

        int discardedAmount = manager.getCurrentPlayer().getWarehouse().update(clientDepot, clientExtraResources);

        if(discardedAmount == -1){
            //Todo: Handle error
        } else { //Todo: Check if 0
            Player currentPlayer = manager.getCurrentPlayer();
            manager.getPlayers().stream().filter(player -> { //Todo: Change with single increment for each player
                return !player.equals(currentPlayer);
            }).forEach(player -> {
                manager.addPlayerFaith(player, discardedAmount); //ERROR: Method not implemented yet
            });
        }

        return null;
    }

    private void argumentValidation(GameManager manager) throws InvalidArgumentsException {

        //NonNull Condition
        if(clientDepot == null || clientExtraResources == null)
            throw new InvalidArgumentsException(InstructionCode.UPDATE_WAREHOUSE);

        ArrayList<Resource> clientResources = new ArrayList<>(); //To store both depot and extra resources from client
        ArrayList<Resource> serverResources = new ArrayList<>(); //To store both depot and extra resources from server

        //Todo: Group amount
        //Add the client resources from depot and extra (extra can be empty but nonNull)
        Arrays.stream(clientDepot).filter(Objects::nonNull).forEach(clientResources::add);
        clientResources.addAll(clientExtraResources);

        ArrayList<Resource> temp = new ArrayList<>();

        clientResources.forEach(res -> {
            if(temp.stream().noneMatch(tempRes -> {
                return tempRes.getType() == res.getType();
            }))
                temp.add(res);
            else
                temp.stream().filter(tempRes -> {
                    return tempRes.getType() == res.getType();
                }).findFirst().get().addAmount(res.getAmount());
        });

        //Add the client resources from depot and extra (extra can be empty but nonNull)
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
        AtomicBoolean error = new AtomicBoolean(false);

        //Check if server contains all the elements in client (can have more)
        clientResources.forEach(clientRes -> {
            if (serverResources.stream().noneMatch(serverRes -> {
                return serverRes.getType() == clientRes.getType() && serverRes.getAmount() == clientRes.getAmount();
            }))
                error.set(true);
        });

        if (error.get())
            throw new InvalidArgumentsException(InstructionCode.CHOOSE_INIT_RES);

        //Check if client contains all the elements in server
        serverResources.forEach(serverRes -> {
            if (clientResources.stream().noneMatch(clientRes -> {
                return serverRes.getType() == clientRes.getType() && serverRes.getAmount() == clientRes.getAmount();
            }))
                error.set(true);
        });

        if (error.get())
            throw new InvalidArgumentsException(InstructionCode.CHOOSE_INIT_RES);

    }
}