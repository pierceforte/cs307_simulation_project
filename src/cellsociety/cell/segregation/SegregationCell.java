package cellsociety.cell.segregation;

import cellsociety.cell.Cell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class SegregationCell extends Cell {
    public static final String EMPTY = "0";
    public static final String AGENT_A = "1";
    public static final String AGENT_B = "2";
    public static final double DEFAULT_SATISFACTION_MIN = 0.75;

    private Map<String, BiFunction<List<SegregationCell>, List<List<SegregationCell>>, List<List<SegregationCell>>>> handleCell = Map.of(
            EMPTY, (neighbors, nextGrid) -> handleEmptyCell(neighbors, nextGrid),
            AGENT_A, (neighbors, nextGrid) -> handleAgentCell(neighbors, nextGrid),
            AGENT_B, (neighbors, nextGrid) -> handleAgentCell(neighbors, nextGrid));

    public SegregationCell(String state, int row, int col) {
        super(state, row, col);
    }

    public List<List<SegregationCell>> setWhatToDoNext(List<SegregationCell> neighbors, List<List<SegregationCell>> nextGrid){
        return handleCell.get(getState()).apply(neighbors, nextGrid);
    }

    private List<List<SegregationCell>> handleEmptyCell(List<SegregationCell> neighbors, List<List<SegregationCell>> nextGrid) {
        return nextGrid;
    }

    private List<List<SegregationCell>> handleAgentCell(List<SegregationCell> neighbors, List<List<SegregationCell>> nextGrid) {
        double satisfaction = getSatisfaction(neighbors);
        if (satisfaction < DEFAULT_SATISFACTION_MIN) {
            List<Integer> newPosition = getRandomNewPosition(nextGrid);
            int myCurRow = getRow();
            int myCurCol = getCol();
            nextGrid.get(myCurRow).set(myCurCol, new SegregationCell(EMPTY, myCurRow, myCurCol));
            int myNewRow = newPosition.get(Cell.ROW_INDEX);
            int myNewCol = newPosition.get(Cell.COL_INDEX);
            nextGrid.get(myNewRow).set(myNewCol, this);
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

    private List<Integer> getRandomNewPosition(List<List<SegregationCell>> nextGrid) {
        List<List<Integer>> potentialNewPositions = new ArrayList<>();
        for (List<SegregationCell> row : nextGrid) {
            for (SegregationCell cell : row) {
                if (cell.getState().equals(EMPTY)) {
                    potentialNewPositions.add(List.of(cell.getRow(), cell.getCol()));
                }
            }
        }
        Collections.shuffle(potentialNewPositions);
        return potentialNewPositions.get(0);
    }

}
