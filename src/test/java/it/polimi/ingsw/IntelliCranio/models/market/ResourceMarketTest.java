package it.polimi.ingsw.IntelliCranio.models.market;

import it.polimi.ingsw.IntelliCranio.Utility;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static com.cedarsoftware.util.DeepEquals.deepEquals;
import static org.junit.jupiter.api.Assertions.*;

class ResourceMarketTest {

    @Test
    void setup() {
        ResourceMarket market = new ResourceMarket(3,4);
        ArrayList<Resource> resources = new ArrayList<>(Arrays.asList(
                new Resource(FinalResource.ResourceType.BLANK,4),
                new Resource(FinalResource.ResourceType.SHIELD,2),
                new Resource(FinalResource.ResourceType.STONE,2),
                new Resource(FinalResource.ResourceType.SERVANT,2),
                new Resource(FinalResource.ResourceType.COIN,2),
                new Resource(FinalResource.ResourceType.FAITH,1)
        ));
        market.setup(resources);

        ArrayList<FinalResource> list = new ArrayList<>();
        for (int r=0; r<3; r++) {
            for (int c=0; c<4; c++) {
                list.add(market.getMarbleGrid()[r][c]);
            }
        }
        list.add(market.getExtraMarble());
        list = Utility.unifyResourceAmounts(list);

        list.sort(Comparator.comparing(FinalResource::getType));
        resources.sort(Comparator.comparing(FinalResource::getType));

        assertTrue(deepEquals(list,resources));
    }

}