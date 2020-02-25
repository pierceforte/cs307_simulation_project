package cellsociety.simulation;

import cellsociety.cell.Cell;
import cellsociety.grid.GridModel;
import javafx.scene.paint.Color;

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

    protected abstract String getConfigFilePrefix();

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

    /*
    private void saveCurrentConfig(List<List<Cell>> cells) {
        try {
            PrintWriter pw = new PrintWriter("current_config.csv");
            pw.println(cells.size() + "," + cells.get(0).size());
            for (String entry : highscores) {
                if (numEntries < NUM_HIGHSCORES_STORED) {
                    pw.println(entry);
                    numEntries++;
                }
                else break;
            }
            pw.close();
        } catch (FileNotFoundException e) {
            logError(e);
        }
    }
     */
}
