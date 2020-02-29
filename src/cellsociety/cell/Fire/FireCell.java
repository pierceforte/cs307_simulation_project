package cellsociety.cell.Fire;

import cellsociety.cell.Cell;

import java.util.List;

public abstract class FireCell extends Cell {

    public static final String EMPTY = "0"; //represented in data file as 0
    public static final String TREE = "1"; //represented in data file as 1
    public static final String BURNING = "2"; //represented in data file as 2

    public FireCell(String state, int row, int col) {
        super(state, row, col);
    }

    public abstract void setWhatToDoNext(List<FireCell> neighbors);


}
