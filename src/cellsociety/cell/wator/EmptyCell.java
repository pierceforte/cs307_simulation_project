package cellsociety.cell.wator;

import java.util.List;

public class EmptyCell extends WaTorCell {
    public static final String STATE = WaTorCell.EMPTY;

    public EmptyCell(int row, int col) {
        super(STATE, row, col);
    }

    @Override
    public List<List<WaTorCell>> setWhatToDoNext(List<WaTorCell> neighbors, List<List<WaTorCell>> nextGrid) {
        return nextGrid;
    }

}
