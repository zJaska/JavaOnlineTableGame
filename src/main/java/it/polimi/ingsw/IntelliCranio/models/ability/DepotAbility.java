package it.polimi.ingsw.IntelliCranio.models.ability;

import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;

import java.util.ArrayList;

public class DepotAbility extends Ability{

    Resource depot;

    public DepotAbility(FinalResource.ResourceType type) {
        super(type);
    }


    public void addResource() {

        if(isEmpty())
            depot = new Resource(type, 1);
        else if(!isFull())
            depot.addAmount(1);
    }

    /**
     * Remove a single unit from depot if is not empty.
     * If depot amount is 0, it is set to null.
     */
    public void removeResource() {
        if(!isEmpty())
            depot.removeAmount(1);

        if(depot.getAmount() == 0)
            depot = null;
    }

    public Resource getDepot() {
        return depot;
    }

    public boolean isEmpty() {
        return depot == null;
    }

    public boolean isFull() {

        if(isEmpty())
            return false;

        return depot.getAmount() == 2;
    }

}
