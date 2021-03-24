package it.polimi.ingsw.IntelliCranio.server.market;

import it.polimi.ingsw.IntelliCranio.server.cards.DevCard;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

public class CardMarket {

    private ArrayList<DevCard>[][] marketGrid = new ArrayList[3][4];

    public void setup() {
        throw new NotImplementedException();
    }

    public void shuffle() {
        throw new NotImplementedException();
    }

    public DevCard getCard(int row, int col) {
        throw new NotImplementedException();
    }

    public void removeCard(int row, int col) {
        throw new NotImplementedException();
    }
}
