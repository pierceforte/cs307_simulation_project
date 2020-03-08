package cellsociety.backend;

import cellsociety.SimController;
import cellsociety.cell.Cell;
import cellsociety.frontend.SimView;
import cellsociety.grid.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public abstract class SimModel <T extends Cell>{
    private Grid grid;
    private SimController simController;
    private SimView simView;

    public SimModel(List<List<String>> cellStates, SimController simController) {
        this.grid = new Grid(cellStates, getOrderedCellTypesMap());
        this.simController = simController;
    }

    public void update() {
        setNextStates(grid);
        updateStates(grid);
    }

    public void clickResponse(int row, int col){
        Cell cell = grid.get(row, col);
        String curState = cell.getState();
        List<String> states = new ArrayList<>(getOrderedCellTypesMap().keySet());
        int curStateIndex = states.indexOf(curState);
        String newState = (curStateIndex == states.size()-1) ? states.get(0) : states.get(curStateIndex+1);
        cell.setState(newState);
    }

    public Grid getGrid(){
        return grid;
    }

    public abstract TreeMap<String, Class> getOrderedCellTypesMap();

    protected abstract void setNextStates(Grid<T> grid);

    protected abstract void updateStates(Grid<T> grid);

    protected abstract List<T> getNeighbors(T cell);

}
