package cellsociety;

import cellsociety.cell.wator.FishCell;
import cellsociety.cell.wator.WaTorCell;
import cellsociety.grid.Grid;
import cellsociety.simulation.SimModel;
import cellsociety.simulation.WaTorSimModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WaTorTest extends CellSocietyTest {
    public static final String WATOR_CONFIG_TESTS_PATH = "test_configs/WaTor/";

    @Test
    public void testWaTorGridPopulation() {
        testGridPopulation(WaTorSimModel.class, "WaTorConfig");
    }
/*
    @Test
    public void testSharkToEatFish() {
        SimModel simModel = createModelFromFile(WaTorSimModel.class,
                WATOR_CONFIG_TESTS_PATH + "shark_to_eat_fish" + CONFIG_TESTS_EXTENSION);
        Grid<WaTorCell> grid = simModel.getGrid();
        assertTrue(gridContainsFish(grid));
        simModel.update();
        assertFalse(gridContainsFish(grid));
    }*/

    private boolean gridContainsFish(Grid<WaTorCell> grid) {
        for (int row = 0; row < grid.getNumRows(); row++) {
            for (int col = 0; col < grid.getNumCols(); col++) {
                if (grid.get(row, col) instanceof FishCell) {
                    return true;
                }
            }
        }
        return false;
    }
}
