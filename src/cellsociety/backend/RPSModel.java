package cellsociety.backend;

import cellsociety.SimController;
import cellsociety.cell.RPSCell;
import cellsociety.grid.Grid;

import java.util.*;

public class RPSModel extends SimModel<RPSCell> {
    public static final String CONFIG_FILE_PREFIX = "RPS";

    public RPSModel(List<List<String>> cellStates, SimController simController) {
        super(cellStates, simController);
    }

    @Override
    public TreeMap<String, Class> getOrderedCellStatesMap() {
        TreeMap<String, Class> cellTypes = new TreeMap<>();
        // kind of hacky, but not requiring RPSCell class to maintain certain states
        // would require us to find a new way of creating cells based on state
        // (not really a problem here since all states correlate to one class, but
        // needed for sims like WaTor which have multiple classes for cells)
        for (String state : RPSCell.STATES) {
            cellTypes.put(state, RPSCell.class);
        }
        return cellTypes;
    }

    @Override
    protected void setNextStates(Grid<RPSCell> grid) {
        // TODO: try to put this in executeForAll runnable
        for (int row = 0; row < grid.getNumRows(); row++) {
            for (int col = 0; col < grid.getNumCols(); col++) {
                RPSCell cell = grid.get(row,col);
                cell.setWhatToDoNext(getNeighbors(cell));
            }
        }
    }

    @Override
    protected void updateStates(Grid<RPSCell> grid) {
        grid.executeForAllCells(cell -> cell.updateState());

    }

    @Override
    protected List<RPSCell> getNeighbors(RPSCell cell) {
        return getGrid().getAllNeighbors(cell);
    }

}
