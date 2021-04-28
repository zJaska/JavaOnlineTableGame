package it.polimi.ingsw.IntelliCranio.models.player;

import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.util.Lists;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;

import java.util.ArrayList;
import java.util.Objects;

public class Warehouse {

    private Resource[] depot; //First element is top line (1 resource only), every other element is one resource bigger

    /**
     * It's the constructor of Warehouse class
     *
     * @param dim the dimension of the depot
     */
    public Warehouse(int dim) {
        depot = new Resource[dim];
    }


    public Resource[] getDepot() {
        return depot;
    }

    /**
     *
     * Swap two lines in the depot and add the resource surplus
     * to player extra resources.
     * Set the amount to depot line capacity
     *
     * @param first first index of depot line
     * @param second second index of depot line
     * @param playerExtra pool of the player extra resources
     */
    public void swapLines(int first, int second, ArrayList<Resource> playerExtra) {

        Resource temp; //To store a resource from depot before swapping

        temp = depot[first]; //Store the first index selected

        depot[first] = depot[second]; //Add resource from second index to first one
        depot[second] = temp; //Complete the swap

        //region Check for surplus

        //Surplus on first index after swapping
        if(depot[first].getAmount() > first + 1) {
            //Add to extra a new resource of the same type and with amount
            //given by difference between resource amount and depot capacity
            playerExtra.add(new Resource(depot[first].getType(), depot[first].getAmount() - (first + 1)));
            depot[first].setAmount(first + 1);
        }

        //Surplus on second index after swapping
        if(depot[second].getAmount() > second + 1) {
            //Add to extra a new resource of the same type and with amount
            //given by difference between resource amount and depot capacity
            playerExtra.add(new Resource(depot[second].getType(), depot[second].getAmount() - (second + 1)));
            depot[second].setAmount(second+1);
        }

        //Unify the extra resources before returning
        playerExtra = Lists.unifyResourceAmounts(playerExtra);

        //endregion

    }

    /**
     *
     * Increment the value of a resource in depot and remove one
     * from the extra resources list.
     * Remove the resource from list if value reduced to 0
     *
     * @param depotLine index of the depot array
     * @param resource the resource to take from extra resources
     * @param player The current player
     */
    public void addFromExtra(int depotLine, Resource resource, Player player) {

        depot[depotLine].addAmount(1); //Add the amount to depot

        //reduce the amount of the resource by 1
        player.removeExtra(resource.getType(), 1);

    }

    /**
     * Add a resource to the extra resources list. The list is then unified again.
     * Remove a single amount of that resource from selected depot line
     * @param depotLine index of the line in depot
     * @param player The current player
     */
    public void removeToExtra(int depotLine, Player player) {

        //Add to extra resources before removing. Prevent null check on depot line later.
        player.addExtra(depot[depotLine].getType(), 1);

        //Remove 1 resource from depot
        removeAmount(depotLine, 1);

    }

    /**
     * <summary>
     * check number of resources discarded and update and
     * check if there are 2 items on different lines.
     * </summary>
     *
     * @param tempDepot this is sent by client
     * @param extraRes  this is sent by client which is left from market.
     * @return -1 if error else number of cards discarded.
     */
    public int update(Resource[] tempDepot, ArrayList<Resource> extraRes) {
        int numDiscards = 0;

        if (tempDepot.length > depot.length)
            return -1;

        for (int i = 0; i < tempDepot.length - 1; ++i)
            for (int j = i + 1; j < tempDepot.length; ++j)
                if (tempDepot[i] != null && tempDepot[j] != null)
                    if (tempDepot[i].getType() == tempDepot[j].getType())
                        return -1;


        for (int i = 0; i < tempDepot.length; ++i)
            if (tempDepot[i] != null)
                if (tempDepot[i].getAmount() > (i + 1)) {
                    numDiscards += tempDepot[i].getAmount() - (i + 1);
                    tempDepot[i].setAmount(i + 1);
                }

        depot = tempDepot;


        numDiscards += extraRes.stream().filter(Objects::nonNull).mapToInt(Resource::getAmount).sum();


        return numDiscards;
    }

    /**
     * Remove the specified amount from selected line. If the amount of resources is 0, depot line is set to null.
     * @param depotLine Index of the depot
     * @param amount Amount of resources to remove
     */
    public void removeAmount(int depotLine, int amount) {
        depot[depotLine].removeAmount(amount);

        //Set the line to null if there are no resources there
        if(depot[depotLine].getAmount() == 0)
            depot[depotLine] = null;
    }

    //region Utility

    /**
     * Check if selected line in depot is full
     * @param depotLine The line to check
     * @return True if line is full, false otherwise
     */
    public boolean isFull(int depotLine) {
        return depot[depotLine].getAmount() == depotLine + 1;
    }

    /**
     * Check if selected line in depot is empty
     * @param depotLine The line to check
     * @return True if line is null, false otherwise
     */
    public boolean isEmpty(int depotLine) {
        return depot[depotLine] == null;
    }

    public FinalResource.ResourceType getType(int depotLine) {
        return depot[depotLine].getType();
    }

    //endregion

}
