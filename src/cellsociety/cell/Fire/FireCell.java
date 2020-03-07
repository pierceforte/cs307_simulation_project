package cellsociety.cell.Fire;

import cellsociety.cell.Cell;

import java.util.List;
import java.util.Random;

public abstract class FireCell extends Cell {

    public static final String EMPTY = "0"; //represented in data file as 0
    public static final String TREE = "1"; //represented in data file as 1
    public static final String BURNING = "2"; //represented in data file as 2

    protected Random rnd;


    public FireCell(String state, int row, int col) {
        super(state, row, col);
        rnd = new Random();
    }

    public abstract void setWhatToDoNext(List<FireCell> neighbors);


    public void setRndSeed(int i){
        rnd.setSeed(i);
    }
}
