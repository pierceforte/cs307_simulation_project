package cellsociety;

import cellsociety.cell.Cell;
import cellsociety.grid.Grid;
import cellsociety.cell.gol.GOLCell;
import cellsociety.backend.GOLModel;
import cellsociety.backend.SimModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testing suite for this Game of Life Simulation.
 *
 * @author Pierce Forte
 */
public class GOLTest extends CellSocietyTest {
    public static final String GOL_CONFIG_TESTS_PATH = "test_configs/GOL/";

    @Test
    public void testGOLGridPopulation() {
        testGridPopulation(GOLModel.class, "GOLConfig");
    }

    @Test
    public void testGOLCellDeathUnderPopulation() {
        // test that a cell with zero neighbors dies
        testGOLCellDeath(0,1);
        // test that a cell with one neighbor dies
        testGOLCellDeath(1,22);
    }

    @Test
    public void testGOLCellDeathOverPopulation() {
        // test that a cell with more than 3 neighbors (in this case, 4) dies
        testGOLCellDeath(0,15);
    }

    @Test
    public void testGOLCellBirth() {
        // test that a dead cell with 3 neighbors becomes alive
        testGOLCellStateChange(0, 7, GOLCell.DEAD, GOLCell.ALIVE);
    }

    @Test
    public void testGOLCellStaysAlive() {
        // test a cell with 2 neighbors
        testGOLCellStateChange(0, 10, GOLCell.ALIVE, GOLCell.ALIVE);
        // test a cell with 3 neighbors
        testGOLCellStateChange(11, 3, GOLCell.ALIVE, GOLCell.ALIVE);
    }

    @Test
    public void testGOLCellStaysDead() {
        // test a dead cell with less than 3 neighbors (in this case, 2)
        testGOLCellStateChange(0, 5, GOLCell.DEAD, GOLCell.DEAD);
        // test a dead cell with more than 3 neighbors (in this case, 4)
        testGOLCellStateChange(0, 9, GOLCell.DEAD, GOLCell.DEAD);
    }

    /*

    Below are the initial configuration tests for Game of Life.
    There are 5 Still Lifes, 1 Oscillator (Blinker), and 1 Spaceship (Glider)

     */
    @Test
    public void testGOLBeehiveConfig() {
        SimModel simModel = createModelFromFile(GOLModel.class,
                GOL_CONFIG_TESTS_PATH + "beehive/beehive" + CONFIG_TESTS_EXTENSION);
        testGOLStillLifes(simModel);
    }

    @Test
    public void testGOLBlockConfig() {
        SimModel simModel = createModelFromFile(GOLModel.class,
                GOL_CONFIG_TESTS_PATH + "block/block" + CONFIG_TESTS_EXTENSION);
        testGOLStillLifes(simModel);
    }

    @Test
    public void testGOLBoatConfig() {
        SimModel simModel = createModelFromFile(GOLModel.class,
                GOL_CONFIG_TESTS_PATH + "boat/boat" + CONFIG_TESTS_EXTENSION);
        testGOLStillLifes(simModel);
    }

    @Test
    public void testGOLLoafConfig() {
        SimModel simModel = createModelFromFile(GOLModel.class,
                GOL_CONFIG_TESTS_PATH + "loaf/loaf" + CONFIG_TESTS_EXTENSION);
        testGOLStillLifes(simModel);
    }

    @Test
    public void testGOLTubConfig() {
        SimModel simModel = createModelFromFile(GOLModel.class,
                GOL_CONFIG_TESTS_PATH + "tub/tub" + CONFIG_TESTS_EXTENSION);
        testGOLStillLifes(simModel);
    }

    @Test
    public void testGOLBlinkerConfig() {
        SimModel simModel1 = createModelFromFile(GOLModel.class,
                GOL_CONFIG_TESTS_PATH + "blinker1/blinker1" + CONFIG_TESTS_EXTENSION);
        SimModel simModel2 = createModelFromFile(GOLModel.class,
                GOL_CONFIG_TESTS_PATH + "blinker2/blinker2" + CONFIG_TESTS_EXTENSION);
        testGOLStateLoops(List.of(simModel1, simModel2));
    }

    @Test
    public void testGOLGliderConfig() {
        SimModel simModel1 = createModelFromFile(GOLModel.class,
                GOL_CONFIG_TESTS_PATH + "glider1/glider1" + CONFIG_TESTS_EXTENSION);
        SimModel simModel2 = createModelFromFile(GOLModel.class,
                GOL_CONFIG_TESTS_PATH + "glider2/glider2" + CONFIG_TESTS_EXTENSION);
        SimModel simModel3 = createModelFromFile(GOLModel.class,
                GOL_CONFIG_TESTS_PATH + "glider3/glider3" + CONFIG_TESTS_EXTENSION);
        SimModel simModel4 = createModelFromFile(GOLModel.class,
                GOL_CONFIG_TESTS_PATH + "glider4/glider4" + CONFIG_TESTS_EXTENSION);
        SimModel simModel5 = createModelFromFile(GOLModel.class,
                GOL_CONFIG_TESTS_PATH + "glider5/glider5" + CONFIG_TESTS_EXTENSION);
        testGOLStateLoops(List.of(simModel1, simModel2, simModel3, simModel4, simModel5));
    }

    private void testGOLStateLoops(List<SimModel> models) {
        if (models.size() == 0) return;
        SimModel referenceModel = models.get(0);
        // step our reference model and check it with the expected cells (which are gathered by creating
        // a new model with the expected configuration)
        for (int modelNum = 1; modelNum < models.size(); modelNum++) {
            referenceModel.update();
            Grid referenceModelCells = referenceModel.getGrid();
            Grid expectedModelCells = models.get(modelNum).getGrid();
            for (int row = 0; row < referenceModelCells.getNumRows(); row++) {
                for (int col = 0; col < referenceModelCells.getNumCols(); col++) {
                    assertEquals(referenceModelCells.get(row, col).getState(), expectedModelCells.get(row, col).getState());
                }
            }
        }
    }

    private void testGOLStillLifes(SimModel simModel) {
        Grid<GOLCell> grid = simModel.getGrid();
        List<List<String>> initialCellStates = getListOfCellStates(grid);

        // update model and its cells
        simModel.update();

        // check that cells after updating are the same as the initial cells
        for (int row = 0; row < grid.getNumRows(); row++) {
            for (int col = 0; col < grid.getNumCols(); col++) {
                assertEquals(initialCellStates.get(row).get(col), grid.get(row, col).getState());
            }
        }
    }

    private void testGOLCellStateChange(int row, int col, String initialState, String updatedStated) {
        createModelFromStart(GOLModel.class);
        Grid cells = getMySimModel().getGrid();

        // get a cell
        Cell loneCell = cells.get(row, col);
        // assert that this cell's initial state is correct
        assertEquals(initialState, loneCell.getState());
        // update simulation (one step)
        getMySimModel().update();
        // assert that this cell's updated state is correct
        assertEquals(updatedStated, loneCell.getState());
    }

    private void testGOLCellDeath(int row, int col) {
        testGOLCellStateChange(row, col, GOLCell.ALIVE, GOLCell.DEAD);
    }

}
