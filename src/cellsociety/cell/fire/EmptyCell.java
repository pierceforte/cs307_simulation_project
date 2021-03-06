package cellsociety.cell.fire;

import java.util.List;

public class EmptyCell extends FireCell {
    public static final String STATE = FireCell.EMPTY;
    public static final double PROB_TREE = 0.5;

    public EmptyCell(int row, int col) {
        super(STATE, row, col);

    }

    @Override
    public void setWhatToDoNext(List<FireCell> neighbors) {
        if (rnd.nextDouble() < PROB_TREE){
            setNextState(TREE);
        } else {
            setNextState(EMPTY);
        }
    }
}
