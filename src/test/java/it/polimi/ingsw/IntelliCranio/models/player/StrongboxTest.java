package it.polimi.ingsw.IntelliCranio.models.player;

import com.cedarsoftware.util.DeepEquals;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Objects.deepEquals;
import static org.junit.jupiter.api.Assertions.*;

class StrongboxTest {

    static Strongbox strongbox;

    @BeforeAll
    static void SetUp(){
        strongbox=new Strongbox();
        strongbox.addResources(FinalResource.ResourceType.COIN,1);
        strongbox.addResources(FinalResource.ResourceType.SHIELD,1);
        strongbox.addResources(FinalResource.ResourceType.SERVANT,1);
        strongbox.addResources(FinalResource.ResourceType.STONE,1);
    }

    @Test
    void getAmount() {
        assertTrue(deepEquals(1,strongbox.getAmount(FinalResource.ResourceType.COIN)));
        assertTrue(deepEquals(1,strongbox.getAmount(FinalResource.ResourceType.SHIELD)));
        assertTrue(deepEquals(1,strongbox.getAmount(FinalResource.ResourceType.SERVANT)));
        assertTrue(deepEquals(1,strongbox.getAmount(FinalResource.ResourceType.STONE)));
    }

    @Test
    void addResources() {
        strongbox.addResources(FinalResource.ResourceType.COIN,1);
        assertTrue(deepEquals(2,strongbox.getAmount(FinalResource.ResourceType.COIN)));
    }

    @Test
    void removeResources() {
        strongbox.removeResources(FinalResource.ResourceType.COIN,1);
        assertTrue(deepEquals(1,strongbox.getAmount(FinalResource.ResourceType.COIN)));
    }


}