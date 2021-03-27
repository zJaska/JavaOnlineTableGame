package it.polimi.ingsw.IntelliCranio.server.ability;

import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.server.resource.Resource;

import java.util.ArrayList;

public class ResourceAbility extends Ability{

    public ResourceAbility(FinalResource.ResourceType type) {
        super(type);
    }

    @Override
    public ArrayList<Resource> effect() {
        throw  new UnsupportedOperationException();
    }
}
