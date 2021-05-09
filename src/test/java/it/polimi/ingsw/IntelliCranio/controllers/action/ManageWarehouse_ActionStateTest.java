package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.cards.Card;
import it.polimi.ingsw.IntelliCranio.models.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.resource.CardResource;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.ability.Ability;
import it.polimi.ingsw.IntelliCranio.server.ability.DepotAbility;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.Save;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static com.cedarsoftware.util.DeepEquals.deepEquals;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

class ManageWarehouse_ActionStateTest {

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
    void nullPacket() {//Le instruction code e listobject

        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, null);
        });

        assertEquals(PACKET_NULL, e.getCode());

    }

    @ParameterizedTest
    @EnumSource(value = Packet.InstructionCode.class, mode = EXCLUDE, names = {"SWAP_LINES", "ADD_FROM_EXTRA", "REMOVE_FROM_DEPOT", "DEPOT_TO_CARD", "EXTRA_TO_CARD", "CANCEL", "CONFIRM"})
    void codeNotAllowed(Packet.InstructionCode p) {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);

        Packet packet = new Packet(p, null, null);// For each test

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(ARGS_NULL, e.getCode());
    }

    @Test
    void TestSwapLines_LESS_ARGS() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();

        Packet packet = new Packet(SWAP_LINES, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(NOT_ENOUGH_ARGS, e.getCode());

    }

    @Test
    void TestSwapLines_MANY_ARGS() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(1);
        args.add(2);
        args.add(3);


        Packet packet = new Packet(SWAP_LINES, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(TOO_MANY_ARGS, e.getCode());


    }

    @Test
    void TestSwapLines_TYPE_MISSMATCH() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add("1");
        args.add("2");


        Packet packet = new Packet(SWAP_LINES, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(TYPE_MISMATCH, e.getCode());


    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -5, -4, -3, 3, 4, 5, 6, 7, Integer.MAX_VALUE})
    void TestSwapLines_Selection_Invalid(int val) {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(val);
        args.add(2);


        Packet packet = new Packet(SWAP_LINES, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(SELECTION_INVALID, e.getCode());

    }

    @Test
    void TestSwapLines_Correct() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);


        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.SHIELD, 2));
        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.COIN, 2));

        ArrayList<Object> argsRes = new ArrayList<>();
        argsRes.add(new Resource(FinalResource.ResourceType.SHIELD, 2));
        argsRes.add(1);

        Packet packetAdder1 = new Packet(ADD_FROM_EXTRA, null, argsRes);

        assertDoesNotThrow(() -> {
            action.execute(game, packetAdder1);
            action.execute(game, packetAdder1);
            argsRes.set(0, new Resource(FinalResource.ResourceType.COIN, 2));
            argsRes.set(1, 2);
            Packet packetAdder2 = new Packet(ADD_FROM_EXTRA, null, argsRes);
            action.execute(game, packetAdder2);
            action.execute(game, packetAdder2);
        });

        ArrayList<Object> expectedExtra = new ArrayList<>();
        expectedExtra.add(new Resource(FinalResource.ResourceType.SHIELD, 2));
        expectedExtra.add(new Resource(FinalResource.ResourceType.COIN, 2));


        assertTrue(deepEquals(expectedExtra.toArray()[0], game.getCurrentPlayer().getWarehouse().getDepot()[1]));
        assertTrue(deepEquals(expectedExtra.toArray()[1], game.getCurrentPlayer().getWarehouse().getDepot()[2]));

        ArrayList<Object> args = new ArrayList<>();
        args.add(1);
        args.add(2);

        Packet packet = new Packet(SWAP_LINES, null, args);


        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });

        assertTrue(deepEquals(expectedExtra.toArray()[1], game.getCurrentPlayer().getWarehouse().getDepot()[1]));
        assertTrue(deepEquals(expectedExtra.toArray()[0], game.getCurrentPlayer().getWarehouse().getDepot()[2]));

    }

    @Test
    void TestSwapLines_Correct2() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);


        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.SHIELD, 2));
        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.COIN, 3));

        ArrayList<Object> argsRes = new ArrayList<>();
        argsRes.add(new Resource(FinalResource.ResourceType.SHIELD, 2));
        argsRes.add(1);

        Packet packetAdder1 = new Packet(ADD_FROM_EXTRA, null, argsRes);

        assertDoesNotThrow(() -> {
            action.execute(game, packetAdder1);
            action.execute(game, packetAdder1);
            argsRes.set(0, new Resource(FinalResource.ResourceType.COIN, 3));
            argsRes.set(1, 2);
            Packet packetAdder2 = new Packet(ADD_FROM_EXTRA, null, argsRes);
            action.execute(game, packetAdder2);
            action.execute(game, packetAdder2);
            action.execute(game, packetAdder2);
        });

        ArrayList<Object> expectedExtra = new ArrayList<>();
        expectedExtra.add(new Resource(FinalResource.ResourceType.SHIELD, 2));
        expectedExtra.add(new Resource(FinalResource.ResourceType.COIN, 3));


        assertTrue(deepEquals(expectedExtra.toArray()[0], game.getCurrentPlayer().getWarehouse().getDepot()[1]));
        assertTrue(deepEquals(expectedExtra.toArray()[1], game.getCurrentPlayer().getWarehouse().getDepot()[2]));

        ArrayList<Object> args = new ArrayList<>();
        args.add(1);
        args.add(2);

        Packet packet = new Packet(SWAP_LINES, null, args);


        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });

        expectedExtra.set(1, new Resource(FinalResource.ResourceType.COIN, 2));

        assertTrue(deepEquals(expectedExtra.toArray()[1], game.getCurrentPlayer().getWarehouse().getDepot()[1]));
        assertTrue(deepEquals(expectedExtra.toArray()[0], game.getCurrentPlayer().getWarehouse().getDepot()[2]));

        expectedExtra.add(new Resource(FinalResource.ResourceType.COIN, 1));

        assertTrue(deepEquals(expectedExtra.toArray()[2], game.getCurrentPlayer().getExtra(FinalResource.ResourceType.COIN)));


    }
    //End regione

    //New region
    @Test
    void TestAddFromExtra_LESS_ARGS() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();

        Packet packet = new Packet(ADD_FROM_EXTRA, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(NOT_ENOUGH_ARGS, e.getCode());

    }

    @Test
    void TestAddFromExtra_MANY_ARGS() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(null);
        args.add(null);
        args.add(null);


        Packet packet = new Packet(ADD_FROM_EXTRA, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(TOO_MANY_ARGS, e.getCode());

    }

    @Test
    void TestAddFromExtra_TYPE_MISSMATCH() {//Resource AND int
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add("FinalResource.ResourceType.SHIELD, 1");
        args.add("2");


        Packet packet = new Packet(ADD_FROM_EXTRA, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(TYPE_MISMATCH, e.getCode());


    }

    @ParameterizedTest
    @EnumSource(value = FinalResource.ResourceType.class, mode = EXCLUDE, names = {"STONE", "SHIELD", "SERVANT", "COIN"})
    void TestAddFromExtra_NOT_VALID_RESOURCES(FinalResource.ResourceType p) {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(p, 1));
        args.add(0);


        Packet packet = new Packet(ADD_FROM_EXTRA, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(SELECTION_INVALID, e.getCode());


    }

    @ParameterizedTest
    @EnumSource(value = FinalResource.ResourceType.class, mode = EXCLUDE, names = {"FAITH", "BLANK"})
    void TestAddFromExtra_NOT_VALID_AMOUNT(FinalResource.ResourceType p) {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(p, 2));
        args.add(3);

        game.getCurrentPlayer().addExtra(p, 2);

        Packet packet = new Packet(ADD_FROM_EXTRA, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
            action.execute(game, packet);
            action.execute(game, packet);
        });
      //  System.out.println(e.getErrorMessage());
        assertEquals(SELECTION_INVALID, e.getCode());


    }

    @Test
    void TestWhenExtraIsEmpty() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(FinalResource.ResourceType.COIN, 2));
        args.add(1);

        Packet packet = new Packet(ADD_FROM_EXTRA, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(SELECTION_INVALID, e.getCode());

    }

    @Test
    void TestWhenPlayerHasWhatNeed() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(FinalResource.ResourceType.COIN, 2));
        args.add(1);

        Packet packet = new Packet(ADD_FROM_EXTRA, null, args);

        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.COIN, 2));

        ArrayList<Resource> expectedExtra = new ArrayList<>();
        expectedExtra.add(new Resource(FinalResource.ResourceType.COIN, 2));

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
            action.execute(game, packet);
        });

        assertTrue(deepEquals(expectedExtra.toArray()[0], game.getCurrentPlayer().getWarehouse().getDepot()[1]));

    }

    @Test
    void TestOverFlowDepot() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(FinalResource.ResourceType.COIN, 2));
        args.add(0);

        Packet packet = new Packet(ADD_FROM_EXTRA, null, args);

        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.COIN, 2));

        ArrayList<Resource> expectedExtra = new ArrayList<>();
        expectedExtra.add(new Resource(FinalResource.ResourceType.COIN, 2));

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
            action.execute(game, packet);
        });
        assertEquals(SELECTION_INVALID, e.getCode());

    }

    //Risorsa di diversa tipo
    @Test
    void TestDepotWhenAddedNotSameResource() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(FinalResource.ResourceType.COIN, 2));
        args.add(1);

        Packet packet = new Packet(ADD_FROM_EXTRA, null, args);

        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.COIN, 2));

        ArrayList<Resource> expectedExtra = new ArrayList<>();
        expectedExtra.add(new Resource(FinalResource.ResourceType.COIN, 2));

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
            args.set(0, new Resource(FinalResource.ResourceType.SHIELD, 2));
            action.execute(game, packet);

        });
        assertEquals(SELECTION_INVALID, e.getCode());
    }

    //Risorse uguali su due righe
    @Test
    void TestDepotDifferentLinesSameResources() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(FinalResource.ResourceType.COIN, 1));
        args.add(0);

        Packet packet = new Packet(ADD_FROM_EXTRA, null, args);

        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.COIN, 2));

        ArrayList<Resource> expectedExtra = new ArrayList<>();
        expectedExtra.add(new Resource(FinalResource.ResourceType.COIN, 2));

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
            args.set(0, new Resource(FinalResource.ResourceType.COIN, 2));
            args.set(1, 1);
            action.execute(game, packet);
            action.execute(game, packet);

        });
        assertEquals(SELECTION_INVALID, e.getCode());
    }

    //End region

    //New region
    @Test
    void TestRemoveFromDepot_LESS_ARGS() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();

        Packet packet = new Packet(REMOVE_FROM_DEPOT, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(NOT_ENOUGH_ARGS, e.getCode());

    }

    @Test
    void TestRemoveFromDepot_MANY_ARGS() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(1);
        args.add(2);


        Packet packet = new Packet(REMOVE_FROM_DEPOT, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(TOO_MANY_ARGS, e.getCode());


    }

    @Test
    void TestRemoveFromDepot_TYPE_MISSMATCH() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add("1");


        Packet packet = new Packet(REMOVE_FROM_DEPOT, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(TYPE_MISMATCH, e.getCode());


    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -5, -4, -3, 3, 4, 5, 6, 7, Integer.MAX_VALUE})
    void TestRemoveFromDepot_Selection_Invalid(int val) {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(val);


        Packet packet = new Packet(REMOVE_FROM_DEPOT, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(SELECTION_INVALID, e.getCode());

    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void TestRemoveFromDepot_Selection_ValidButEmpty(int val) {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(val);


        Packet packet = new Packet(REMOVE_FROM_DEPOT, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(SELECTION_INVALID, e.getCode());

    }

    @ParameterizedTest
    @EnumSource(value = FinalResource.ResourceType.class, mode = EXCLUDE, names = {"FAITH", "BLANK"})
    void TestUsedToVerifyRemoveAndAddFromExtra(FinalResource.ResourceType p) {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(p, 2));
        args.add(1);

        Packet packet = new Packet(ADD_FROM_EXTRA, null, args);

        game.getCurrentPlayer().addExtra(new Resource(p, 2));

        ArrayList<Resource> expectedExtra = new ArrayList<>();
        expectedExtra.add(new Resource(p, 2));

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
            action.execute(game, packet);
        });

        assertTrue(deepEquals(expectedExtra.toArray()[0], game.getCurrentPlayer().getWarehouse().getDepot()[1]));

        ArrayList<Object> args2 = new ArrayList<>();
        args2.add(1);

        Packet packet2 = new Packet(REMOVE_FROM_DEPOT, null, args2);

        assertDoesNotThrow(() -> {
            action.execute(game, packet2);
            action.execute(game, packet2);
        });

        assertTrue(deepEquals(null, game.getCurrentPlayer().getWarehouse().getDepot()[1]));

    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void TestUsedToVerifyRemoveAndAddFromExtra2(int val) {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(FinalResource.ResourceType.COIN, val + 1));
        args.add(val);

        Packet packet = new Packet(ADD_FROM_EXTRA, null, args);

        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.COIN, val + 1));

        ArrayList<Resource> expectedExtra = new ArrayList<>();
        expectedExtra.add(new Resource(FinalResource.ResourceType.COIN, val + 1));

        assertDoesNotThrow(() -> {
            for (int i = 0; i <= val; i++) {
                action.execute(game, packet);
            }
        });

        assertTrue(deepEquals(expectedExtra.toArray()[0], game.getCurrentPlayer().getWarehouse().getDepot()[val]));

        ArrayList<Object> args2 = new ArrayList<>();
        args2.add(val);

        Packet packet2 = new Packet(REMOVE_FROM_DEPOT, null, args2);

        assertDoesNotThrow(() -> {
            for (int i = 0; i <= val; i++) {
                action.execute(game, packet2);
            }
        });

        assertTrue(deepEquals(null, game.getCurrentPlayer().getWarehouse().getDepot()[val]));

    }

    //End region

    //New region
    @Test
    void TestDepotToCard_LESS_ARGS() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();

        Packet packet = new Packet(DEPOT_TO_CARD, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(NOT_ENOUGH_ARGS, e.getCode());


    }

    @Test
    void TestDepotToCard_MANY_ARGS() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(1);
        args.add(2);


        Packet packet = new Packet(DEPOT_TO_CARD, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(TOO_MANY_ARGS, e.getCode());


    }

    @Test
    void TestDepotToCard_TYPE_MISSMATCH() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add("1");


        Packet packet = new Packet(DEPOT_TO_CARD, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(TYPE_MISMATCH, e.getCode());

    }

    @Test
    void TestDepotToCard_Selection_Valid_ButFromDefaultScene() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, DEFAULT), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(0);


        Packet packet = new Packet(DEPOT_TO_CARD, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(STATE_INVALID, e.getCode());

    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void TestDepotToCard_Selection_Valid_NOT_FROM_CORRECT_STATE(int val) {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, DEFAULT), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(val);


        Packet packet = new Packet(DEPOT_TO_CARD, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(STATE_INVALID, e.getCode());

    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void TestDepotToCard_NOT_HAVING_SOURCE_IN_LINE(int val) {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(val);

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.STONE, 1));
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        ArrayList<LeadCard> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, true);
        card.setupAbility();

        cards.add(card);


        Packet packet = new Packet(DEPOT_TO_CARD, null, args);


        InvalidArgumentsException e1 = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(SELECTION_INVALID, e1.getCode());
    }

    @Test
    void TestDepotToCard_CORRECT() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(1);

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.STONE, 1));
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        ArrayList<LeadCard> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, true);
        card.setupAbility();

        cards.add(card);

        game.getCurrentPlayer().setLeaders(cards);

        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.SERVANT, 1));

        ArrayList<Object> args1 = new ArrayList<>();
        args1.add(new Resource(FinalResource.ResourceType.SERVANT, 1));
        args1.add(1);

        Packet packet1 = new Packet(ADD_FROM_EXTRA, null, args1);

        assertDoesNotThrow(() -> {
            action.execute(game, packet1);
        });

        ArrayList<Object> expectedExtra = new ArrayList<>();
        expectedExtra.add(new Resource(FinalResource.ResourceType.SERVANT, 1));

        assertTrue(deepEquals(expectedExtra.toArray()[0], game.getCurrentPlayer().getWarehouse().getDepot()[1]));

        Packet packet = new Packet(DEPOT_TO_CARD, null, args);

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });

        assertTrue(deepEquals(expectedExtra.toArray()[0],((DepotAbility)game.getCurrentPlayer().getLeader(card).getSpecialAbility()).getDepot()));

        assertTrue(deepEquals(null, game.getCurrentPlayer().getWarehouse().getDepot()[1]));//L'ho tolto ovviamente deve sparire pure da qua

    }

    @Test
    void TestDepotToCard_NOTCORRECT_RESOURCE() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(1);

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.STONE, 1));
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        ArrayList<LeadCard> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, true);
        card.setupAbility();

        cards.add(card);

        game.getCurrentPlayer().setLeaders(cards);

        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.COIN, 1));

        ArrayList<Object> args1 = new ArrayList<>();
        args1.add(new Resource(FinalResource.ResourceType.COIN, 1));
        args1.add(1);

        Packet packet1 = new Packet(ADD_FROM_EXTRA, null, args1);

        assertDoesNotThrow(() -> {
            action.execute(game, packet1);
        });

        ArrayList<Object> expectedExtra = new ArrayList<>();
        expectedExtra.add(new Resource(FinalResource.ResourceType.COIN, 1));

        assertTrue(deepEquals(expectedExtra.toArray()[0], game.getCurrentPlayer().getWarehouse().getDepot()[1]));

        Packet packet = new Packet(DEPOT_TO_CARD, null, args);

        InvalidArgumentsException e1 = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
      //  System.out.println(e1.getErrorMessage());
        assertEquals(SELECTION_INVALID, e1.getCode());


    }

    @Test
    void TestDepotToCard_NOTCORRECT_OVERFLOW_DEPOTCARD() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(2);

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.STONE, 1));
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        ArrayList<LeadCard> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, true);
        card.setupAbility();

        cards.add(card);

        game.getCurrentPlayer().setLeaders(cards);

        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.SERVANT, 3));

        ArrayList<Object> args1 = new ArrayList<>();
        args1.add(new Resource(FinalResource.ResourceType.SERVANT, 3));
        args1.add(2);

        Packet packet1 = new Packet(ADD_FROM_EXTRA, null, args1);

        assertDoesNotThrow(() -> {
            action.execute(game, packet1);
            action.execute(game, packet1);
            action.execute(game, packet1);
        });

        ArrayList<Object> expectedExtra = new ArrayList<>();
        expectedExtra.add(new Resource(FinalResource.ResourceType.SERVANT, 3));

        assertTrue(deepEquals(expectedExtra.toArray()[0], game.getCurrentPlayer().getWarehouse().getDepot()[2]));

        Packet packet = new Packet(DEPOT_TO_CARD, null, args);

        InvalidArgumentsException e1 = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
            action.execute(game, packet);
            action.execute(game, packet);
        });
      //  System.out.println(e1.getErrorMessage());

        assertEquals(SELECTION_INVALID, e1.getCode());


    }

    @Test
    void TestDepotToCard_NOTCORRECT_CARD_NOT_ACTIVED() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(2);

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.STONE, 1));
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        ArrayList<LeadCard> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, false);
        card.setupAbility();

        cards.add(card);

        game.getCurrentPlayer().setLeaders(cards);

        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.SERVANT, 3));

        ArrayList<Object> args1 = new ArrayList<>();
        args1.add(new Resource(FinalResource.ResourceType.SERVANT, 3));
        args1.add(2);

        Packet packet1 = new Packet(ADD_FROM_EXTRA, null, args1);

        assertDoesNotThrow(() -> {
            action.execute(game, packet1);
            action.execute(game, packet1);
            action.execute(game, packet1);
        });

        ArrayList<Object> expectedExtra = new ArrayList<>();
        expectedExtra.add(new Resource(FinalResource.ResourceType.SERVANT, 3));

        assertTrue(deepEquals(expectedExtra.toArray()[0], game.getCurrentPlayer().getWarehouse().getDepot()[2]));

        Packet packet = new Packet(DEPOT_TO_CARD, null, args);

        InvalidArgumentsException e1 = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
      //  System.out.println(e1.getErrorMessage());

        assertEquals(SELECTION_INVALID, e1.getCode());

    }

    //end region

    //new region

    @Test
    void TestExtraToCard_LESS_ARGS() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();

        Packet packet = new Packet(EXTRA_TO_CARD, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(NOT_ENOUGH_ARGS, e.getCode());


    }

    @Test
    void TestExtraToCard_MANY_ARGS() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(1);
        args.add(2);


        Packet packet = new Packet(EXTRA_TO_CARD, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(TOO_MANY_ARGS, e.getCode());


    }

    @Test
    void TestExtraToCard_TYPE_MISSMATCH() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add("new Resource()");


        Packet packet = new Packet(EXTRA_TO_CARD, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(TYPE_MISMATCH, e.getCode());

    }

    @Test
    void TestExtraToCard_Selection_Valid_ButFromDefaultScene() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, DEFAULT), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(FinalResource.ResourceType.SERVANT,1));

        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.SERVANT, 2));

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.STONE, 1));
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        ArrayList<LeadCard> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, true);
        card.setupAbility();

        cards.add(card);

        game.getCurrentPlayer().setLeaders(cards);


        Packet packet = new Packet(EXTRA_TO_CARD, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(STATE_INVALID, e.getCode());

    }

    @Test
    void TestExtraToCardWhenExtraIsEmpty() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(FinalResource.ResourceType.SERVANT, 2));


        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.STONE, 1));
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        ArrayList<LeadCard> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, true);
        card.setupAbility();

        cards.add(card);

        game.getCurrentPlayer().setLeaders(cards);

        Packet packet = new Packet(EXTRA_TO_CARD, null, args);


        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
       // System.out.println(e.getErrorMessage());
        assertEquals(SELECTION_INVALID, e.getCode());

    }

    @Test
    void TestExtraToCardWhenCorrect(){
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(FinalResource.ResourceType.SERVANT, 2));

        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.SERVANT, 2));

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.STONE, 1));
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        ArrayList<LeadCard> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, true);
        card.setupAbility();

        cards.add(card);

        game.getCurrentPlayer().setLeaders(cards);

        Packet packet = new Packet(EXTRA_TO_CARD, null, args);


        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });

        ArrayList<Object> expectedExtra = new ArrayList<>();
        expectedExtra.add(new Resource(FinalResource.ResourceType.SERVANT, 1));

        assertTrue(deepEquals(expectedExtra.toArray()[0],((DepotAbility)game.getCurrentPlayer().getLeader(card).getSpecialAbility()).getDepot()));

        assertTrue(deepEquals(1, game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SERVANT).getAmount()));
    }

    @Test
    void TestDepotToCardWhenOverFlowDepotCard(){
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(FinalResource.ResourceType.SERVANT, 3));

        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.SERVANT, 3));

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.STONE, 1));
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        ArrayList<LeadCard> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, true);
        card.setupAbility();

        cards.add(card);

        game.getCurrentPlayer().setLeaders(cards);

        Packet packet = new Packet(EXTRA_TO_CARD, null, args);


        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
            action.execute(game, packet);
            action.execute(game, packet);
        });
        System.out.println(e.getErrorMessage());
        assertEquals(SELECTION_INVALID, e.getCode());


    }

    @Test
    void TestExtraToCard_NOTCORRECT_RESOURCE() {
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(FinalResource.ResourceType.COIN, 2));

        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.COIN, 2));

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.STONE, 1));
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        ArrayList<LeadCard> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, true);
        card.setupAbility();

        cards.add(card);

        game.getCurrentPlayer().setLeaders(cards);

        Packet packet = new Packet(EXTRA_TO_CARD, null, args);


        InvalidArgumentsException e1 = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        //  System.out.println(e1.getErrorMessage());
        assertEquals(SELECTION_INVALID, e1.getCode());



    }

    @Test
    void TestExtraToCardWhenCorrect_BUT_CARD_NOT_ACTIVED(){
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(FinalResource.ResourceType.SERVANT, 2));

        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.SERVANT, 2));

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.STONE, 1));
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        ArrayList<LeadCard> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, false);
        card.setupAbility();

        cards.add(card);

        game.getCurrentPlayer().setLeaders(cards);

        Packet packet = new Packet(EXTRA_TO_CARD, null, args);


        InvalidArgumentsException e1 = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        //  System.out.println(e1.getErrorMessage());
        assertEquals(SELECTION_INVALID, e1.getCode());
    }

    @Test
    void TestExtraToCardWhenCorrect_ABILITY_NON_CORRECT(){
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(FinalResource.ResourceType.SERVANT, 2));

        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.SERVANT, 2));

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.STONE, 1));
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 1, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.GREEN, 1, 0));
        ArrayList<LeadCard> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_1_1", 2, cardRequirements, null, Ability.AbilityType.SALE, FinalResource.ResourceType.SERVANT, true);
        card.setupAbility();

        cards.add(card);

        game.getCurrentPlayer().setLeaders(cards);

        Packet packet = new Packet(EXTRA_TO_CARD, null, args);


        InvalidArgumentsException e1 = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        //  System.out.println(e1.getErrorMessage());
        assertEquals(SELECTION_INVALID, e1.getCode());
    }

    //end region

    //new region
    @Test
    void CancelTest(){
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, null), MNG_WARE);


        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.SHIELD, 2));
        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.COIN, 3));

        ArrayList<Object> argsRes = new ArrayList<>();
        argsRes.add(new Resource(FinalResource.ResourceType.SHIELD, 2));
        argsRes.add(1);

        Packet packetAdder1 = new Packet(ADD_FROM_EXTRA, null, argsRes);

        assertDoesNotThrow(() -> {
            action.execute(game, packetAdder1);
            action.execute(game, packetAdder1);
            argsRes.set(0, new Resource(FinalResource.ResourceType.COIN, 3));
            argsRes.set(1, 2);
            Packet packetAdder2 = new Packet(ADD_FROM_EXTRA, null, argsRes);
            action.execute(game, packetAdder2);
            action.execute(game, packetAdder2);
            action.execute(game, packetAdder2);
        });

        ArrayList<Object> expectedExtra = new ArrayList<>();
        expectedExtra.add(new Resource(FinalResource.ResourceType.SHIELD, 2));
        expectedExtra.add(new Resource(FinalResource.ResourceType.COIN, 3));

        Save.saveGame(game);

        assertTrue(deepEquals(expectedExtra.toArray()[0], game.getCurrentPlayer().getWarehouse().getDepot()[1]));
        assertTrue(deepEquals(expectedExtra.toArray()[1], game.getCurrentPlayer().getWarehouse().getDepot()[2]));

        ArrayList<Object> args = new ArrayList<>();
        args.add(1);
        args.add(2);

        Packet packet = new Packet(SWAP_LINES, null, args);


        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });

        expectedExtra.set(1,new Resource(FinalResource.ResourceType.COIN, 2));
        assertTrue(deepEquals(expectedExtra.toArray()[1], game.getCurrentPlayer().getWarehouse().getDepot()[1]));
        assertTrue(deepEquals(expectedExtra.toArray()[0], game.getCurrentPlayer().getWarehouse().getDepot()[2]));

        Packet packetCanc = new Packet(CANCEL, null, new ArrayList<>());

        action.setActionState(new ManageWarehouse_ActionState(action, MNG_WARE), MNG_WARE);

        assertDoesNotThrow(() -> {
            action.execute(game, packetCanc);
        });

        expectedExtra.set(1,new Resource(FinalResource.ResourceType.COIN, 3));
        assertTrue(deepEquals(expectedExtra.toArray()[0], game.getCurrentPlayer().getWarehouse().getDepot()[1]));
        assertTrue(deepEquals(expectedExtra.toArray()[1], game.getCurrentPlayer().getWarehouse().getDepot()[2]));
    }

    //end region

    //new region

    @Test
    void ConfirmTest1(){
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, CHOOSE_INIT_RES), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(FinalResource.ResourceType.SERVANT, 2));

        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.SERVANT, 2));


        Packet packet = new Packet(CONFIRM, null, args);


        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });
        int test=2;
        assertTrue(deepEquals(test,(int)game.getPlayers().get(1).getFaithPosition()));
        assertTrue(deepEquals(test,(int)game.getPlayers().get(2).getFaithPosition()));
        assertTrue(deepEquals(test,(int)game.getPlayers().get(3).getFaithPosition()));
        assertTrue(deepEquals(DEFAULT,game.getPlayers().get(0).getLastAction()));
    }

    @Test
    void ConfirmTest2(){
        Action action = new Action();
        action.setActionState(new ManageWarehouse_ActionState(action, ACT_PROD), MNG_WARE);
        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(FinalResource.ResourceType.SERVANT, 2));

        game.getCurrentPlayer().addExtra(new Resource(FinalResource.ResourceType.SERVANT, 2));


        Packet packet = new Packet(CONFIRM, null, args);


        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });
        int test=2;
        assertTrue(deepEquals(test,(int)game.getPlayers().get(1).getFaithPosition()));
        assertTrue(deepEquals(test,(int)game.getPlayers().get(2).getFaithPosition()));
        assertTrue(deepEquals(test,(int)game.getPlayers().get(3).getFaithPosition()));
        assertTrue(deepEquals(DEFAULT,game.getPlayers().get(0).getLastAction()));

    }





    @Test
    void execute() {
        assert (true);
    }
}