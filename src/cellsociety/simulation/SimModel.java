package cellsociety.simulation;

import cellsociety.ConfigReader;
import cellsociety.cell.Cell;
import cellsociety.grid.GridModel;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.util.List;

public abstract class SimModel {
    private GridModel gridModel;

    public SimModel(List<List<Cell>> grid) {
        this.gridModel = new GridModel(grid);
    }

    public void update() {
        List<List<Cell>> cells = gridModel.getCells();
        //saveCurrentConfig(cells);
        setNextStates(cells);
        updateStates(cells);
    }

    protected abstract String determineNextState(Cell cell, List<Cell> neighbors);

    protected abstract String getConfigFileIdentifier();

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

    //repetitive method here for testing MVC
    public List<List<Cell>> getCells(){
        return gridModel.getCells();
    }

    private void saveCurrentConfig(List<List<Cell>> cells) {

    }
}
