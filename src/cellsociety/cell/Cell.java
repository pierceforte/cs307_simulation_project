package cellsociety.cell;

public class Cell {
    public static final int ROW_INDEX = 0;
    public static final int COL_INDEX = 1;

    private String state, nextState;
    private int row, col;
    private CellView cellView;

    public Cell(String state, int row, int col){
        this.state = state;
        this.row = row;
        this.col = col;
        cellView = new CellView();
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState(){
        return state;
    }

    public String getNextState(){
        return nextState;
    }

    public void setNextState(String nextState) {
        this.nextState = nextState;
    }


    public void updateState(){
        this.state = this.nextState;
    }

    //makes it easier to compare states
    public boolean isState(String state){
        return this.state.equals(state);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public void setCol(int col) {
        this.col = col;
    }

    public CellView getView() {
        return cellView;
    }
}
