package cellsociety.cell.segregation;

import cellsociety.cell.Cell;
import cellsociety.grid.Grid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * This class inherits from the abstract class Cell, implementing the backend for the Segregation cells.
 *
 * This class defines the rules for how each SegregationCell is updated in the grid based on its current state.
 *
 * It is assumed that cells are satisfied when 75% of their neighbors are of the same type. Likewise, it is assumed that
 * an Empty neighbor cell does not affect this satisfaction, BUT if a cell's neighbors are all Empty, the cell will NOT be
 * satisfied.
 *
 * @author Pierce Forte
 */
public class SegregationCell extends Cell {
    public static final String EMPTY = "0";
    public static final String AGENT_A = "1";
    public static final String AGENT_B = "2";
    public static final double DEFAULT_SATISFACTION_MIN = 0.75;

    private Map<String, BiFunction<List<SegregationCell>,  Grid<SegregationCell>, Grid<SegregationCell>>> handleCell = Map.of(
            EMPTY, (neighbors, nextGrid) -> handleEmptyCell(neighbors, nextGrid),
            AGENT_A, (neighbors, nextGrid) -> handleAgentCell(neighbors, nextGrid),
            AGENT_B, (neighbors, nextGrid) -> handleAgentCell(neighbors, nextGrid));

    /**
     * The constructor to create a SegregationCell's backend.
     * @param state The state (or "type") of the cell to be created
     * @param row The row in which the cell is located
     * @param col The column in which the cell is located
     */
    public SegregationCell(String state, int row, int col) {
        super(state, row, col);
    }

    /**
     * Set what the cell should do next.
     * @param neighbors A list of this cell's neighbors
     * @param nextGrid The grid to keep track of which next positions have already been taken
     */
    public Grid<SegregationCell> setWhatToDoNext(List<SegregationCell> neighbors, Grid<SegregationCell> nextGrid){
        return handleCell.get(getState()).apply(neighbors, nextGrid);
    }

    private Grid<SegregationCell> handleEmptyCell(List<SegregationCell> neighbors, Grid<SegregationCell> nextGrid) {
        return nextGrid;
    }

    private Grid<SegregationCell> handleAgentCell(List<SegregationCell> neighbors, Grid<SegregationCell> nextGrid) {
        double satisfaction = getSatisfaction(neighbors);
        if (satisfaction < DEFAULT_SATISFACTION_MIN) {
            List<Integer> newPosition = getRandomNewPosition(nextGrid);
            int myCurRow = getRow();
            int myCurCol = getCol();
            nextGrid.set(myCurRow, myCurCol, new SegregationCell(EMPTY, myCurRow, myCurCol));
            int myNewRow = newPosition.get(Cell.ROW_INDEX);
            int myNewCol = newPosition.get(Cell.COL_INDEX);
            nextGrid.set(myNewRow, myNewCol, this);
            setRow(myNewRow);
            setCol(myNewCol);
        }

        return nextGrid;
    }

    private double getSatisfaction(List<SegregationCell> neighbors) {
        double numAlikeNeighbors = 0;
        double numTotalNeighbors = 0; 
        for (Cell neighbor : neighbors) {
            if (!neighbor.getState().equals(EMPTY)) {
                numTotalNeighbors++;
                if (neighbor.getState().equals(this.getState())) numAlikeNeighbors++; 
            }
        }
        if (numTotalNeighbors == 0) {
            return 0;
        }
        return numAlikeNeighbors/numTotalNeighbors;
    }

    private List<Integer> getRandomNewPosition(Grid<SegregationCell> nextGrid) {
        List<List<Integer>> potentialNewPositions = new ArrayList<>();
        for (int row = 0; row < nextGrid.getNumRows(); row++) {
            for (int col = 0; col < nextGrid.getNumCols(); col++) {
                SegregationCell cell = nextGrid.get(row, col);
                if (cell.getState().equals(EMPTY)) {
                    potentialNewPositions.add(List.of(cell.getRow(), cell.getCol()));
                }
            }
        }
        Collections.shuffle(potentialNewPositions);
        return potentialNewPositions.get(0);
    }

}
