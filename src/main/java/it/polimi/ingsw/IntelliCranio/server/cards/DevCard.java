package it.polimi.ingsw.IntelliCranio.server.cards;

import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;

import java.util.ArrayList;

public class DevCard extends Card {

    public enum CardType { GREEN, YELLOW, PURPLE, BLUE}

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

    @Override
    public int getVictoryPoints() {
        return vp;
    }

    @Override
    public String getID() {
        return ID;
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

}
