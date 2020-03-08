package cellsociety.backend;

import cellsociety.SimController;
import cellsociety.cell.wator.*;
import cellsociety.grid.Grid;

import java.util.*;

public class WaTorSimModel extends SimModel <WaTorCell> {
    public static final String CONFIG_FILE_PREFIX = "WaTor";

    private Grid<WaTorCell> nextGrid;

    public WaTorSimModel(List<List<String>> cellStates, SimController simController) {
        super(cellStates, simController);
        initializeNextGrid();
    }

    @Override
    public TreeMap<String, Class> getOrderedCellTypesMap() {
        TreeMap<String, Class> cellTypes = new TreeMap<>();
        cellTypes.put(WaTorCell.EMPTY, EmptyCell.class);
        cellTypes.put(WaTorCell.FISH, FishCell.class);
        cellTypes.put(WaTorCell.SHARK, SharkCell.class);
        return cellTypes;
    }

    @Override
    protected void setNextStates(Grid<WaTorCell> grid) {
        Set<List<Integer>> posOfFishThatWillBeEaten = new HashSet<>();
        // give priority to sharks by letting them choose where to go first.
        // we do this because otherwise, a fish can only be eaten by a shark
        // if it is blocked from moving. we also would like to assume that
        // sharks are faster than fish.

        for (int row = 0; row < grid.getNumRows(); row++) {
            for (int col = 0; col < grid.getNumCols(); col++) {
                WaTorCell cell = grid.get(row, col);
                if (cell instanceof SharkCell) {
                    nextGrid = cell.setWhatToDoNext(getNeighbors(cell), nextGrid);
                    if (((SharkCell) cell).getPosOfFishToEatNext() != null) {
                        posOfFishThatWillBeEaten.add(((SharkCell) cell).getPosOfFishToEatNext());
                    }
                }
            }
        }


        // get rid of fish that will be eaten
        for (int row = 0; row < grid.getNumRows(); row++) {
            for (int col = 0; col < grid.getNumCols(); col++) {
                for (List<Integer> posOfFishToBeEaten : posOfFishThatWillBeEaten) {
                    if (row == posOfFishToBeEaten.get(0) && col == posOfFishToBeEaten.get(1)) {
                        grid.set(row, col, new EmptyCell(row, col));
                    }
                }
            }
        }

        // then let fish that won't be eaten move
        for (int row = 0; row < grid.getNumRows(); row++) {
            for (int col = 0; col < grid.getNumCols(); col++) {
                WaTorCell cell = grid.get(row, col);
                if (cell.getState().equals(WaTorCell.FISH)) {
                    nextGrid = cell.setWhatToDoNext(getNeighbors(cell), nextGrid);
                }
            }
        }
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
    protected String getConfigFileIdentifier() {
        return CONFIG_FILE_PREFIX;
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

}
