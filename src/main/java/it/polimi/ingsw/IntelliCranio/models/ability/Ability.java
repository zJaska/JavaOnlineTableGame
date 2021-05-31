package it.polimi.ingsw.IntelliCranio.models.ability;

import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Ability implements Serializable {

    public enum AbilityType{SALE, DEPOT, RESOURCE, PRODUCTION}

    protected ResourceType type;

    public Ability(ResourceType type) {
        this.type = type;
    }


}
