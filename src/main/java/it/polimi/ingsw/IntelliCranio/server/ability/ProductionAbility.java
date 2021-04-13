package it.polimi.ingsw.IntelliCranio.server.ability;

import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;

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
