package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.resource.CardResource;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.ability.Ability;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.Save;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.ArrayList;

import static com.cedarsoftware.util.DeepEquals.deepEquals;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

class Default_ActionStateTest {

    ArrayList<String> nicknames = new ArrayList<>();
    Game game;

    @BeforeEach
    void setupTest() {
        nicknames.add("1");
        nicknames.add("2");
        nicknames.add("3");
        nicknames.add("4");
        game = new Game(nicknames);
        Save.saveGame(game);

    }

    @Test
    void nullPacket() {

        Action action = new Action();
        action.setActionState(new Default_ActionState(action),DEFAULT);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, null);
        });

        assertEquals(CODE_NULL, e.getCode());

    }

    @ParameterizedTest
    @EnumSource(value = Packet.InstructionCode.class, mode = EXCLUDE, names = {"PLAY_LEADER", "DISCARD_LEAD", "MNG_WARE", "CARD_MARKET", "RES_MARKET", "ACT_PROD"})
    void codeNotAllowed(Packet.InstructionCode p) {
        Action action = new Action();
        action.setActionState(new Default_ActionState(action), DEFAULT);

        Packet packet = new Packet(p, null, null);// For each test

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
       // System.out.println(e.getErrorMessage());
        assertEquals(CODE_NOT_ALLOWED, e.getCode());
    }

    @Test
    void PlayLeader_Less_Argument(){
        Action action = new Action();
        action.setActionState(new Default_ActionState(action), DEFAULT);

        Packet packet = new Packet(PLAY_LEADER, null, null);// For each test

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        System.out.println(e.getErrorMessage());
        assertEquals(NOT_ENOUGH_ARGS, e.getCode());
    }

    @Test
    void PlayLeader_TO_MANY_Arguments(){
        Action action = new Action();
        action.setActionState(new Default_ActionState(action), DEFAULT);

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.STONE, 1));
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        ArrayList<Object> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, false);
        card.setupAbility();

        cards.add(card);
        cards.add(card);

        Packet packet = new Packet(PLAY_LEADER, null, cards);// For each test

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        //System.out.println(e.getErrorMessage());
        assertEquals(TOO_MANY_ARGS, e.getCode());

    }

    @Test
    void PlayLeader_Type_Miss_Match(){
        Action action = new Action();
        action.setActionState(new Default_ActionState(action), DEFAULT);

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.STONE, 1));
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        ArrayList<Object> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, false);
        card.setupAbility();

        cards.add("card");

        Packet packet = new Packet(PLAY_LEADER, null, cards);// For each test

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        //System.out.println(e.getErrorMessage());
        assertEquals(TYPE_MISMATCH, e.getCode());
    }

    @Test
    void PlayLeader_Card_Not_In_Hand(){
        Action action = new Action();
        action.setActionState(new Default_ActionState(action), DEFAULT);

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.STONE, 1));
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        ArrayList<Object> cards = new ArrayList<>();

        Object card = new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, false);
        ((LeadCard)(card)).setupAbility();

        cards.add(card);

        Packet packet = new Packet(PLAY_LEADER, null, cards);// For each test

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        System.out.println(e.getErrorMessage());
        assertEquals(SELECTION_INVALID, e.getCode());
    }

    @Test
    void PlayLeader_Card_NOT_RESOURCES(){
        Action action = new Action();
        action.setActionState(new Default_ActionState(action), DEFAULT);

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.STONE, 1));
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        ArrayList<Object> cards = new ArrayList<>();

        Object card = new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, false);
        ((LeadCard)(card)).setupAbility();

        cards.add(card);

        ArrayList<LeadCard> ins=new ArrayList<>();
        LeadCard i1=new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, false);
        i1.setupAbility();
        ins.add(i1);

        game.getCurrentPlayer().setLeaders(ins);

        Packet packet = new Packet(PLAY_LEADER, null, cards);// For each test

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        System.out.println(e.getErrorMessage());
        assertEquals(SELECTION_INVALID, e.getCode());
    }

    @Test
    void PlayLeader_Card_NOT_CARD_REQUIRMENTS(){
        Action action = new Action();
        action.setActionState(new Default_ActionState(action), DEFAULT);

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.STONE, 1));
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        ArrayList<Object> cards = new ArrayList<>();

        Object card = new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, false);
        ((LeadCard)(card)).setupAbility();

        cards.add(card);

        ArrayList<LeadCard> ins=new ArrayList<>();
        LeadCard i1=new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, false);
        i1.setupAbility();
        ins.add(i1);

        game.getCurrentPlayer().setLeaders(ins);

        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.STONE,1));

        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(FinalResource.ResourceType.STONE, 1));
        args.add(1);

        Packet PackAdder=new Packet(ADD_FROM_EXTRA, null, args);

        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.STONE, 1));

        ArrayList<Resource> expectedExtra = new ArrayList<>();
        expectedExtra.add(new Resource(FinalResource.ResourceType.STONE, 1));
        action.setActionState(new ManageWarehouse_ActionState(action,null), MNG_WARE);

        assertDoesNotThrow(() -> {
            action.execute(game, PackAdder);
        });

        assertTrue(deepEquals(expectedExtra.toArray()[0], game.getCurrentPlayer().getWarehouse().getDepot()[1]));


        Packet packet = new Packet(PLAY_LEADER, null, cards);// For each test
        action.setActionState(new Default_ActionState(action), DEFAULT);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        System.out.println(e.getErrorMessage());
        assertEquals(SELECTION_INVALID, e.getCode());
    }

    //End region

    //New region
    @Test
    void DiscardLeader_Less_Argument(){
        Action action = new Action();
        action.setActionState(new Default_ActionState(action), DEFAULT);

        Packet packet = new Packet(DISCARD_LEAD, null, null);// For each test

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        System.out.println(e.getErrorMessage());
        assertEquals(NOT_ENOUGH_ARGS, e.getCode());
    }

    @Test
    void DiscardLeader_TO_MANY_Arguments(){
        Action action = new Action();
        action.setActionState(new Default_ActionState(action), DEFAULT);

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.STONE, 1));
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        ArrayList<Object> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, false);
        card.setupAbility();

        cards.add(card);
        cards.add(card);

        Packet packet = new Packet(DISCARD_LEAD, null, cards);// For each test

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        //System.out.println(e.getErrorMessage());
        assertEquals(TOO_MANY_ARGS, e.getCode());

    }

    @Test
    void DiscardLeader_Type_Miss_Match(){
        Action action = new Action();
        action.setActionState(new Default_ActionState(action), DEFAULT);

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.STONE, 1));
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        ArrayList<Object> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, false);
        card.setupAbility();

        cards.add("card");

        Packet packet = new Packet(DISCARD_LEAD, null, cards);// For each test

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        //System.out.println(e.getErrorMessage());
        assertEquals(TYPE_MISMATCH, e.getCode());
    }

    @Test
    void DiscardLeader_Card_Not_In_Hand(){
        Action action = new Action();
        action.setActionState(new Default_ActionState(action), DEFAULT);

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.STONE, 1));
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        ArrayList<Object> cards = new ArrayList<>();

        Object card = new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, false);
        ((LeadCard)(card)).setupAbility();

        cards.add(card);

        Packet packet = new Packet(DISCARD_LEAD, null, cards);// For each test

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        System.out.println(e.getErrorMessage());
        assertEquals(SELECTION_INVALID, e.getCode());
    }

    @Test
    void CardLeader_Card_ACTIVED(){
        Action action = new Action();
        action.setActionState(new Default_ActionState(action), DEFAULT);

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.STONE, 1));
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        ArrayList<Object> cards = new ArrayList<>();

        Object card = new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, true);
        ((LeadCard)(card)).setupAbility();

        cards.add(card);

        ArrayList<LeadCard> ins=new ArrayList<>();
        LeadCard i1=new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, true);
        i1.setupAbility();
        ins.add(i1);

        game.getCurrentPlayer().setLeaders(ins);

        Packet packet = new Packet(DISCARD_LEAD, null, cards);// For each test

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        System.out.println(e.getErrorMessage());
        assertEquals(SELECTION_INVALID, e.getCode());
    }

    @Test
    void CardLeader_Card_CORRECT(){
        Action action = new Action();
        action.setActionState(new Default_ActionState(action), DEFAULT);

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.STONE, 1));
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        ArrayList<Object> cards = new ArrayList<>();

        Object card = new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, false);
        ((LeadCard)(card)).setupAbility();

        cards.add(card);

        ArrayList<LeadCard> ins=new ArrayList<>();
        LeadCard i1=new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, false);
        i1.setupAbility();
        ins.add(i1);

        game.getCurrentPlayer().setLeaders(ins);

        Packet packet = new Packet(DISCARD_LEAD, null, cards);// For each test

        assertDoesNotThrow(() -> {
            action.execute(game, packet);

        });

        assertTrue(deepEquals(null,game.getCurrentPlayer().getLeader(i1)));
        assertTrue(deepEquals(0,game.getCurrentPlayer().getLeaders().size()));
        assertTrue(deepEquals(1,game.getPlayers().get(1).getFaithPosition()));
        assertTrue(deepEquals(1,game.getPlayers().get(2).getFaithPosition()));
        assertTrue(deepEquals(1,game.getPlayers().get(3).getFaithPosition()));

    }


    @Test
    void execute() {
        assertTrue(true);
    }
}