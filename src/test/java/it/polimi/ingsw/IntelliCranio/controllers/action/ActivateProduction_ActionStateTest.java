package it.polimi.ingsw.IntelliCranio.controllers.action;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import it.polimi.ingsw.IntelliCranio.util.Save;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.ArrayList;

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
    void TestSelectCard(){

    }


    @Test
    void execute() {
        assertTrue(true);
    }
}