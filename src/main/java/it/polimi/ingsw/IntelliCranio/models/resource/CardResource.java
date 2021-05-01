package it.polimi.ingsw.IntelliCranio.models.resource;

import it.polimi.ingsw.IntelliCranio.models.cards.DevCard.CardType;

import java.io.Serializable;

public class CardResource implements Serializable {

    private CardType type;
    private int amount;
    private int level;

    public CardResource(CardType type, int amount, int level) {
        this.type = type;
        this.amount = amount;
        this.level = level;
    }

    public CardType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public void addAmount(int amount) { this.amount += amount; }

    public int getLevel() {
        return level;
    }
}
