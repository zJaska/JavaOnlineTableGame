package it.polimi.ingsw.IntelliCranio.server.resource;

public class Resource extends FinalResource {

    public Resource(ResourceType type, int amount) {
        super(type, amount);
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    public void removeAmount(int amount) {
        this.amount -= amount;
    }

    public void setAmount(int amount) { this.amount=amount; }
}
