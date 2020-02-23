package cellsociety.simulation;

import cellsociety.cell.Cell;
import cellsociety.grid.GridModel;

import java.util.List;

public abstract class SimModel {

    private SimView simView;
    private SimController simController;
    private GridModel gridModel;

    public SimModel(List<List<Cell>> cells) {
        simView = new SimView();
        simController = new SimController(this);
        this.gridModel = new GridModel(cells);
    }

    public void update() {
        List<List<Cell>> cells = gridModel.getCells();
        setNextStates(cells);
        updateStates(cells);
        simView.updateCellGrid(cells);
    }

    protected abstract String determineNextState(Cell cell, List<Cell> neighbors);

    private void setNextStates(List<List<Cell>> cells) {
        for (List<Cell> row : cells) {
            for (Cell cell : row) {
                String nextState = determineNextState(cell, gridModel.getNeighbors(cell));
                cell.setNextState(nextState);
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
