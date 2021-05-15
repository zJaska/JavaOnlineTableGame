package it.polimi.ingsw.IntelliCranio.controllers.action;

import com.cedarsoftware.util.DeepEquals;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static com.cedarsoftware.util.DeepEquals.deepEquals;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

class CardMarket_ActionStateTest {

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
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, null);
        });

        assertEquals(PACKET_NULL, e.getCode());

    }

    @Test
    void nullCode() {//Le instruction code e listobject

        Action action = new Action();
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);

        Packet packet = new Packet(null, null, new ArrayList<>());

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(CODE_NULL, e.getCode());

    }

    @ParameterizedTest
    @EnumSource(value = Packet.InstructionCode.class, names = {"SELECT_CARD", "RES_FROM_DEPOT", "RES_FROM_STRONG", "RES_FROM_CARD", "CANCEL", "CONFIRM", "SELECT_SLOT"})
    void nullArg(Packet.InstructionCode p) {//Le instruction code e listobject

        Action action = new Action();
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);

        Packet packet = new Packet(p, null, null);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(ARGS_NULL, e.getCode());

    }

    @Test
    void SelectCard_less_arg(){
        Action action = new Action();
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);

        Packet packet = new Packet(SELECT_CARD, null, new ArrayList<>());

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(NOT_ENOUGH_ARGS, e.getCode());
    }

    @Test
    void SelectCard_to_many_args(){
        Action action = new Action();
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);
        ArrayList<Object> args=new ArrayList<>();

        args.add(1);
        args.add(2);
        args.add(3);

        Packet packet = new Packet(SELECT_CARD, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(TOO_MANY_ARGS, e.getCode());
    }

    @Test
    void SelectCard_typeMissMatch(){
        Action action = new Action();
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);
        ArrayList<Object> args=new ArrayList<>();

        args.add("1");
        args.add("2");


        Packet packet = new Packet(SELECT_CARD, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(TYPE_MISMATCH, e.getCode());
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -5, -4, -3, -2,-1,3,4,5,6,7, Integer.MAX_VALUE})
    void SelectCard_OverSize(int val){
        Action action = new Action();
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);
        ArrayList<Object> args=new ArrayList<>();

        args.add(val);
        args.add(val);




        Packet packet = new Packet(SELECT_CARD, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(SELECTION_INVALID, e.getCode());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2,3})
    void SelectCard_Valid(int column){
        Action action = new Action();
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);
        ArrayList<Object> args=new ArrayList<>();

        args.add(2);
        args.add(column);




        Packet packet = new Packet(SELECT_CARD, null, args);

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });


    }

    @ParameterizedTest
    @ValueSource(ints ={0, 1, 2,3})
    void SelectCard_EmptySlotCardParametricColumn(int column){
        Action action = new Action();
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);
        ArrayList<Object> args=new ArrayList<>();

        args.add(2);
        args.add(column);

        for(int i=0;i<4;i++)
            game.getCardMarket().removeCard(2,column);

        Packet packet = new Packet(SELECT_CARD, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(SELECTION_INVALID, e.getCode());
    }

    //fare un test in cui ho tutti i livelli e slitto per righe
    @ParameterizedTest
    @ValueSource(ints ={0,1,2})
    void SelectCard_Valid2(int row){
        Action action = new Action();
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);
        ArrayList<Object> args=new ArrayList<>();



        game.getCurrentPlayer().addDevCard(game.getCardMarket().getCard(2,0),0 );//Level1

        game.getCardMarket().removeCard(2,0);

        game.getCurrentPlayer().addDevCard(game.getCardMarket().getCard(1,0),1 );//Level2

        game.getCardMarket().removeCard(1,0);



        args.add(row);
        args.add(1);


        Packet packet = new Packet(SELECT_CARD, null, args);

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });


    }

    //Test dove tutti slot pieni ma non posso
    @ParameterizedTest
    @ValueSource(ints ={1,2})//[0,1]LEVEL2, [2,1]LEVEL3
    void SelectCard_WRONG(int row){
        Action action = new Action();
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);
        ArrayList<Object> args=new ArrayList<>();

        game.getCurrentPlayer().addDevCard(game.getCardMarket().getCard(0,3),0 );//Level3

        game.getCardMarket().removeCard(0,3);

        game.getCurrentPlayer().addDevCard(game.getCardMarket().getCard(0,2),1 );//Level3

        game.getCardMarket().removeCard(0,2);

        game.getCurrentPlayer().addDevCard(game.getCardMarket().getCard(0,0),2 );//Level3

        game.getCardMarket().removeCard(0,0);

        args.add(row);
        args.add(1);

        Packet packet = new Packet(SELECT_CARD, null, args);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(SELECTION_INVALID, e.getCode());
    }


    @Test
    void Confirm_WithoutSelectedCard(){
        Action action = new Action();
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);

        Packet packet = new Packet(CONFIRM, null, new ArrayList<>());

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(SELECTION_INVALID, e.getCode());
    }


    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -5, -4, -3, -2,-1,3,4,5,6,7, Integer.MAX_VALUE})
    void ResFromDepot_NotCorrectLine(int val){
        Action action = new Action();
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);
        ArrayList<Object> args=new ArrayList<>();

        args.add(2);
        args.add(0);

        for(int i=0;i<3;i++)
        game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.SHIELD,1));


        Packet packet = new Packet(SELECT_CARD, null, args);

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });




        ArrayList<Object> args2=new ArrayList<>();
        args2.add(val);

        Packet packet2=new Packet(RES_FROM_DEPOT,null,args2);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet2);
        });

        assertEquals(SELECTION_INVALID, e.getCode());

        Packet packet1 = new Packet(CONFIRM, null, new ArrayList<>());

        assertDoesNotThrow(() -> {
            action.execute(game, packet1);
        });

    }

    @Test
    void ResFromDepot_Correct(){
        Action action = new Action();
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);
        ArrayList<Object> args=new ArrayList<>();
        Card card=game.getCardMarket().getCard(2,0);

        switch (card.getID()) {
            case "developmentcard_front_g_1_1":
                game.getCurrentPlayer().getWarehouse().add(1,new Resource(FinalResource.ResourceType.SHIELD,1));
                game.getCurrentPlayer().getWarehouse().add(1,new Resource(FinalResource.ResourceType.SHIELD,1));
                break;
            case "developmentcard_front_g_1_2":
                game.getCurrentPlayer().getWarehouse().add(0,new Resource(FinalResource.ResourceType.SHIELD,1));
                game.getCurrentPlayer().getWarehouse().add(1,new Resource(FinalResource.ResourceType.STONE,1));
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.SERVANT,1));
                break;
            case "developmentcard_front_g_1_3":
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.SHIELD,1));
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.SHIELD,1));
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.SHIELD,1));
                break;
            case "developmentcard_front_g_1_4":
                game.getCurrentPlayer().getWarehouse().add(1,new Resource(FinalResource.ResourceType.SHIELD,1));
                game.getCurrentPlayer().getWarehouse().add(1,new Resource(FinalResource.ResourceType.SHIELD,1));
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.COIN,1));
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.COIN,1));
                break;
        }

        args.add(2);
        args.add(0);


        Packet packet = new Packet(SELECT_CARD, null, args);

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });



        assertDoesNotThrow(() -> {
            switch (card.getID()) {
                case "developmentcard_front_g_1_1":
                    System.out.println("1");
                    ArrayList<Object> args2=new ArrayList<>();
                    args2.add(1);

                    Packet packet2=new Packet(RES_FROM_DEPOT,null,args2);

                    action.execute(game, packet2);
                    action.execute(game, packet2);
                    break;
                case "developmentcard_front_g_1_2":
                    System.out.println("2");
                    ArrayList<Object> args3=new ArrayList<>();
                    args3.add(0);

                    Packet packet3=new Packet(RES_FROM_DEPOT,null,args3);
                    action.execute(game, packet3);

                    args3.set(0,1);
                    action.execute(game, packet3);

                    args3.set(0,2);
                    action.execute(game, packet3);
                    break;
                case "developmentcard_front_g_1_3":
                    System.out.println("3");
                    ArrayList<Object> args4=new ArrayList<>();
                    args4.add(2);
                    Packet packet4=new Packet(RES_FROM_DEPOT,null,args4);
                    action.execute(game, packet4);
                    action.execute(game, packet4);
                    action.execute(game, packet4);
                    break;
                case "developmentcard_front_g_1_4":
                    System.out.println("4");
                    ArrayList<Object> args5=new ArrayList<>();
                    args5.add(1);
                    Packet packet5=new Packet(RES_FROM_DEPOT,null,args5);

                    action.execute(game, packet5);
                    action.execute(game, packet5);

                    args5.set(0,2);
                    action.execute(game, packet5);
                    action.execute(game, packet5);

                    break;
            }




        });

        assertTrue(deepEquals(null, game.getCurrentPlayer().getWarehouse().getDepot()[2]));
        assertTrue(deepEquals(null, game.getCurrentPlayer().getWarehouse().getDepot()[1]));
        assertTrue(deepEquals(null, game.getCurrentPlayer().getWarehouse().getDepot()[0]));

        Packet packet1 = new Packet(CONFIRM, null, new ArrayList<>());

        assertDoesNotThrow(() -> {
            action.execute(game, packet1);
        });
    }

    @Test
    void ResFromDepot_WrongResources(){
        Action action = new Action();
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);
        ArrayList<Object> args=new ArrayList<>();
        Card card=game.getCardMarket().getCard(2,0);

        switch (card.getID()) {
            case "developmentcard_front_g_1_1":
                game.getCurrentPlayer().getWarehouse().add(1,new Resource(FinalResource.ResourceType.COIN,1));
                game.getCurrentPlayer().getWarehouse().add(1,new Resource(FinalResource.ResourceType.COIN,1));
                break;
            case "developmentcard_front_g_1_2":
                game.getCurrentPlayer().getWarehouse().add(0,new Resource(FinalResource.ResourceType.COIN,1));
                game.getCurrentPlayer().getWarehouse().add(1,new Resource(FinalResource.ResourceType.STONE,1));
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.SERVANT,1));
                break;
            case "developmentcard_front_g_1_3":
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.COIN,1));
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.COIN,1));
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.COIN,1));
                break;
            case "developmentcard_front_g_1_4":
                game.getCurrentPlayer().getWarehouse().add(1,new Resource(FinalResource.ResourceType.STONE,1));
                game.getCurrentPlayer().getWarehouse().add(1,new Resource(FinalResource.ResourceType.STONE,1));
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.COIN,1));
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.COIN,1));
                break;
        }

        args.add(2);
        args.add(0);


        Packet packet = new Packet(SELECT_CARD, null, args);

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });



        assertDoesNotThrow(() -> {
            switch (card.getID()) {
                case "developmentcard_front_g_1_1":
                    System.out.println("1");
                    ArrayList<Object> args2=new ArrayList<>();
                    args2.add(1);

                    Packet packet2=new Packet(RES_FROM_DEPOT,null,args2);

                    action.execute(game, packet2);
                    action.execute(game, packet2);
                    break;
                case "developmentcard_front_g_1_2":
                    System.out.println("2");
                    ArrayList<Object> args3=new ArrayList<>();
                    args3.add(0);

                    Packet packet3=new Packet(RES_FROM_DEPOT,null,args3);
                    action.execute(game, packet3);

                    args3.set(0,1);
                    action.execute(game, packet3);

                    args3.set(0,2);
                    action.execute(game, packet3);
                    break;
                case "developmentcard_front_g_1_3":
                    System.out.println("3");
                    ArrayList<Object> args4=new ArrayList<>();
                    args4.add(2);
                    Packet packet4=new Packet(RES_FROM_DEPOT,null,args4);
                    action.execute(game, packet4);
                    action.execute(game, packet4);
                    action.execute(game, packet4);
                    break;
                case "developmentcard_front_g_1_4":
                    System.out.println("4");
                    ArrayList<Object> args5=new ArrayList<>();
                    args5.add(1);
                    Packet packet5=new Packet(RES_FROM_DEPOT,null,args5);

                    action.execute(game, packet5);
                    action.execute(game, packet5);

                    args5.set(0,2);
                    action.execute(game, packet5);
                    action.execute(game, packet5);

                    break;
            }




        });

        assertTrue(deepEquals(null, game.getCurrentPlayer().getWarehouse().getDepot()[2]));
        assertTrue(deepEquals(null, game.getCurrentPlayer().getWarehouse().getDepot()[1]));
        assertTrue(deepEquals(null, game.getCurrentPlayer().getWarehouse().getDepot()[0]));

        Packet packet1 = new Packet(CONFIRM, null, new ArrayList<>());

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet1);
        });
        assertEquals(SELECTION_INVALID, e.getCode());


    }

    @Test
    void ResFromDepot_WrongAmount(){
        Action action = new Action();
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);
        ArrayList<Object> args=new ArrayList<>();
        Card card=game.getCardMarket().getCard(2,0);

        switch (card.getID()) {
            case "developmentcard_front_g_1_1":
                game.getCurrentPlayer().getWarehouse().add(1,new Resource(FinalResource.ResourceType.SHIELD,1));
                break;
            case "developmentcard_front_g_1_2":
                game.getCurrentPlayer().getWarehouse().add(0,new Resource(FinalResource.ResourceType.SHIELD,1));
                game.getCurrentPlayer().getWarehouse().add(1,new Resource(FinalResource.ResourceType.STONE,1));
                break;
            case "developmentcard_front_g_1_3":
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.SHIELD,1));
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.SHIELD,1));
                break;
            case "developmentcard_front_g_1_4":
                game.getCurrentPlayer().getWarehouse().add(1,new Resource(FinalResource.ResourceType.SHIELD,1));
                game.getCurrentPlayer().getWarehouse().add(1,new Resource(FinalResource.ResourceType.SHIELD,1));
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.COIN,1));
                break;
        }

        args.add(2);
        args.add(0);


        Packet packet = new Packet(SELECT_CARD, null, args);

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });



        assertDoesNotThrow(() -> {
            switch (card.getID()) {
                case "developmentcard_front_g_1_1":
                    System.out.println("1");
                    ArrayList<Object> args2=new ArrayList<>();
                    args2.add(1);

                    Packet packet2=new Packet(RES_FROM_DEPOT,null,args2);

                    action.execute(game, packet2);
                    break;
                case "developmentcard_front_g_1_2":
                    System.out.println("2");
                    ArrayList<Object> args3=new ArrayList<>();
                    args3.add(0);

                    Packet packet3=new Packet(RES_FROM_DEPOT,null,args3);
                    action.execute(game, packet3);

                    args3.set(0,1);
                    action.execute(game, packet3);
                    break;
                case "developmentcard_front_g_1_3":
                    System.out.println("3");
                    ArrayList<Object> args4=new ArrayList<>();
                    args4.add(2);
                    Packet packet4=new Packet(RES_FROM_DEPOT,null,args4);
                    action.execute(game, packet4);
                    action.execute(game, packet4);
                    break;
                case "developmentcard_front_g_1_4":
                    System.out.println("4");
                    ArrayList<Object> args5=new ArrayList<>();
                    args5.add(1);
                    Packet packet5=new Packet(RES_FROM_DEPOT,null,args5);

                    action.execute(game, packet5);
                    action.execute(game, packet5);

                    args5.set(0,2);
                    action.execute(game, packet5);

                    break;
            }




        });

        assertTrue(deepEquals(null, game.getCurrentPlayer().getWarehouse().getDepot()[2]));
        assertTrue(deepEquals(null, game.getCurrentPlayer().getWarehouse().getDepot()[1]));
        assertTrue(deepEquals(null, game.getCurrentPlayer().getWarehouse().getDepot()[0]));

        Packet packet1 = new Packet(CONFIRM, null, new ArrayList<>());


        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet1);
        });
        assertEquals(SELECTION_INVALID, e.getCode());
    }

    @Test
    void ResFromStrong_Correct(){
        Action action = new Action();
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);
        ArrayList<Object> args=new ArrayList<>();
        Card card=game.getCardMarket().getCard(2,0);

        switch (card.getID()) {
            case "developmentcard_front_g_1_1":
                game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.SHIELD,2);
                break;
            case "developmentcard_front_g_1_2":
                game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.SHIELD,1);
                game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.STONE,1);
                game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.SERVANT,1);
                break;
            case "developmentcard_front_g_1_3":
                game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.SHIELD,3);
                break;
            case "developmentcard_front_g_1_4":
                game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.SHIELD,2);
                game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.COIN,2);
                break;
        }

        args.add(2);
        args.add(0);


        Packet packet = new Packet(SELECT_CARD, null, args);

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });



        assertDoesNotThrow(() -> {
            switch (card.getID()) {
                case "developmentcard_front_g_1_1":
                    System.out.println("1");
                    ArrayList<Object> args2=new ArrayList<>();
                    args2.add(new Resource(FinalResource.ResourceType.SHIELD,1));

                    Packet packet2=new Packet(RES_FROM_STRONG,null,args2);

                    action.execute(game, packet2);
                    action.execute(game, packet2);
                    break;
                case "developmentcard_front_g_1_2":
                    System.out.println("2");
                    ArrayList<Object> args3=new ArrayList<>();
                    args3.add(new Resource(FinalResource.ResourceType.SHIELD,1));

                    Packet packet3=new Packet(RES_FROM_STRONG,null,args3);
                    action.execute(game, packet3);

                    args3.set(0,new Resource(FinalResource.ResourceType.STONE,1));
                    action.execute(game, packet3);

                    args3.set(0,new Resource(FinalResource.ResourceType.SERVANT,1));
                    action.execute(game, packet3);
                    break;
                case "developmentcard_front_g_1_3":
                    System.out.println("3");
                    ArrayList<Object> args4=new ArrayList<>();
                    args4.add(new Resource(FinalResource.ResourceType.SHIELD,1));
                    Packet packet4=new Packet(RES_FROM_STRONG,null,args4);
                    action.execute(game, packet4);
                    action.execute(game, packet4);
                    action.execute(game, packet4);
                    break;
                case "developmentcard_front_g_1_4":
                    System.out.println("4");
                    ArrayList<Object> args5=new ArrayList<>();
                    args5.add(new Resource(FinalResource.ResourceType.SHIELD,1));
                    Packet packet5=new Packet(RES_FROM_STRONG,null,args5);

                    action.execute(game, packet5);
                    action.execute(game, packet5);

                    args5.set(0,new Resource(FinalResource.ResourceType.COIN,1));
                    action.execute(game, packet5);
                    action.execute(game, packet5);

                    break;
            }




        });

        assertTrue(deepEquals(0, game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.COIN)));
        assertTrue(deepEquals(0, game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.SHIELD)));
        assertTrue(deepEquals(0, game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.SERVANT)));
        assertTrue(deepEquals(0, game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.STONE)));

        Packet packet1 = new Packet(CONFIRM, null, new ArrayList<>());

        assertDoesNotThrow(() -> {
            action.execute(game, packet1);
        });
    }

    @Test
    void ResFromStrong_WrongResources(){
        Action action = new Action();
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);
        ArrayList<Object> args=new ArrayList<>();
        Card card=game.getCardMarket().getCard(2,0);

        switch (card.getID()) {
            case "developmentcard_front_g_1_1":
                game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.SHIELD,2);
                break;
            case "developmentcard_front_g_1_2":
                game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.SHIELD,1);
                game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.STONE,1);
                game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.SERVANT,1);
                break;
            case "developmentcard_front_g_1_3":
                game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.SHIELD,3);
                break;
            case "developmentcard_front_g_1_4":
                game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.SHIELD,2);
                game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.COIN,2);
                break;
        }

        args.add(2);
        args.add(0);


        Packet packet = new Packet(SELECT_CARD, null, args);

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });



        assertDoesNotThrow(() -> {
            switch (card.getID()) {
                case "developmentcard_front_g_1_1":
                    System.out.println("1");
                    ArrayList<Object> args2=new ArrayList<>();
                    args2.add(new Resource(FinalResource.ResourceType.SHIELD,1));

                    Packet packet2=new Packet(RES_FROM_STRONG,null,args2);

                    action.execute(game, packet2);
                    action.execute(game, packet2);
                    break;
                case "developmentcard_front_g_1_2":
                    System.out.println("2");
                    ArrayList<Object> args3=new ArrayList<>();
                    args3.add(new Resource(FinalResource.ResourceType.SHIELD,1));

                    Packet packet3=new Packet(RES_FROM_STRONG,null,args3);
                    action.execute(game, packet3);

                    args3.set(0,new Resource(FinalResource.ResourceType.STONE,1));
                    action.execute(game, packet3);

                    args3.set(0,new Resource(FinalResource.ResourceType.SERVANT,1));
                    action.execute(game, packet3);
                    break;
                case "developmentcard_front_g_1_3":
                    System.out.println("3");
                    ArrayList<Object> args4=new ArrayList<>();
                    args4.add(new Resource(FinalResource.ResourceType.SHIELD,1));
                    Packet packet4=new Packet(RES_FROM_STRONG,null,args4);
                    action.execute(game, packet4);
                    action.execute(game, packet4);
                    action.execute(game, packet4);
                    break;
                case "developmentcard_front_g_1_4":
                    System.out.println("4");
                    ArrayList<Object> args5=new ArrayList<>();
                    args5.add(new Resource(FinalResource.ResourceType.SHIELD,1));
                    Packet packet5=new Packet(RES_FROM_STRONG,null,args5);

                    action.execute(game, packet5);
                    action.execute(game, packet5);

                    args5.set(0,new Resource(FinalResource.ResourceType.COIN,1));
                    action.execute(game, packet5);
                    action.execute(game, packet5);

                    break;
            }




        });

        assertTrue(deepEquals(0, game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.COIN)));
        assertTrue(deepEquals(0, game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.SHIELD)));
        assertTrue(deepEquals(0, game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.SERVANT)));
        assertTrue(deepEquals(0, game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.STONE)));

        Packet packet1 = new Packet(CONFIRM, null, new ArrayList<>());

        assertDoesNotThrow(() -> {
            action.execute(game, packet1);
        });
    }

    @Test
    void ResFromStrong_WrongAmount(){
        Action action = new Action();
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);
        ArrayList<Object> args=new ArrayList<>();
        Card card=game.getCardMarket().getCard(2,0);

        switch (card.getID()) {
            case "developmentcard_front_g_1_1":
                game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.SHIELD,1);
                break;
            case "developmentcard_front_g_1_2":
                game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.SHIELD,1);
                game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.STONE,1);
                break;
            case "developmentcard_front_g_1_3":
                game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.SHIELD,2);
                break;
            case "developmentcard_front_g_1_4":
                game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.SHIELD,1);
                game.getCurrentPlayer().getStrongbox().addResources(FinalResource.ResourceType.COIN,2);
                break;
        }

        args.add(2);
        args.add(0);


        Packet packet = new Packet(SELECT_CARD, null, args);

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });



        assertDoesNotThrow(() -> {
            switch (card.getID()) {
                case "developmentcard_front_g_1_1":
                    System.out.println("1");
                    ArrayList<Object> args2=new ArrayList<>();
                    args2.add(new Resource(FinalResource.ResourceType.SHIELD,1));

                    Packet packet2=new Packet(RES_FROM_STRONG,null,args2);

                    action.execute(game, packet2);
                    break;
                case "developmentcard_front_g_1_2":
                    System.out.println("2");
                    ArrayList<Object> args3=new ArrayList<>();
                    args3.add(new Resource(FinalResource.ResourceType.SHIELD,1));

                    Packet packet3=new Packet(RES_FROM_STRONG,null,args3);
                    action.execute(game, packet3);

                    args3.set(0,new Resource(FinalResource.ResourceType.STONE,1));
                    action.execute(game, packet3);

                    break;
                case "developmentcard_front_g_1_3":
                    System.out.println("3");
                    ArrayList<Object> args4=new ArrayList<>();
                    args4.add(new Resource(FinalResource.ResourceType.SHIELD,1));
                    Packet packet4=new Packet(RES_FROM_STRONG,null,args4);
                    action.execute(game, packet4);
                    action.execute(game, packet4);
                    break;
                case "developmentcard_front_g_1_4":
                    System.out.println("4");
                    ArrayList<Object> args5=new ArrayList<>();
                    args5.add(new Resource(FinalResource.ResourceType.SHIELD,1));
                    Packet packet5=new Packet(RES_FROM_STRONG,null,args5);

                    action.execute(game, packet5);

                    args5.set(0,new Resource(FinalResource.ResourceType.COIN,1));
                    action.execute(game, packet5);
                    action.execute(game, packet5);

                    break;
            }




        });

        assertTrue(deepEquals(0, game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.COIN)));
        assertTrue(deepEquals(0, game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.SHIELD)));
        assertTrue(deepEquals(0, game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.SERVANT)));
        assertTrue(deepEquals(0, game.getCurrentPlayer().getStrongbox().getAmount(FinalResource.ResourceType.STONE)));

        Packet packet1 = new Packet(CONFIRM, null, new ArrayList<>());

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet1);
        });
        assertEquals(SELECTION_INVALID, e.getCode());
    }

    @Test
    void ResFromCard_Correct(){
        Action action = new Action();
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);
        ArrayList<Object> args=new ArrayList<>();
        Card card=game.getCardMarket().getCard(2,0);


        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.COIN, 5));
        ArrayList<LeadCard> cards = new ArrayList<>();

        LeadCard cardL = new LeadCard("leadercard_front_2_1", 3, cardRequirements, resourceRequirements, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SHIELD, true);
        cardL.setupAbility();

        cards.add(cardL);

        game.getCurrentPlayer().setLeaders(cards);

        DepotAbility depotAbility = (DepotAbility) cardL.getSpecialAbility();
        depotAbility.addResource();
        depotAbility.addResource();

        switch (card.getID()) {
            case "developmentcard_front_g_1_1":
                game.getCurrentPlayer().getWarehouse().add(1,new Resource(FinalResource.ResourceType.SHIELD,1));
                //One because the other one is in card

                break;
            case "developmentcard_front_g_1_2":
                //game.getCurrentPlayer().getWarehouse().add(0,new Resource(FinalResource.ResourceType.SHIELD,1));In card
                game.getCurrentPlayer().getWarehouse().add(1,new Resource(FinalResource.ResourceType.STONE,1));
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.SERVANT,1));
                break;
            case "developmentcard_front_g_1_3":
                //game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.SHIELD,1));
                //game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.SHIELD,1)); in card two
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.SHIELD,1));
                break;
            case "developmentcard_front_g_1_4":
              //  game.getCurrentPlayer().getWarehouse().add(1,new Resource(FinalResource.ResourceType.SHIELD,1));
             //   game.getCurrentPlayer().getWarehouse().add(1,new Resource(FinalResource.ResourceType.SHIELD,1)); in card
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.COIN,1));
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.COIN,1));
                break;
        }

        args.add(2);
        args.add(0);


        Packet packet = new Packet(SELECT_CARD, null, args);

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });



        assertDoesNotThrow(() -> {
            switch (card.getID()) {
                case "developmentcard_front_g_1_1":
                    System.out.println("1");
                    ArrayList<Object> args2=new ArrayList<>();
                    args2.add(1);

                    Packet packet2=new Packet(RES_FROM_DEPOT,null,args2);

                    action.execute(game, packet2);

                    args2.set(0,cardL);

                    Packet packet6=new Packet(RES_FROM_CARD,null,args2);

                    action.execute(game,packet6);

                    break;
                case "developmentcard_front_g_1_2":
                    System.out.println("2");
                    ArrayList<Object> args3=new ArrayList<>();
                    args3.add(cardL);

                    Packet packet7=new Packet(RES_FROM_CARD,null,args3);

                    action.execute(game,packet7);

                    Packet packet3=new Packet(RES_FROM_DEPOT,null,args3);


                    args3.set(0,1);
                    action.execute(game, packet3);

                    args3.set(0,2);
                    action.execute(game, packet3);
                    break;
                case "developmentcard_front_g_1_3":
                    System.out.println("3");
                    ArrayList<Object> args4=new ArrayList<>();
                    args4.add(2);
                    Packet packet4=new Packet(RES_FROM_DEPOT,null,args4);
                    action.execute(game, packet4);

                    args4.set(0,cardL);

                    Packet packet8=new Packet(RES_FROM_CARD,null,args4);

                    action.execute(game,packet8);
                    action.execute(game,packet8);

                    break;
                case "developmentcard_front_g_1_4":
                    System.out.println("4");
                    ArrayList<Object> args5=new ArrayList<>();
                    args5.add(cardL);

                    Packet packet9=new Packet(RES_FROM_CARD,null,args5);

                    action.execute(game,packet9);
                    action.execute(game,packet9);


                    Packet packet5=new Packet(RES_FROM_DEPOT,null,args5);

                    args5.set(0,2);
                    action.execute(game, packet5);
                    action.execute(game, packet5);

                    break;
            }




        });

        assertTrue(deepEquals(null, game.getCurrentPlayer().getWarehouse().getDepot()[2]));
        assertTrue(deepEquals(null, game.getCurrentPlayer().getWarehouse().getDepot()[1]));
        assertTrue(deepEquals(null, game.getCurrentPlayer().getWarehouse().getDepot()[0]));

        Packet packet1 = new Packet(CONFIRM, null, new ArrayList<>());

        assertDoesNotThrow(() -> {
            action.execute(game, packet1);
        });
    }

    @Test
    void ResFromCard_WrongAmount(){
        Action action = new Action();
        action.setActionState(new CardMarket_ActionState(action), CARD_MARKET);
        ArrayList<Object> args=new ArrayList<>();
        Card card=game.getCardMarket().getCard(2,0);


        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        ArrayList<FinalResource> resourceRequirements = new ArrayList<>();
        resourceRequirements.add(new Resource(FinalResource.ResourceType.COIN, 5));
        ArrayList<LeadCard> cards = new ArrayList<>();

        LeadCard cardL = new LeadCard("leadercard_front_2_1", 3, cardRequirements, resourceRequirements, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SHIELD, true);
        cardL.setupAbility();

        cards.add(cardL);

        game.getCurrentPlayer().setLeaders(cards);

        DepotAbility depotAbility = (DepotAbility) cardL.getSpecialAbility();
        depotAbility.addResource();
        depotAbility.addResource();

        switch (card.getID()) {
            case "developmentcard_front_g_1_1":
                game.getCurrentPlayer().getWarehouse().add(1,new Resource(FinalResource.ResourceType.SHIELD,1));
                //One because the other one is in card

                break;
            case "developmentcard_front_g_1_2":
                //game.getCurrentPlayer().getWarehouse().add(0,new Resource(FinalResource.ResourceType.SHIELD,1));In card
                game.getCurrentPlayer().getWarehouse().add(1,new Resource(FinalResource.ResourceType.STONE,1));
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.SERVANT,1));
                break;
            case "developmentcard_front_g_1_3":
                //game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.SHIELD,1));
                //game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.SHIELD,1)); in card two
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.SHIELD,1));
                break;
            case "developmentcard_front_g_1_4":
                //  game.getCurrentPlayer().getWarehouse().add(1,new Resource(FinalResource.ResourceType.SHIELD,1));
                //   game.getCurrentPlayer().getWarehouse().add(1,new Resource(FinalResource.ResourceType.SHIELD,1)); in card
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.COIN,1));
                game.getCurrentPlayer().getWarehouse().add(2,new Resource(FinalResource.ResourceType.COIN,1));
                break;
        }

        args.add(2);
        args.add(0);


        Packet packet = new Packet(SELECT_CARD, null, args);

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });



        assertDoesNotThrow(() -> {
            switch (card.getID()) {
                case "developmentcard_front_g_1_1":
                    System.out.println("1");
                    ArrayList<Object> args2=new ArrayList<>();
                    args2.add(1);

                    Packet packet2=new Packet(RES_FROM_DEPOT,null,args2);

                    action.execute(game, packet2);

                    args2.set(0,cardL);

                    Packet packet6=new Packet(RES_FROM_CARD,null,args2);

                    action.execute(game,packet6);

                    break;
                case "developmentcard_front_g_1_2":
                    System.out.println("2");
                    ArrayList<Object> args3=new ArrayList<>();
                    args3.add(cardL);

                    Packet packet7=new Packet(RES_FROM_CARD,null,args3);

                    action.execute(game,packet7);

                    Packet packet3=new Packet(RES_FROM_DEPOT,null,args3);


                    args3.set(0,1);
                    action.execute(game, packet3);

                    args3.set(0,2);
                    action.execute(game, packet3);
                    break;
                case "developmentcard_front_g_1_3":
                    System.out.println("3");
                    ArrayList<Object> args4=new ArrayList<>();
                    args4.add(2);
                    Packet packet4=new Packet(RES_FROM_DEPOT,null,args4);
                    action.execute(game, packet4);

                    args4.set(0,cardL);

                    Packet packet8=new Packet(RES_FROM_CARD,null,args4);

                    action.execute(game,packet8);
                    action.execute(game,packet8);

                    break;
                case "developmentcard_front_g_1_4":
                    System.out.println("4");
                    ArrayList<Object> args5=new ArrayList<>();
                    args5.add(cardL);

                    Packet packet9=new Packet(RES_FROM_CARD,null,args5);

                    action.execute(game,packet9);
                    action.execute(game,packet9);


                    Packet packet5=new Packet(RES_FROM_DEPOT,null,args5);

                    args5.set(0,2);
                    action.execute(game, packet5);
                    action.execute(game, packet5);

                    break;
            }




        });

        assertTrue(deepEquals(null, game.getCurrentPlayer().getWarehouse().getDepot()[2]));
        assertTrue(deepEquals(null, game.getCurrentPlayer().getWarehouse().getDepot()[1]));
        assertTrue(deepEquals(null, game.getCurrentPlayer().getWarehouse().getDepot()[0]));

        Packet packet1 = new Packet(CONFIRM, null, new ArrayList<>());

        assertDoesNotThrow(() -> {
            action.execute(game, packet1);
        });
    }







    @Test
    void execute() {
        assertTrue(true);
    }
}