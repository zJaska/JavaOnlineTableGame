package it.polimi.ingsw.IntelliCranio.server.player;

import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.server.resource.Resource;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

public class Strongbox {

    private ArrayList<Resource> resources;


    public int getValue(FinalResource.ResourceType resource) {
        throw new NotImplementedException();
    }

    public void addResources(FinalResource.ResourceType resource, int amount) {
        throw new NotImplementedException();
    }

    public void removeResources(FinalResource.ResourceType resource, int amount) {
        throw new NotImplementedException();
    }


}
