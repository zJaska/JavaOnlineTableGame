package it.polimi.ingsw.IntelliCranio.server.ability;

import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;

import java.util.ArrayList;

public abstract class Ability {

    public enum AbilityType{SALE, DEPOT, RESOURCE, PRODUCTION}

    protected ResourceType type;

    public Ability(ResourceType type) {
        this.type = type;
    }

    public ArrayList<Resource> effect() {
        System.out.println("Ability");
        return null;
    }

}
