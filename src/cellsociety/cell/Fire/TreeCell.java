package cellsociety.cell.Fire;

import cellsociety.cell.WaTor.WaTorCell;

import java.util.List;
import java.util.Random;

public class TreeCell extends FireCell{
    public static final String STATE = FireCell.TREE;
    private double probCatch;


    public TreeCell(int row, int col, double probCatch) {
        super(STATE, row, col);
        this.probCatch = probCatch;
    }


    @Override
    public void setWhatToDoNext(List<FireCell> neighbors) {
        if(Math.random() < probCatch){
            setNextState(BURNING);
        } else setNextState(TREE);
    }
}
