package cellsociety.simulation;

import cellsociety.cell.GOL.GOLCell;
import cellsociety.cell.segregation.SegregationCell;
import cellsociety.grid.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GOLSimModel extends SimModel<GOLCell> {
    public static final String CONFIG_FILE_PREFIX = "GOL";

    public GOLSimModel(List<List<String>> cells, SimController simController) {
        super(cells, simController);
    }

    @Override
    protected Map<String, Class> getCellTypesMap() {
        return Map.of(GOLCell.DEAD, GOLCell.class,
                GOLCell.ALIVE, GOLCell.class);
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
