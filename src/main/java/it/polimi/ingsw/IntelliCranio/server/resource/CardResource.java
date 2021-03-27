package it.polimi.ingsw.IntelliCranio.server.resource;

import it.polimi.ingsw.IntelliCranio.server.cards.DevCard;

public class CardResource {

    private DevCard.CardType type;
    private int amount;
    private int minLevelRequired;

    public CardResource(DevCard.CardType type, int amount, int minLevelRequired) {
        this.type = type;
        this.amount = amount;
        this.minLevelRequired = minLevelRequired;
    }

    public DevCard.CardType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public int getMinLevelRequired() {
        return minLevelRequired;
    }
}
