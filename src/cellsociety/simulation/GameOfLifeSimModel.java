package cellsociety.simulation;

import cellsociety.cell.Cell;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class GameOfLifeSimModel extends SimModel<Cell> {
    public static final String DEAD = "0"; //represented in data file as 0
    public static final String ALIVE = "1"; //represented in data file as 1
    public static final String CONFIG_FILE_PREFIX = "GOL";

    public GameOfLifeSimModel(List<List<Cell>> cells, SimController simController) {
        super(cells, simController);
    }

    @Override
    protected void determineAndSetNextState(Cell cell, List<Cell> neighbors) {
        cell.setNextState(neighbors);
    }

    @Override
    protected void updateStates(List<List<Cell>> cells) {
        for (List<Cell> row : cells) {
            for (Cell cell : row) {
                cell.updateState();
            }
        }
    }

    @Override
    protected String getConfigFileIdentifier() {
        return CONFIG_FILE_PREFIX;
    }

    @Override
    protected List<Cell> getNeighbors(Cell cell) {
        return getGridModel().getAllNeighbors(cell);
    }
}
