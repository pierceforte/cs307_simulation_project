package cellsociety.cell.Fire;

import java.util.List;

public class BurningCell extends FireCell {
    public static final String STATE = FireCell.BURNING;

    public BurningCell(int row, int col) {
        super(STATE, row, col);
    }

    @Override
    public void setWhatToDoNext(List<FireCell> neighbors) {
        setNextState(EMPTY);
    }
}
