package it.polimi.ingsw.IntelliCranio.server.player;

import it.polimi.ingsw.IntelliCranio.server.resource.Resource;

import java.util.ArrayList;

public class Warehouse {

    private Resource[] depot;
    private int dim;

    /**
     * It's the constructor of Warehouse class
     * @param dim the dimension of the depot
     */
    public Warehouse(int dim){
        this.dim = dim;
        depot = new Resource[dim];
    }


    public Resource[] getDepot() {
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
     *  check number of resources discarded and update and
     *  check if there are 2 items on different lines.
     * </summary>
     * @param tempDepot this is sent by client
     * @param extraRes this is sent by client which is left from market.
     * @return -1 if error else number of cards discarded.
     */
    public int update(Resource[] tempDepot, ArrayList<Resource> extraRes) {
        int numDiscards=0;

        if(tempDepot.length > dim)
            return -1;

        for(int i = 0; i < tempDepot.length - 1; ++i)
            for(int j = i + 1; j < tempDepot.length; ++j)
                if(tempDepot[i] != null && tempDepot[j] != null)
                    if(tempDepot[i].getType() == tempDepot[j].getType())
                        return -1;


        for(int i = 0; i < tempDepot.length; ++i)
            if(tempDepot[i] != null)
                if( tempDepot[i].getAmount() > (i + 1) ) {
                    numDiscards += tempDepot[i].getAmount() - (i + 1);
                    tempDepot[i].setAmount(i+1);
                }

        depot = tempDepot;

        if(extraRes!=null)
            for(int i=0;i<extraRes.size();++i)
                numDiscards+=extraRes.get(i).getAmount();


        return numDiscards;
    }

    /**
     * This is a getter of dim
     * @return value of dim
     */
    public int getDim(){ return dim;}
}
