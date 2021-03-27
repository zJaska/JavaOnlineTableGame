package it.polimi.ingsw.IntelliCranio.server.action;

import it.polimi.ingsw.IntelliCranio.server.GameManager;
import it.polimi.ingsw.IntelliCranio.server.ability.Ability;
import it.polimi.ingsw.IntelliCranio.server.ability.SaleAbility;
import it.polimi.ingsw.IntelliCranio.server.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.server.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.server.player.Player;
import it.polimi.ingsw.IntelliCranio.server.resource.CardResource;
import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;
import org.junit.Test;

import java.util.ArrayList;

import static com.cedarsoftware.util.DeepEquals.deepEquals;
import static org.junit.Assert.*;

public class DiscardInitLeadersTest {

    @Test
    public void playAction() {
        GameManager manager = new GameManager();
        manager.addPlayer(new Player(), 0);

        manager.createLeaderCards("src/main/resources/leadcards_config.json", false);

        String ID = "leadercard_front_1_1";
        int vp = 2;
        ArrayList<CardResource> cardReq = new ArrayList<>();
        ArrayList<FinalResource> resReq = new ArrayList<>();
        cardReq.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardReq.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        Ability ability = new Ability(FinalResource.ResourceType.SERVANT);
        LeadCard expected = new LeadCard(ID, vp, cardReq, null, ability, false);

        LeadCard actual = manager.getCurrentPlayer().getLeaders().get(0);

        assertTrue(deepEquals(actual, expected));

    }
}