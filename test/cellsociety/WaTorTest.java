package cellsociety;

import cellsociety.backend.SimModel;
import cellsociety.backend.WaTorSimModel;
import cellsociety.cell.wator.FishCell;
import cellsociety.cell.wator.SharkCell;
import cellsociety.cell.wator.WaTorCell;
import cellsociety.grid.Grid;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WaTorTest extends CellSocietyTest {
    public static final String WATOR_CONFIG_TESTS_PATH = "test_configs/WaTor/";

    @Test
    public void testWaTorGridPopulation() {
        testGridPopulation(WaTorSimModel.class, "WaTorConfig");
    }

    @Test
    public void testSharkToEatFish() {
        SimModel simModel = createModelFromFile(WaTorSimModel.class,
                WATOR_CONFIG_TESTS_PATH + "shark_to_eat_fish/shark_to_eat_fish" + CONFIG_TESTS_EXTENSION);
        Grid<WaTorCell> grid = simModel.getGrid();

        // assert fish and shark are in right spots
        assertTrue(grid.get(1,0) instanceof SharkCell);
        assertTrue(grid.get(1,1) instanceof FishCell);

        SharkCell shark = (SharkCell) grid.get(1,0);
        int initEnergy = shark.getEnergy();

        simModel.update();

        // assert fish has been eaten
        assertEquals(0, getNumOfCellTypeInGrid(grid, FishCell.class));
        assertTrue(grid.get(1,1) instanceof SharkCell);
        shark = (SharkCell) grid.get(1,1);
        int curEnergy = shark.getEnergy();
        assertEquals(initEnergy + SharkCell.ENERGY_FOR_EATING_FISH, curEnergy);
    }

    @Test
    public void testFishMovesProperly() {
        SimModel simModel = createModelFromFile(WaTorSimModel.class,
                WATOR_CONFIG_TESTS_PATH + "single_fish/single_fish" + CONFIG_TESTS_EXTENSION);
        Grid<WaTorCell> grid = simModel.getGrid();

        // assert fish is in right spot
        assertTrue(grid.get(1,0) instanceof FishCell);

        simModel.update();

        // assert fish has either moved up or right
        assertTrue(grid.get(0,0) instanceof FishCell || grid.get(1,1) instanceof FishCell);
    }

    @Test
    public void testFishReproduction() {
        SimModel simModel = createModelFromFile(WaTorSimModel.class,
                WATOR_CONFIG_TESTS_PATH + "single_fish/single_fish" + CONFIG_TESTS_EXTENSION);
        Grid<WaTorCell> grid = simModel.getGrid();

        // assert that only 1 fish exists before reproduction timer is complete
        for (int step = 0; step < FishCell.CHRONONS_TO_REPRODUCE; step++) {
            assertEquals(1, getNumOfCellTypeInGrid(grid, FishCell.class));
            simModel.update();
        }
        simModel.update();

        // assert that after timer is complete, a new fish has been added and there are now 2
        assertEquals(2, getNumOfCellTypeInGrid(grid, FishCell.class));

    }

    @Test
    public void testSharkDeath() {
        SimModel simModel = createModelFromFile(WaTorSimModel.class,
                WATOR_CONFIG_TESTS_PATH + "single_shark/single_shark" + CONFIG_TESTS_EXTENSION);
        Grid<WaTorCell> grid = simModel.getGrid();

        // assert that 1 shark exists before energy runs out
        for (int step = 0; step < SharkCell.DEFAULT_SHARK_ENERGY; step++) {
            assertEquals(1, getNumOfCellTypeInGrid(grid, SharkCell.class));
            simModel.update();
        }

        // assert that after energy runs out, shark dies and none exist
        assertEquals(0, getNumOfCellTypeInGrid(grid, FishCell.class));

    }

    private int getNumOfCellTypeInGrid(Grid<WaTorCell> grid, Class cellClass) {
        int numCells = 0;
        for (int row = 0; row < grid.getNumRows(); row++) {
            for (int col = 0; col < grid.getNumCols(); col++) {
                if (grid.get(row, col).getClass() == cellClass) {
                    numCells++;
                }
            }
        }
        return numCells;
    }
}
