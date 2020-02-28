package cellsociety.simulation;

import cellsociety.ConfigReader;
import cellsociety.cell.Cell;
import cellsociety.grid.GridModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.util.List;

public abstract class SimModel <T extends Cell>{
    private GridModel gridModel;
    private SimController simController;
    private SimView simView;

    public SimModel(List<List<String>> cellStates, SimController simController) {
        List<List<T>> grid = createGrid(cellStates);
        this.gridModel = new GridModel(grid);
        this.simController = simController;
    }

    public void update() {
        List<List<T>> cells = gridModel.getCells();
        saveCurrentConfig(cells);
        setNextStates(cells);
        updateStates(cells);
    }

    //repetitive method here for testing MVC
    public List<List<T>> getCells(){
        return gridModel.getCells();
    }

    public SimController getSimController() {
        return simController;
    }

    public SimView getSimView() {
        return simView;
    }

    protected abstract List<List<T>> createGrid(List<List<String>> cellStates);

    protected abstract void setNextStates(List<List<T>> cells);

    protected abstract void determineAndSetNextState(T cell, List<T> neighbors);

    protected abstract void updateStates(List<List<T>> cells);

    protected abstract String getConfigFileIdentifier();

    protected abstract List<T> getNeighbors(T cell);

    protected GridModel getGridModel(){
        return gridModel;
    }

    private void saveCurrentConfig(List<List<T>> cells) {
        try {
            String currentConfigFileName = SimController.CURRENT_CONFIG_FILE_PREFIX + getConfigFileIdentifier() +
                    SimController.CONFIG_FILE_SUFFIX;
            PrintWriter pw = new PrintWriter(new File(this.getClass().getClassLoader().getResource(currentConfigFileName).getPath()));
            pw.println(cells.size() + ConfigReader.DATA_REGEX + cells.get(0).size());

            for (int row = 0; row < cells.size(); row++) {
                int cols = cells.get(0).size();
                if (cols == 0) {
                    break;
                }
                String line = cells.get(row).get(0).getState();
                for (int col = 1; col < cols; col++) {
                    line += ConfigReader.DATA_REGEX + cells.get(row).get(col).getState();
                }
                pw.println(line);
            }
            pw.close();
        } catch (FileNotFoundException e) {
            //logError(e);
            System.exit(0);
        }
        catch (NullPointerException e) {
            //logError(e);
            // don't save file
            e.printStackTrace();
        }
    }
}
