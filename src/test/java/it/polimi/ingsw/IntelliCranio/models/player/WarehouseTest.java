package it.polimi.ingsw.IntelliCranio.models.player;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import org.junit.Test;

import java.util.ArrayList;

import static java.util.Objects.deepEquals;
import static org.junit.Assert.*;

public class WarehouseTest {

    @Test
    public void someTest(){
        Warehouse w1 = new Warehouse(3);

        w1.add(1,new Resource(FinalResource.ResourceType.SHIELD,1));
        w1.add(1,new Resource(FinalResource.ResourceType.SHIELD,1));

        w1.add(2,new Resource(FinalResource.ResourceType.COIN,1));
        w1.add(2,new Resource(FinalResource.ResourceType.COIN,1));
        w1.add(2,new Resource(FinalResource.ResourceType.COIN,1));



        ArrayList<Resource> ret=w1.swapLines(1,2);

        assertTrue(deepEquals(FinalResource.ResourceType.COIN,ret.get(0).getType()));
        assertTrue(deepEquals(1,ret.get(0).getAmount()));

        assertTrue(deepEquals(2,w1.getDepot()[1].getAmount()));
        assertTrue(deepEquals(2,w1.getDepot()[2].getAmount()));

        assertTrue(deepEquals(FinalResource.ResourceType.SHIELD,w1.getDepot()[2].getType()));

        assertTrue(deepEquals(FinalResource.ResourceType.COIN,w1.getDepot()[1].getType()));

        w1.add(2,new Resource(FinalResource.ResourceType.COIN,1));

        assertTrue(w1.isFull(2));

        w1.remove(2);

        assertFalse(w1.isFull(2));

        assertTrue(w1.isPresent(2,new Resource(FinalResource.ResourceType.COIN,1)));//

        assertFalse(w1.isPresent(1,new Resource(FinalResource.ResourceType.COIN,1)));

        assertFalse(w1.isEmpty(2));

        assertTrue(w1.isEmpty(0));




    }

    @Test
    public void update() {
        Resource[] tempDepot = new Resource[3];
        ArrayList<Resource> marketRes = new ArrayList<>(2);
        Warehouse w1 = new Warehouse(3);

        tempDepot[0] = new Resource(FinalResource.ResourceType.COIN,3);//2
        tempDepot[1] = new Resource(FinalResource.ResourceType.COIN,3);//1
        tempDepot[2] = new Resource(FinalResource.ResourceType.COIN,3);//0

        marketRes.add(new Resource(FinalResource.ResourceType.COIN,2));//2

        assertEquals(-1,w1.update(tempDepot,marketRes));
        tempDepot = new Resource[3];
        tempDepot[0] = new Resource(FinalResource.ResourceType.COIN,3);//2
        tempDepot[1] = new Resource(FinalResource.ResourceType.FAITH,3);//1
        tempDepot[2] = new Resource(FinalResource.ResourceType.STONE,3);//0

        assertEquals(5,w1.update(tempDepot,marketRes));
        tempDepot = new Resource[3];

        tempDepot[0] = new Resource(FinalResource.ResourceType.COIN,3);//2
        tempDepot[1] = new Resource(FinalResource.ResourceType.COIN,3);//1
        tempDepot[2] = new Resource(FinalResource.ResourceType.FAITH,3);//0

        assertEquals(-1,w1.update(tempDepot,marketRes));


    }
}