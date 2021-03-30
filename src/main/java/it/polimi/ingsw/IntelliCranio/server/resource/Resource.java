package it.polimi.ingsw.IntelliCranio.server.resource;

public class Resource extends FinalResource {

    public Resource(ResourceType type, int amount) {
        super(type, amount);
    }

    public int addAmount(int amount) {
        this.amount += amount;
        return amount;
    }

    /**
     * This method doesn't remove any resource if the new amount would be less than 0
     *
     * @return  The modified amount if it's >= 0, otherwise -1
     */
    public int removeAmount(int amount) {
        if (this.amount - amount >= 0)
            this.amount -= amount;
        else
            return -1;
        return this.amount;
    }

    public void setAmount(int amount) { this.amount=amount; }
}
