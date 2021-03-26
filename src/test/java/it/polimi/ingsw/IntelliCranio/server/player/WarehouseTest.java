package it.polimi.ingsw.IntelliCranio.server.player;

import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.server.resource.Resource;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class WarehouseTest {

    @Test
    public void update() {
        ArrayList<Resource> tempDepot=new ArrayList<>(3);
        ArrayList<Resource> marketRes=new ArrayList<>(2);
        Warehouse w1=new Warehouse(3);

        tempDepot.add(new Resource(FinalResource.ResourceType.COIN,3));//2
        tempDepot.add(new Resource(FinalResource.ResourceType.COIN,3));//1
        tempDepot.add(new Resource(FinalResource.ResourceType.COIN,3));//0

        marketRes.add(new Resource(FinalResource.ResourceType.COIN,2));//2

        assertEquals(-1,w1.update(tempDepot,marketRes));
        tempDepot.clear();
        tempDepot.add(new Resource(FinalResource.ResourceType.COIN,3));//2
        tempDepot.add(new Resource(FinalResource.ResourceType.FAITH,3));//1
        tempDepot.add(new Resource(FinalResource.ResourceType.STONE,3));//0

        assertEquals(5,w1.update(tempDepot,marketRes));
        tempDepot.clear();

        tempDepot.add(new Resource(FinalResource.ResourceType.COIN,3));//2
        tempDepot.add(new Resource(FinalResource.ResourceType.COIN,3));//1
        tempDepot.add(new Resource(FinalResource.ResourceType.FAITH,3));//0

        assertEquals(-1,w1.update(tempDepot,marketRes));


    }
}