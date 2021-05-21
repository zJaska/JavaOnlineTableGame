package it.polimi.ingsw.IntelliCranio.models.player;

import it.polimi.ingsw.IntelliCranio.models.ability.Ability;
import it.polimi.ingsw.IntelliCranio.models.ability.DepotAbility;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.ArrayList;

import static java.util.Objects.deepEquals;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @ParameterizedTest
    @EnumSource( value= FinalResource.ResourceType.class, names = {"STONE", "SHIELD", "SERVANT", "COIN"})
    void addExtra(FinalResource.ResourceType res) {
        Player p1=new Player("1",3,3);
        p1.addExtra(res,1);

        assertTrue(p1.hasExtra(res));
        assertTrue(deepEquals(1,p1.getExtra(res).getAmount()));
        p1.removeExtra(res,1);
        assertTrue(deepEquals(null,p1.getExtra(res)));
    }


    @Test
    void leader(){
        Player p1=new Player("1",3,3);
        ArrayList<LeadCard> cards = new ArrayList<>();
        LeadCard card = new LeadCard("leadercard_front_1_1", 2, null, null,
                Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, false);

        cards.add(card);

        p1.setLeaders(cards);

        assertTrue(p1.hasLeader(card));
        assertTrue(p1.hasLeader(Ability.AbilityType.DEPOT));
        assertTrue(p1.hasLeader(FinalResource.ResourceType.SERVANT));
        assertTrue(p1.hasLeader(Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT));

        p1.removeLeader(card);

        assertFalse(p1.hasLeader(card));
        assertFalse(p1.hasLeader(Ability.AbilityType.DEPOT));
        assertFalse(p1.hasLeader(FinalResource.ResourceType.SERVANT));
        assertFalse(p1.hasLeader(Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT));




    }




    @Test
    void getAllResources() {
        Player p1=new Player("1",3,3);

        ArrayList<LeadCard> cards=new ArrayList<>();
        LeadCard card = new LeadCard("leadercard_front_1_1", 2, null, null,
                Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, true);
        card.setupAbility();
        cards.add(card);

        p1.setLeaders(cards);

        DepotAbility depotAbility = (DepotAbility) card.getSpecialAbility();
        depotAbility.addResource();
        depotAbility.addResource();



        p1.getWarehouse().add(0,new Resource(FinalResource.ResourceType.SERVANT,1));
        p1.getStrongbox().addResources(FinalResource.ResourceType.SERVANT,1);

        ArrayList<Resource> temp=p1.getAllResources();



        assertTrue(deepEquals(4,temp.get(0).getAmount()));


    }
}