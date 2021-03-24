package it.polimi.ingsw.IntelliCranio.server;

import it.polimi.ingsw.IntelliCranio.server.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.server.market.CardMarket;
import it.polimi.ingsw.IntelliCranio.server.market.ResourceMarket;
import it.polimi.ingsw.IntelliCranio.server.player.Player;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

public class GameManager implements Runnable{

    private FaithTrack faithTrack;
    private CardMarket cardMarket;
    private ResourceMarket resourceMarket;
    private ArrayList<Player> players;
    private ArrayList<LeadCard> leaders;

    @Override
    public void run() {
        throw new NotImplementedException();
    }

    private void createLeaderCards() {
        throw new NotImplementedException();
    }

    private void assignCards() {
        throw new NotImplementedException();
    }

    private void shufflePlayers() {
        throw new NotImplementedException();
    }

    private void playerSetup() {
        throw new NotImplementedException();
    }

    private void playGame() {
        throw new NotImplementedException();
    }

    private void endingGame() {
        throw new NotImplementedException();
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public CardMarket getCardMarket() {
        return cardMarket;
    }

    public ResourceMarket getResourceMarket() {
        return resourceMarket;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

}
