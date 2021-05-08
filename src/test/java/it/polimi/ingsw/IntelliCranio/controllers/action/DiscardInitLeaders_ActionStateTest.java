package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;


import java.util.ArrayList;


import static it.polimi.ingsw.IntelliCranio.network.Packet.InstructionCode.*;
import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

class DiscardInitLeaders_ActionStateTest {

    static Action  action=new Action();

    static ArrayList<String> nicknames=new ArrayList<>();
    static Game game;



    @BeforeAll
    static void setupTest(){
        nicknames.add("1");
        nicknames.add("2");
        nicknames.add("3");
        nicknames.add("4");
        game=new Game(nicknames);
        action.setActionState(new ChooseInitResources_ActionState(action), DISCARD_INIT_LEAD);

    }

    @Test
    void nullPacket(){//Le instruction code e listobject
        InvalidArgumentsException e =assertThrows(InvalidArgumentsException.class,()->{action.execute(game,null);});
        assertEquals(CODE_NULL,e.getCode());
    }


    @ParameterizedTest
    @EnumSource(value = Packet.InstructionCode.class, mode = EXCLUDE, names = {"CHOOSE_RES"})
    void codeNotAllowed(Packet.InstructionCode p){
        Packet packet=new Packet(p,null,null);// For each test
        InvalidArgumentsException e= assertThrows(InvalidArgumentsException.class,()->{action.execute(game,packet );});
        assertEquals(CODE_NOT_ALLOWED,e.getCode());
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
        Packet packet=new Packet(CHOOSE_RES,null,args);// For each test
        InvalidArgumentsException e= assertThrows(InvalidArgumentsException.class,()->{action.execute(game,packet );});
        assertEquals(NOT_ENOUGH_ARGS,e.getCode());
    }

    @Test
    void tooManyArgs(){//Le instruction code e listobject
        ArrayList<Object> args= new ArrayList<>();
        args.add(new Resource(FinalResource.ResourceType.SHIELD,1));
        args.add(new Resource(FinalResource.ResourceType.STONE,1));
        args.add(new Resource(FinalResource.ResourceType.COIN,1));
        Packet packet=new Packet(CHOOSE_RES,null,args);// For each test
        InvalidArgumentsException e= assertThrows(InvalidArgumentsException.class,()->{action.execute(game,packet );});
        assertEquals(TOO_MANY_ARGS,e.getCode());
    }

    @Test
    void typeMissMatch(){//Le instruction code e listobject
        ArrayList<Object> args= new ArrayList<>();
        args.add(5);
        Packet packet=new Packet(CHOOSE_RES,null,args);// For each test
        InvalidArgumentsException e= assertThrows(InvalidArgumentsException.class,()->{action.execute(game,packet );});
        assertEquals(TYPE_MISMATCH,e.getCode());
    }

    @Test
    void execute() {
        assert(true);
    }
}