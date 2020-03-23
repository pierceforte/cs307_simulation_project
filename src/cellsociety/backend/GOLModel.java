package cellsociety.backend;
import cellsociety.cell.gol.GOLCell;
import cellsociety.grid.Grid;
import java.util.List;
import java.util.TreeMap;

/**
 * This class inherits from the abstract class SimModel, implementing the backend for the Game of Life simulation.
 * This class defines the rules for each update, relying on the GOLCELL and its different implementations.
 * This class is well designed because of its simplicity; although it represents a complete implementation of a SimModel, minimal work is required to implement it, as can be seen below. All of the required functionality for a general SimModel is in the superclass, and all that is done here is entirely specific to this type of SimModel.
 * Note that this class's superclass – SimModel – is included in the masterpiece as well and more information regarding the methods in this class can be found there.
 * Note that the following was done to refactor this class: the setNextStates method was refactored to replace the for loop that was previously there with a consumer function (executeForAllCells), implemented by the Grid object. This consumer function was also used for the updateStates function in the original code, but there was previously an issue that did not allow me to use it for setNextStates – I was able to resolve that.
 * @author Pierce Forte
 */
public class GOLModel extends SimModel<GOLCell> {
    public static final String CONFIG_FILE_PREFIX = "GOL";
    /**
     * The constructor to create a Game of Life simulation's backend.
     * @param cells the initial cell states, as collected from the csv file
     */
    public GOLModel(List<List<String>> cells) {
        super(cells);
    }
    @Override
    public TreeMap<String, Class> getOrderedCellStatesMap() {
        TreeMap<String, Class> cellTypes = new TreeMap<>();
        cellTypes.put(GOLCell.DEAD, GOLCell.class);
        cellTypes.put(GOLCell.ALIVE, GOLCell.class);
        return cellTypes;
    }
    @Override
    protected void setNextStates(Grid<GOLCell> grid) {
        grid.executeForAllCells(cell -> cell.setWhatToDoNext(getNeighbors(cell)));
    }
    @Override
    protected void updateStates(Grid<GOLCell> grid) {
        grid.executeForAllCells(cell -> cell.updateState());
    }
    @Override
    protected List<GOLCell> getNeighbors(GOLCell cell) {
        return getGrid().getAllNeighbors(cell);
    }
}