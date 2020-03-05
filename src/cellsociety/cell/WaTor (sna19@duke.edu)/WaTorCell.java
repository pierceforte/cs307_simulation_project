package cellsociety.cell.wator;

import cellsociety.cell.Cell;
import cellsociety.grid.Grid;

import java.util.*;

public abstract class WaTorCell extends Cell {
    public static final String EMPTY = "0"; //represented in data file as 0
    public static final String FISH = "1"; //represented in data file as 1
    public static final String SHARK = "2"; //represented in data file as 1

    private int nextRow;
    private int nextCol;

    public WaTorCell(String state, int row, int col) {
        super(state, row, col);
    }

    public int getNextRow() {
        return nextRow;
    }

    public int getNextCol() {
        return nextCol;
    }

    public void setNextRow(int nextRow) {
        this.nextRow = nextRow;
    }

    public void setNextCol(int nextCol) {
        this.nextCol = nextCol;
    }

    public abstract Grid<WaTorCell> setWhatToDoNext(List<WaTorCell> neighbors, Grid<WaTorCell> nextGrid);

}
