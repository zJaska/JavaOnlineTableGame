package it.polimi.ingsw.IntelliCranio.models.cards;

import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;

import java.util.ArrayList;

public class DevCard extends Card {

    public enum CardType { GREEN, BLUE, YELLOW, PURPLE }

    private CardType type;
    private int level;

    private ArrayList<FinalResource> cardCost;
    private ArrayList<FinalResource> productionCost;
    private ArrayList<FinalResource> product;


    public DevCard(String ID, int vp, CardType type, int level, ArrayList<FinalResource> cardCost,
                   ArrayList<FinalResource> productionCost, ArrayList<FinalResource> product) {
        this.ID = ID;
        this.vp = vp;
        this.type = type;
        this.level = level;
        this.cardCost = cardCost;
        this.productionCost = productionCost;
        this.product = product;
    }

    public CardType getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public ArrayList<FinalResource> getCardCost() {
        return cardCost;
    }

    public ArrayList<FinalResource> getProductionCost() {
        return productionCost;
    }

    public ArrayList<FinalResource> getProduct() {
        return product;
    }

    /**
     * Creates a single string of the card from its fields
     * @return A string with all the data of the card
     */
    @Override
    public String toString() {
        return "DevCard {" +
                "vp=" + vp +
                ", type=" + type +
                ", level=" + level +
                ", cardCost=[ " + cardCost.stream().map(FinalResource::toString).reduce("", (x,y) -> x + " " + y) + "]" +
                ", productionCost=" + productionCost.stream().map(FinalResource::toString).reduce("", (x,y) -> x + " " + y) + "]" +
                ", product=" + product.stream().map(FinalResource::toString).reduce("", (x,y) -> x + " " + y) + "]" +
                " }";
    }

    public DevCard getCopy() {
        return new DevCard(getID(), getVictoryPoints(), getType(), getLevel(),
                (ArrayList<FinalResource>) cardCost.clone(),
                (ArrayList<FinalResource>) productionCost.clone(),
                (ArrayList<FinalResource>) product.clone());
    }
}
