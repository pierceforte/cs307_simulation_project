package cellsociety.cell.gol;
import cellsociety.cell.Cell;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * This class inherits from the abstract class Cell, implementing the backend for the Game of Life cells.
 * This class defines the rules for how each GOLCell is updated in the grid based on its current state.
 * This class is well designed because of its simplicity; all that needs to be done here is add in the rules for the Game of Life cells, for all of the other Cell attributes/functionality is already available in the Cell superclass. As can be seen below, the rules are implemented rather cleanly, and cells can be updated based on their state through well-named, single-purpose methods.
 * Note that this class's superclass – Cell – is included in the masterpiece as well and more information regarding the methods in this class can be found there.
 * Note that the following was done to refactor this class: magic values that referred to rules of the game – like number of neighbors necessary for a cell to become alive – were replaced with constants.
 * @author Pierce Forte
 */
public class GOLCell extends Cell {
    public static final String DEAD = "0";
    public static final String ALIVE = "1";
    public static final int MIN_NEIGHBORS_TO_STAY_ALIVE = 2;
    public static final int MAX_NEIGHBORS_TO_STAY_ALIVE = 3;
    public static final int NUM_NEIGHBORS_TO_BECOME_ALIVE = 3;
    private Map<String, Function<Integer, String>> handleCell = Map.of(
            ALIVE, (numNeighbors) -> handleLivingCell(numNeighbors),
            DEAD, (numNeighbors) -> handleDeadCell(numNeighbors));
    /**
     * The constructor to create a GOLCell's backend.
     * @param state The state (or "type") of the cell to be created
     * @param row The row in which the cell is located
     * @param col The column in which the cell is located
     */
    public GOLCell(String state, int row, int col) {
        super(state, row, col);
    }
    /**
     * Set what the cell should do next.
     * @param neighbors A list of this cell's neighbors
     */
    public void setWhatToDoNext(List<GOLCell> neighbors){
        int numNeighbors = getNumNeighbors(neighbors);
        String nextState = handleCell.get(getState()).apply(numNeighbors);
        setNextState(nextState);
    }
    /**
     * Tell a dead cell what to do.
     * @param numNeighbors How many living neighbors the cell has
     * @return The cell's next state
     */
    private String handleDeadCell(int numNeighbors) {
        if (numNeighbors == NUM_NEIGHBORS_TO_BECOME_ALIVE) {
            return ALIVE;
        }
        return DEAD;
    }
    /**
     * Tell a living cell what to do.
     * @param numNeighbors How many living neighbors the cell has
     * @return The cell's next state
     */
    private String handleLivingCell(int numNeighbors) {
        if (numNeighbors < MIN_NEIGHBORS_TO_STAY_ALIVE || numNeighbors > MAX_NEIGHBORS_TO_STAY_ALIVE) {
            return DEAD;
        }
        return ALIVE;
    }
    /**
     * Get how many living neighbors a cell has.
     * @param neighbors A list of this cell's neighbors
     * @return How many living neighbors a cell has
     */
    private int getNumNeighbors(List<GOLCell> neighbors) {
        int numNeighbors = 0;
        for (Cell neighbor : neighbors) {
            if (isCellAlive(neighbor)) numNeighbors++;
        }
        return numNeighbors;
    }
    /**
     * Determine if the cell is alive.
     * @param cell The cell to be assessed
     * @return Whether the cell is alive or not
     */
    private boolean isCellAlive(Cell cell) {
        return cell.getState().equals(ALIVE);
    }
}