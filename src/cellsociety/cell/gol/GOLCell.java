package cellsociety.cell.gol;

import cellsociety.cell.Cell;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GOLCell extends Cell {
    public static final String DEAD = "0"; //represented in data file as 0
    public static final String ALIVE = "1"; //represented in data file as 1

    private Map<String, Function<Integer, String>> handleCell = Map.of(
            ALIVE, (numNeighbors) -> handleLivingCell(numNeighbors),
            DEAD, (numNeighbors) -> handleDeadCell(numNeighbors));

    public GOLCell(String state, int row, int col) {
        super(state, row, col);
    }

    public <T extends Cell> void setWhatToDoNext(List<T> neighbors){
        int numNeighbors = getNumNeighbors(neighbors);
        String nextState = handleCell.get(getState()).apply(numNeighbors);
        setNextState(nextState);
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
