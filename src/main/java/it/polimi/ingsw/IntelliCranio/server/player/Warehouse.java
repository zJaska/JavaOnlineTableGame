package it.polimi.ingsw.IntelliCranio.server.player;

import it.polimi.ingsw.IntelliCranio.server.resource.Resource;

import java.util.ArrayList;

public class Warehouse {

    private ArrayList<Resource> depot = new ArrayList<>(3);

    public ArrayList<Resource> getDepot() {
        return depot;
    }

    public void swapLines(int first, int second) {
        throw new UnsupportedOperationException();
    }//Maybe useless because it's a client method.

    public void addFromMarket(int first, int second, ArrayList<Resource> marketRes) {
        throw new UnsupportedOperationException();
    }//Maybe useless because it's a client method.

    //check number of cards discarded and update
    public int update(ArrayList<Resource> tempDepot, ArrayList<Resource> marketRes) {
        int numDiscards=0;

            for(int i=0;i<tempDepot.size();++i)
            {
                if( tempDepot.get(i).getAmount() > (i+1) ) {
                    numDiscards += tempDepot.get(i).getAmount() - (i + 1);
                    tempDepot.get(i).setAmount(i+1);
                }

            }

            depot=tempDepot;

            for(int i=0;i<marketRes.size();++i){
                numDiscards+=marketRes.get(i).getAmount();
            }

        return numDiscards;
    }
}
