package cellsociety.cell.wator;

import cellsociety.cell.Cell;

import java.util.List;

public class FishCell extends LivingWaTorCell {
    public static final String STATE = WaTorCell.FISH;
    public static final int CHRONONS_TO_REPRODUCE = 5;

    public FishCell(int row, int col) {
        super(STATE, row, col);
    }

    @Override
    public List<List<WaTorCell>> setWhatToDoNext(List<WaTorCell> neighbors, List<List<WaTorCell>> nextGrid) {
        List<List<Integer>> potentialNewPositions = getAdjacentEnterableCells(neighbors, nextGrid, List.of(FISH, SHARK));
        if (potentialNewPositions.isEmpty()) {
            nextGrid.get(getRow()).set(getCol(), this);
            return nextGrid;
        }
        List<Integer> newPosition = getRandomNewPosition(potentialNewPositions);
        setNextRow(newPosition.get(Cell.ROW_INDEX));
        setNextCol(newPosition.get(Cell.COL_INDEX));

        nextGrid.get(getNextRow()).set(getNextCol(), this);

        nextGrid = handleReproduction(nextGrid);

        return nextGrid;
    }

    @Override
    protected int getChrononsToReproduce() {
        return CHRONONS_TO_REPRODUCE;
    }

}
