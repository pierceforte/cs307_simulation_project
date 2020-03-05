package cellsociety.cell.wator;

import cellsociety.cell.Cell;
import cellsociety.cell.wator.WaTorCell;
import cellsociety.grid.Grid;

import java.util.List;

public class FishCell extends LivingWaTorCell {
    public static final String STATE = WaTorCell.FISH;
    public static final int CHRONONS_TO_REPRODUCE = 5;

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
