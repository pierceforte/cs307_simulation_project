package cellsociety.backend;

import cellsociety.SimController;
import cellsociety.cell.segregation.SegregationCell;
import cellsociety.grid.Grid;

import java.util.List;
import java.util.TreeMap;

/**
 * This class inherits from the abstract class SimModel, implementing the backend for the Segregation simulation.
 *
 * This class defines the rules for each update, relying on the SegregationCell and its different implementations.
 *
 * It is important to note that this simulation implements a "next grid"; because cells move to random locations on each
 * update if they are not currently satisfied, it is essential to keep track of which random locations have already
 * been taken. This process still results in a two-pass system (where cells choose where to move independently of where
 * other cells **are**), but the cells do not choose where to move independently of where they **will be**.
 *
 * @author Pierce Forte
 */
public class SegregationModel extends SimModel<SegregationCell> {
    public static final String CONFIG_FILE_PREFIX = "Segregation";

    private Grid<SegregationCell> nextGrid;

    /**
     * The constructor to create a Segregation simulation's backend.
     * @param cellStates the initial cell states, as collected from the csv file
     */
    public SegregationModel(List<List<String>> cellStates) {
        super(cellStates);
        initializeNextGrid();
    }

    @Override
    public TreeMap<String, Class> getOrderedCellStatesMap() {
        TreeMap<String, Class> cellTypes = new TreeMap<>();
        cellTypes.put(SegregationCell.EMPTY, SegregationCell.class);
        cellTypes.put(SegregationCell.AGENT_A, SegregationCell.class);
        cellTypes.put(SegregationCell.AGENT_B, SegregationCell.class);
        return cellTypes;
    }

    @Override
    protected void setNextStates(Grid<SegregationCell> grid) {
        grid.executeForAllCells(cell -> cell.setWhatToDoNext(getNeighbors(cell), nextGrid));
    }

    @Override
    protected void updateStates(Grid<SegregationCell> grid) {
        // update actual states of current grid
        for (int row = 0; row < nextGrid.getNumRows(); row++) {
            for (int col = 0; col < nextGrid.getNumCols(); col++) {
                SegregationCell cell = nextGrid.get(row, col);
                cell.setRow(row);
                cell.setCol(col);
                grid.set(row, col, nextGrid.get(row, col));
            }
        }
    }

    @Override
    protected List<SegregationCell> getNeighbors(SegregationCell cell) {
        return getGrid().getAllNeighbors(cell);
    }

    private void initializeNextGrid() {
        nextGrid = new Grid<SegregationCell>(getGrid());
    }
}
