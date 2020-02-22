package cellsociety.simulation;

import cellsociety.cell.Cell;
import cellsociety.grid.GridModel;

import java.util.Collection;
import java.util.List;

public abstract class SimModel {

    private SimView simView;
    private SimController simController;
    private GridModel gridModel;

    public SimModel(Collection<Collection<Object>> grid) {
        simView = new SimView();
        simController = new SimController(this);
        this.gridModel = new GridModel(grid);
    }

    public void update() {
        setNextStates();
        updateStates();
    }

    private void setNextStates() {
        List<List<Cell>> grid = gridModel.getCells();
        for (List<Cell> row : grid) {
            for (Cell cell : row) {
                cell.setNextState();
            }
        }
    }

    private void updateStates() {
        List<List<Cell>> grid = gridModel.getCells();
        for (List<Cell> row : grid) {
            for (Cell cell : row) {
                cell.updateState();
            }
        }
    }
}
