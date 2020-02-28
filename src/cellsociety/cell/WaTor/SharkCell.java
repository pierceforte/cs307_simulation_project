package cellsociety.cell.WaTor;

import java.util.ArrayList;
import java.util.List;

public class SharkCell extends LivingWaTorCell{
    public static final String STATE = WaTorCell.SHARK;
    public static final int CHRONONS_TO_REPRODUCE = 5;
    public static final int DEFAULT_SHARK_ENERGY = 4;
    public static final int ENERGY_FOR_EATING_FISH = 2;

    private List<Integer> posOfFishToEatNext;
    private int energy;

    public SharkCell(int row, int col) {
        super(STATE, row, col);
        energy = DEFAULT_SHARK_ENERGY;
    }

    public List<Integer> getPosOfFishToEatNext() {
        return posOfFishToEatNext;
    }

    // TODO: clean this method up

    @Override
    public List<List<WaTorCell>> setWhatToDoNext(List<WaTorCell> neighbors, List<List<WaTorCell>> nextGrid) {

        // if shark has no energy, it dies and becomes an EMPTY cell
        if (energy == 0) {
            nextGrid.get(getRow()).set(getCol(), new EmptyCell(getRow(), getCol()));
            return nextGrid;
        }

        List<List<Integer>> potentialNewPositions = new ArrayList<>();
        // first look for any nearby fish
        for (WaTorCell cell : neighbors) {
            if (cell.getState().equals(FISH)) {
                potentialNewPositions.add(List.of(cell.getRow(), cell.getCol()));
            }
        }
        boolean willEatFish = false;
        // if no fish, look for potential empty spaces to move to
        if (potentialNewPositions.isEmpty()) {
            potentialNewPositions = getAdjacentEnterableCells(neighbors, nextGrid, List.of(SHARK));
        }
        else {
            willEatFish = true;
        }
        // if no potential empty spaces to move to, don't move
        if (potentialNewPositions.isEmpty()) {
            nextGrid.get(getRow()).set(getCol(), this);
            return nextGrid;
        }
        // choose randomly from the potential spaces to move to from above
        List<Integer> newPosition = getRandomNewPosition(potentialNewPositions);
        setNextRow(newPosition.get(0));
        setNextCol(newPosition.get(1));

        nextGrid.get(getNextRow()).set(getNextCol(), this);

        if (willEatFish) {
            energy += ENERGY_FOR_EATING_FISH;
            for (WaTorCell cell : neighbors) {
                if (cell.getRow() == getNextRow() && cell.getCol() == getNextCol()) {
                    posOfFishToEatNext = List.of(getNextRow(), getNextCol());
                }
            }
        }
        else {
            energy--;
        }
        nextGrid = handleReproduction(nextGrid);

        return nextGrid;
    }

    @Override
    protected int getChrononsToReproduce() {
        return CHRONONS_TO_REPRODUCE;
    }

}
