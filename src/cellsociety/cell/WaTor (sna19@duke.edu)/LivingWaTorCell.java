package cellsociety.cell.WaTor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class LivingWaTorCell extends WaTorCell{

    private int reproductionTimer = 0;

    public LivingWaTorCell(String state, int row, int col) {
        super(state, row, col);
    }

    protected abstract int getChrononsToReproduce();

    protected List<List<WaTorCell>> handleReproduction(List<List<WaTorCell>> nextGrid) {
        if (reproductionTimer == getChrononsToReproduce()) {
            reproductionTimer = 0;
            if (this instanceof FishCell) {
                nextGrid.get(getRow()).set(getCol(), new FishCell(getRow(), getCol()));
            }
            else if (this instanceof SharkCell) {
                nextGrid.get(getRow()).set(getCol(), new SharkCell(getRow(), getCol()));
            }
        }
        else {
            reproductionTimer++;
            nextGrid.get(getRow()).set(getCol(), new EmptyCell(getRow(), getCol()));
        }
        return nextGrid;
    }

    protected List<Integer> getRandomNewPosition(List<List<Integer>> potentialNewPositions) {
        Collections.shuffle(potentialNewPositions);
        return potentialNewPositions.get(0);
    }

    protected List<List<Integer>> getAdjacentEnterableCells(List<WaTorCell> neighbors, List<List<WaTorCell>> nextGrid, List<String> unenterableStates) {
        List<List<Integer>> potentialNewPositions = new ArrayList<>();
        for (WaTorCell cell : neighbors) {
            List<Integer> position = List.of(cell.getRow(), cell.getCol());
            // check that adjacent cell is empty AND has not been claimed by another creature with the same
            // or higher priority (sharks have "higher" priority than fish when moving to a cell because they
            // can eat fish, but a fish can't move into a cell that has been claimed by shark)
            if (cell.getState().equals(EMPTY) &&
                    !unenterableStates.contains(nextGrid.get(cell.getRow()).get(cell.getCol()).getState())) {
                potentialNewPositions.add(position);
            }
        }

        return potentialNewPositions;
    }
}
