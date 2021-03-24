package it.polimi.ingsw.IntelliCranio.server.ability;

import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.server.resource.Resource;

import java.util.ArrayList;

public abstract class Ability {

    protected FinalResource.ResourceType type;

    public abstract ArrayList<Resource> effect();
}
