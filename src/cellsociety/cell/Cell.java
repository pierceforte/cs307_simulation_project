package cellsociety.cell;

/**
 * This class is an abstract class that is used to create each of the cell's backend.
 *
 * It defines essential, abstract methods that must be implemented to update and set states,
 * provide grid location information, and access to the cell's frontend, represented by a
 * CellView.
 *
 * This class is used to populate a grid and update/modify it accordingly. The interaction among cells
 * is the key aspect of the entire project.
 *
 * @author Pierce Forte
 * @author Mary Jiang
 */
public class Cell {
    public static final int ROW_INDEX = 0;
    public static final int COL_INDEX = 1;

    private String state, nextState;
    private int row, col;
    private CellView cellView;

    /**
     * The constructor to create a Cell's backend.
     * @param state The state (or "type") of the cell to be created
     * @param row The row in which the cell is located
     * @param col The column in which the cell is located
     */
    public Cell(String state, int row, int col){
        this.state = state;
        this.row = row;
        this.col = col;
        cellView = new CellView();
    }

    /**
     * Set the state (or "type") of the cell.
     * @param state The new state (or "type")
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Get the state (or "type") of the cell.
     * @return The state (or "type")
     */
    public String getState(){
        return state;
    }

    /**
     * Get the next state (or "type") of the cell.
     * @return The next state (or "type")
     */
    public String getNextState(){
        return nextState;
    }

    /**
     * Set the next state (or "type") of the cell.
     * @return The new next state (or "type")
     */
    public void setNextState(String nextState) {
        this.nextState = nextState;
    }

    /**
     * Set the current state of the cell to its next state.
     */
    public void updateState(){
        this.state = this.nextState;
    }

    /**
     * Determine if the current state is the same as the provided state.
     * @param state The provided state to be assessed
     * @return Whether the provided state is equal to the current state
     */
    public boolean isState(String state){
        return this.state.equals(state);
    }

    /**
     * Get the row of the cell.
     * @return The row in which the cell is located
     */
    public int getRow() {
        return row;
    }

    /**
     * Get the column of the cell.
     * @return The column in which the cell is located
     */
    public int getCol() {
        return col;
    }

    /**
     * Set the row of the cell.
     * @param row The new row of the cell
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Set the column of the cell.
     * @param col The new column of the cell
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * Get the cell's frontend (called a CellView)
     * @return The cell's frontend
     */
    public CellView getView() {
        return cellView;
    }
}
