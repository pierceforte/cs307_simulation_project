package cellsociety.backend;

import cellsociety.SimController;
import cellsociety.cell.gol.GOLCell;
import cellsociety.grid.Grid;

import java.util.List;
import java.util.TreeMap;

/**
 * This class inherits from the abstract class SimModel, implementing the backend for the Game of Life simulation.
 *
 * This class defines the rules for each update, relying on the GOLCELL and its different implementations.
 *
 * @author Pierce Forte
 */
public class GOLModel extends SimModel<GOLCell> {
    public static final String CONFIG_FILE_PREFIX = "GOL";

    /**
     * The constructor to create a Game of Life simulation's backend.
     * @param cells the initial cell states, as collected from the csv file
     * @param simController the SimController used to interact with the frontend
     */
    public GOLModel(List<List<String>> cells, SimController simController) {
        super(cells, simController);
    }

    @Override
    public TreeMap<String, Class> getOrderedCellStatesMap() {
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
