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
        CardMarket market = new CardMarket("devcards_config_test.json", 3, 4, false);
        //market.setup("src/test/resources/devcards_config_test.json", false);

        //Region used to create a test card
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

        //Second region used to create a test card
        String cardID2 = "developmentcard_front_b_1_1";
        int vp1 = 1;
        DevCard.CardType type1 = DevCard.CardType.BLUE;
        int level1 = 1;
        ArrayList<FinalResource> cardCost1=new ArrayList<>();
        cardCost1.add(new FinalResource(FinalResource.ResourceType.COIN,2));
        ArrayList<FinalResource> producationCost2=new ArrayList<>();
        producationCost2.add(new FinalResource(FinalResource.ResourceType.SHIELD,1));
        ArrayList<FinalResource> product2 = new ArrayList<>();
        product2.add(new FinalResource((FinalResource.ResourceType.FAITH),1));

        DevCard expected2=new DevCard(cardID2,vp1,type1,level1,cardCost1,producationCost2,product2);

        DevCard actual2= market.getCard(2,1);
        assertTrue(deepEquals(actual2,expected2));


    }

   /** @Test
    public void shuffle(){
        CardMarket market = new CardMarket(3, 4);
        market.setup("src/test/resources/devcards_config_test.json", true);

        //Region used to create a test card
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

        DevCard actual = market.getCard(2,0);

        assertFalse(deepEquals(actual, expected));
    }*/

    @Test
    public void removeCard() {
        CardMarket market = new CardMarket("devcards_config_test.json", 3, 4, false);
        //market.setup("src/test/resources/devcards_config_test.json", false);

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

       // market.removeCard(0,0);

        assertTrue(deepEquals(actual, expected));

        //region Create Test Card
        String cardID2 = "developmentcard_front_g_1_3";
        int vp2 = 3;
        DevCard.CardType type2 = DevCard.CardType.GREEN;
        int level2 = 1;

        ArrayList<FinalResource> cardCost2 = new ArrayList<>();
        cardCost2.add(new FinalResource(FinalResource.ResourceType.SHIELD, 3));

        ArrayList<FinalResource> productionCost2 = new ArrayList<>();
        productionCost2.add(new FinalResource(FinalResource.ResourceType.SERVANT, 2));

        ArrayList<FinalResource> product2 = new ArrayList<>();
        product2.add(new FinalResource(FinalResource.ResourceType.COIN, 1));
        product2.add(new FinalResource(FinalResource.ResourceType.SHIELD, 1));
        product2.add(new FinalResource(FinalResource.ResourceType.STONE, 1));

        DevCard expected2 = new DevCard(cardID2, vp2, type2, level2, cardCost2, productionCost2, product2);
        //endregion

        market.removeCard(2,0);
        DevCard actual2 = market.getCard(2,0);

        assertTrue(deepEquals(actual2, expected2));

        //region Create Test Card
        String cardID3 = "developmentcard_front_g_1_4";
        int vp3 = 4;
        DevCard.CardType type3 = DevCard.CardType.GREEN;
        int level3 = 1;

        ArrayList<FinalResource> cardCost3 = new ArrayList<>();
        cardCost3.add(new FinalResource(FinalResource.ResourceType.SHIELD, 2));
        cardCost3.add(new FinalResource(FinalResource.ResourceType.COIN, 2));

        ArrayList<FinalResource> productionCost3 = new ArrayList<>();
        productionCost3.add(new FinalResource(FinalResource.ResourceType.STONE, 1));
        productionCost3.add(new FinalResource(FinalResource.ResourceType.SERVANT, 1));

        ArrayList<FinalResource> product3 = new ArrayList<>();
        product3.add(new FinalResource(FinalResource.ResourceType.COIN, 2));
        product3.add(new FinalResource(FinalResource.ResourceType.FAITH, 1));

        DevCard expected3 = new DevCard(cardID3, vp3, type3, level3, cardCost3, productionCost3, product3);
        //endregion

        market.removeCard(2,0);
        DevCard actual3 = market.getCard(2,0);

        assertTrue(deepEquals(actual3, expected3));

        market.removeCard(2,0);

        DevCard actual4 = market.getCard(2,0);

        assertNull(actual4);

    }

}