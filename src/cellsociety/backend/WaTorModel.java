package cellsociety.backend;

import cellsociety.SimController;
import cellsociety.cell.wator.*;
import cellsociety.grid.Grid;

import java.util.*;

/**
 * This class inherits from the abstract class SimModel, implementing the backend for the Wa-Tor simulation.
 *
 * This class defines the rules for each update, relying on the WaTorCell and its different implementations.
 *
 * It is important to note that this simulation implements a "next grid"; because cells move to random locations on each
 * update, it is essential to keep track of which random locations have already been taken. This process still results in
 * a two-pass system (where cells choose where to move independently of where other cells **are**), but the cells do not
 * choose where to move independently of where they **will be**.
 *
 * It also important to note the assumption that Shark cells are granted the opportunity to move first; this assumption
 * was made because it is assumed that sharks are faster than fish, and, otherwise, sharks could only eat fish if the
 * fish were cornered in (because the fish would move otherwise).
 *
 * @author Pierce Forte
 */
public class WaTorModel extends SimModel <WaTorCell> {
    public static final String CONFIG_FILE_PREFIX = "WaTor";

    private Grid<WaTorCell> nextGrid;

    /**
     * The constructor to create a Wa-Tor simulation's backend.
     * @param cellStates the initial cell states, as collected from the csv file
     */
    public WaTorModel(List<List<String>> cellStates) {
        super(cellStates);
        initializeNextGrid();
    }

    @Override
    public TreeMap<String, Class> getOrderedCellStatesMap() {
        TreeMap<String, Class> cellTypes = new TreeMap<>();
        cellTypes.put(WaTorCell.EMPTY, EmptyCell.class);
        cellTypes.put(WaTorCell.FISH, FishCell.class);
        cellTypes.put(WaTorCell.SHARK, SharkCell.class);
        return cellTypes;
    }

    @Override
    protected void setNextStates(Grid<WaTorCell> grid) {
        Set<List<Integer>> posOfFishThatWillBeEaten = new HashSet<>();
        // randomize order of update; it is important to note that a cell's row and col (instance variables in
        // cell class) are independent from their row and col in the Grid object, so we can randomize the grid
        // without actually changing where each cell is located
        Grid<WaTorCell> randomGrid = new Grid(grid);
        randomGrid.shuffle();
        // give priority to sharks by letting them choose where to go first.
        // we do this because otherwise, a fish can only be eaten by a shark
        // if it is blocked from moving. we also would like to assume that
        // sharks are faster than fish
        setNextStateForSharks(grid, randomGrid, posOfFishThatWillBeEaten);
        // get rid of fish that will be eaten
        removeFishThatWillBeEaten(grid, randomGrid, posOfFishThatWillBeEaten);
        // then let fish that won't be eaten move
        setNextStateForRemainingFish(randomGrid);
    }

    // TODO: eliminate duplication here and in SegregationSimModel
    @Override
    protected void updateStates(Grid<WaTorCell> grid) {
        // update actual states of current grid
        for (int row = 0; row < nextGrid.getNumRows(); row++) {
            for (int col = 0; col < nextGrid.getNumCols(); col++) {
                WaTorCell cell = nextGrid.get(row, col);
                cell.setRow(row);
                cell.setCol(col);
                grid.set(row, col, nextGrid.get(row, col));
            }
        }
        // clear the "next" grid to eliminate changes that have already been made
        initializeNextGrid();
    }
    @Override
    protected List<WaTorCell> getNeighbors(WaTorCell cell) {
        return getGrid().getCardinalNeighbors(cell);
    }

    private void initializeNextGrid() {
        nextGrid = new Grid<WaTorCell>(getGrid());
        for (int row = 0; row < nextGrid.getNumRows(); row++) {
            for (int col = 0; col < nextGrid.getNumCols(); col++) {
                nextGrid.set(row, col, new EmptyCell(row, col));
            }
        }
    }

    private void setNextStateForSharks(Grid<WaTorCell> grid, Grid<WaTorCell> randomGrid, Set<List<Integer>> posOfFishThatWillBeEaten) {
        for (int randRow = 0; randRow < randomGrid.getNumRows(); randRow++) {
            for (int randCol = 0; randCol < randomGrid.getNumCols(); randCol++) {
                WaTorCell cell = randomGrid.get(randRow, randCol);
                if (cell instanceof SharkCell) {
                    nextGrid = cell.setWhatToDoNext(getNeighbors(cell), nextGrid);
                    if (((SharkCell) cell).getPosOfFishToEatNext() != null) {
                        posOfFishThatWillBeEaten.add(((SharkCell) cell).getPosOfFishToEatNext());
                    }
                    int cellRow = cell.getRow();
                    int cellCol = cell.getCol();
                    grid.set(cellRow, cellCol, new EmptyCell(cellRow, cellCol));
                    randomGrid.set(randRow, randCol, new EmptyCell(cellRow, cellCol));
                }
            }
        }
    }

    private void removeFishThatWillBeEaten(Grid<WaTorCell> grid, Grid<WaTorCell> randomGrid, Set<List<Integer>> posOfFishThatWillBeEaten) {
        for (int randRow = 0; randRow < randomGrid.getNumRows(); randRow++) {
            for (int randCol = 0; randCol < randomGrid.getNumCols(); randCol++) {
                WaTorCell cell = randomGrid.get(randRow, randCol);
                for (List<Integer> posOfFishToBeEaten : posOfFishThatWillBeEaten) {
                    int cellRow = cell.getRow();
                    int cellCol = cell.getCol();
                    if (cellRow == posOfFishToBeEaten.get(0) && cellCol  == posOfFishToBeEaten.get(1)) {
                        grid.set(cellRow, cellCol, new EmptyCell(cellRow, cellCol));
                        randomGrid.set(randRow, randCol, new EmptyCell(cellRow, cellCol));
                    }
                }
            }
        }
    }

    private void setNextStateForRemainingFish(Grid<WaTorCell> randomGrid) {
        for (int randRow = 0; randRow < randomGrid.getNumRows(); randRow++) {
            for (int randCol = 0; randCol < randomGrid.getNumCols(); randCol++) {
                WaTorCell cell = randomGrid.get(randRow, randCol);
                nextGrid = cell.setWhatToDoNext(getNeighbors(cell), nextGrid);
            }
        }
    }

}
