package it.polimi.ingsw.IntelliCranio.models.market;

import it.polimi.ingsw.IntelliCranio.util.Lists;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;

import java.util.*;

public class ResourceMarket {

    public final int ROWS;
    public final int COLUMNS;

    private FinalResource extraMarble;
    private FinalResource[][] marbleGrid;

    
    public ResourceMarket(int rows, int columns) {
        marbleGrid = new FinalResource[rows][columns];
        ROWS = rows;
        COLUMNS = columns;
    }

    /**
     * This method generate a my resource market.
     * @param resourcesParam
     */
    public void setup(ArrayList<Resource> resourcesParam) {
        ArrayList<Resource> resources = new ArrayList<>();
        resourcesParam.forEach(res -> resources.add(new Resource(res.getType(),res.getAmount())));

        if (ROWS * COLUMNS > resources.stream().map(FinalResource::getAmount).reduce(Integer::sum).get() - 1)
            throw new IllegalArgumentException();

        // Assigning random resources to the grid
        for (int r=0; r<ROWS; r++) {
            for (int c=0; c<COLUMNS; c++) {
                int random = (int)(Math.random() * resources.size());
                marbleGrid[r][c] = new FinalResource(resources.get(random).getType(),1);
                if (resources.get(random).removeAmount(1) == 0)
                    resources.remove(random);
            }
        }

        extraMarble = resources.get(0);
    }

    /**
     *  This method return a row of my resourceMarket
     * @param row
     * @return a row of resource market
     */
    public ArrayList<FinalResource> selectRow(int row) {
        return Lists.unifyResourceAmounts(Arrays.asList(marbleGrid[row].clone()));
    }

    /**
     * This method return a column of my resourceMarket
     * @param column
     * @return a column of my resourceMarket
     */
    public ArrayList<FinalResource> selectColumn(int column) {
        ArrayList<FinalResource> list = new ArrayList<>();
        for (int r=0; r<ROWS; r++)
            list.add(marbleGrid[r][column]);
        return Lists.unifyResourceAmounts(list);
    }

    // NEEDED ONLY TO TEST

    public FinalResource[][] getMarbleGrid() {
        return marbleGrid;
    }

    public FinalResource getExtraMarble() {
        return extraMarble;
    }
}
