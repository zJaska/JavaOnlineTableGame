package it.polimi.ingsw.IntelliCranio.models.market;

import it.polimi.ingsw.IntelliCranio.util.Lists;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import static com.cedarsoftware.util.DeepEquals.deepEquals;
import static org.junit.jupiter.api.Assertions.*;

class ResourceMarketTest {


    @Test
    void setup() {
        // Todo: the constructor changed, need to review this test

        ResourceMarket market = new ResourceMarket(3, 4);
        ArrayList<Resource> resources = new ArrayList<>(Arrays.asList(
                new Resource(FinalResource.ResourceType.BLANK, 4),
                new Resource(FinalResource.ResourceType.SHIELD, 2),
                new Resource(FinalResource.ResourceType.STONE, 2),
                new Resource(FinalResource.ResourceType.SERVANT, 2),
                new Resource(FinalResource.ResourceType.COIN, 2),
                new Resource(FinalResource.ResourceType.FAITH, 1)
        ));

        ArrayList<FinalResource> list = new ArrayList<>();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 4; c++) {
                list.add(market.getGridCopy()[r][c]);
            }
        }
        list.add(market.getExtraMarbleCopy());
        list = Lists.unifyResourceAmounts(list);

        list.sort(Comparator.comparing(FinalResource::getType));
        resources.sort(Comparator.comparing(FinalResource::getType));

        assertTrue(deepEquals(list, resources));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void testSelectRow(int test) {
        ResourceMarket market = new ResourceMarket(3, 4);
        //I would like to select a row and shift that
        ArrayList<Resource> expected = new ArrayList<>();
        FinalResource ExtraMrblOld = market.getExtraMarbleCopy();

        for (int c = 0; c < market.COLUMNS; c++)
            expected.add(new Resource(market.getGridCopy()[test][c].getType(), 1));

        market.selectRow(test);

        for (int c = 0; c < market.COLUMNS - 1; c++)
            assertTrue(deepEquals(expected.get(c + 1).getType(), market.getGridCopy()[test][c].getType()));

        FinalResource lastMarble = market.getGridCopy()[test][market.COLUMNS - 1];
        System.out.println();
        assertTrue(deepEquals(ExtraMrblOld, lastMarble));
        assertTrue(deepEquals(expected.get(0).getType(), market.getExtraMarbleCopy().getType()));

    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    void testSelectColumn(int test) {
        ResourceMarket market = new ResourceMarket(3, 4);
        //I would like to select a row and shift that
        ArrayList<Resource> expected = new ArrayList<>();
        FinalResource ExtraMrblOld = market.getExtraMarbleCopy();

        for (int c = 0; c < market.ROWS; c++)
            expected.add(new Resource(market.getGridCopy()[c][test].getType(), 1));

        market.selectColumn(test);

        for (int c = 0; c < market.ROWS - 1; c++)
            assertTrue(deepEquals(expected.get(c + 1).getType(), market.getGridCopy()[c][test].getType()));

        assertTrue(deepEquals(ExtraMrblOld, market.getGridCopy()[market.ROWS - 1][test]));
        assertTrue(deepEquals(expected.get(0).getType(), market.getExtraMarbleCopy().getType()));


    }

}