package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
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

class ChooseInitResources_ActionStateTest {
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
        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, null);
        });
        assertEquals(PACKET_NULL, e.getCode());
    }


    @ParameterizedTest
    @EnumSource(value = Packet.InstructionCode.class, mode = EXCLUDE, names = {"CHOOSE_RES"})
    void ArgsNull(Packet.InstructionCode p) {
        Packet packet = new Packet(p, null, null);// For each test
        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(ARGS_NULL, e.getCode());
    }

    @Test
    void CodeNull() {//Le instruction code e listobject
        Packet packet = new Packet(null, null, null);// For each test
        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(CODE_NULL, e.getCode());
    }


    @Test
    void notEnoughArgs() {//Le instruction code e listobject
        ArrayList<Object> args = new ArrayList<>();
        Packet packet = new Packet(CHOOSE_RES, null, args);// For each test
        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(NOT_ENOUGH_ARGS, e.getCode());
    }

    @Test
    void tooManyArgs() {//Le instruction code e listobject
        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(FinalResource.ResourceType.SHIELD, 1));
        args.add(new Resource(FinalResource.ResourceType.STONE, 1));
        args.add(new Resource(FinalResource.ResourceType.COIN, 1));
        Packet packet = new Packet(CHOOSE_RES, null, args);// For each test
        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(TOO_MANY_ARGS, e.getCode());
    }

    @Test
    void typeMissMatch() {//Le instruction code e listobject
        ArrayList<Object> args = new ArrayList<>();
        args.add(5);
        Packet packet = new Packet(CHOOSE_RES, null, args);// For each test
        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(TYPE_MISMATCH, e.getCode());
    }

    @Test
     void OneArgumentWithCorrectInstructionCodeButImFirstPlayer() {//Blank e faith attenzione
        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(FinalResource.ResourceType.SHIELD, 1));
        Packet packet = new Packet(CHOOSE_RES, null, args);// For each test

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });

        ArrayList<Resource> expectedExtra = new ArrayList<>();
        expectedExtra.add(new Resource(FinalResource.ResourceType.SHIELD, 1));

        assertTrue(deepEquals(0, game.getCurrentPlayer().getExtraRes().size()));


    }

    @Test
    void OneArgumentWithCorrectInstructionCodeButImNotFirstPlayer() {//Blank e faith attenzione
        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(FinalResource.ResourceType.SHIELD, 1));
        Packet packet = new Packet(CHOOSE_RES, null, args);// For each test

        game.changeTurn();

        assertDoesNotThrow(() -> {
            action.execute(game, packet);
        });

        ArrayList<Resource> expectedExtra = new ArrayList<>();
        expectedExtra.add(new Resource(FinalResource.ResourceType.SHIELD, 1));

        assertTrue(deepEquals(expectedExtra.get(0), game.getCurrentPlayer().getExtraRes().get(0)));


    }

    @ParameterizedTest
    @EnumSource(value = FinalResource.ResourceType.class, mode = EXCLUDE, names = {"STONE", "SHIELD", "SERVANT", "COIN"})
     void OneArgumentWithCorrectInstructionCodeButWrongResource(FinalResource.ResourceType p) {//Blank e faith attenzione
        ArrayList<Object> args = new ArrayList<>();
        args.add(new Resource(p, 1));
        Packet packet = new Packet(CHOOSE_RES, null, args);// For each test

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            action.execute(game, packet);
        });
        assertEquals(SELECTION_INVALID, e.getCode());


    }

    @Test
    void execute() {
        assertTrue(true);
    }
}