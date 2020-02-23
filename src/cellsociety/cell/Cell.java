package cellsociety.cell;

public class Cell {

    private String state, nextState;
    private int row, col;

    public Cell(String state, int row, int col){
        this.state = state;
        this.row = row;
        this.col = col;
    }

    public String getState(){
        return state;
    }

    public void setNextState(String state){
        this.nextState = state;
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

}
