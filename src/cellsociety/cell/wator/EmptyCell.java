package cellsociety.cell.wator;

import cellsociety.grid.Grid;
import java.util.List;

/**
 * This class inherits from the abstract class WaTorCell.
 *
 * This class's purpose is to update empty cells in the grid. As can be seen, no functionality is required
 * for this type of cell, but it must implement the "setWhatToDoNext" method to support the polymorphism
 * of the WaTorCell class.
 *
 * @author Pierce Forte
 */
public class EmptyCell extends WaTorCell {
    public static final String STATE = WaTorCell.EMPTY;

    /**
     * The constructor to create an EmptyCell's backend.
     * @param row The row in which the cell is located
     * @param col The column in which the cell is located
     */
    public EmptyCell(int row, int col) {
        super(STATE, row, col);
    }

    @Override
    public Grid<WaTorCell> setWhatToDoNext(List<WaTorCell> neighbors, Grid<WaTorCell> nextGrid) {
        return nextGrid;
    }

}
