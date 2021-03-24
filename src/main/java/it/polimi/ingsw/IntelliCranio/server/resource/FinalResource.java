package it.polimi.ingsw.IntelliCranio.server.resource;

public class FinalResource {

    public enum ResourceType {STONE, SHIELD, SERVANT, COIN, FAITH, BLANK}

    protected final ResourceType type;
    protected int amount;

    public FinalResource(ResourceType type, int amount) {
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
