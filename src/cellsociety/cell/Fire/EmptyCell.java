package cellsociety.cell.Fire;

import java.util.List;

public class EmptyCell extends FireCell {
    public static final String STATE = FireCell.EMPTY;
    private double probTree;

    public EmptyCell(int row, int col, double probTree) {
        super(STATE, row, col);
        this.probTree = probTree;

    }

    @Override
    public void setWhatToDoNext(List<FireCell> neighbors) {
        if (Math.random() < probTree){
            setNextState(TREE);
        } else {
            setNextState(EMPTY);
        }
    }
}
