package cellsociety.backend;

import cellsociety.SimController;
import cellsociety.cell.segregation.SegregationCell;
import cellsociety.grid.Grid;

import java.util.List;
import java.util.TreeMap;

public class SegregationSimModel extends SimModel<SegregationCell> {
    public static final String CONFIG_FILE_PREFIX = "Segregation";

    private Grid<SegregationCell> nextGrid;

    public SegregationSimModel(List<List<String>> cellStates, SimController simController) {
        super(cellStates, simController);
        initializeNextGrid();
    }

    @Override
    public TreeMap<String, Class> getOrderedCellTypesMap() {
        TreeMap<String, Class> cellTypes = new TreeMap<>();
        cellTypes.put(SegregationCell.EMPTY, SegregationCell.class);
        cellTypes.put(SegregationCell.AGENT_A, SegregationCell.class);
        cellTypes.put(SegregationCell.AGENT_B, SegregationCell.class);
        return cellTypes;
    }

    @Override
    protected void setNextStates(Grid<SegregationCell> grid) {
        // TODO: try to put this in executeForAll runnable
        for (int row = 0; row < grid.getNumRows(); row++) {
            for (int col = 0; col < grid.getNumCols(); col++) {
                SegregationCell cell = grid.get(row,col);
                cell.setWhatToDoNext(getNeighbors(cell), nextGrid);
            }
        }
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
