package it.polimi.ingsw.IntelliCranio.server.player;

import it.polimi.ingsw.IntelliCranio.server.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.server.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.server.cards.PopeCard;

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


    public void setLeaders(ArrayList<LeadCard> cards) {
        throw new UnsupportedOperationException();
    }

    public void addFaith(int amount) {
        throw new UnsupportedOperationException();
    }

    public int getFaithPosition() {
        return faithPosition;
    }

    public PopeCard getPopeCard(int index) {
        throw new UnsupportedOperationException();
    }

}
