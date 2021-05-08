package it.polimi.ingsw.IntelliCranio.server.ability;

import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;

import java.util.ArrayList;

public class DepotAbility extends Ability{

    Resource depot;

    public DepotAbility(FinalResource.ResourceType type) {
        super(type);
    }

    @Override
    public ArrayList<Resource> effect() {
        System.out.println("DepotAbility");
        return null;
    }

    public void addResource() {

        if(isEmpty())
            depot = new Resource(type, 1);
        else if(!isFull())
            depot.addAmount(1);
    }

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
