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
     */
    public PercolationModel(List<List<String>> cellStates) {
        super(cellStates);
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
        grid.executeForAllCells(cell -> cell.setWhatToDoNext(getNeighbors(cell)));
    }

    @Override
    protected void updateStates(Grid<PercolationCell> grid) {
        grid.executeForAllCells(cell -> cell.updateState());
    }

    @Override
    protected List<PercolationCell> getNeighbors(PercolationCell cell) {
        return getGrid().getCardinalNeighbors(cell);
    }

}
