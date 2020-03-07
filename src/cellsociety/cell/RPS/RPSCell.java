package cellsociety.cell.RPS;

import cellsociety.cell.Cell;

public class RPSCell extends Cell {
    static RPSCell cell;
    public static final String STATE = cell.getState();

        public RPSCell(String state, int row, int col) {
            super(state, row, col);
        }

}
