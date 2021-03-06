package cellsociety.backend;

import cellsociety.cell.rps.RPSCell;
import cellsociety.grid.Grid;

import java.util.*;

/**
 * This class inherits from the abstract class SimModel, implementing the backend for the Rock, Paper, Scissors simulation.
 *
 * This class defines the rules for each update, relying on the RPSCell and its different implementations.
 *
 * @author Pierce Forte
 */
public class RPSModel extends SimModel<RPSCell> {
    public static final String CONFIG_FILE_PREFIX = "RPS";

    /**
     * The constructor to create a Rock, Paper, Scissors simulation's backend.
     * @param cellStates the initial cell states, as collected from the csv file
     */
    public RPSModel(List<List<String>> cellStates) {
        super(cellStates);
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
        grid.executeForAllCells(cell -> cell.setWhatToDoNext(getNeighbors(cell)));
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
