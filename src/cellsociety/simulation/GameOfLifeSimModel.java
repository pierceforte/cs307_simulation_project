package cellsociety.simulation;

import cellsociety.cell.Cell;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class GameOfLifeSimModel extends SimModel {
    public static final String DEAD = "DEAD"; //represented in data file as 0
    public static final String ALIVE = "ALIVE"; //represented in data file as 1

    public GameOfLifeSimModel(List<List<String>> grid) {
        super(grid);
    }

    protected String determineNextState(Cell cell, List<Cell> neighbors) throws Exception {
        int numNeighbors = getNumNeighbors(neighbors);
        Map<String, Callable> handleCell = Map.of(
                ALIVE, () -> handleDeadCell(numNeighbors),
                DEAD, () -> handleLivingCell(numNeighbors));
        String curCellState = cell.getState();
        return (String) handleCell.get(curCellState).call();
    }

    private String handleDeadCell(int numNeighbors) {
        if (numNeighbors == 3) {
            return ALIVE;
        }
        return DEAD;
    }

    private String handleLivingCell(int numNeighbors) {
        if (numNeighbors < 2 || numNeighbors > 3) {
            return DEAD;
        }
        return ALIVE;
    }

    private int getNumNeighbors(List<Cell> neighbors) {
        int numNeighbors = 0;
        for (Cell neighbor : neighbors) {
            if (isCellAlive(neighbor)) numNeighbors++;
        }
        return numNeighbors;
    }

    private boolean isCellAlive(Cell cell) {
        return cell.getState().equals(ALIVE);
    }
}
