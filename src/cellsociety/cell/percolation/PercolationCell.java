package cellsociety.cell.percolation;

import cellsociety.cell.Cell;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This class inherits from the abstract class Cell, implementing the backend for the Percolation cells.
 *
 * This class defines the rules for how each PercolationCell is updated in the grid based on its current state.
 *
 * @author Pierce Forte
 * @author Donald Groh
 */
public class PercolationCell extends Cell{
    public static final String OPEN = "0";
    public static final String CLOSED = "1";
    public static final String FULL = "2";

    private Map<String, Consumer<List<cellsociety.cell.percolation.PercolationCell>>> handleCell = Map.of(
            OPEN, (neighbors) -> handleOPENCell(neighbors),
            CLOSED, (neighbors) -> handleCLOSEDCell(neighbors),
            FULL, (neighbors) -> handleFULLCell(neighbors));

    /**
     * The constructor to create a PercolationCell's backend.
     * @param state The state (or "type") of the cell to be created
     * @param row The row in which the cell is located
     * @param col The column in which the cell is located
     */
    public PercolationCell(String state, int row, int col) {
        super(state, row, col);
    }

    /**
     * Set what the cell should do next.
     * @param neighbors A list of this cell's neighbors
     */
    public void setWhatToDoNext(List<cellsociety.cell.percolation.PercolationCell> neighbors){
        handleCell.get(getState()).accept(neighbors);
    }

    private void handleCLOSEDCell(List<cellsociety.cell.percolation.PercolationCell> neighbors) {
        setNextState(CLOSED);
        return;
    }
    private void handleOPENCell(List<cellsociety.cell.percolation.PercolationCell> neighbors) {
        if(this.getRow() == 0){
            this.setNextState(FULL);
            return;
        }
        for (Cell neighbor: neighbors) {
            if(neighbor.getState().equals(FULL)){
                setNextState(FULL);
                return;
            }
        }
        setNextState(OPEN);
    }

    private void handleFULLCell(List<cellsociety.cell.percolation.PercolationCell> neighbors) {
        setNextState(FULL);
        return;
    }

}
