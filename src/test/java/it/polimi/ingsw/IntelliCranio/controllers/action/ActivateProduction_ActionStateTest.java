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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static com.cedarsoftware.util.DeepEquals.deepEquals;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static org.junit.jupiter.api.Assertions.*;

class ActivateProduction_ActionStateTest {

    Action action = new Action();

    ArrayList<String> nicknames = new ArrayList<>();
    Game game;


    @BeforeEach
    void setupTest() {
        nicknames.add("1");
        nicknames.add("2");
        nicknames.add("3");
        nicknames.add("4");
        game = new Game(nicknames);
        action.setActionState(new ChooseInitResources_ActionState(action), CHOOSE_INIT_RES);
        Save.saveGame(game);

    }

    @Test
    void nullPacket() {//Le instruction code e listobject

        Action action = new Action();
        action.setActionState(new ActivateProduction_ActionState(action), ACT_PROD);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, null);
        });

        assertEquals(PACKET_NULL, e.getCode());

    }

    @Test
    void nullCode() {//Le instruction code e listobject

        Action action = new Action();
        action.setActionState(new ActivateProduction_ActionState(action), ACT_PROD);

        Packet packet = new Packet(null, null, new ArrayList<>());

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(CODE_NULL, e.getCode());

    }

    @ParameterizedTest
    @EnumSource(value = Packet.InstructionCode.class, names = {"SELECT_SLOT", "SELECT_CARD", "RES_FROM_DEPOT", "RES_FROM_STRONG", "RES_FROM_CARD", "CHOOSE_RES", "CANCEL", "CONFIRM"})
    void nullArg(Packet.InstructionCode p) {//Le instruction code e listobject

        Action action = new Action();
        action.setActionState(new ActivateProduction_ActionState(action), ACT_PROD);

        Packet packet = new Packet(p, null, null);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(ARGS_NULL, e.getCode());

    }

    @Test
    void TestSelectCardTypeNotValid(){
        Action action = new Action();
        action.setActionState(new ActivateProduction_ActionState(action), ACT_PROD);

        ArrayList<Object> arg=new ArrayList<>();
        arg.add("1");

        Packet packet = new Packet(SELECT_SLOT, null, arg);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(TYPE_MISMATCH, e.getCode());
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, 4, 5, -3, 15, Integer.MAX_VALUE})
    void TestSelectSlotOverSize(int val){
        Action action = new Action();
        action.setActionState(new ActivateProduction_ActionState(action), ACT_PROD);

        ArrayList<Object> arg=new ArrayList<>();
        arg.add(val);

        game.getCurrentPlayer().addDevCard(game.getCardMarket().getCard(2,0),0 );//Level1
        game.getCurrentPlayer().addDevCard(game.getCardMarket().getCard(2,0),1 );//Level1
        game.getCurrentPlayer().addDevCard(game.getCardMarket().getCard(2,0),2 );//Level1


        Packet packet = new Packet(SELECT_SLOT, null, arg);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(SELECTION_INVALID, e.getCode());
    }


    @ParameterizedTest
    @ValueSource(ints = {1,2,3})
    void TestSelectSlotWithoutCard(int val){
        Action action = new Action();
        action.setActionState(new ActivateProduction_ActionState(action), ACT_PROD);

        ArrayList<Object> arg=new ArrayList<>();
        arg.add(val);

        Packet packet = new Packet(SELECT_SLOT, null, arg);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(SELECTION_INVALID, e.getCode());
    }

    @Test
    void TestProductionFromDepot(){

        game.changeTurn();

        Action action = new Action();
        action.setActionState(new ActivateProduction_ActionState(action), ACT_PROD);

        Card card=game.getCardMarket().getCard(2,0);

        game.getCurrentPlayer().addDevCard((DevCard) card,0 );//Level1



        ArrayList<Object> arg=new ArrayList<>();
        arg.add(1);

        Packet packet=new Packet(SELECT_SLOT,null,arg);

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });

        assertDoesNotThrow(() -> {
            switch (card.getID()) {
                case "developmentcard_front_g_1_1":
                    System.out.println(1);
                    game.getCurrentPlayer().getWarehouse().add(1, new Resource(FinalResource.ResourceType.COIN, 1));

                    ArrayList<Object> arg2 = new ArrayList<>();
                    arg2.add(1);

                    Packet packet2=new Packet(RES_FROM_DEPOT,null,arg2);

                    action.execute(game, packet2);

                    //Faith gived
                    break;
                case "developmentcard_front_g_1_2":
                    System.out.println(2);
                    game.getCurrentPlayer().getWarehouse().add(1, new Resource(FinalResource.ResourceType.STONE, 1));

                    ArrayList<Object> arg3 = new ArrayList<>();
                    arg3.add(1);

                    Packet packet3=new Packet(RES_FROM_DEPOT,null,arg3);

                    action.execute(game, packet3);

                    //Servant gived
                    break;
                case "developmentcard_front_g_1_3":
                    System.out.println(3);
                    game.getCurrentPlayer().getWarehouse().add(1, new Resource(FinalResource.ResourceType.SERVANT, 1));
                    game.getCurrentPlayer().getWarehouse().add(1, new Resource(FinalResource.ResourceType.SERVANT, 1));

                    ArrayList<Object> arg4 = new ArrayList<>();
                    arg4.add(1);

                    Packet packet4=new Packet(RES_FROM_DEPOT,null,arg4);

                    action.execute(game, packet4);
                    action.execute(game, packet4);

                    //1 Coin, 1 Shield,1 Stone
                    break;
                case "developmentcard_front_g_1_4":
                    System.out.println(4);
                    game.getCurrentPlayer().getWarehouse().add(1, new Resource(FinalResource.ResourceType.STONE, 1));
                    game.getCurrentPlayer().getWarehouse().add(2, new Resource(FinalResource.ResourceType.SERVANT, 1));

                    ArrayList<Object> arg5 = new ArrayList<>();
                    arg5.add(1);

                    Packet packet5=new Packet(RES_FROM_DEPOT,null,arg5);

                    action.execute(game, packet5);

                    arg5.set(0, 2);

                    action.execute(game, packet5);

                    //2 Coin, 1fa
                    break;
            }
        });

        assertTrue(deepEquals(null, game.getCurrentPlayer().getWarehouse().getDepot()[2]));
        assertTrue(deepEquals(null, game.getCurrentPlayer().getWarehouse().getDepot()[1]));
        assertTrue(deepEquals(null, game.getCurrentPlayer().getWarehouse().getDepot()[0]));

        Packet packet6 = new Packet(CONFIRM, null, new ArrayList<>());

        assertDoesNotThrow(() -> {
            action.execute(game, packet6);
        });

        switch (card.getID()) {
            case "developmentcard_front_g_1_1":
                assertTrue(deepEquals(1,game.getCurrentPlayer().getFaithPosition()));

                //Faith gived
                break;
            case "developmentcard_front_g_1_2":
                assertTrue(deepEquals(1,game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.SERVANT)));

                //Servant gived
                break;
            case "developmentcard_front_g_1_3":
                assertTrue(deepEquals(1,game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.COIN)));
                assertTrue(deepEquals(1,game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.SHIELD)));
                assertTrue(deepEquals(1,game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.STONE)));

                //1 Coin, 1 Shield,1 Stone
                break;
            case "developmentcard_front_g_1_4":
                assertTrue(deepEquals(2,game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.COIN)));
                assertTrue(deepEquals(1,game.getCurrentPlayer().getFaithPosition()));

                //2 Coin, 1fa
                break;
        }


    }

    @Test
    void TestProductionFromRES_FROM_STRONG(){

        game.changeTurn();

        Action action = new Action();
        action.setActionState(new ActivateProduction_ActionState(action), ACT_PROD);

        Card card=game.getCardMarket().getCard(2,0);

        game.getCurrentPlayer().addDevCard((DevCard) card,0 );//Level1



        ArrayList<Object> arg=new ArrayList<>();
        arg.add(1);

        Packet packet=new Packet(SELECT_SLOT,null,arg);

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });

        assertDoesNotThrow(() -> {
            switch (card.getID()) {
                case "developmentcard_front_g_1_1":
                    System.out.println(1);
                    game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.COIN,1);
                    ArrayList<Object> arg2 = new ArrayList<>();
                    arg2.add(new Resource(FinalResource.ResourceType.COIN,1));

                    Packet packet2=new Packet(RES_FROM_STRONG,null,arg2);

                    action.execute(game, packet2);

                    //Faith gived
                    break;
                case "developmentcard_front_g_1_2":
                    System.out.println(2);
                    game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.STONE,1);
                    ArrayList<Object> arg3 = new ArrayList<>();
                    arg3.add(new Resource(FinalResource.ResourceType.STONE,1));

                    Packet packet3=new Packet(RES_FROM_STRONG,null,arg3);

                    action.execute(game, packet3);

                    //Servant gived
                    break;
                case "developmentcard_front_g_1_3":
                    System.out.println(3);
                    game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.SERVANT,2);

                    ArrayList<Object> arg4 = new ArrayList<>();
                    arg4.add(new Resource(FinalResource.ResourceType.SERVANT,1));

                    Packet packet4=new Packet(RES_FROM_STRONG,null,arg4);

                    action.execute(game, packet4);
                    action.execute(game, packet4);

                    //1 Coin, 1 Shield,1 Stone
                    break;
                case "developmentcard_front_g_1_4":
                    System.out.println(4);
                    game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.STONE,1);
                    game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.SERVANT,1);

                    ArrayList<Object> arg5 = new ArrayList<>();
                    arg5.add(new Resource(FinalResource.ResourceType.STONE,1));

                    Packet packet5=new Packet(RES_FROM_STRONG,null,arg5);

                    action.execute(game, packet5);

                    arg5.set(0,new Resource(FinalResource.ResourceType.SERVANT,1));

                    action.execute(game, packet5);

                    //2 Coin, 1fa
                    break;
            }
        });

        assertTrue(deepEquals(0, game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.COIN)));
        assertTrue(deepEquals(0, game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.SHIELD)));
        assertTrue(deepEquals(0, game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.SERVANT)));
        assertTrue(deepEquals(0, game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.STONE)));

        Packet packet6 = new Packet(CONFIRM, null, new ArrayList<>());

        assertDoesNotThrow(() -> {
            action.execute(game, packet6);
        });

        switch (card.getID()) {
            case "developmentcard_front_g_1_1":
                assertTrue(deepEquals(1,game.getCurrentPlayer().getFaithPosition()));

                //Faith gived
                break;
            case "developmentcard_front_g_1_2":
                assertTrue(deepEquals(1,game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.SERVANT)));

                //Servant gived
                break;
            case "developmentcard_front_g_1_3":
                assertTrue(deepEquals(1,game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.COIN)));
                assertTrue(deepEquals(1,game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.SHIELD)));
                assertTrue(deepEquals(1,game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.STONE)));

                //1 Coin, 1 Shield,1 Stone
                break;
            case "developmentcard_front_g_1_4":
                assertTrue(deepEquals(2,game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.COIN)));
                assertTrue(deepEquals(1,game.getCurrentPlayer().getFaithPosition()));

                //2 Coin, 1fa
                break;
        }


    }

    @Test
    void TestProductionFromRES_FROM_CARD(){

        game.changeTurn();

        Action action = new Action();
        action.setActionState(new ActivateProduction_ActionState(action), ACT_PROD);

        Card card=game.getCardMarket().getCard(2,0);

        game.getCurrentPlayer().addDevCard((DevCard) card,0 );//Level1



        ArrayList<Object> arg=new ArrayList<>();
        arg.add(1);

        Packet packet=new Packet(SELECT_SLOT,null,arg);

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });

        assertDoesNotThrow(() -> {
            switch (card.getID()) {
                case "developmentcard_front_g_1_1":
                    System.out.println(1);
                    ArrayList<LeadCard> cards = new ArrayList<>();
                    LeadCard cardL = new LeadCard("leadercard_front_2_1", 3, null, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.COIN, true);
                    cardL.setupAbility();

                    cards.add(cardL);

                    game.getCurrentPlayer().setLeaders(cards);

                    DepotAbility depotAbility = (DepotAbility) cardL.getSpecialAbility();
                    depotAbility.addResource();

                    ArrayList<Object> arg2 = new ArrayList<>();
                    arg2.add(cardL);

                    Packet packet2=new Packet(RES_FROM_CARD,null,arg2);

                    action.execute(game, packet2);

                    //Faith gived
                    break;
                case "developmentcard_front_g_1_2":
                    System.out.println(2);
                    ArrayList<LeadCard> cards1 = new ArrayList<>();
                    LeadCard cardL1 = new LeadCard("leadercard_front_2_1", 3, null, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.STONE, true);
                    cardL1.setupAbility();

                    cards1.add(cardL1);

                    game.getCurrentPlayer().setLeaders(cards1);

                    DepotAbility depotAbility1 = (DepotAbility) cardL1.getSpecialAbility();
                    depotAbility1.addResource();

                    ArrayList<Object> arg3 = new ArrayList<>();
                    arg3.add(cardL1);

                    Packet packet3=new Packet(RES_FROM_CARD,null,arg3);

                    action.execute(game, packet3);

                    //Servant gived
                    break;
                case "developmentcard_front_g_1_3":
                    System.out.println(3);
                    ArrayList<LeadCard> cards2 = new ArrayList<>();
                    LeadCard cardL2 = new LeadCard("leadercard_front_2_1", 3, null, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, true);
                    cardL2.setupAbility();

                    cards2.add(cardL2);

                    game.getCurrentPlayer().setLeaders(cards2);

                    DepotAbility depotAbility2 = (DepotAbility) cardL2.getSpecialAbility();
                    depotAbility2.addResource();
                    depotAbility2.addResource();

                    ArrayList<Object> arg4 = new ArrayList<>();
                    arg4.add(cardL2);

                    Packet packet4=new Packet(RES_FROM_CARD,null,arg4);

                    action.execute(game, packet4);
                    action.execute(game, packet4);

                    //1 Coin, 1 Shield,1 Stone
                    break;
                case "developmentcard_front_g_1_4":
                    System.out.println(4);

                    ArrayList<LeadCard> cards3 = new ArrayList<>();
                    LeadCard cardL3 = new LeadCard("leadercard_front_2_1", 3, null, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, true);
                    cardL3.setupAbility();

                    cards3.add(cardL3);

                    LeadCard cardL4 = new LeadCard("leadercard_front_2_2", 3, null, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.STONE, true);
                    cardL4.setupAbility();

                    cards3.add(cardL4);


                    game.getCurrentPlayer().setLeaders(cards3);

                    DepotAbility depotAbility3 = (DepotAbility) cardL3.getSpecialAbility();
                    depotAbility3.addResource();

                    DepotAbility depotAbility4 = (DepotAbility) cardL4.getSpecialAbility();
                    depotAbility4.addResource();

                    ArrayList<Object> arg5 = new ArrayList<>();
                    arg5.add(cardL3);

                    game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.STONE,1);
                    game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.SERVANT,1);


                    Packet packet5=new Packet(RES_FROM_CARD,null,arg5);

                    action.execute(game, packet5);

                    arg5.set(0,cardL4);

                    action.execute(game, packet5);

                    //2 Coin, 1fa
                    break;
            }
        });


        Packet packet6 = new Packet(CONFIRM, null, new ArrayList<>());

        assertDoesNotThrow(() -> {
            action.execute(game, packet6);
        });

        switch (card.getID()) {
            case "developmentcard_front_g_1_1":
                assertTrue(deepEquals(1,game.getCurrentPlayer().getFaithPosition()));

                //Faith gived
                break;
            case "developmentcard_front_g_1_2":
                assertTrue(deepEquals(1,game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.SERVANT)));

                //Servant gived
                break;
            case "developmentcard_front_g_1_3":
                assertTrue(deepEquals(1,game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.COIN)));
                assertTrue(deepEquals(1,game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.SHIELD)));
                assertTrue(deepEquals(1,game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.STONE)));

                //1 Coin, 1 Shield,1 Stone
                break;
            case "developmentcard_front_g_1_4":
                assertTrue(deepEquals(2,game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.COIN)));
                assertTrue(deepEquals(1,game.getCurrentPlayer().getFaithPosition()));

                //2 Coin, 1fa
                break;
        }


    }

    @Test
    void LeadCardProduction(){
        Action action = new Action();
        action.setActionState(new ActivateProduction_ActionState(action), ACT_PROD);

        ArrayList<LeadCard> cards = new ArrayList<>();
        LeadCard cardL = new LeadCard("leadercard_front_4_1", 3, null, null, Ability.AbilityType.PRODUCTION, FinalResource.ResourceType.SHIELD, true);
        cardL.setupAbility();

        cards.add(cardL);

        game.getCurrentPlayer().setLeaders(cards);

        ArrayList<Object> arg=new ArrayList<>();
        arg.add(cardL);


        Packet packet = new Packet(SELECT_CARD, null, arg);

        assertDoesNotThrow( () -> {
            action.execute(game, packet);
        });

        game.getCurrentPlayer().getWarehouse().add(1, new Resource(FinalResource.ResourceType.SHIELD, 1));

        ArrayList<Object> arg2 = new ArrayList<>();
        arg2.add(1);

        Packet packet2=new Packet(RES_FROM_DEPOT,null,arg2);


        assertDoesNotThrow( () -> {
            action.execute(game, packet2);
        });


        ArrayList<Object> arg3 = new ArrayList<>();
        arg3.add(new Resource(FinalResource.ResourceType.COIN,1));

        Packet packet3=new Packet(CHOOSE_RES,null,arg3);

        assertDoesNotThrow( () -> {
            action.execute(game, packet3);
        });

        Packet packet4=new Packet(CONFIRM,null,new ArrayList<>());

        assertDoesNotThrow( () -> {
            action.execute(game, packet4);
        });

        assertTrue(deepEquals(1,game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.COIN)));




    }


    @Test
    void execute() {
        assertTrue(true);
    }
}