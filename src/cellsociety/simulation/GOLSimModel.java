package cellsociety.simulation;

import cellsociety.cell.GOL.GOLCell;
import cellsociety.grid.Grid;

import java.util.ArrayList;
import java.util.List;

public class GOLSimModel extends SimModel<GOLCell> {
    public static final String CONFIG_FILE_PREFIX = "GOL";

    public GOLSimModel(List<List<String>> cells, SimController simController) {
        super(cells, simController);
    }

    @Override
    protected List<List<GOLCell>> createGrid(List<List<String>> cellStates) {
        List<List<GOLCell>> grid = new ArrayList<>();
        for (int row = 0; row < cellStates.size(); row++) {
            grid.add(new ArrayList<>());
            for (int col = 0; col < cellStates.size(); col++) {
                GOLCell cell;
                if (cellStates.get(row).get(col).equals(GOLCell.DEAD)) {
                    cell = new GOLCell(GOLCell.DEAD, row, col);
                }
                // TODO: throw error if invalid state
                else {
                    cell = new GOLCell(GOLCell.ALIVE, row, col);
                }
                grid.get(row).add(cell);
            }
        }
        return grid;
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
    protected String getConfigFileIdentifier() {
        return CONFIG_FILE_PREFIX;
    }

    @Override
    protected List<GOLCell> getNeighbors(GOLCell cell) {
        return getGrid().getAllNeighbors(cell);
    }
}
