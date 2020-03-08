package cellsociety;

import cellsociety.cell.segregation.SegregationCell;
import cellsociety.grid.Grid;
import cellsociety.backend.SegregationSimModel;
import cellsociety.backend.SimModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SegregationTest extends CellSocietyTest {
    public static final String SEGREGATION_CONFIG_TESTS_PATH = "test_configs/Segregation/";

    @Test
    public void testSegregationGridPopulation() {
        testGridPopulation(SegregationSimModel.class, "SegregationConfig");
    }

    @Test
    public void testSatisfiedInitConfig() {
        SimModel simModel = createModelFromFile(SegregationSimModel.class,
                SEGREGATION_CONFIG_TESTS_PATH + "satisfied_agents" + CONFIG_TESTS_EXTENSION);
        Grid<SegregationCell> grid = simModel.getGrid();
        List<List<String>> initStates = getListOfCellStates(grid);
        simModel.update();
        assertStatesListAndGridEqual(initStates, grid);
    }

    @Test
    public void testUnsatisfiedInitConfig() {
        SimModel simModel = createModelFromFile(SegregationSimModel.class,
                SEGREGATION_CONFIG_TESTS_PATH + "unsatisfied_agents" + CONFIG_TESTS_EXTENSION);
        Grid<SegregationCell> grid = simModel.getGrid();
        List<List<String>> initStates = getListOfCellStates(grid);
        simModel.update();
        assertStatesListAndGridNotEqual(initStates, grid);
    }

    @Test
    public void testUnsatisfiedMovement() {
        SimModel simModel = createModelFromFile(SegregationSimModel.class,
                SEGREGATION_CONFIG_TESTS_PATH + "known_unsatisfied_movement" + CONFIG_TESTS_EXTENSION);
        Grid<SegregationCell> grid = simModel.getGrid();
        // assert agent B and empty cell are in correct initial positions
        assertTrue(grid.get(1,2).getState().equals(SegregationCell.EMPTY));
        assertTrue(grid.get(2,2).getState().equals(SegregationCell.AGENT_B));

        simModel.update();

        // assert that agent b (the only unsatisfied agent) has moved to the empty cell,
        // leaving an empty cell in its place
        assertTrue(grid.get(1,2).getState().equals(SegregationCell.AGENT_B));
        assertTrue(grid.get(2,2).getState().equals(SegregationCell.EMPTY));
    }
}
