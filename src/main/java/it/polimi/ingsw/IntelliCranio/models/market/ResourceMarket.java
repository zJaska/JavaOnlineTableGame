package it.polimi.ingsw.IntelliCranio.models.market;

import it.polimi.ingsw.IntelliCranio.util.Lists;
import it.polimi.ingsw.IntelliCranio.models.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.models.resource.Resource;

import java.io.Serializable;
import java.util.*;

public class ResourceMarket implements Serializable {

    public final int ROWS;
    public final int COLUMNS;

    private FinalResource extraMarble;
    private FinalResource[][] marbleGrid;


    public ResourceMarket(int rows, int columns) {
        marbleGrid = new FinalResource[rows][columns];
        ROWS = rows;
        COLUMNS = columns;

        ArrayList<Resource> resources = new ArrayList<>(Arrays.asList(
                new Resource(FinalResource.ResourceType.BLANK, 4),
                new Resource(FinalResource.ResourceType.SHIELD, 2),
                new Resource(FinalResource.ResourceType.STONE, 2),
                new Resource(FinalResource.ResourceType.SERVANT, 2),
                new Resource(FinalResource.ResourceType.COIN, 2),
                new Resource(FinalResource.ResourceType.FAITH, 1)
        ));

        setup(resources);
    }

    //Todo

    /**
     * This method generate a my resource market.
     *
     * @param resourcesParam
     */
    public void setup(ArrayList<Resource> resourcesParam) {
        ArrayList<Resource> resources = new ArrayList<>();
        resourcesParam.forEach(res -> resources.add(new Resource(res.getType(), res.getAmount())));

        if (ROWS * COLUMNS > resources.stream().map(FinalResource::getAmount).reduce(Integer::sum).get() - 1)
            throw new IllegalArgumentException();

        // Assigning random resources to the grid
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                int random = (int) (Math.random() * resources.size());
                marbleGrid[r][c] = new FinalResource(resources.get(random).getType(), 1);
                if (resources.get(random).removeAmount(1) == 0)
                    resources.remove(random);
            }
        }

        extraMarble = resources.get(0);
    }

    /**
     * This method return a row of my resourceMarket
     * and updates its model
     *
     * @param row
     * @return a row of resource market
     */
    public ArrayList<FinalResource> selectRow(int row) {
        ArrayList<FinalResource> rowResources = new ArrayList<>(); //List to return

        //region Take the resources and add to return list
        for (int col = 0; col < COLUMNS; ++col)
            rowResources.add(marbleGrid[row][col]);
        //endregion

        //region Update the grid

        //Take the first element (from SX) of the row and temporary store it
        FinalResource tempExtraMarble = marbleGrid[row][0];

        //Left-Shift the row
        for (int col = 1; col < COLUMNS; ++col)
            marbleGrid[row][col - 1] = marbleGrid[row][col];

        //Add the extra marble at the end of the row
        marbleGrid[row][COLUMNS - 1] = extraMarble;

        //Set the extra marble with the stored temp resource pushed out from row
        extraMarble = tempExtraMarble;

        //endregion

        return Lists.unifyResourceAmounts(rowResources);
    }

    /**
     * This method return a column of my resourceMarket
     * and updates its model
     *
     * @param column
     * @return a column of my resourceMarket
     */
    public ArrayList<FinalResource> selectColumn(int column) {
        ArrayList<FinalResource> colResources = new ArrayList<>();

        //region Take the resources and add to return list
        for (int row = 0; row < ROWS; ++row)
            colResources.add(marbleGrid[row][column]);
        //endregion

        //region Update the grid

        //Take the first element (from TOP) of the column and temporary store it
        FinalResource tempExtraMarble = marbleGrid[0][column];

        //Top-Shift the column
        for (int row = 1; row < ROWS; ++row)
            marbleGrid[row - 1][column] = marbleGrid[row][column];

        //Add the extra marble at the end of the column
        marbleGrid[ROWS - 1][column] = extraMarble;

        //Set the extra marble with the stored temp resource pushed out from column
        extraMarble = tempExtraMarble;

        //endregion

        return Lists.unifyResourceAmounts(colResources);
    }

    public FinalResource[][] getGridCopy() {
        FinalResource[][] newGrid = new FinalResource[ROWS][COLUMNS];

        for (int i = 0; i < ROWS; i++)
            for (int j = 0; j < COLUMNS; j++)
                newGrid[i][j] = new FinalResource(marbleGrid[i][j].getType(), marbleGrid[i][j].getAmount());

        return newGrid;
    }

    public FinalResource getExtraMarbleCopy() {
        return new FinalResource(extraMarble.getType(), extraMarble.getAmount());
    }

    // NEEDED ONLY TO TEST

    public FinalResource[][] getMarbleGrid() {
        return marbleGrid;
    }

    public FinalResource getExtraMarble() {
        return extraMarble;
    }
}
