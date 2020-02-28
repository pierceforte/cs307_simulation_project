package cellsociety.cell;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Cell {
    public static final String DEAD = "0"; //represented in data file as 0
    public static final String ALIVE = "1"; //represented in data file as 1

    private String state, nextState;
    private int row, col;
    private Map<String, Function<Integer, String>> handleCell = Map.of(
            ALIVE, (numNeighbors) -> handleLivingCell(numNeighbors),
            DEAD, (numNeighbors) -> handleDeadCell(numNeighbors));

    public Cell(String state, int row, int col){
        this.state = state;
        this.row = row;
        this.col = col;
    }

    public String getState(){
        return state;
    }

    public void setNextState(List<Cell> neighbors){
        int numNeighbors = getNumNeighbors(neighbors);
        String nextState = handleCell.get(state).apply(numNeighbors);
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

    public String handleDeadCell(int numNeighbors) {
        if (numNeighbors == 3) {
            return ALIVE;
        }
        return DEAD;
    }

    public String handleLivingCell(int numNeighbors) {
        if (numNeighbors < 2 || numNeighbors > 3) {
            return DEAD;
        }
        return ALIVE;
    }

    public <T extends Cell> int getNumNeighbors(List<T> neighbors) {
        int numNeighbors = 0;
        for (Cell neighbor : neighbors) {
            if (isCellAlive(neighbor)) numNeighbors++;
        }
        return numNeighbors;
    }

    public boolean isCellAlive(Cell cell) {
        return cell.getState().equals(ALIVE);
    }

}
