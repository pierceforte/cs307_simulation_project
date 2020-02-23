package cellsociety.simulation;

import cellsociety.cell.Cell;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GameOfLifeSimModel extends SimModel {
    public static final String DEAD = "DEAD"; //represented in data file as 0
    public static final String ALIVE = "ALIVE"; //represented in data file as 1

    private Map<String, Function<Integer, String>> handleCell = Map.of(
            ALIVE, (numNeighbors) -> handleDeadCell(numNeighbors),
            DEAD, (numNeighbors) -> handleLivingCell(numNeighbors));

    public GameOfLifeSimModel(List<List<String>> grid) {
        super(grid);
    }

    @Override
    protected String determineNextState(Cell cell, List<Cell> neighbors) {
        int numNeighbors = getNumNeighbors(neighbors);
        String curCellState = cell.getState();
        return handleCell.get(curCellState).apply(numNeighbors);
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
