package cellsociety.simulation;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GameOfLifeSimModel extends SimModel {
    public static final List<Integer> STATES = List.of(0,1);

    public GameOfLifeSimModel(Collection<Collection<Object>> grid) {
        super(grid);
    }
}
