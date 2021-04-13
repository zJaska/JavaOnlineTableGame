package it.polimi.ingsw.IntelliCranio.models.cards;

public abstract class Card {

    protected int vp;
    protected String ID;

    public int getVictoryPoints() {
        return vp;
    }
    public String getID() {
        return ID;
    }

}
