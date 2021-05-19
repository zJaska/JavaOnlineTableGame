package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.market.ResourceMarket;
import it.polimi.ingsw.IntelliCranio.models.resource.CardResource;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.models.ability.Ability;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.Save;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;

import static com.cedarsoftware.util.DeepEquals.deepEquals;
import static it.polimi.ingsw.IntelliCranio.models.resource.FinalResource.ResourceType.FAITH;
import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

class ResourceMarket_ActionStateTest {

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
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, null);
        });

        assertEquals(PACKET_NULL, e.getCode());

    }

    @Test
    void nullArg() {

        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(SELECT_ROW,null,null);


        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(ARGS_NULL, e.getCode());

    }

    @Test
    void nullCode() {

        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(null,null,new ArrayList<>());


        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(CODE_NULL, e.getCode());

    }

    @ParameterizedTest
    @EnumSource(value = Packet.InstructionCode.class, mode = EXCLUDE, names = {"SELECT_ROW", "SELECT_COLUMN", "CHOOSE_RES", "CANCEL", "CONFIRM"})
    void nullCode(Packet.InstructionCode p) {
        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(p,null,new ArrayList<>());


        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(CODE_NOT_ALLOWED, e.getCode());

    }

    @Test
    void SelectRowButNotEnoughArgs() {
        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(SELECT_ROW,null,new ArrayList<>());


        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(NOT_ENOUGH_ARGS, e.getCode());

    }

    @Test
    void SelectRowButTooManyArgs() {
        ArrayList<Object> args=new ArrayList<>();

        args.add(1);
        args.add(2);

        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(SELECT_ROW,null,args);


        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(TOO_MANY_ARGS, e.getCode());

    }

    @Test
    void SelectRowButTypeMissMatch() {
        ArrayList<Object> args=new ArrayList<>();

        args.add("1");

        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(SELECT_ROW,null,args);


        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(TYPE_MISMATCH, e.getCode());

    }

    @Test
    void SelectRowButTypeArgNull() {
        ArrayList<Object> args=new ArrayList<>();

        args.add(null);

        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(SELECT_ROW,null,args);


        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(TYPE_MISMATCH, e.getCode());

    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 3, 5, -3, 15, Integer.MAX_VALUE})
    void SelectRowButOutOfBound(int value) {
        ArrayList<Object> args=new ArrayList<>();
        args.add(value);

        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(SELECT_ROW,null,args);


        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(SELECTION_INVALID, e.getCode());

    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2 })
    void testSelectRowWithoutLeaders(int test){

        game.changeTurn();
        game.getCurrentPlayer().setLeaders(new ArrayList<>());

        ArrayList<Object> args=new ArrayList<>();
        args.add(test);

        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(SELECT_ROW,null,args);


        ResourceMarket market = game.getResourceMarket();
        //I would like to select a row and shift that
        ArrayList<Resource> expected=new ArrayList<>();
        Resource ExtraMrblExpected=new Resource(market.getExtraMarbleCopy().getType(),1);

        int incrementFaith=0;
        int numCoin=0;
        int numShield=0;
        int numStone=0;
        int numServant=0;




        for(int c=0;c< market.COLUMNS;c++)
            expected.add(new Resource(market.getGridCopy()[test][c].getType(),1));

        for(int i=0;i<expected.size();i++){
            switch (expected.get(i).getType()){
                case FAITH:
                    incrementFaith++;
                    break;

                case COIN:
                    numCoin++;
                    break;

                case SHIELD:
                    numShield++;
                    break;

                case STONE:
                    numStone++;
                    break;

                case SERVANT:
                    numServant++;
                    break;

                case BLANK:
                    //IDontHaveLeaders
                    break;
            }
        }

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });

        for(int c=0;c<market.COLUMNS-1;c++)
            assertTrue(deepEquals(expected.get(c+1).getType(),game.getResourceMarket().getGridCopy()[test][c].getType()));

        assertTrue(deepEquals(ExtraMrblExpected,game.getResourceMarket().getGridCopy()[test][market.COLUMNS-1]));
        assertTrue(deepEquals(expected.get(0).getType(),game.getResourceMarket().getExtraMarbleCopy().getType()));

        assertTrue(deepEquals(incrementFaith,game.getCurrentPlayer().getFaithPosition()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.COIN)!=null)
            assertTrue(deepEquals(numCoin,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.COIN).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SHIELD)!=null)
            assertTrue(deepEquals(numShield,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SHIELD).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.STONE)!=null)
            assertTrue(deepEquals(numStone,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.STONE).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SERVANT)!=null)
            assertTrue(deepEquals(numServant,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SERVANT).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.BLANK)!=null)
            assertTrue(deepEquals(0,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.BLANK).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.BLANK)!=null)
            assertTrue(deepEquals(0,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.BLANK).getAmount()));

        assertTrue(deepEquals(null,game.getCurrentPlayer().getExtra(FAITH)));

        ArrayList<Object> args1=new ArrayList<>();
        args1.add(new Resource(FinalResource.ResourceType.COIN,1));

        Packet packet1=new Packet(CHOOSE_RES,null,args1);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet1);
        });

        assertEquals(SELECTION_INVALID, e.getCode());


    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2 })
    void testSelectRowWithLeadersButNotActived(int test){

        game.changeTurn();

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 2, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.BLUE, 1, 0));
        ArrayList<LeadCard> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_3_1", 5, cardRequirements, null, Ability.AbilityType.RESOURCE, FinalResource.ResourceType.SERVANT, false);
        card.setupAbility();

        cards.add(card);

        game.getCurrentPlayer().setLeaders(cards);

        ArrayList<Object> args=new ArrayList<>();
        args.add(test);

        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(SELECT_ROW,null,args);


        ResourceMarket market = game.getResourceMarket();
        //I would like to select a row and shift that
        ArrayList<Resource> expected=new ArrayList<>();
        Resource ExtraMrblExpected=new Resource(market.getExtraMarbleCopy().getType(),1);

        int incrementFaith=0;
        int numCoin=0;
        int numShield=0;
        int numStone=0;
        int numServant=0;




        for(int c=0;c< market.COLUMNS;c++)
            expected.add(new Resource(market.getGridCopy()[test][c].getType(),1));

        for(int i=0;i<expected.size();i++){
            switch (expected.get(i).getType()){
                case FAITH:
                    incrementFaith++;
                    break;

                case COIN:
                    numCoin++;
                    break;

                case SHIELD:
                    numShield++;
                    break;

                case STONE:
                    numStone++;
                    break;

                case SERVANT:
                    numServant++;
                    break;

                case BLANK:
                    //IDontHaveLeaders
                    break;
            }
        }

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });

        for(int c=0;c<market.COLUMNS-1;c++)
            assertTrue(deepEquals(expected.get(c+1).getType(),game.getResourceMarket().getGridCopy()[test][c].getType()));

        assertTrue(deepEquals(ExtraMrblExpected,game.getResourceMarket().getGridCopy()[test][market.COLUMNS-1]));
        assertTrue(deepEquals(expected.get(0).getType(),game.getResourceMarket().getExtraMarbleCopy().getType()));

        assertTrue(deepEquals(incrementFaith,game.getCurrentPlayer().getFaithPosition()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.COIN)!=null)
            assertTrue(deepEquals(numCoin,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.COIN).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SHIELD)!=null)
            assertTrue(deepEquals(numShield,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SHIELD).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.STONE)!=null)
            assertTrue(deepEquals(numStone,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.STONE).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SERVANT)!=null)
            assertTrue(deepEquals(numServant,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SERVANT).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.BLANK)!=null)
            assertTrue(deepEquals(0,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.BLANK).getAmount()));

        assertTrue(deepEquals(null,game.getCurrentPlayer().getExtra(FAITH)));

        ArrayList<Object> args1=new ArrayList<>();
        args1.add(new Resource(FinalResource.ResourceType.COIN,1));

        Packet packet1=new Packet(CHOOSE_RES,null,args1);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet1);
        });

        assertEquals(SELECTION_INVALID, e.getCode());


    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2 })
    void testSelectRowWithLeadersButWrongAbility(int test){

        game.changeTurn();

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 2, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.BLUE, 1, 0));
        ArrayList<LeadCard> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_3_1", 5, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, false);
        card.setupAbility();

        cards.add(card);

        game.getCurrentPlayer().setLeaders(cards);

        ArrayList<Object> args=new ArrayList<>();
        args.add(test);

        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(SELECT_ROW,null,args);


        ResourceMarket market = game.getResourceMarket();
        //I would like to select a row and shift that
        ArrayList<Resource> expected=new ArrayList<>();
        Resource ExtraMrblExpected=new Resource(market.getExtraMarbleCopy().getType(),1);

        int incrementFaith=0;
        int numCoin=0;
        int numShield=0;
        int numStone=0;
        int numServant=0;




        for(int c=0;c< market.COLUMNS;c++)
            expected.add(new Resource(market.getGridCopy()[test][c].getType(),1));

        for(int i=0;i<expected.size();i++){
            switch (expected.get(i).getType()){
                case FAITH:
                    incrementFaith++;
                    break;

                case COIN:
                    numCoin++;
                    break;

                case SHIELD:
                    numShield++;
                    break;

                case STONE:
                    numStone++;
                    break;

                case SERVANT:
                    numServant++;
                    break;

                case BLANK:
                    //IDontHaveLeaders
                    break;
            }
        }

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });

        for(int c=0;c<market.COLUMNS-1;c++)
            assertTrue(deepEquals(expected.get(c+1).getType(),game.getResourceMarket().getGridCopy()[test][c].getType()));

        assertTrue(deepEquals(ExtraMrblExpected,game.getResourceMarket().getGridCopy()[test][market.COLUMNS-1]));
        assertTrue(deepEquals(expected.get(0).getType(),game.getResourceMarket().getExtraMarbleCopy().getType()));

        assertTrue(deepEquals(incrementFaith,game.getCurrentPlayer().getFaithPosition()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.COIN)!=null)
            assertTrue(deepEquals(numCoin,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.COIN).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SHIELD)!=null)
            assertTrue(deepEquals(numShield,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SHIELD).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.STONE)!=null)
            assertTrue(deepEquals(numStone,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.STONE).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SERVANT)!=null)
            assertTrue(deepEquals(numServant,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SERVANT).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.BLANK)!=null)
            assertTrue(deepEquals(0,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.BLANK).getAmount()));

        assertTrue(deepEquals(null,game.getCurrentPlayer().getExtra(FAITH)));

        ArrayList<Object> args1=new ArrayList<>();
        args1.add(new Resource(FinalResource.ResourceType.SERVANT,1));

        Packet packet1=new Packet(CHOOSE_RES,null,args1);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet1);
        });

        assertEquals(SELECTION_INVALID, e.getCode());


    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2 })
    void testSelectRowWithLeadersWrongResource(int test){

        game.changeTurn();

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 2, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.BLUE, 1, 0));
        ArrayList<LeadCard> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_3_1", 5, cardRequirements, null, Ability.AbilityType.RESOURCE, FinalResource.ResourceType.SERVANT, true);
        card.setupAbility();

        cards.add(card);

        game.getCurrentPlayer().setLeaders(cards);

        ArrayList<Object> args=new ArrayList<>();
        args.add(test);

        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(SELECT_ROW,null,args);


        ResourceMarket market = game.getResourceMarket();
        //I would like to select a row and shift that
        ArrayList<Resource> expected=new ArrayList<>();
        Resource ExtraMrblExpected=new Resource(market.getExtraMarbleCopy().getType(),1);

        int incrementFaith=0;
        int numCoin=0;
        int numShield=0;
        int numStone=0;
        int numServant=0;
        //int numBlank=0;



        for(int c=0;c< market.COLUMNS;c++)
            expected.add(new Resource(market.getGridCopy()[test][c].getType(),1));

        for(int i=0;i<expected.size();i++){
            switch (expected.get(i).getType()){
                case FAITH:
                    incrementFaith++;
                    break;

                case COIN:
                    numCoin++;
                    break;

                case SHIELD:
                    numShield++;
                    break;

                case STONE:
                    numStone++;
                    break;

                case SERVANT:
                    numServant++;
                    break;

                case BLANK:
                    //numBlank++; The Cart is not correct, is useless to do a check
                    break;
            }
        }

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });

        ArrayList<Object> args1=new ArrayList<>();
        args1.add(new Resource(FinalResource.ResourceType.SHIELD,1));

        Packet packet1=new Packet(CHOOSE_RES,null,args1);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet1);
        });

        assertEquals(SELECTION_INVALID, e.getCode());



    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2 })
    void testSelectRowWithLeadersCorrect(int test){

        game.changeTurn();

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 2, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.BLUE, 1, 0));
        ArrayList<LeadCard> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_3_1", 5, cardRequirements, null, Ability.AbilityType.RESOURCE, FinalResource.ResourceType.SERVANT, true);
        card.setupAbility();

        cards.add(card);

        game.getCurrentPlayer().setLeaders(cards);

        ArrayList<Object> args=new ArrayList<>();
        args.add(test);

        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(SELECT_ROW,null,args);


        ResourceMarket market = game.getResourceMarket();
        //I would like to select a row and shift that
        ArrayList<Resource> expected=new ArrayList<>();
        Resource ExtraMrblExpected=new Resource(market.getExtraMarbleCopy().getType(),1);

        int incrementFaith=0;
        int numCoin=0;
        int numShield=0;
        int numStone=0;
        int numServant=0;
        int numBlank=0;



        for(int c=0;c< market.COLUMNS;c++)
            expected.add(new Resource(market.getGridCopy()[test][c].getType(),1));

        for(int i=0;i<expected.size();i++){
            switch (expected.get(i).getType()){
                case FAITH:
                    incrementFaith++;
                    break;

                case COIN:
                    numCoin++;
                    break;

                case SHIELD:
                    numShield++;
                    break;

                case STONE:
                    numStone++;
                    break;

                case SERVANT:
                    numServant++;
                    break;

                case BLANK:
                    numBlank++;
                    break;
            }
        }

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });

        for(int c=0;c<market.COLUMNS-1;c++)
            assertTrue(deepEquals(expected.get(c+1).getType(),game.getResourceMarket().getGridCopy()[test][c].getType()));

        assertTrue(deepEquals(ExtraMrblExpected,game.getResourceMarket().getGridCopy()[test][market.COLUMNS-1]));
        assertTrue(deepEquals(expected.get(0).getType(),game.getResourceMarket().getExtraMarbleCopy().getType()));

        assertTrue(deepEquals(incrementFaith,game.getCurrentPlayer().getFaithPosition()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.COIN)!=null)
            assertTrue(deepEquals(numCoin,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.COIN).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SHIELD)!=null)
            assertTrue(deepEquals(numShield,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SHIELD).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.STONE)!=null)
            assertTrue(deepEquals(numStone,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.STONE).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SERVANT)!=null)
            assertTrue(deepEquals(numServant,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SERVANT).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.BLANK)!=null)
            assertTrue(deepEquals(0,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.BLANK).getAmount()));

        assertTrue(deepEquals(null,game.getCurrentPlayer().getExtra(FAITH)));

        if(numBlank>0) {


            Packet packet2=new Packet(CONFIRM,null,new ArrayList<>());

            InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
                action.execute(game, packet2);
            });

            assertEquals(SELECTION_INVALID, e.getCode());





            ArrayList<Object> args1 = new ArrayList<>();
            args1.add(new Resource(FinalResource.ResourceType.SERVANT, 1));

            Packet packet1 = new Packet(CHOOSE_RES, null, args1);

            while(numBlank>0) {
                assertDoesNotThrow(() -> {
                    action.execute(game, packet1);
                });
                numServant++;

                assertTrue(deepEquals(numServant, game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SERVANT).getAmount()));
                numBlank--;
            }

            Packet packet3=new Packet(CONFIRM,null,new ArrayList<>());

            assertDoesNotThrow(() -> {
                action.execute(game, packet3);
            });

        }else{
            ArrayList<Object> args1=new ArrayList<>();
            args1.add(new Resource(FinalResource.ResourceType.SERVANT,1));

            Packet packet1=new Packet(CHOOSE_RES,null,args1);

            InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
                action.execute(game, packet1);
            });

            assertEquals(SELECTION_INVALID, e.getCode());

            Packet packet2=new Packet(CONFIRM,null,new ArrayList<>());

            assertDoesNotThrow(() -> {
                action.execute(game, packet2);
            });


        }

    }

    //End region

    //New region

    @Test
    void SelectColumnButNotEnoughArgs() {
        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(SELECT_COLUMN,null,new ArrayList<>());


        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(NOT_ENOUGH_ARGS, e.getCode());

    }

    @Test
    void SelectColumnButTooManyArgs() {
        ArrayList<Object> args=new ArrayList<>();

        args.add(1);
        args.add(2);

        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(SELECT_COLUMN,null,args);


        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(TOO_MANY_ARGS, e.getCode());

    }

    @Test
    void SelectColumnButTypeMissMatch() {
        ArrayList<Object> args=new ArrayList<>();

        args.add("1");

        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(SELECT_COLUMN,null,args);


        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(TYPE_MISMATCH, e.getCode());

    }

    @Test
    void SelectColumnButTypeArgNull() {
        ArrayList<Object> args=new ArrayList<>();

        args.add(null);

        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(SELECT_COLUMN,null,args);


        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(TYPE_MISMATCH, e.getCode());

    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 4, 5, -3, 15, Integer.MAX_VALUE})
    void SelectColumnButOutOfBound(int value) {
        ArrayList<Object> args=new ArrayList<>();
        args.add(value);

        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(SELECT_COLUMN,null,args);


        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });

        assertEquals(SELECTION_INVALID, e.getCode());

    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2,3 })
    void testSelectColumnWithoutLeaders(int test){

        game.changeTurn();
        game.getCurrentPlayer().setLeaders(new ArrayList<>());

        ArrayList<Object> args=new ArrayList<>();
        args.add(test);

        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(SELECT_COLUMN,null,args);


        ResourceMarket market = game.getResourceMarket();
        //I would like to select a row and shift that
        ArrayList<Resource> expected=new ArrayList<>();
        Resource ExtraMrblOld=new Resource(market.getExtraMarbleCopy().getType(),1);

        int incrementFaith=0;
        int numCoin=0;
        int numShield=0;
        int numStone=0;
        int numServant=0;

        for(int c=0;c< market.ROWS;c++)
            expected.add(new Resource(market.getGridCopy()[c][test].getType(),1));

        for(int i=0;i<expected.size();i++){
            switch (expected.get(i).getType()){
                case FAITH:
                    incrementFaith++;
                    break;

                case COIN:
                    numCoin++;
                    break;

                case SHIELD:
                    numShield++;
                    break;

                case STONE:
                    numStone++;
                    break;

                case SERVANT:
                    numServant++;
                    break;

                case BLANK:
                    //IDontHaveLeaders
                    break;
            }
        }

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });

        for(int c=0;c<market.ROWS-1;c++)
            assertTrue(deepEquals(expected.get(c+1).getType(),game.getResourceMarket().getGridCopy()[c][test].getType()));

        assertTrue(deepEquals(ExtraMrblOld,game.getResourceMarket().getGridCopy()[market.ROWS-1][test]));
        assertTrue(deepEquals(expected.get(0).getType(),game.getResourceMarket().getExtraMarbleCopy().getType()));

        assertTrue(deepEquals(incrementFaith,game.getCurrentPlayer().getFaithPosition()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.COIN)!=null)
            assertTrue(deepEquals(numCoin,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.COIN).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SHIELD)!=null)
            assertTrue(deepEquals(numShield,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SHIELD).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.STONE)!=null)
            assertTrue(deepEquals(numStone,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.STONE).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SERVANT)!=null)
            assertTrue(deepEquals(numServant,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SERVANT).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.BLANK)!=null)
            assertTrue(deepEquals(0,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.BLANK).getAmount()));

        assertTrue(deepEquals(null,game.getCurrentPlayer().getExtra(FAITH)));

        ArrayList<Object> args1=new ArrayList<>();
        args1.add(new Resource(FinalResource.ResourceType.COIN,1));

        Packet packet1=new Packet(CHOOSE_RES,null,args1);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet1);
        });

        assertEquals(SELECTION_INVALID, e.getCode());


    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3 })
    void testSelectColumnButLeadersNotActived(int test){

        game.changeTurn();

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 2, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.BLUE, 1, 0));
        ArrayList<LeadCard> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_3_1", 5, cardRequirements, null, Ability.AbilityType.RESOURCE, FinalResource.ResourceType.SERVANT, false);
        card.setupAbility();

        cards.add(card);

        game.getCurrentPlayer().setLeaders(cards);

        ArrayList<Object> args=new ArrayList<>();
        args.add(test);

        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(SELECT_COLUMN,null,args);


        ResourceMarket market = game.getResourceMarket();
        //I would like to select a row and shift that
        ArrayList<Resource> expected=new ArrayList<>();
        Resource ExtraMrblOld=new Resource(market.getExtraMarbleCopy().getType(),1);

        int incrementFaith=0;
        int numCoin=0;
        int numShield=0;
        int numStone=0;
        int numServant=0;

        for(int c=0;c< market.ROWS;c++)
            expected.add(new Resource(market.getGridCopy()[c][test].getType(),1));

        for(int i=0;i<expected.size();i++){
            switch (expected.get(i).getType()){
                case FAITH:
                    incrementFaith++;
                    break;

                case COIN:
                    numCoin++;
                    break;

                case SHIELD:
                    numShield++;
                    break;

                case STONE:
                    numStone++;
                    break;

                case SERVANT:
                    numServant++;
                    break;

                case BLANK:
                    //IDontHaveLeaders
                    break;
            }
        }

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });

        for(int c=0;c<market.ROWS-1;c++)
            assertTrue(deepEquals(expected.get(c+1).getType(),game.getResourceMarket().getGridCopy()[c][test].getType()));

        assertTrue(deepEquals(ExtraMrblOld,game.getResourceMarket().getGridCopy()[market.ROWS-1][test]));
        assertTrue(deepEquals(expected.get(0).getType(),game.getResourceMarket().getExtraMarbleCopy().getType()));

        assertTrue(deepEquals(incrementFaith,game.getCurrentPlayer().getFaithPosition()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.COIN)!=null)
            assertTrue(deepEquals(numCoin,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.COIN).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SHIELD)!=null)
            assertTrue(deepEquals(numShield,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SHIELD).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.STONE)!=null)
            assertTrue(deepEquals(numStone,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.STONE).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SERVANT)!=null)
            assertTrue(deepEquals(numServant,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SERVANT).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.BLANK)!=null)
            assertTrue(deepEquals(0,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.BLANK).getAmount()));

        assertTrue(deepEquals(null,game.getCurrentPlayer().getExtra(FAITH)));

        ArrayList<Object> args1=new ArrayList<>();
        args1.add(new Resource(FinalResource.ResourceType.SERVANT,1));

        Packet packet1=new Packet(CHOOSE_RES,null,args1);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet1);
        });

        assertEquals(SELECTION_INVALID, e.getCode());


    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3 })
    void testSelectColumnButLeadersHasWrongAbility(int test){

        game.changeTurn();

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 2, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.BLUE, 1, 0));
        ArrayList<LeadCard> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_3_1", 5, cardRequirements, null, Ability.AbilityType.DEPOT, FinalResource.ResourceType.SERVANT, true);
        card.setupAbility();

        cards.add(card);

        game.getCurrentPlayer().setLeaders(cards);

        ArrayList<Object> args=new ArrayList<>();
        args.add(test);

        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(SELECT_COLUMN,null,args);


        ResourceMarket market = game.getResourceMarket();
        //I would like to select a row and shift that
        ArrayList<Resource> expected=new ArrayList<>();
        Resource ExtraMrblOld=new Resource(market.getExtraMarbleCopy().getType(),1);

        int incrementFaith=0;
        int numCoin=0;
        int numShield=0;
        int numStone=0;
        int numServant=0;

        for(int c=0;c< market.ROWS;c++)
            expected.add(new Resource(market.getGridCopy()[c][test].getType(),1));

        for(int i=0;i<expected.size();i++){
            switch (expected.get(i).getType()){
                case FAITH:
                    incrementFaith++;
                    break;

                case COIN:
                    numCoin++;
                    break;

                case SHIELD:
                    numShield++;
                    break;

                case STONE:
                    numStone++;
                    break;

                case SERVANT:
                    numServant++;
                    break;

                case BLANK:
                    //IDontHaveLeaders
                    break;
            }
        }

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });

        for(int c=0;c<market.ROWS-1;c++)
            assertTrue(deepEquals(expected.get(c+1).getType(),game.getResourceMarket().getGridCopy()[c][test].getType()));

        assertTrue(deepEquals(ExtraMrblOld,game.getResourceMarket().getGridCopy()[market.ROWS-1][test]));
        assertTrue(deepEquals(expected.get(0).getType(),game.getResourceMarket().getExtraMarbleCopy().getType()));

        assertTrue(deepEquals(incrementFaith,game.getCurrentPlayer().getFaithPosition()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.COIN)!=null)
            assertTrue(deepEquals(numCoin,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.COIN).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SHIELD)!=null)
            assertTrue(deepEquals(numShield,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SHIELD).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.STONE)!=null)
            assertTrue(deepEquals(numStone,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.STONE).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SERVANT)!=null)
            assertTrue(deepEquals(numServant,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SERVANT).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.BLANK)!=null)
            assertTrue(deepEquals(0,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.BLANK).getAmount()));

        assertTrue(deepEquals(null,game.getCurrentPlayer().getExtra(FAITH)));

        ArrayList<Object> args1=new ArrayList<>();
        args1.add(new Resource(FinalResource.ResourceType.SERVANT,1));

        Packet packet1=new Packet(CHOOSE_RES,null,args1);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet1);
        });

        assertEquals(SELECTION_INVALID, e.getCode());


    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3 })
    void testSelectColumnButLeadersHasWrongResource(int test){

        game.changeTurn();

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 2, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.BLUE, 1, 0));
        ArrayList<LeadCard> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_3_1", 5, cardRequirements, null, Ability.AbilityType.RESOURCE, FinalResource.ResourceType.SERVANT, true);
        card.setupAbility();

        cards.add(card);

        game.getCurrentPlayer().setLeaders(cards);

        ArrayList<Object> args=new ArrayList<>();
        args.add(test);

        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(SELECT_COLUMN,null,args);


        ResourceMarket market = game.getResourceMarket();
        //I would like to select a row and shift that
        ArrayList<Resource> expected=new ArrayList<>();
        Resource ExtraMrblOld=new Resource(market.getExtraMarbleCopy().getType(),1);

        int incrementFaith=0;
        int numCoin=0;
        int numShield=0;
        int numStone=0;
        int numServant=0;

        for(int c=0;c< market.ROWS;c++)
            expected.add(new Resource(market.getGridCopy()[c][test].getType(),1));

        for(int i=0;i<expected.size();i++){
            switch (expected.get(i).getType()){
                case FAITH:
                    incrementFaith++;
                    break;

                case COIN:
                    numCoin++;
                    break;

                case SHIELD:
                    numShield++;
                    break;

                case STONE:
                    numStone++;
                    break;

                case SERVANT:
                    numServant++;
                    break;

                case BLANK:
                    //IDontHaveLeaders
                    break;
            }
        }

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });

        for(int c=0;c<market.ROWS-1;c++)
            assertTrue(deepEquals(expected.get(c+1).getType(),game.getResourceMarket().getGridCopy()[c][test].getType()));

        assertTrue(deepEquals(ExtraMrblOld,game.getResourceMarket().getGridCopy()[market.ROWS-1][test]));
        assertTrue(deepEquals(expected.get(0).getType(),game.getResourceMarket().getExtraMarbleCopy().getType()));

        assertTrue(deepEquals(incrementFaith,game.getCurrentPlayer().getFaithPosition()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.COIN)!=null)
            assertTrue(deepEquals(numCoin,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.COIN).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SHIELD)!=null)
            assertTrue(deepEquals(numShield,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SHIELD).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.STONE)!=null)
            assertTrue(deepEquals(numStone,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.STONE).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SERVANT)!=null)
            assertTrue(deepEquals(numServant,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SERVANT).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.BLANK)!=null)
            assertTrue(deepEquals(0,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.BLANK).getAmount()));

        assertTrue(deepEquals(null,game.getCurrentPlayer().getExtra(FAITH)));

        ArrayList<Object> args1=new ArrayList<>();
        args1.add(new Resource(FinalResource.ResourceType.COIN,1));

        Packet packet1=new Packet(CHOOSE_RES,null,args1);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet1);
        });

        assertEquals(SELECTION_INVALID, e.getCode());


    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3 })
    void testSelectColumnCorrectWithLeader(int test){

        game.changeTurn();

        ArrayList<CardResource> cardRequirements = new ArrayList<>();
        cardRequirements.add(new CardResource(DevCard.CardType.YELLOW, 2, 0));
        cardRequirements.add(new CardResource(DevCard.CardType.BLUE, 1, 0));
        ArrayList<LeadCard> cards = new ArrayList<>();

        LeadCard card = new LeadCard("leadercard_front_3_1", 5, cardRequirements, null, Ability.AbilityType.RESOURCE, FinalResource.ResourceType.SERVANT, true);
        card.setupAbility();

        cards.add(card);

        game.getCurrentPlayer().setLeaders(cards);

        ArrayList<Object> args=new ArrayList<>();
        args.add(test);

        Action action = new Action();
        action.setActionState(new ResourceMarket_ActionState(action), RES_MARKET);
        Packet packet=new Packet(SELECT_COLUMN,null,args);


        ResourceMarket market = game.getResourceMarket();
        //I would like to select a row and shift that
        ArrayList<Resource> expected=new ArrayList<>();
        Resource ExtraMrblOld=new Resource(market.getExtraMarbleCopy().getType(),1);

        int incrementFaith=0;
        int numCoin=0;
        int numShield=0;
        int numStone=0;
        int numServant=0;
        int numBlank=0;

        for(int c=0;c< market.ROWS;c++)
            expected.add(new Resource(market.getGridCopy()[c][test].getType(),1));

        for(int i=0;i<expected.size();i++){
            switch (expected.get(i).getType()){
                case FAITH:
                    incrementFaith++;
                    break;

                case COIN:
                    numCoin++;
                    break;

                case SHIELD:
                    numShield++;
                    break;

                case STONE:
                    numStone++;
                    break;

                case SERVANT:
                    numServant++;
                    break;

                case BLANK:
                    numBlank++;
                    break;
            }
        }

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });

        for(int c=0;c<market.ROWS-1;c++)
            assertTrue(deepEquals(expected.get(c+1).getType(),game.getResourceMarket().getGridCopy()[c][test].getType()));

        assertTrue(deepEquals(ExtraMrblOld,game.getResourceMarket().getGridCopy()[market.ROWS-1][test]));
        assertTrue(deepEquals(expected.get(0).getType(),game.getResourceMarket().getExtraMarbleCopy().getType()));

        assertTrue(deepEquals(incrementFaith,game.getCurrentPlayer().getFaithPosition()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.COIN)!=null)
            assertTrue(deepEquals(numCoin,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.COIN).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SHIELD)!=null)
            assertTrue(deepEquals(numShield,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SHIELD).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.STONE)!=null)
            assertTrue(deepEquals(numStone,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.STONE).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SERVANT)!=null)
            assertTrue(deepEquals(numServant,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SERVANT).getAmount()));

        if(game.getCurrentPlayer().getExtra(FinalResource.ResourceType.BLANK)!=null)
            assertTrue(deepEquals(0,game.getCurrentPlayer().getExtra(FinalResource.ResourceType.BLANK).getAmount()));

        assertTrue(deepEquals(null,game.getCurrentPlayer().getExtra(FAITH)));

        if(numBlank>0) {


            Packet packet2=new Packet(CONFIRM,null,new ArrayList<>());

            InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
                action.execute(game, packet2);
            });

            assertEquals(SELECTION_INVALID, e.getCode());





            ArrayList<Object> args1 = new ArrayList<>();
            args1.add(new Resource(FinalResource.ResourceType.SERVANT, 1));

            Packet packet1 = new Packet(CHOOSE_RES, null, args1);

            while(numBlank>0) {
                assertDoesNotThrow(() -> {
                    action.execute(game, packet1);
                });
                numServant++;

                assertTrue(deepEquals(numServant, game.getCurrentPlayer().getExtra(FinalResource.ResourceType.SERVANT).getAmount()));
                numBlank--;
            }

            Packet packet3=new Packet(CONFIRM,null,new ArrayList<>());

            assertDoesNotThrow(() -> {
                action.execute(game, packet3);
            });

        }else{
            ArrayList<Object> args1=new ArrayList<>();
            args1.add(new Resource(FinalResource.ResourceType.SERVANT,1));

            Packet packet1=new Packet(CHOOSE_RES,null,args1);

            InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
                action.execute(game, packet1);
            });

            assertEquals(SELECTION_INVALID, e.getCode());

            Packet packet2=new Packet(CONFIRM,null,new ArrayList<>());

            assertDoesNotThrow(() -> {
                action.execute(game, packet2);
            });


        }


    }

    @Test
    void execute() {
        assertTrue(true);
    }
}