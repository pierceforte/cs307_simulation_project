package cellsociety.cell;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RPSCell extends Cell{

    public static final int THRESHOLD = 3;

    // TODO: find a flexible way of doing this based on the number of states present in csv file
    public static final List<Integer> INT_STATES = IntStream.range(0, 9).boxed().collect(Collectors.toList());
    public static final List<String> STATES = INT_STATES.stream().map(Object::toString).collect(Collectors.toList());

    public RPSCell(String state, int row, int col) {
        super(state, row, col);
    }

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
