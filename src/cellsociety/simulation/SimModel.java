package cellsociety.simulation;

import cellsociety.cell.Cell;
import cellsociety.grid.Grid;

import java.util.List;
import java.util.Map;

public abstract class SimModel <T extends Cell>{
    private Grid grid;
    private SimController simController;
    private SimView simView;

    public SimModel(List<List<String>> cellStates, SimController simController) {
        this.grid = new Grid(cellStates, getCellTypesMap());
        this.simController = simController;
    }

    public void update() {
        setNextStates(grid);
        updateStates(grid);
    }

    public void clickResponse(int row, int col){
        Cell cell = grid.get(row, col);
        cell.setNextState("1");
        update();
    }

    //repetitive method here for testing MVC
    public List<List<T>> getCells(){
        return grid.getCells();
    }

    public SimController getSimController() {
        return simController;
    }

    public SimView getSimView() {
        return simView;
    }

    protected abstract Map<String, Class> getCellTypesMap();

    protected abstract void setNextStates(Grid<T> grid);

    protected abstract void updateStates(Grid<T> grid);

    protected abstract String getConfigFileIdentifier();

    protected abstract List<T> getNeighbors(T cell);

    protected Grid getGrid(){
        return grid;
    }

}
