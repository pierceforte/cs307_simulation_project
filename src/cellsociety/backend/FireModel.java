package cellsociety.backend;

import cellsociety.cell.fire.BurningCell;
import cellsociety.cell.fire.EmptyCell;
import cellsociety.cell.fire.FireCell;
import cellsociety.cell.fire.TreeCell;
import cellsociety.grid.Grid;

import java.util.List;
import java.util.TreeMap;

public class FireModel extends SimModel<FireCell> {
    // TODO: make probCatch and probTree adjustable/initializable
    public static final String CONFIG_FILE_PREFIX = "Fire";


    public FireModel(List<List<String>> cells){
        super(cells);
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
        grid.executeForAllCells(cell -> cell.setWhatToDoNext(getNeighbors(cell)));
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
