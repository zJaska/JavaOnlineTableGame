package it.polimi.ingsw.IntelliCranio.server.player;

import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource.ResourceType;
import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.server.resource.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Strongbox {

    private ArrayList<Resource> resources = new ArrayList<>();

    /**
     * Initializes all types of Resources to 0 amount
     */

    public Strongbox() {
        Arrays.stream(ResourceType.values())
                .filter(type -> type != ResourceType.BLANK && type != ResourceType.FAITH)
                .forEach( type -> resources.add(new Resource(type, 0)));
    }

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
