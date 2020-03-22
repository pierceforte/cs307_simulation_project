package cellsociety.backend;

import cellsociety.SimController;
import cellsociety.cell.Cell;
import cellsociety.frontend.SimView;
import cellsociety.grid.Grid;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * This class is an abstract class that is used to create each of the simulation model's backend.
 *
 * It defines essential, abstract methods that must be implemented to update the grid on each step.
 * This update includes the implementation of a two-pass system, where the next state is first
 * determined, and it is then set for each cell.
 *
 * This class is depended on the implementation of the Cell class used for the given simulation. It also
 * appears to be dependent on the associated SimController and SimView; however, these dependencies are
 * not necessary/are not utilized and would have been eliminated if the project were not ended early.
 *
 * This class can be used to create a specific implementation of a simulation and handle its backend as
 * the overall application runs.
 *
 * Note that if this project had not been ended early due to COVID-19, a high priority next step would have
 * been to eliminate some duplication seen in subclasses. This would likely have been done by creating
 * interfaces or abstract classes for the different simulation "types", as in simulations that required a
 * next grid to keep track of where cells chose to go, simulations that did not require this next grid, and a
 * mix of both.
 *
 * @author Pierce Forte
 */
public abstract class SimModel <T extends Cell>{
    private Grid grid;
    private SimController simController;
    private SimView simView;

    /**
     * The constructor to create a simulation's backend.
     * @param cellStates the initial cell states, as collected from the csv file
     * @param simController the SimController used to interact with the frontend
     */
    public SimModel(List<List<String>> cellStates, SimController simController) {
        this.grid = new Grid(cellStates, getOrderedCellStatesMap());
        this.simController = simController;
    }

    /**
     * Update the simulation backend.
     */
    public void update() {
        setNextStates(grid);
        updateStates(grid);
    }

    /**
     * Handle when a cell is clicked by changing it to the "next" state.
     */
    public void clickResponse(int row, int col){
        Cell curCell = grid.get(row, col);
        String curState = curCell.getState();
        List<String> states = new ArrayList<>(getOrderedCellStatesMap().keySet());
        int curStateIndex = states.indexOf(curState);
        String newState = (curStateIndex == states.size()-1) ? states.get(0) : states.get(curStateIndex+1);
        Cell newCell = grid.createCell(newState, getOrderedCellStatesMap(), row, col);
        grid.set(row, col, newCell);
    }

    /**
     * Provides the grid associated with this simulation's backend.
     * @return The grid associated with this simulation's backend
     */
    public Grid getGrid(){
        return grid;
    }

    /**
     * Abstract method for providing all the cell states for a simulation implementation,
     * mapped to their associated classes. This map is given a predefined order.
     * @return This map from cells states to their associated classes
     */
    public abstract TreeMap<String, Class> getOrderedCellStatesMap();

    /**
     * Set the next state for each cell in the simulation's grid backend.
     * @param grid The simulation's grid backend
     */
    protected abstract void setNextStates(Grid<T> grid);

    /**
     * Activate each cell's next state in the simulation's grid backend.
     * @param grid The simulation's grid backend
     */
    protected abstract void updateStates(Grid<T> grid);

    /**
     * Get the neighbors for a given cell based on the simulation's grid backend.
     * @param cell A cell in the simulation's grid backend
     * @return A list of the cell's neighbors
     */
    protected abstract List<T> getNeighbors(T cell);

}
