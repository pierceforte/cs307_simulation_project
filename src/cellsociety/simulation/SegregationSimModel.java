package cellsociety.simulation;

import cellsociety.cell.Fire.FireCell;
import cellsociety.cell.segregation.SegregationCell;
import cellsociety.grid.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SegregationSimModel extends SimModel<SegregationCell> {
    public static final String CONFIG_FILE_PREFIX = "Segregation";

    private Grid<SegregationCell> nextGrid;

    public SegregationSimModel(List<List<String>> cellStates, SimController simController) {
        super(cellStates, simController);
        initializeNextGrid();
    }

    @Override
    protected List<List<SegregationCell>> createGrid(List<List<String>> cellStates) {
        List<List<SegregationCell>> grid = new ArrayList<>();
        for (int row = 0; row < cellStates.size(); row++) {
            grid.add(new ArrayList<>());
            for (int col = 0; col < cellStates.size(); col++) {
                SegregationCell cell;
                if (cellStates.get(row).get(col).equals(SegregationCell.EMPTY)) {
                    cell = new SegregationCell(SegregationCell.EMPTY, row, col);
                }
                else if (cellStates.get(row).get(col).equals(SegregationCell.AGENT_A)) {
                    cell = new SegregationCell(SegregationCell.AGENT_A, row, col);
                }
                // TODO: throw error if invalid state
                else {
                    cell = new SegregationCell(SegregationCell.AGENT_B, row, col);
                }
                grid.get(row).add(cell);
            }
        }
        return grid;
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
    protected String getConfigFileIdentifier() {
        return CONFIG_FILE_PREFIX;
    }

    @Override
    protected List<SegregationCell> getNeighbors(SegregationCell cell) {
        return getGrid().getAllNeighbors(cell);
    }

    private void initializeNextGrid() {
        nextGrid = new Grid<SegregationCell>(getGrid());
    }
}