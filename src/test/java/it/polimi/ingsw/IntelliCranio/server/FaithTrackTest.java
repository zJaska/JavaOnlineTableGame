package it.polimi.ingsw.IntelliCranio.server;

import it.polimi.ingsw.IntelliCranio.models.FaithTrack;
import it.polimi.ingsw.IntelliCranio.models.Game;
import it.polimi.ingsw.IntelliCranio.models.cards.PopeCard;
import org.junit.Test;

import java.util.ArrayList;

import static java.util.Objects.deepEquals;
import static org.junit.Assert.*;

public class FaithTrackTest {

    @Test
    public void isXSectionOrHigher() {
        FaithTrack faith = new FaithTrack("src/main/resources/faithtrack_config.json");
        assertFalse(faith.isXSectionOrHigher(0,4));
        assertTrue(faith.isXSectionOrHigher(0,5));
        assertTrue(faith.isXSectionOrHigher(0,8));
        assertTrue(faith.isXSectionOrHigher(1,12));
        assertTrue(faith.isXSectionOrHigher(1,15));
        assertTrue(faith.isXSectionOrHigher(2,19));
        assertFalse(faith.isXSectionOrHigher(-1,-1));
        assertFalse(faith.isXSectionOrHigher(-2,6));
        assertFalse(faith.isXSectionOrHigher(-23,-6));
    }

    @Test
    public void testCheck1(){
        FaithTrack faith = new FaithTrack("src/main/resources/faithtrack_config.json");
        ArrayList<String> nicknames = new ArrayList<>();
        Game game;

        nicknames.add("1");
        nicknames.add("2");
        nicknames.add("3");
        nicknames.add("4");
        game = new Game(nicknames);

        for(int i=0;i<8;i++)
            game.getCurrentPlayer().addFaith();

        assertTrue(deepEquals(PopeCard.Status.INACTIVE,game.getCurrentPlayer().getPopeCards().get(0).getStatus()));

        faith.checkStatus(game,game.getCurrentPlayer().getFaithPosition());

        assertTrue(deepEquals(PopeCard.Status.ACTIVE,game.getCurrentPlayer().getPopeCards().get(0).getStatus()));

        game.changeTurn();

        assertTrue(deepEquals(PopeCard.Status.REMOVED,game.getCurrentPlayer().getPopeCards().get(0).getStatus()));

        game.changeTurn();

        assertTrue(deepEquals(PopeCard.Status.REMOVED,game.getCurrentPlayer().getPopeCards().get(0).getStatus()));

        game.changeTurn();

        assertTrue(deepEquals(PopeCard.Status.REMOVED,game.getCurrentPlayer().getPopeCards().get(0).getStatus()));

        game.changeTurn();


    }

    @Test
    public void testCheck2(){
        FaithTrack faith = new FaithTrack("src/main/resources/faithtrack_config.json");
        ArrayList<String> nicknames = new ArrayList<>();
        Game game;

        nicknames.add("1");
        nicknames.add("2");
        nicknames.add("3");
        nicknames.add("4");
        game = new Game(nicknames);

        game.changeTurn();//Second
        for(int i=0;i<5;i++)
            game.getCurrentPlayer().addFaith();

        game.changeTurn();//Third
        for(int i=0;i<6;i++)
            game.getCurrentPlayer().addFaith();

        game.changeTurn();//Fourth

        game.changeTurn();//First


        for(int i=0;i<8;i++)
            game.getCurrentPlayer().addFaith();

        assertTrue(deepEquals(PopeCard.Status.INACTIVE,game.getCurrentPlayer().getPopeCards().get(0).getStatus()));

        faith.checkStatus(game,game.getCurrentPlayer().getFaithPosition());

        assertTrue(deepEquals(PopeCard.Status.ACTIVE,game.getCurrentPlayer().getPopeCards().get(0).getStatus()));

        game.changeTurn();//second

        assertTrue(deepEquals(PopeCard.Status.ACTIVE,game.getCurrentPlayer().getPopeCards().get(0).getStatus()));

        game.changeTurn();//third

        assertTrue(deepEquals(PopeCard.Status.ACTIVE,game.getCurrentPlayer().getPopeCards().get(0).getStatus()));

        game.changeTurn();//fourth

        assertTrue(deepEquals(PopeCard.Status.REMOVED,game.getCurrentPlayer().getPopeCards().get(0).getStatus()));


    }

    @Test
    public void endGame(){
        FaithTrack faith = new FaithTrack("src/main/resources/faithtrack_config.json");
        ArrayList<String> nicknames = new ArrayList<>();
        Game game;

        nicknames.add("1");
        nicknames.add("2");
        nicknames.add("3");
        nicknames.add("4");
        game = new Game(nicknames);

        for(int i=0;i<24;i++)
            game.getCurrentPlayer().addFaith();

        faith.checkStatus(game,game.getCurrentPlayer().getFaithPosition());

        assertTrue(game.isGameEnded());
    }

    //Controlla posizione e attiva varie cose
}