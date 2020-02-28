package cellsociety.cell;

import cellsociety.simulation.WaTorSimModel;

import java.util.*;
import java.util.function.BiFunction;

public class WaTorCell extends Cell {
    public static final String EMPTY = "0"; //represented in data file as 0
    public static final String FISH = "1"; //represented in data file as 1
    public static final String SHARK = "2"; //represented in data file as 1
    public static final int CHRONONS_TO_REPRODUCE = 0;
    public static final Map<String, Integer> DEFAULT_ENERGY_MAP = Map.of(
            WaTorSimModel.EMPTY, 0,
            WaTorSimModel.FISH, 0,
            WaTorSimModel.SHARK, 4
    );

    private int nextRow;
    private int nextCol;
    private int energy;
    private int reproductionTimer = 0;
    private Map<String, BiFunction<List<WaTorCell>, List<List<WaTorCell>>, List<List<WaTorCell>>>> handleCell = Map.of(
            EMPTY, (neighbors, nextGrid) -> handleEmptyCell(neighbors, nextGrid),
            FISH, (neighbors, nextGrid) -> handleFishCell(neighbors, nextGrid),
            SHARK, (neighbors, nextGrid) -> handleSharkCell(neighbors, nextGrid));

    public WaTorCell(String state, int row, int col) {
        super(state, row, col);
        energy = DEFAULT_ENERGY_MAP.get(state);
    }

    public void incrementEnergy() {
        energy++;
    }

    public void decrementEnergy() {
        energy--;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }

    public void resetReproductionTimer() {
        reproductionTimer = 0;
    }

    public void incrementReproductionTimer() {
        reproductionTimer++;
    }

    public int getReproductionTimer() {
        return reproductionTimer;
    }

    public List<List<WaTorCell>> setNextState(List<WaTorCell> neighbors,  List<List<WaTorCell>> nextGrid){
        nextGrid = handleCell.get(getState()).apply(neighbors, nextGrid);
        return nextGrid;
    }

    @Override
    public void updateState(){

    }

    // TODO: properly implement the methods below
    private List<List<WaTorCell>> handleEmptyCell(List<WaTorCell> neighbors, List<List<WaTorCell>> nextGrid) {

        return nextGrid;
    }

    private List<List<WaTorCell>> handleFishCell(List<WaTorCell> neighbors, List<List<WaTorCell>> nextGrid) {
        List<List<Integer>> potentialNewPositions = getAdjacentEnterableCells(neighbors, nextGrid, List.of(FISH, SHARK));
        if (potentialNewPositions.isEmpty()) {
            nextGrid.get(getRow()).set(getCol(), this);
            return nextGrid;
        }
        List<Integer> newPosition = getRandomNewPosition(potentialNewPositions);
        nextRow = newPosition.get(0);
        nextCol = newPosition.get(1);

        nextGrid.get(nextRow).set(nextCol, this);

        if (reproductionTimer == CHRONONS_TO_REPRODUCE) {
            reproductionTimer = 0;
            nextGrid.get(getRow()).set(getCol(), new WaTorCell(FISH, getRow(), getCol()));
        }
        else {
            reproductionTimer++;
            nextGrid.get(getRow()).set(getCol(), new WaTorCell(EMPTY, getRow(), getCol()));
        }
        return nextGrid;
    }

    private List<List<WaTorCell>> handleSharkCell(List<WaTorCell> neighbors, List<List<WaTorCell>> nextGrid) {
        return nextGrid;
    }

    private List<Integer> getRandomNewPosition(List<List<Integer>> potentialNewPositions) {
        Collections.shuffle(potentialNewPositions);
        return potentialNewPositions.get(0);
    }

    private List<List<Integer>> getAdjacentEnterableCells(List<WaTorCell> neighbors, List<List<WaTorCell>> nextGrid, List<String> invalidStates) {
        List<List<Integer>> potentialNewPositions = new ArrayList<>();
        for (WaTorCell cell : neighbors) {
            List<Integer> position = List.of(cell.getRow(), cell.getCol());
            // check that adjacent cell is empty AND has not been claimed by another creature with the same
            // or higher priority (sharks have "higher" priority than fish when moving to a cell because they
            // can eat fish, but a fish can't move into a cell that has been claimed by shark)
            if (cell.getState().equals(EMPTY) &&
                    !invalidStates.contains(nextGrid.get(cell.getRow()).get(cell.getCol()).getState())) {
                potentialNewPositions.add(position);
            }
        }

        return potentialNewPositions;
    }

}
