package it.polimi.ingsw.IntelliCranio.models.player;

import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Strongbox implements Serializable {

    private ArrayList<Resource> resources = new ArrayList<>();

    /**
     * Initializes all types of Resources to 0 amount
     */

    public Strongbox() {
        Arrays.stream(ResourceType.values())
                .filter(type -> !FinalResource.EXCLUDED.contains(type))
                .forEach( type -> resources.add(new Resource(type, 0)));
    }

    /**
     * Get the amount of the resource given its type
     * @param rt The type to get its amount
     * @return The amount of resource
     */
    public int getAmount(ResourceType rt) {
        return resources.stream()
                .filter(res -> res.getType() == rt)
                .findFirst().get().getAmount();
    }

    /**
     * Add amount resources to the strongbox
     * @param rt The resource to add
     * @param amount The amount of resource to add
     */
    public void addResources(ResourceType rt, int amount) {
        if (FinalResource.EXCLUDED.contains(rt))
            return;

        resources.stream()
                .filter(res -> res.getType() == rt)
                .findFirst().get()
                .addAmount(amount);
    }

    /**
     * Remove selected amount of resources of type rt
     * If amount is greater than actual value, resource amount is set to 0.
     * @param rt The type of the resource
     * @param amount The amount to remove
     */
    public void removeResources(ResourceType rt, int amount) {
        if (FinalResource.EXCLUDED.contains(rt))
            return;

        Resource temp = resources.stream()
                .filter(res -> res.getType() == rt)
                .findFirst().get();

        if(temp.getAmount() < amount){
            temp.setAmount(0);
            return;
        }

        resources.stream()
                .filter(res -> res.getType() == rt)
                .findFirst().get()
                .removeAmount(amount);
    }

    //region Utility

    public ArrayList<Resource> getAllResources() {
        return new ArrayList<>(resources);
    }

    //endregion


}
