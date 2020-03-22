package cellsociety.backend;

import cellsociety.SimController;
import cellsociety.cell.percolation.PercolationCell;
import cellsociety.grid.Grid;

import java.util.List;
import java.util.TreeMap;
/**
 * This class inherits from the abstract class SimModel, implementing the backend for the Percolation simulation.
 *
 * This class defines the rules for each update, relying on the PercolationCell and its different implementations.
 *
 * @author Pierce Forte
 * @author Donald Groh
 */
public class PercolationModel extends SimModel<PercolationCell>{
    public static final String CONFIG_FILE_PREFIX = "Percolation";

    /**
     * The constructor to create a Percolation simulation's backend.
     * @param cellStates the initial cell states, as collected from the csv file
     * @param simController the SimController used to interact with the frontend
     */
    public PercolationModel(List<List<String>> cellStates, SimController simController) {
        super(cellStates, simController);
    }

    @Override
    public TreeMap<String, Class> getOrderedCellStatesMap() {
        TreeMap<String, Class> cellTypes = new TreeMap<>();
        cellTypes.put(PercolationCell.OPEN, PercolationCell.class);
        cellTypes.put(PercolationCell.CLOSED, PercolationCell.class);
        cellTypes.put(PercolationCell.FULL, PercolationCell.class);
        return cellTypes;
    }

    @Override
    protected void setNextStates(Grid<PercolationCell> grid) {
        for (int row = 0; row < grid.getNumRows(); row++) {
            for (int col = 0; col < grid.getNumCols(); col++) {
                PercolationCell cell = grid.get(row,col);
                cell.setWhatToDoNext(getNeighbors(cell));
            }
        }
    }

    @Override
    protected void updateStates(Grid<PercolationCell> grid) {
        // update actual states of current grid
        grid.executeForAllCells(cell -> cell.updateState());
    }

    @Override
    protected List<PercolationCell> getNeighbors(PercolationCell cell) {
        return getGrid().getCardinalNeighbors(cell);
    }

}
