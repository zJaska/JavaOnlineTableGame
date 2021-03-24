package it.polimi.ingsw.IntelliCranio.server.cards;

public abstract class Card {

    protected int vp;
    protected String ID;

    public abstract int getVictoryPoints();
    public abstract String getID();

}
