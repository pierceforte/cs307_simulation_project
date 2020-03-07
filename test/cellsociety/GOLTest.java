package cellsociety;

import cellsociety.cell.Cell;
import cellsociety.config.ConfigReader;
import cellsociety.config.ConfigSaver;
import cellsociety.grid.Grid;
import cellsociety.cell.GOL.GOLCell;
import cellsociety.simulation.FireSimModel;
import cellsociety.simulation.GOLSimModel;
import cellsociety.simulation.SimModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GOLTest extends CellSocietyTest {

    @Test
    public void testGOLGridPopulation() {
        testGridPopulation(GOLSimModel.class, "GOLConfig");
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
        testGOLCellStateChange(0, 7, cellsociety.cell.GOL.GOLCell.DEAD, cellsociety.cell.GOL.GOLCell.ALIVE);
    }

    @Test
    public void testGOLCellStaysAlive() {
        // test a cell with 2 neighbors
        testGOLCellStateChange(0, 10, cellsociety.cell.GOL.GOLCell.ALIVE, cellsociety.cell.GOL.GOLCell.ALIVE);
        // test a cell with 3 neighbors
        testGOLCellStateChange(11, 3, cellsociety.cell.GOL.GOLCell.ALIVE, cellsociety.cell.GOL.GOLCell.ALIVE);
    }

    @Test
    public void testGOLCellStaysDead() {
        // test a dead cell with less than 3 neighbors (in this case, 2)
        testGOLCellStateChange(0, 5, cellsociety.cell.GOL.GOLCell.DEAD, cellsociety.cell.GOL.GOLCell.DEAD);
        // test a dead cell with more than 3 neighbors (in this case, 4)
        testGOLCellStateChange(0, 9, cellsociety.cell.GOL.GOLCell.DEAD, cellsociety.cell.GOL.GOLCell.DEAD);
    }

    /*

    Below are the initial configuration tests for Game of Life.
    There are 5 Still Lifes, 1 Oscillator (Blinker), and 1 Spaceship (Glider)

     */
    @Test
    public void testGOLBeehiveConfig() {
        SimModel simModel = createModelFromFile(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "beehive" + GOL_CONFIG_TESTS_EXTENSION);
        testGOLStillLifes(simModel);
    }

    @Test
    public void testGOLBlockConfig() {
        SimModel simModel = createModelFromFile(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "block" + GOL_CONFIG_TESTS_EXTENSION);
        testGOLStillLifes(simModel);
    }

    @Test
    public void testGOLBoatConfig() {
        SimModel simModel = createModelFromFile(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "boat" + GOL_CONFIG_TESTS_EXTENSION);
        testGOLStillLifes(simModel);
    }

    @Test
    public void testGOLLoafConfig() {
        SimModel simModel = createModelFromFile(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "loaf" + GOL_CONFIG_TESTS_EXTENSION);
        testGOLStillLifes(simModel);
    }

    @Test
    public void testGOLTubConfig() {
        SimModel simModel = createModelFromFile(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "tub" + GOL_CONFIG_TESTS_EXTENSION);
        testGOLStillLifes(simModel);
    }

    @Test
    public void testGOLBlinkerConfig() {
        SimModel simModel1 = createModelFromFile(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "blinker1" + GOL_CONFIG_TESTS_EXTENSION);
        SimModel simModel2 = createModelFromFile(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "blinker2" + GOL_CONFIG_TESTS_EXTENSION);
        testGOLStateLoops(List.of(simModel1, simModel2));
    }

    @Test
    public void testGOLGliderConfig() {
        SimModel simModel1 = createModelFromFile(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "glider1" + GOL_CONFIG_TESTS_EXTENSION);
        SimModel simModel2 = createModelFromFile(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "glider2" + GOL_CONFIG_TESTS_EXTENSION);
        SimModel simModel3 = createModelFromFile(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "glider3" + GOL_CONFIG_TESTS_EXTENSION);
        SimModel simModel4 = createModelFromFile(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "glider4" + GOL_CONFIG_TESTS_EXTENSION);
        SimModel simModel5 = createModelFromFile(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "glider5" + GOL_CONFIG_TESTS_EXTENSION);
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
        createModelFromStart(GOLSimModel.class);
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
        testGOLCellStateChange(row, col, cellsociety.cell.GOL.GOLCell.ALIVE, cellsociety.cell.GOL.GOLCell.DEAD);
    }

}
