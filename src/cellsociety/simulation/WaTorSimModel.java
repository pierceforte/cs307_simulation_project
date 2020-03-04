package cellsociety.simulation;

import cellsociety.cell.wator.EmptyCell;
import cellsociety.cell.wator.FishCell;
import cellsociety.cell.wator.SharkCell;
import cellsociety.cell.wator.WaTorCell;

import java.util.*;

public class WaTorSimModel extends SimModel <WaTorCell> {
    public static final String CONFIG_FILE_PREFIX = "WaTor";

    private List<List<WaTorCell>> nextGrid = new ArrayList<>();

    public WaTorSimModel(List<List<String>> cellStates, SimController simController) {
        super(cellStates, simController);
        initializeNextGrid();
    }

    @Override
    protected List<List<WaTorCell>> createGrid(List<List<String>> cellStates) {
        List<List<WaTorCell>> grid = new ArrayList<>();
        for (int row = 0; row < cellStates.size(); row++) {
            grid.add(new ArrayList<>());
            for (int col = 0; col < cellStates.size(); col++) {
                WaTorCell cell;
                if (cellStates.get(row).get(col).equals(WaTorCell.EMPTY)) {
                    cell = new EmptyCell(row, col);
                }
                else if (cellStates.get(row).get(col).equals(WaTorCell.FISH)) {
                    cell = new FishCell(row, col);
                }
                // TODO: throw error if invalid state
                else {
                    cell = new SharkCell(row, col);
                }
                grid.get(row).add(cell);
            }
        }
        return grid;
    }

    @Override
    protected void setNextStates(List<List<WaTorCell>> cells) {
        Set<List<Integer>> posOfFishThatWillBeEaten = new HashSet<>();
        // give priority to sharks by letting them choose where to go first.
        // we do this because otherwise, a fish can only be eaten by a shark
        // if it is blocked from moving. we also would like to assume that
        // sharks are faster than fish.
        for (List<WaTorCell> row : cells) {
            for (WaTorCell cell : row) {
                if (cell instanceof SharkCell) {
                    determineAndSetNextState(cell, getNeighbors(cell));
                    if (((SharkCell) cell).getPosOfFishToEatNext() != null) {
                        posOfFishThatWillBeEaten.add(((SharkCell) cell).getPosOfFishToEatNext());
                    }
                }
            }
        }
        // get rid of fish that will be eaten
        for (int row = 0; row < cells.size(); row++) {
            for (int col = 0; col < cells.get(0).size(); col++) {
                for (List<Integer> posOfFishToBeEaten : posOfFishThatWillBeEaten) {
                    if (row == posOfFishToBeEaten.get(0) && col == posOfFishToBeEaten.get(1)) {
                        cells.get(row).set(col, new EmptyCell(row, col));
                    }
                }
            }
        }

        // then let fish that won't be eaten move
        for (List<WaTorCell> row : cells) {
            for (WaTorCell cell : row) {
                if (cell.getState().equals(WaTorCell.FISH)) {
                    determineAndSetNextState(cell, getNeighbors(cell));
                }
            }
        }
    }

    @Override
    protected void determineAndSetNextState(WaTorCell cell, List<WaTorCell> neighbors) {
        nextGrid = cell.setWhatToDoNext(neighbors, nextGrid);
    }

    @Override
    protected void updateStates(List<List<WaTorCell>> cells) {
        // update actual states of current grid
        for (int row = 0; row < cells.size(); row++) {
            for (int col = 0; col < cells.get(0).size(); col++) {
                WaTorCell cell = nextGrid.get(row).get(col);
                cell.setRow(row);
                cell.setCol(col);
                cells.get(row).set(col, nextGrid.get(row).get(col));
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
        return getGridModel().getCardinalNeighbors(cell);
    }

    private void initializeNextGrid() {
        nextGrid.clear();
        for (int row = 0; row < getCells().size(); row++) {
            nextGrid.add(new ArrayList<>());
            for (int col = 0; col < getCells().get(0).size(); col++) {
                nextGrid.get(row).add(new EmptyCell(row, col));
            }
        }
    }

}

/*
BASIC RULES:
Fish
- At each chronon, a fish moves randomly to one of the adjacent unoccupied squares.
If there are no free squares, no movement takes place.
- Once a fish has survived a certain number of chronons it may reproduce.
This is done as it moves to a neighbouring square, leaving behind a new fish in its old position.
Its reproduction time is also reset to zero.

Sharks
- At each chronon, a shark moves randomly to an adjacent square occupied by a fish.
If there is none, the shark moves to a random adjacent unoccupied square. If there are no free squares, no movement takes place.
- At each chronon, each shark is deprived of a unit of energy.
- Upon reaching zero energy, a shark dies.
- If a shark moves to a square occupied by a fish, it eats the fish and earns a certain amount of energy.
- Once a shark has survived a certain number of chronons it may reproduce in exactly the same way as the fish.


CONFLICTS:
- More than one fish or sharks move to the same cell
    -  I think a good resolution here is to keep a hashmap of next state cells. In the determine next state function,
       fish and sharks will check if the cell they are trying to move to has already been "chosen" by another creature. If
       this is the case, then they will randomly choose another cell to move to for its next state.


 */
