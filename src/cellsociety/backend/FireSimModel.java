package cellsociety.backend;

import cellsociety.SimController;
import cellsociety.cell.Fire.BurningCell;
import cellsociety.cell.Fire.EmptyCell;
import cellsociety.cell.Fire.FireCell;
import cellsociety.cell.Fire.TreeCell;
import cellsociety.grid.Grid;

import java.util.List;
import java.util.TreeMap;

public class FireSimModel extends SimModel<FireCell> {
    // TODO: make probCatch and probTree adjustable/initializable
    public static final String CONFIG_FILE_PREFIX = "Fire";


    public FireSimModel(List<List<String>> cells, SimController simController){
        super(cells, simController);
    }

    @Override
    public TreeMap<String, Class> getOrderedCellStatesMap() {
        TreeMap<String, Class> cellTypes = new TreeMap<>();
        cellTypes.put(FireCell.EMPTY, EmptyCell.class);
        cellTypes.put(FireCell.TREE, TreeCell.class);
        cellTypes.put(FireCell.BURNING, BurningCell.class);
        return cellTypes;
    }

    @Override
    protected void setNextStates(Grid<FireCell> grid) {
        // TODO: try to put this in executeForAll runnable
        for (int row = 0; row < grid.getNumRows(); row++) {
            for (int col = 0; col < grid.getNumCols(); col++) {
                FireCell cell = grid.get(row,col);
                cell.setWhatToDoNext(getNeighbors(cell));
            }
        }
    }

    @Override
    protected void updateStates(Grid<FireCell> grid) {
        grid.executeForAllCells(cell -> cell.updateState());
    }

    @Override
    protected List<FireCell> getNeighbors(FireCell cell) {
        return getGrid().getCardinalNeighbors(cell);
    }

}
