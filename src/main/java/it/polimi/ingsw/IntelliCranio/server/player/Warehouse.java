package it.polimi.ingsw.IntelliCranio.server.player;

import it.polimi.ingsw.IntelliCranio.server.resource.Resource;

import java.util.ArrayList;

public class Warehouse {

    private ArrayList<Resource> depot;

    /**
     * It's the constructor of Warehouse class
     * @param dim the dimension of the depot
     */
    public Warehouse(int dim){
        depot=new ArrayList<>(dim);
    }


    public ArrayList<Resource> getDepot() {
        return depot;
    }

    public void swapLines(int first, int second) {
        throw new UnsupportedOperationException();
    }//Maybe useless because it's a client method.

    public void addFromMarket(int first, int second, ArrayList<Resource> marketRes) {
        throw new UnsupportedOperationException();
    }//Maybe useless because it's a client method.

    /**
     *
     * <summary>
     *  check number of cards discarded and update and
     *  check if there are 2 items on different lines.
     * </summary>
     * @param tempDepot this is sent by client
     * @param marketRes this is sent by client which is left from market.
     * @return -1 if error else number of cards discarded.
     */
    public int update(ArrayList<Resource> tempDepot, ArrayList<Resource> marketRes) {
        int numDiscards=0;

            for(int i=0;i<tempDepot.size()-1;++i)
                for(int j=i+1;j<tempDepot.size();++j)
                    if(tempDepot.get(i).getType()==tempDepot.get(j).getType())
                        return -1;


            for(int i=0;i<tempDepot.size();++i)
            {
                if( tempDepot.get(i).getAmount() > (i+1) ) {
                    numDiscards += tempDepot.get(i).getAmount() - (i + 1);
                    tempDepot.get(i).setAmount(i+1);
                }

            }

            depot=tempDepot;

            if(marketRes!=null)
                for(int i=0;i<marketRes.size();++i)
                    numDiscards+=marketRes.get(i).getAmount();


        return numDiscards;
    }
}
