package it.polimi.ingsw.IntelliCranio.server.player;

import it.polimi.ingsw.IntelliCranio.server.resource.Resource;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

public class Warehouse {

    private ArrayList<Resource> depot = new ArrayList<>(3);

    public ArrayList<Resource> getDepot() {
        return depot;
    }

    public void swapLines(int first, int second) {
        throw new NotImplementedException();
    }

    public void addFromMarket(int first, int second, ArrayList<Resource> marketRes) {
        throw new NotImplementedException();
    }

    public void update() {
        throw new NotImplementedException();
    }
}
