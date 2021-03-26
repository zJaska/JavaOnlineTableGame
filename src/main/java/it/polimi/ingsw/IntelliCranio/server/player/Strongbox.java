package it.polimi.ingsw.IntelliCranio.server.player;

import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.server.resource.Resource;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Strongbox {

    private ArrayList<Resource> resources;


    public int getValue(FinalResource.ResourceType resource) {
        return this.resources.stream().filter(res -> res.getType() == resource ).findFirst().get().getAmount();
    }

    public void addResources(FinalResource.ResourceType resource, int amount) {
        this.resources.stream().filter(res -> res.getType() == resource ).findFirst().get().addAmount(amount);
    }

    public void removeResources(FinalResource.ResourceType resource, int amount) {
        Resource temp=this.resources.stream().filter(res -> res.getType() == resource ).findFirst().get();

        if(temp.getAmount()<amount){
            temp.setAmount(0);
            return;
        }

        this.resources.stream().filter(res -> res.getType() == resource ).findFirst().get().removeAmount(amount);
    }


}
