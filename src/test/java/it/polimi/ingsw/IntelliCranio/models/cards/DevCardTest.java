package it.polimi.ingsw.IntelliCranio.models.cards;

import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DevCardTest {


    @Test
    public void get(){
        String ID="1";
        int vp=1;
        DevCard.CardType type= DevCard.CardType.BLUE;
        int level=1;
        ArrayList<FinalResource > cardCost=new ArrayList<>();
        cardCost.add(new FinalResource(FinalResource.ResourceType.STONE,1));
        ArrayList<FinalResource> productionCost=new ArrayList<>();
        productionCost.add(new FinalResource(FinalResource.ResourceType.COIN,1));
        ArrayList<FinalResource> product=new ArrayList<>();
        product.add(new FinalResource(FinalResource.ResourceType.SHIELD,1));

        DevCard card1=new DevCard(ID, vp, type,  level,  cardCost,  productionCost,  product);

        assertEquals(DevCard.CardType.BLUE,card1.getType());
        assertEquals(1,card1.getLevel());
        assertEquals(cardCost,card1.getCardCost());
        assertEquals(productionCost,card1.getProductionCost());
        assertEquals(product,card1.getProduct());
    }

}