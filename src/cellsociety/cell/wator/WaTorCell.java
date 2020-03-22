package cellsociety.cell.wator;

import cellsociety.cell.Cell;
import cellsociety.grid.Grid;

import java.util.*;

/**
 * This abstract class inherits from the abstract class Cell, implementing the backend for the Wa-Tor cells.
 *
 * This class's implementations define how each Wa-Tor cell type is updated in the grid.
 *
 * @author Pierce Forte
 */
public abstract class WaTorCell extends Cell {
    public static final String EMPTY = "0"; //represented in data file as 0
    public static final String FISH = "1"; //represented in data file as 1
    public static final String SHARK = "2"; //represented in data file as 1

    private int nextRow;
    private int nextCol;

    /**
     * The constructor to create a WaTorCell's backend.
     * @param state The state (or "type") of the cell to be created
     * @param row The row in which the cell is located
     * @param col The column in which the cell is located
     */
    public WaTorCell(String state, int row, int col) {
        super(state, row, col);
    }

    /**
     * Get the next row of this cell.
     * @return The next row of this cell
     */
    public int getNextRow() {
        return nextRow;
    }

    /**
     * Get the next column of this cell.
     * @return The next column of this cell
     */
    public int getNextCol() {
        return nextCol;
    }

    /**
     * Set the next row of this cell.
     * @param nextRow The next row of this cell
     */
    public void setNextRow(int nextRow) {
        this.nextRow = nextRow;
    }

    /**
     * Set the next column of this cell.
     * @param nextCol The next column of this cell
     */
    public void setNextCol(int nextCol) {
        this.nextCol = nextCol;
    }

    /**
     * Abstract method to set what the cell should do next.
     * @param neighbors A list of this cell's neighbors
     * @param nextGrid The grid to keep track of which next positions have already been taken
     */
    public abstract Grid<WaTorCell> setWhatToDoNext(List<WaTorCell> neighbors, Grid<WaTorCell> nextGrid);

}
