package it.polimi.ingsw.IntelliCranio.server.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.IntelliCranio.server.GameManager;
import it.polimi.ingsw.IntelliCranio.server.Packet.InstructionCode;
import it.polimi.ingsw.IntelliCranio.server.Utility;
import it.polimi.ingsw.IntelliCranio.server.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.server.market.ResourceMarket;
import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static it.polimi.ingsw.IntelliCranio.server.ability.Ability.AbilityType.*;
import static it.polimi.ingsw.IntelliCranio.server.resource.FinalResource.ResourceType.*;

public class TakeResources implements Action{

    private char code; //Identify row or col number "c1", "r2", ...
    private int selected; //The number of row/col
    private ArrayList<LeadCard> usedLeaders; //The leader card used by client in this action

    /**
     * Get the row/col code and convert it into a character representing row or col
     * and a integer representing the selected row/col.
     * Get all the leader card used as a list of cards.
     * <p>
     *     Gets all the necessary parameter from json strings.
     * </p>
     * @param jsonArgs args[0] = the string with the code
     *                 args[1] = the list of leader cards
      */
    public TakeResources(ArrayList<String> jsonArgs) throws InvalidArgumentsException {

        Gson gson = new Gson();
        String input;

        try {
            input = gson.fromJson(jsonArgs.get(0), new TypeToken<String>(){}.getType());
            usedLeaders = gson.fromJson(jsonArgs.get(1), new TypeToken<ArrayList<LeadCard>>(){}.getType());

            if(input.length() != 2)
                throw new InvalidArgumentsException(InstructionCode.TAKE_RES);

            code = input.charAt(0);
            selected = Character.getNumericValue(input.charAt(1));

        } catch (Exception e) {
            throw new InvalidArgumentsException(InstructionCode.TAKE_RES);
        }
    }

    /**
     * Takes all the resources of the selected row/col and manage the
     * presence of FAITH/BLANK resources.
     *
     * @param manager
     * @return A unified list of resources without any BLANK or FAITH resource.
     * @throws InvalidArgumentsException
     */
    @Override
    public ArrayList<String> playAction(GameManager manager) throws InvalidArgumentsException {

        argumentValidation(manager);

        //I get here if there are no problems

        ResourceMarket resourceMarket = manager.getResourceMarket();
        ArrayList<FinalResource> marketResources = new ArrayList<>();

        if(code == 'c')
            marketResources = resourceMarket.selectColumn(selected);
        else if(code == 'r')
            marketResources = resourceMarket.selectRow(selected);

        //Manage Faith
        if(marketResources.stream().map(FinalResource::getType).anyMatch(type -> type == FAITH))
            manager.addPlayerFaith(manager.getCurrentPlayer());

        //Manage Blank
        for (LeadCard leader : usedLeaders) {
            marketResources.addAll(leader.getSpecialAbility().effect());
        }

        //Clear di Faith e Blank
        marketResources.removeIf(res -> (res.getType() == BLANK || res.getType() == FAITH));

        //Unify the resources
        marketResources = Utility.unifyResourceAmounts(marketResources);

        //Return the json args
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<String> returnArgs = new ArrayList<>();
        returnArgs.add(gson.toJson(marketResources));

        return returnArgs;
    }

    private void argumentValidation(GameManager manager) throws InvalidArgumentsException {

        AtomicBoolean error = new AtomicBoolean(false);

        //Invalid Code Condition
        if(code != 'c' && code != 'r')
            throw new InvalidArgumentsException(InstructionCode.TAKE_RES);

        //Invalid Row Condition
        if(code == 'r' && (selected < 0 || selected >= manager.getResourceMarket().ROWS))
            throw new InvalidArgumentsException(InstructionCode.TAKE_RES);

        //Invalid Column Condition
        if(code == 'c' && (selected < 0 || selected >= manager.getResourceMarket().COLUMNS))
            throw new InvalidArgumentsException(InstructionCode.TAKE_RES);

        //NonNull Condition
        if(usedLeaders == null)
            throw new InvalidArgumentsException(InstructionCode.TAKE_RES);

        //Invalid Ability Type Condition
        if(!usedLeaders.stream().allMatch(leader -> leader.getAbilityType() == RESOURCE))
            throw new InvalidArgumentsException(InstructionCode.TAKE_RES);

        //Not in player hand Condition
        usedLeaders.forEach(clientCard -> {
            ArrayList<LeadCard> playerCards = manager.getCurrentPlayer().getLeaders();
            if(playerCards.stream().noneMatch(serverCard -> serverCard.getID().equals(clientCard.getID())))
                error.set(true);
        });

        if(error.get())
            throw new InvalidArgumentsException(InstructionCode.TAKE_RES);

        //Not Active Condition
        usedLeaders.forEach(clientCard -> {
            ArrayList<LeadCard> playerCards = manager.getCurrentPlayer().getLeaders();
            if(playerCards.stream().anyMatch(serverCard ->
                    (serverCard.getID().equals(clientCard.getID()) && !serverCard.isActive())))
                error.set(true);
        });

        if(error.get())
            throw new InvalidArgumentsException(InstructionCode.TAKE_RES);

        //Invalid Amount Condition
        ResourceMarket resourceMarket = manager.getResourceMarket();
        ArrayList<FinalResource> marketResources = new ArrayList<>();

        if(code == 'c')
            marketResources = resourceMarket.selectColumn(selected);
        else if(code == 'r')
            marketResources = resourceMarket.selectRow(selected);

        int blankAmount = 0;

        if(marketResources.stream().map(FinalResource::getType).anyMatch(type -> type == BLANK))
            blankAmount = marketResources.stream()
                    .filter(res -> res.getType() == BLANK)
                    .mapToInt(FinalResource::getAmount).findFirst().getAsInt();

        if(usedLeaders.size() != blankAmount)
            throw new InvalidArgumentsException(InstructionCode.TAKE_RES);

    }
}
