package it.polimi.ingsw.IntelliCranio.server.ability;

import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.server.resource.Resource;

import java.util.ArrayList;

public class DepotAbility extends Ability{

    public DepotAbility(FinalResource.ResourceType type) {
        super(type);
    }

    @Override
    public ArrayList<Resource> effect() {
        System.out.println("DepotAbility");
        return null;
    }
}
