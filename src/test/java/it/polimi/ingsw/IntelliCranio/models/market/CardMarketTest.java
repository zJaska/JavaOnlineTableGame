package it.polimi.ingsw.IntelliCranio.models.market;

import it.polimi.ingsw.IntelliCranio.models.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import org.junit.Test;

import java.util.ArrayList;

import static com.cedarsoftware.util.DeepEquals.deepEquals;
import static org.junit.Assert.*;

public class CardMarketTest {

    @Test
    public void getCard() {
        CardMarket market = new CardMarket(3, 4);
        market.setup("src/test/resources/devcards_config_test.json", false);

        //region Create Test Card
        String cardID = "developmentcard_front_g_1_1";
        int vp = 1;
        DevCard.CardType type = DevCard.CardType.GREEN;
        int level = 1;

        ArrayList<FinalResource> cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 2));

        ArrayList<FinalResource> productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.COIN, 1));

        ArrayList<FinalResource> product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.FAITH, 1));

        DevCard expected = new DevCard(cardID, vp, type, level, cardCost, productionCost, product);
        //endregion

        DevCard actual = market.getCard(2,0);

        assertTrue(deepEquals(actual, expected));
    }

    @Test
    public void removeCard() {
        CardMarket market = new CardMarket(3, 4);
        market.setup("src/test/resources/devcards_config_test.json", false);

        //region Create Test Card
        String cardID = "developmentcard_front_g_1_2";
        int vp = 2;
        DevCard.CardType type = DevCard.CardType.GREEN;
        int level = 1;

        ArrayList<FinalResource> cardCost = new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));
        cardCost.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));
        cardCost.add(new FinalResource(FinalResource.ResourceType.STONE, 1));

        ArrayList<FinalResource> productionCost = new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.STONE, 1));

        ArrayList<FinalResource> product = new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));

        DevCard expected = new DevCard(cardID, vp, type, level, cardCost, productionCost, product);
        //endregion

        market.removeCard(2, 0);
        DevCard actual = market.getCard(2,0);

        assertTrue(deepEquals(actual, expected));
    }
}