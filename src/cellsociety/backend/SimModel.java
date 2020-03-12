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
        this.grid = new Grid(cellStates, getOrderedCellStatesMap());
        this.simController = simController;
    }

    public void update() {
        setNextStates(grid);
        updateStates(grid);
    }

    public void clickResponse(int row, int col){
        Cell curCell = grid.get(row, col);
        String curState = curCell.getState();
        List<String> states = new ArrayList<>(getOrderedCellStatesMap().keySet());
        int curStateIndex = states.indexOf(curState);
        String newState = (curStateIndex == states.size()-1) ? states.get(0) : states.get(curStateIndex+1);
        Cell newCell = grid.createCell(newState, getOrderedCellStatesMap(), row, col);
        grid.set(row, col, newCell);
    }

    public Grid getGrid(){
        return grid;
    }

    public abstract TreeMap<String, Class> getOrderedCellStatesMap();

    protected abstract void setNextStates(Grid<T> grid);

    protected abstract void updateStates(Grid<T> grid);

    protected abstract List<T> getNeighbors(T cell);

}
