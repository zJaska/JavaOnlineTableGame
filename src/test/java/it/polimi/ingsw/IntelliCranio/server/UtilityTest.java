package it.polimi.ingsw.IntelliCranio.server;

import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class UtilityTest {

    @Test
    void unifyResourceAmounts() {
        ArrayList<Resource> res = new ArrayList<>();
        res.add(new Resource(FinalResource.ResourceType.SHIELD,1));
        res.add(new Resource(FinalResource.ResourceType.SHIELD,2));
        res.add(new Resource(FinalResource.ResourceType.STONE,10));
        res.add(new Resource(FinalResource.ResourceType.COIN,2));
        res.add(new Resource(FinalResource.ResourceType.STONE,1));

        res = Utility.unifyResourceAmounts(res);

        assertEquals(3,res.get(0).getAmount());
        assertEquals(11,res.get(1).getAmount());
        assertEquals(2,res.get(2).getAmount());
    }
}