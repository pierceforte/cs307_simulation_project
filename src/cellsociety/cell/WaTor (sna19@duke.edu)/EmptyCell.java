package cellsociety.cell.wator;

import cellsociety.grid.Grid;

import java.util.List;

public class EmptyCell extends WaTorCell {
    public static final String STATE = WaTorCell.EMPTY;

    public EmptyCell(int row, int col) {
        super(STATE, row, col);
    }

    @Override
    public Grid<WaTorCell> setWhatToDoNext(List<WaTorCell> neighbors, Grid<WaTorCell> nextGrid) {
        return nextGrid;
    }

}
