package it.polimi.ingsw.IntelliCranio.server.ability;

import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.server.resource.Resource;

import java.util.ArrayList;

public abstract class Ability {

    protected FinalResource.ResourceType type;

    public Ability(FinalResource.ResourceType type) {
        this.type = type;
    }

    public ArrayList<Resource> effect() {
        System.out.println("Ability");
        return null;
    }
}
