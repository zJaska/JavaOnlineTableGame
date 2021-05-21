package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
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

class DiscardInitLeaders_ActionStateTest {

     Action  action=new Action();

     ArrayList<String> nicknames=new ArrayList<>();
     Game game;



    @BeforeEach
     void setupTest(){
        nicknames.add("1");
        nicknames.add("2");
        nicknames.add("3");
        nicknames.add("4");
        game=new Game(nicknames);
        action.setActionState(new DiscardInitLeaders_ActionState(action), DISCARD_INIT_LEAD);

    }

    @Test
    void nullPacket(){//Le instruction code e listobject
        InvalidArgumentsException e =assertThrows(InvalidArgumentsException.class,()->{action.execute(game,null);});
        assertEquals(PACKET_NULL,e.getCode());
    }


    @ParameterizedTest
    @EnumSource(value = Packet.InstructionCode.class, mode = EXCLUDE, names = {"DISCARD_LEAD"})
    void codeNotAllowed(Packet.InstructionCode p){
        Packet packet=new Packet(p,null,null);// For each test
        InvalidArgumentsException e= assertThrows(InvalidArgumentsException.class,()->{action.execute(game,packet );});
        assertEquals(ARGS_NULL,e.getCode());
    }

    @Test
    void nullAllArgumentPacket(){//Le instruction code e listobject
        Packet packet=new Packet(null,null,null);// For each test
        InvalidArgumentsException e =assertThrows(InvalidArgumentsException.class,()->{action.execute(game,packet );});
        assertEquals(CODE_NULL,e.getCode());
    }

    @Test
    void notEnoughArgs(){//Le instruction code e listobject
        ArrayList<Object> args= new ArrayList<>();
        Packet packet=new Packet(DISCARD_LEAD,null,args);// For each test
        InvalidArgumentsException e= assertThrows(InvalidArgumentsException.class,()->{action.execute(game,packet );});
        assertEquals(NOT_ENOUGH_ARGS,e.getCode());
    }

    @Test
    void tooManyArgs(){//Le instruction code e listobject
        ArrayList<Object> args= new ArrayList<>();
        args.add(new Resource(FinalResource.ResourceType.SHIELD,1));
        args.add(new Resource(FinalResource.ResourceType.STONE,1));
        args.add(new Resource(FinalResource.ResourceType.COIN,1));
        Packet packet=new Packet(DISCARD_LEAD,null,args);// For each test
        InvalidArgumentsException e= assertThrows(InvalidArgumentsException.class,()->{action.execute(game,packet );});
        assertEquals(TOO_MANY_ARGS,e.getCode());
    }

    @Test
    void typeMissMatch(){//Le instruction code e listobject
        ArrayList<Object> args= new ArrayList<>();
        args.add(5);
        Packet packet=new Packet(DISCARD_LEAD,null,args);// For each test
        InvalidArgumentsException e= assertThrows(InvalidArgumentsException.class,()->{action.execute(game,packet );});
        assertEquals(TYPE_MISMATCH,e.getCode());
    }

    @Test
    void TestCorrect(){//Le instruction code e listobject
        ArrayList<Object> args= new ArrayList<>();
        LeadCard discarded=game.getCurrentPlayer().getLeaders().get(0);

        args.add(discarded);
        Packet packet=new Packet(DISCARD_LEAD,null,args);// For each test


        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });

        assertTrue(deepEquals(null,game.getCurrentPlayer().getLeader(discarded)));

    }

    @Test
    void TestCardNotInHand(){//Le instruction code e listobject
        ArrayList<Object> args= new ArrayList<>();


        args.add(new LeadCard("NON ESISTE",100,null,null,null,null,false));
        Packet packet=new Packet(DISCARD_LEAD,null,args);// For each test


        InvalidArgumentsException e= assertThrows(InvalidArgumentsException.class,()->{action.execute(game,packet );});
        assertEquals(SELECTION_INVALID,e.getCode());


    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3})
    void TestCorrect2(int val){//Le instruction code e listobject
        ArrayList<Object> args= new ArrayList<>();
        LeadCard dis=game.getCurrentPlayer().getLeaders().get(val);

        args.add(dis);
        Packet packet=new Packet(DISCARD_LEAD,null,args);// For each test


        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });

        assertFalse(game.getCurrentPlayer().hasLeader(dis));


    }



    @Test
    void execute() {
        assert(true);
    }
}