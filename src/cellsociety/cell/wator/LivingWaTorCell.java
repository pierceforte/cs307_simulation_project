package cellsociety.cell.wator;

import cellsociety.cell.Cell;
import cellsociety.grid.Grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This abstract class inherits from the abstract class WaTorCell.
 *
 * This class's purpose is to provide duplicate functionality to the two living Wa-Tor cell types.
 * For example, these living cells (FishCell and SharkCell) have the ability to move and reproduce.
 *
 * @author Pierce Forte
 */
public abstract class LivingWaTorCell extends WaTorCell {

    private int reproductionTimer = 0;

    /**
     * The constructor to create a LivingWaTorCell's backend.
     * @param state The state (or "type") of the cell to be created
     * @param row The row in which the cell is located
     * @param col The column in which the cell is located
     */
    public LivingWaTorCell(String state, int row, int col) {
        super(state, row, col);
    }

    protected abstract int getChrononsToReproduce();

    protected Grid<WaTorCell> handleReproduction(Grid<WaTorCell> nextGrid) {
        if (reproductionTimer == getChrononsToReproduce()) {
            reproductionTimer = 0;
            if (this instanceof FishCell) {
                nextGrid.set(getRow(), getCol(), new FishCell(getRow(), getCol()));
            }
            else if (this instanceof SharkCell) {
                nextGrid.set(getRow(), getCol(), new SharkCell(getRow(), getCol()));
            }
        }
        else {
            reproductionTimer++;
            nextGrid.set(getRow(), getCol(), new EmptyCell(getRow(), getCol()));
        }
        return nextGrid;
    }

    protected List<Integer> getRandomNewPosition(List<List<Integer>> potentialNewPositions) {
        Collections.shuffle(potentialNewPositions);
        return potentialNewPositions.get(0);
    }

    protected List<List<Integer>> getAdjacentEnterableCells(List<WaTorCell> neighbors, Grid<WaTorCell> nextGrid, List<String> unenterableStates) {
        List<List<Integer>> potentialNewPositions = new ArrayList<>();
        for (WaTorCell cell : neighbors) {
            List<Integer> position = List.of(cell.getRow(), cell.getCol());
            // check that adjacent cell is empty AND has not been claimed by another creature with the same
            // or higher priority (sharks have "higher" priority than fish when moving to a cell because they
            // can eat fish, but a fish can't move into a cell that has been claimed by shark)
            if (cell.getState().equals(EMPTY) &&
                    !unenterableStates.contains(nextGrid.get(cell.getRow(), cell.getCol()).getState())) {
                potentialNewPositions.add(position);
            }
        }
        return potentialNewPositions;
    }

    protected Grid<WaTorCell> setNextPosition(List<List<Integer>> potentialNewPositions, Grid<WaTorCell> nextGrid) {
        List<Integer> newPosition = getRandomNewPosition(potentialNewPositions);
        setNextRow(newPosition.get(Cell.ROW_INDEX));
        setNextCol(newPosition.get(Cell.COL_INDEX));
        nextGrid.set(getNextRow(), getNextCol(), this);
        return nextGrid;
    }
}
