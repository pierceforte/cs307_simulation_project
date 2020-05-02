package cellsociety.cell.wator;

import cellsociety.cell.Cell;
import cellsociety.grid.Grid;

import java.util.List;

/**
 * This class inherits from the abstract class LivingWaTorCell.
 *
 * This class's purpose is to update the fish cells in the grid. Fish must choose where to move on each update
 * and reproduce after a set time period.
 *
 * @author Pierce Forte
 */
public class FishCell extends LivingWaTorCell {
    public static final String STATE = WaTorCell.FISH;
    public static final int CHRONONS_TO_REPRODUCE = 5;

    /**
     * The constructor to create a FishCell's backend.
     * @param row The row in which the cell is located
     * @param col The column in which the cell is located
     */
    public FishCell(int row, int col) {
        super(STATE, row, col);
    }

    @Override
    public Grid<WaTorCell> setWhatToDoNext(List<WaTorCell> neighbors, Grid<WaTorCell> nextGrid) {
        List<List<Integer>> potentialNewPositions = getAdjacentEnterableCells(neighbors, nextGrid, List.of(FISH, SHARK));
        if (potentialNewPositions.isEmpty()) {
            nextGrid.set(getRow(), getCol(), this);
            return nextGrid;
        }
        nextGrid = setNextPosition(potentialNewPositions, nextGrid);
        nextGrid = handleReproduction(nextGrid);
        return nextGrid;
    }

    @Override
    protected int getChrononsToReproduce() {
        return CHRONONS_TO_REPRODUCE;
    }

}
