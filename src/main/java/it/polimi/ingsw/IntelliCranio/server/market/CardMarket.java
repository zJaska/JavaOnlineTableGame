package it.polimi.ingsw.IntelliCranio.server.market;

import it.polimi.ingsw.IntelliCranio.server.cards.DevCard;

import java.util.ArrayList;

public class CardMarket {

    private ArrayList<DevCard>[][] marketGrid = new ArrayList[3][4];

    public void setup() {
    }

    public void shuffle() {
        throw new UnsupportedOperationException();
    }

    public DevCard getCard(int row, int col) {
        return marketGrid[row][col].get(0);
    }

    public void removeCard(int row, int col) {
        marketGrid[row][col].remove(0);
    }
}
