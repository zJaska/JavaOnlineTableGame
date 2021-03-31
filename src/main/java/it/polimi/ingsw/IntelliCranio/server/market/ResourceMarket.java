package it.polimi.ingsw.IntelliCranio.server.market;

import it.polimi.ingsw.IntelliCranio.server.Utility;
import it.polimi.ingsw.IntelliCranio.server.resource.FinalResource;
import it.polimi.ingsw.IntelliCranio.server.resource.Resource;

import java.util.*;
import java.util.stream.Collectors;

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

    public ArrayList<FinalResource> selectRow(int row) {
        return Utility.unifyResourceAmounts(Arrays.asList(marbleGrid[row].clone()));
    }

    public ArrayList<FinalResource> selectColumn(int column) {
        ArrayList<FinalResource> list = new ArrayList<>();
        for (int r=0; r<ROWS; r++)
            list.add(marbleGrid[r][column]);
        return Utility.unifyResourceAmounts(list);
    }

    // NEEDED ONLY TO TEST

    public FinalResource[][] getMarbleGrid() {
        return marbleGrid;
    }

    public FinalResource getExtraMarble() {
        return extraMarble;
    }
}
