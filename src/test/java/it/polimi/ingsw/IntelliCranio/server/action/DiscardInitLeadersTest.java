package it.polimi.ingsw.IntelliCranio.server.action;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.IntelliCranio.server.GameManager;
import it.polimi.ingsw.IntelliCranio.server.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.server.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
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

        ArrayList<LeadCard> cardsToDiscard = new ArrayList<>();
        ArrayList<LeadCard> expectedCards = new ArrayList<>();

        manager.createLeaderCards("src/main/resources/leadcards_config.json", false);

        //region Test Card Creation
        //region Card 1
        String ID = "leadercard_front_1_1";
        int vp = 2;
        ArrayList<CardResource> cardReq = new ArrayList<>();
        ArrayList<FinalResource> resReq = new ArrayList<>();
        cardReq.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardReq.add(new CardResource(DevCard.CardType.GREEN, 1, 0));

        LeadCard card1 = new LeadCard(ID, vp, cardReq, null, LeadCard.AbilityType.SALE, FinalResource.ResourceType.SERVANT, false);
        //endregion

        //region Card 2
        ID = "leadercard_front_1_2";
        cardReq = new ArrayList<>();
        resReq = new ArrayList<>();
        cardReq.add(new CardResource(DevCard.CardType.BLUE, 1, 0));
        cardReq.add(new CardResource(DevCard.CardType.PURPLE, 1, 0));

        LeadCard card2 = new LeadCard(ID, vp, cardReq, null, LeadCard.AbilityType.SALE, FinalResource.ResourceType.SHIELD, false);
        //endregion

        //region Card 3
        ID = "leadercard_front_1_3";
        cardReq = new ArrayList<>();
        resReq = new ArrayList<>();
        cardReq.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        cardReq.add(new CardResource(DevCard.CardType.BLUE, 1, 0));

        LeadCard card3 = new LeadCard(ID, vp, cardReq, null, LeadCard.AbilityType.SALE, FinalResource.ResourceType.STONE, false);
        //endregion

        //region Card 4
        ID = "leadercard_front_1_4";
        cardReq = new ArrayList<>();
        resReq = new ArrayList<>();
        cardReq.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardReq.add(new CardResource(DevCard.CardType.PURPLE, 1, 0));

        LeadCard card4 = new LeadCard(ID, vp, cardReq, null, LeadCard.AbilityType.SALE, FinalResource.ResourceType.COIN, false);
        //endregion
        //endregion

        //region Setup cards to discard and call action
        cardsToDiscard.add(card1);
        cardsToDiscard.add(card4);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        ArrayList<String> args = new ArrayList<>();
        args.add(gson.toJson(cardsToDiscard));

        Action action = new DiscardInitLeaders(args);

        try {
            action.playAction(manager);
        } catch (InvalidArgumentsException e) {
            e.printStackTrace();
        }
        //endregion

        //Get the actual cards the player has now
        ArrayList<LeadCard> actualCards = manager.getCurrentPlayer().getLeaders();

        //region Setup expected Cards
        card2.setupAbility(LeadCard.AbilityType.SALE, FinalResource.ResourceType.SHIELD);
        card3.setupAbility(LeadCard.AbilityType.SALE, FinalResource.ResourceType.STONE);

        expectedCards.add(card2);
        expectedCards.add(card3);
        //endregion

        assertTrue(deepEquals(expectedCards.get(0), actualCards.get(0)));
        assertTrue(deepEquals(expectedCards.get(1), actualCards.get(1)));


    }
}