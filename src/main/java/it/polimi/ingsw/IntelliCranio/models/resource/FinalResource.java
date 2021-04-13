package it.polimi.ingsw.IntelliCranio.models.resource;

public class FinalResource {

    public enum ResourceType {STONE, SHIELD, SERVANT, COIN, FAITH, BLANK}

    protected final ResourceType type;
    protected int amount;

    public FinalResource(ResourceType type, int amount) {
        if (amount < 0)
            throw new IllegalArgumentException();
        this.type = type;
        this.amount = amount;
    }

    public ResourceType getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }
}
