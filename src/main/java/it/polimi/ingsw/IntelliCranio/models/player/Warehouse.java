package it.polimi.ingsw.IntelliCranio.models.player;

import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.util.Lists;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Warehouse implements Serializable {

    private Resource[] depot; //First element is top line (1 resource only), every other element is one resource bigger

    /**
     * It's the constructor of Warehouse class
     *
     * @param dim the dimension of the depot
     */
    public Warehouse(int dim) { depot = new Resource[dim]; }


    public Resource[] getDepot() {
        return depot;
    }

    /**
     *
     * Swap two lines in the depot and add the resource surplus
     * to player extra resources.
     * Set the amount to depot line capacity
     *  @param first first index of depot line
     * @param second second index of depot line
     * @return
     */
    public ArrayList<Resource> swapLines(int first, int second) {

        //region Swap
        Resource temp; //To store a resource from depot before swapping

        temp = depot[first]; //Store the first index selected

        depot[first] = depot[second]; //Add resource from second index to first one
        depot[second] = temp; //Complete the swap
        //endregion

        //region Check for surplus

        ArrayList<Resource> extra = new ArrayList<>();

        //Surplus on first index after swapping
        if(depot[first] != null && depot[first].getAmount() > first + 1) {
            //Add to extra a new resource of the same type and with amount
            //given by difference between resource amount and depot capacity
            extra.add(new Resource(depot[first].getType(), depot[first].getAmount() - (first + 1)));
            depot[first].setAmount(first + 1);
        }

        //Surplus on second index after swapping
        if(depot[second] != null && depot[second].getAmount() > second + 1) {
            //Add to extra a new resource of the same type and with amount
            //given by difference between resource amount and depot capacity
            extra.add(new Resource(depot[second].getType(), depot[second].getAmount() - (second + 1)));
            depot[second].setAmount(second+1);
        }

        //endregion

        //Return extra resources
        return extra;

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
     * Remove a single amount from selected line. If the amount of resources is 0, depot line is set to null.
     * @param depotLine Index of the depot
     */
    public void remove(int depotLine) {
        if(!isEmpty(depotLine))
            depot[depotLine].removeAmount(1);

        //Set the line to null if there are no resources there
        if(depot[depotLine].getAmount() == 0)
            depot[depotLine] = null;
    }

    /**
     * Add a single amount in specified line. Add a new resource with amount 1 if line is empty.
     * @param depotLine Line of depot to add a resource
     * @param resource The resource to add if line is empty
     */
    public void add(int depotLine, Resource resource) {
        if(isEmpty(depotLine))
            depot[depotLine] = new Resource(resource.getType(), 1);
        else if(!isFull(depotLine))
            depot[depotLine].addAmount(1);

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
        if(depot[depotLine] != null)
            return depot[depotLine].getType();
        return null;
    }

    public ArrayList<Resource> getAll() {
        ArrayList<Resource> temp = new ArrayList<>();

        Arrays.stream(depot).forEach(res -> temp.add(res));

        return temp;
    }

    /**
     * Check if some resource is already present in a different line respect the one to add the new resource
     * @param depotLine The line to put the resource
     * @param resource The resource to add
     * @return True if the resource is already present in a different line, false otherwise
     */
    public boolean isPresent(int depotLine, Resource resource) {
        for(int i = 0; i < depot.length; ++i)
            if(i != depotLine)
                if(depot[i].getType() == resource.getType())
                    return true;

        return false;
    }

    //endregion

}
