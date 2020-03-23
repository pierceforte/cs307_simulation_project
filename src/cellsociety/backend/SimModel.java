package cellsociety.backend;
import cellsociety.cell.Cell;
import cellsociety.grid.Grid;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * This class is an abstract class that is used to create each of the simulation model's backend. It defines essential, abstract methods that must be implemented to update the grid on each step. This update includes the implementation of a two-pass system, where the next state is first determined, and it is then set for each cell.
 * This class is dependent on the implementation of the Cell class used for the given simulation.
 * This class can be used to create a specific implementation of a simulation and handle its backend as the overall application runs.
 * I believe this class is well designed because it makes it very easy to implement new simulations. In fact, adding a new simulation is merely a matter of filling out a simple "checklist" once the rules have been implemented; all one does is implement the methods below, which are designed such that this is a very logical process – the cell's set their "next" states in the first pass and then they become this next state in the second pass. Similarly, the getNeighbors method is implemented as simply as defining which cells are counted as neighbors, and the getOrderedCellStatesMap method is just a matter of listing all the states for the simulation (note that if time had allowed, I would have liked to implemented an Enum here instead of a map).
 * Note that one of this class's subclasses – GOLModel – is included in the masterpiece as well.
 * Note that the following was done to refactor this class: the SimController parameter was removed from the SimModel constructor since this was an unnecessary, unused dependency.
 * @author Pierce Forte
 */
public abstract class SimModel <T extends Cell>{
    private Grid grid;
    /**
     * The constructor to create a simulation's backend.
     * @param cellStates the initial cell states, as collected from the csv file
     */
    public SimModel(List<List<String>> cellStates) {
        this.grid = new Grid(cellStates, getOrderedCellStatesMap());
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