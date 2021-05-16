package it.polimi.ingsw.IntelliCranio.server.ability;

import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.cedarsoftware.util.DeepEquals.deepEquals;
import static org.junit.jupiter.api.Assertions.*;

class DepotAbilityTest {


    @Test
    void TestAll() {
        DepotAbility depot;
        depot=new DepotAbility(FinalResource.ResourceType.COIN);

        depot.addResource();

        assertTrue(deepEquals(1,depot.getDepot().getAmount()));

        depot.addResource();

        assertTrue(deepEquals(2,depot.getDepot().getAmount()));

        depot.addResource();

        assertTrue(deepEquals(2,depot.getDepot().getAmount()));

        assertTrue(depot.isFull());

        depot.removeResource();

        assertFalse(depot.isFull());

        assertFalse(depot.isEmpty());

        depot.removeResource();

        assertTrue(depot.isEmpty());
    }



}