package cellsociety.cell.rps;

import cellsociety.cell.Cell;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class inherits from the abstract class Cell, implementing the backend for the Rock, Paper, Scissors cells.
 *
 * This class defines the rules for how each RPSCell is updated in the grid based on its current state.
 *
 * Currently, this class assumes that a maximum of 9 initial states are present. If this project had not been ended early
 * due to COVID-19, a high priority next step would be to eliminate this assumption to increase flexibility.
 *
 * @author Pierce Forte
 */
public class RPSCell extends Cell {

    public static final int THRESHOLD = 3;

    // TODO: find a flexible way of doing this based on the number of states present in csv file
    public static final List<Integer> INT_STATES = IntStream.range(0, 9).boxed().collect(Collectors.toList());
    public static final List<String> STATES = INT_STATES.stream().map(Object::toString).collect(Collectors.toList());

    /**
     * The constructor to create an RPSCell's backend.
     * @param state The state (or "type") of the cell to be created
     * @param row The row in which the cell is located
     * @param col The column in which the cell is located
     */
    public RPSCell(String state, int row, int col) {
        super(state, row, col);
    }

    /**
     * Set what the cell should do next.
     * @param neighbors A list of this cell's neighbors
     */
    public void setWhatToDoNext(List<RPSCell> neighbors) {
        Map<String, Integer> stateCounts = new HashMap<>();
        for (RPSCell neighbor : neighbors) {
            String neighborState = neighbor.getState();
            stateCounts.putIfAbsent(neighborState, 0);
            stateCounts.put(neighborState, stateCounts.get(neighborState)+1);
        }

        List<String> potentialNextStates = new ArrayList<>();
        for (String neighborState : stateCounts.keySet()) {
            if (stateCounts.get(neighborState) >= THRESHOLD) {
                potentialNextStates.add(neighborState);
            }
        }

        if (potentialNextStates.isEmpty()) {
            // retain cur state
            setNextState(getState());
        }
        else {
            // randomly choose next state from potential next states
            Collections.shuffle(potentialNextStates);
            setNextState(potentialNextStates.get(0));
        }
    }
}
