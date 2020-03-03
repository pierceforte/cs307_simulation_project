package cellsociety.simulation;

import cellsociety.cell.segregation.SegregationCell;

import java.util.ArrayList;
import java.util.List;

public class SegregationSimModel extends SimModel<SegregationCell> {
    public static final String CONFIG_FILE_PREFIX = "Segregation";

    private List<List<SegregationCell>> nextGrid = new ArrayList<>();

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
    protected void setNextStates(List<List<SegregationCell>> cells) {
        for (List<SegregationCell> row : cells) {
            for (SegregationCell cell : row) {
                determineAndSetNextState(cell, getNeighbors(cell));
            }
        }
    }

    @Override
    protected void determineAndSetNextState(SegregationCell cell, List<SegregationCell> neighbors) {
        nextGrid = cell.setWhatToDoNext(neighbors, nextGrid);
    }

    @Override
    protected void updateStates(List<List<SegregationCell>> cells) {
        // update actual states of current grid
        for (int row = 0; row < cells.size(); row++) {
            for (int col = 0; col < cells.get(0).size(); col++) {
                SegregationCell cell = nextGrid.get(row).get(col);
                cell.setRow(row);
                cell.setCol(col);
                cells.get(row).set(col, nextGrid.get(row).get(col));
            }
        }
    }

    @Override
    protected String getConfigFileIdentifier() {
        return CONFIG_FILE_PREFIX;
    }

    @Override
    protected List<SegregationCell> getNeighbors(SegregationCell cell) {
        return getGridModel().getAllNeighbors(cell);
    }

    private void initializeNextGrid() {
        for (int row = 0; row < getCells().size(); row++) {
            nextGrid.add(new ArrayList<>());
            for (int col = 0; col < getCells().get(0).size(); col++) {
                nextGrid.get(row).add(getCells().get(row).get(col));
            }
        }
    }
}
