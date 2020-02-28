package cellsociety.simulation;

import cellsociety.cell.Cell;
import cellsociety.cell.WaTorCell;

import java.util.*;

public class WaTorSimModel extends SimModel <WaTorCell> {
    public static final String EMPTY = "0"; //represented in data file as 0
    public static final String FISH = "1"; //represented in data file as 1
    public static final String SHARK = "2"; //represented in data file as 1
    public static final String CONFIG_FILE_PREFIX = "WaTor";

    private List<List<WaTorCell>> nextGrid = new ArrayList<>();

    public WaTorSimModel(List<List<WaTorCell>> cells, SimController simController) {
        super(cells, simController);
        for (int row = 0; row < cells.size(); row++) {
            nextGrid.add(new ArrayList<>());
            for (int col = 0; col < cells.get(0).size(); col++) {
                nextGrid.get(row).add(new WaTorCell(EMPTY, row, col));
            }
        }
    }

    @Override
    protected void determineAndSetNextState(WaTorCell cell, List<WaTorCell> neighbors) {
        nextGrid = cell.setNextState(neighbors, nextGrid);
    }

    @Override
    protected void updateStates(List<List<WaTorCell>> cells) {
        //System.out.println("\n\n\n");
        for (int row = 0; row < cells.size(); row++) {
            //System.out.println();
            for (int col = 0; col < cells.get(0).size(); col++) {
                WaTorCell cell = nextGrid.get(row).get(col);
                cell.setRow(row);
                cell.setCol(col);
                cells.get(row).set(col, nextGrid.get(row).get(col));
                //System.out.print("," + nextGrid.get(row).get(col).getState());
            }
        }
        for (int row = 0; row < nextGrid.size(); row++) {
            for (int col = 0; col < nextGrid.get(0).size(); col++) {
                nextGrid.get(row).set(col, new WaTorCell(EMPTY, row, col));
            }
        }
    }

    @Override
    protected String getConfigFileIdentifier() {
        return CONFIG_FILE_PREFIX;
    }

    @Override
    protected List<WaTorCell> getNeighbors(WaTorCell cell) {
        return getGridModel().getCardinalNeighbors(cell);
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
