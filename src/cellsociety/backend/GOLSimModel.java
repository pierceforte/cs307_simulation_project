package cellsociety.backend;

import cellsociety.SimController;
import cellsociety.cell.GOL.GOLCell;
import cellsociety.grid.Grid;

import java.util.List;
import java.util.TreeMap;

public class GOLSimModel extends SimModel<GOLCell> {
    public static final String CONFIG_FILE_PREFIX = "GOL";

    public GOLSimModel(List<List<String>> cells, SimController simController) {
        super(cells, simController);
    }

    @Override
    public TreeMap<String, Class> getOrderedCellTypesMap() {
        TreeMap<String, Class> cellTypes = new TreeMap<>();
        cellTypes.put(GOLCell.DEAD, GOLCell.class);
        cellTypes.put(GOLCell.ALIVE, GOLCell.class);
        return cellTypes;
    }

    //TODO: eliminate duplication here and in FireSimModel and SegregationSimModel
    @Override
    protected void setNextStates(Grid<GOLCell> grid) {
        // TODO: try to put this in executeForAll runnable
        for (int row = 0; row < grid.getNumRows(); row++) {
            for (int col = 0; col < grid.getNumCols(); col++) {
                GOLCell cell = grid.get(row,col);
                cell.setWhatToDoNext(getNeighbors(cell));
            }
        }
    }

    //TODO: eliminate duplication here and in FireSimModel
    @Override
    protected void updateStates(Grid<GOLCell> grid) {
        grid.executeForAllCells(cell -> cell.updateState());
    }

    @Override
    protected List<GOLCell> getNeighbors(GOLCell cell) {
        return getGrid().getAllNeighbors(cell);
    }
}
