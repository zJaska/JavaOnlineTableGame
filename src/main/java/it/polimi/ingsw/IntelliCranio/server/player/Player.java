package it.polimi.ingsw.IntelliCranio.server.player;

import it.polimi.ingsw.IntelliCranio.server.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.server.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.server.cards.PopeCard;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

public class Player {

    private String nickname;
    private int faithPosition;

    private Warehouse warehouse;
    private Strongbox strongbox;

    private ArrayList<DevCard> firstSlot;
    private ArrayList<DevCard> secondSlot;
    private ArrayList<DevCard> thirdSlot;
    private ArrayList<LeadCard> leaders;
    private ArrayList<PopeCard> popeCards;

    private boolean hasPlayed;


    public void addLeaders(ArrayList<LeadCard> cards) {
        throw new NotImplementedException();
    }

    public void addFaith(int amount) {
        throw new NotImplementedException();
    }

    public int getFaithPosition() {
        return faithPosition;
    }

    public PopeCard getPopeCard(int index) {
        throw new NotImplementedException();
    }

}
