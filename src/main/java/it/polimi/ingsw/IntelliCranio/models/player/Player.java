package it.polimi.ingsw.IntelliCranio.models.player;

import it.polimi.ingsw.IntelliCranio.models.cards.DevCard;
import it.polimi.ingsw.IntelliCranio.models.cards.LeadCard;
import it.polimi.ingsw.IntelliCranio.models.cards.PopeCard;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Player {

    private String nickname;
    private int faithPosition;

    private Warehouse warehouse;
    private Strongbox strongbox;

    private ArrayList<Resource> extraRes;

    private ArrayList<DevCard> firstSlot;
    private ArrayList<DevCard> secondSlot;
    private ArrayList<DevCard> thirdSlot;
    private ArrayList<LeadCard> leaders;
    private ArrayList<PopeCard> popeCards;

    private boolean hasPlayed;

    public Player() {

    }

    public Player(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public void setLeaders(ArrayList<LeadCard> cards) {
        leaders = cards;
    }

    public void setWarehouse(Warehouse warehouse){
        this.warehouse = warehouse;
    }

    public ArrayList<LeadCard> getLeaders() {
        return leaders;
    }

    public Strongbox getStrongbox() {
        return strongbox;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public ArrayList<Resource> getExtraRes() {
        return extraRes;
    }

    public void incrementFaith() {
        faithPosition++;
    }

    public int getFaithPosition() {
        return faithPosition;
    }

    //Todo
    public PopeCard getPopeCard(int index) {
        throw new UnsupportedOperationException();
    }

    public ArrayList<DevCard> getAllDevCards() {
        ArrayList<DevCard> temp = new ArrayList<>();

        temp.addAll(firstSlot);
        temp.addAll(secondSlot);
        temp.addAll(thirdSlot);

        return temp;
    }

}
