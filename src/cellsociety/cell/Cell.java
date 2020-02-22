package cellsociety.cell;

public class Cell {

    public static final String DEAD = "DEAD"; //represented in data file as 0
    public static final String ALIVE = "ALIVE"; //represented in data file as 1

    String state, nextState;
    int row, col;

    public Cell(int state, int row, int col){
        if (state == 0){
            this.state = DEAD;
        } else if (state == 1){
            this.state = ALIVE;
        }

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

}
