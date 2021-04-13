package it.polimi.ingsw.IntelliCranio.models.player;

import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;

import java.util.ArrayList;
import java.util.Arrays;

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

    public int getAmount(ResourceType resource) {
        return resources.stream()
                .filter(res -> res.getType() == resource)
                .findFirst().get().getAmount();
    }

    public void addResources(ResourceType resource, int amount) {
        resources.stream()
                .filter(res -> res.getType() == resource)
                .findFirst().get()
                .addAmount(amount);
    }

    public void removeResources(ResourceType resource, int amount) {
        Resource temp = resources.stream()
                .filter(res -> res.getType() == resource)
                .findFirst().get();

        if(temp.getAmount() < amount){
            temp.setAmount(0);
            return;
        }

        resources.stream()
                .filter(res -> res.getType() == resource)
                .findFirst().get()
                .removeAmount(amount);
    }


}
