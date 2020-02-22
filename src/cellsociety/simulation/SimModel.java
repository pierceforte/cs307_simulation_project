package cellsociety.simulation;

import cellsociety.cell.Cell;
import cellsociety.grid.GridModel;

import java.util.Collection;
import java.util.List;

public abstract class SimModel {

    private SimView simView;
    private SimController simController;
    private GridModel gridModel;

    public SimModel(List<List<String>> grid) {
        simView = new SimView();
        simController = new SimController(this);
        this.gridModel = new GridModel(grid);
    }

    public void update() {
        List<List<Cell>> cells = gridModel.getCells();
        setNextStates(cells);
        updateStates(cells);
    }

    protected abstract String determineNextState(Cell cell, List<Cell> neighbors) throws Exception;

    private void setNextStates(List<List<Cell>> cells) {
        for (List<Cell> row : cells) {
            for (Cell cell : row) {
                try {
                    String nextState = determineNextState(cell, gridModel.getNeighbors(cell));
                    cell.setNextState(nextState);
                }
                catch (Exception e) {

                }
            }
        }
    }

    private void updateStates(List<List<Cell>> cells) {
        for (List<Cell> row : cells) {
            for (Cell cell : row) {
                cell.updateState();
            }
        }
    }
}
