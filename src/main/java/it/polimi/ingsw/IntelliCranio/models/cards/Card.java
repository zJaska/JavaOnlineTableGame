package it.polimi.ingsw.IntelliCranio.models.cards;

import java.io.Serializable;

public abstract class Card implements Serializable {

    protected int vp;
    protected String ID;

    public int getVictoryPoints() {
        return vp;
    }
    public String getID() {
        return ID;
    }

}
