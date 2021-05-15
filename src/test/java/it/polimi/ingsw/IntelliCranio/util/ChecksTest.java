package it.polimi.ingsw.IntelliCranio.util;

import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.network.Packet;
import it.polimi.ingsw.IntelliCranio.server.exceptions.InvalidArgumentsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static it.polimi.ingsw.IntelliCranio.network.Packet.Response.*;
import static org.junit.jupiter.api.Assertions.*;

class ChecksTest {

    @Test
    void packetCheckNULL() {
        Packet packet=null;

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            Checks.packetCheck(packet);
        });

        assertEquals(PACKET_NULL, e.getCode());
    }

    @Test
    void packetCheckARGNULL() {
        Packet packet=new Packet(Packet.InstructionCode.PLAY_LEADER,null,null);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            Checks.packetCheck(packet);
        });

        assertEquals(NULL_ARG, e.getCode());
    }

    @Test
    void packetCheckInstructionCodeNull() {
        Packet packet=new Packet(null,null,new ArrayList<>());

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            Checks.packetCheck(packet);
        });

        assertEquals(CODE_NULL, e.getCode());
    }

    @Test
    void packetCheckCorrect() {
        Packet packet=new Packet(Packet.InstructionCode.PLAY_LEADER,null,new ArrayList<>());

        assertDoesNotThrow( () -> {
            Checks.packetCheck(packet);
        });

    }


    @Test
    void argsAmountLESS() {
        ArrayList<Object> arg=new ArrayList<>();
        arg.add(1);


        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            Checks.argsAmount(arg,2);
        });

        assertEquals(NOT_ENOUGH_ARGS, e.getCode());
    }

    @Test
    void argsAmountToMany() {
        ArrayList<Object> arg=new ArrayList<>();
        arg.add(1);
        arg.add(2);


        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            Checks.argsAmount(arg,1);
        });

        assertEquals(TOO_MANY_ARGS, e.getCode());
    }

    @Test
    void argsAmountCorrect() {
        ArrayList<Object> arg=new ArrayList<>();
        arg.add(1);
        arg.add(2);


        assertDoesNotThrow(() -> {
            Checks.argsAmount(arg,2);
        });

    }

    @Test
    void sameLineError() {
        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            Checks.sameLine(1,1);
        });

        assertEquals(SELECTION_INVALID, e.getCode());
    }

    @Test
    void sameLineCorrect() {
        assertDoesNotThrow(() -> {
            Checks.sameLine(1,2);
        });


    }

    @Test
    void negativeValue() {
    }

    @Test
    void overMaxValue() {
    }

    @Test
    void overSizeLine() {
    }

    @Test
    void nullElement() {
    }

    @Test
    void extraEmpty() {
        ArrayList<String> nicknames = new ArrayList<>();
        Game game;
        
            nicknames.add("1");
            nicknames.add("2");
            nicknames.add("3");
            nicknames.add("4");
            game = new Game(nicknames);
            Save.saveGame(game);

            game.getCurrentPlayer().addExtra(FinalResource.ResourceType.COIN,1);

            assertDoesNotThrow(() ->{
                Checks.extraEmpty(game.getCurrentPlayer());}
            );


        
    }

    @Test
    void extraEmpty2() {
        ArrayList<String> nicknames = new ArrayList<>();
        Game game;

        nicknames.add("1");
        nicknames.add("2");
        nicknames.add("3");
        nicknames.add("4");
        game = new Game(nicknames);
        Save.saveGame(game);

        //game.getCurrentPlayer().addExtra(FinalResource.ResourceType.COIN,1);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            Checks.extraEmpty(game.getCurrentPlayer());
        });

        assertEquals(SELECTION_INVALID, e.getCode());



    }

    @Test
    void notInExtra1() {
        ArrayList<String> nicknames = new ArrayList<>();
        Game game;

        nicknames.add("1");
        nicknames.add("2");
        nicknames.add("3");
        nicknames.add("4");
        game = new Game(nicknames);
        Save.saveGame(game);

        game.getCurrentPlayer().addExtra(FinalResource.ResourceType.COIN,1);

        assertDoesNotThrow(() ->{
            Checks.notInExtra(game.getCurrentPlayer(), FinalResource.ResourceType.COIN);}
        );
    }

    @Test
    void notInExtra2(){
        ArrayList<String> nicknames = new ArrayList<>();
        Game game;

        nicknames.add("1");
        nicknames.add("2");
        nicknames.add("3");
        nicknames.add("4");
        game = new Game(nicknames);
        Save.saveGame(game);

        game.getCurrentPlayer().addExtra(FinalResource.ResourceType.COIN,1);

        InvalidArgumentsException e = assertThrows(InvalidArgumentsException.class, () -> {
            Checks.notInExtra(game.getCurrentPlayer(), FinalResource.ResourceType.STONE);
        });

        assertEquals(SELECTION_INVALID, e.getCode());


    }

    @Test
    void notSameResource() {
    }

    @Test
    void resInDifferentLine() {
    }

    @Test
    void depotFull() {
    }

    @Test
    void depotEmpty() {
    }

    @Test
    void notInHand() {
    }

    @Test
    void testNotInHand() {
    }

    @Test
    void testNotInHand1() {
    }

    @Test
    void testNotInHand2() {
    }

    @Test
    void invalidAbility() {
    }

    @Test
    void cardInactive() {
    }

    @Test
    void cardDepotFull() {
    }

    @Test
    void cardDepotEmpty() {
    }

    @Test
    void invalidResource() {
    }

    @Test
    void invalidAmount() {
    }

    @Test
    void invalidState() {
    }

    @Test
    void resourceRequirements() {
    }

    @Test
    void cardRequirements() {
    }

    @Test
    void cardActive() {
    }

    @Test
    void hasPlayed() {
    }

    @Test
    void hasSelected() {
    }

    @Test
    void notSelected() {
    }

    @Test
    void noBlanks() {
    }

    @Test
    void hasBlanks() {
    }

    @Test
    void blankOrFaith() {
    }

    @Test
    void slotEmpty() {
    }

    @Test
    void strongboxEmpty() {
    }

    @Test
    void invalidProdCostResources() {
    }

    @Test
    void invalidCardCostResources() {
    }

    @Test
    void cardMarketEmpty() {
    }

    @Test
    void invalidLevel() {
    }

    @Test
    void alreadyConfirmed() {
    }

    @Test
    void notConfirmed() {
    }

    @Test
    void cardSelected() {
    }

    @Test
    void cardNotSelected() {
    }

    @Test
    void invalidSlot() {
    }
}