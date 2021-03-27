package it.polimi.ingsw.IntelliCranio.server.ability;

import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.server.resource.Resource;

import java.util.ArrayList;

public class ProductionAbility extends Ability{

    public ProductionAbility(FinalResource.ResourceType type) {
        super(type);
    }

    @Override
    public ArrayList<Resource> effect() {
        System.out.println("ProductionAbility");
        return null;
    }
}
