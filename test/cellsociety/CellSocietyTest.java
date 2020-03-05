package cellsociety;

import cellsociety.cell.Cell;
import cellsociety.cell.GOL.GOLCell;
import cellsociety.config.ConfigReader;
import cellsociety.config.ConfigSaver;
import cellsociety.simulation.GOLSimModel;
import cellsociety.simulation.SimController;
import cellsociety.simulation.SimModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
//import org.junit.Test;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class CellSocietyTest extends DukeApplicationTest {

    public static final String GOL_CONFIG_TESTS_PATH = "test_configs/GOL/";
    public static final String GOL_CONFIG_TESTS_EXTENSION = ".csv";

    private MainController myMainController;
    private SimController mySimController;
    private SimModel mySimModel;


    @Test
    public void testGOLGridPopulation() {
        mySimModel = createModelFromStart(GOLSimModel.class);
        List<List<Cell>> cellsFromModel = mySimModel.getCells();

        ConfigReader data = new ConfigReader(GOLSimModel.CONFIG_FILE_PREFIX + SimController.CONFIG_FILE_SUFFIX);
        List<List<String>> cellStatesFromFile = data.getCellList();

        assertEquals(data.getManualQuantityOfColumns(),data.getQuantityOfColumns());
        assertEquals(data.getManualQuantityOfRows(),data.getQuantityOfRows());
        for (int row = 0; row < cellsFromModel.size(); row++) {
            for (int col = 0; col < cellsFromModel.get(0).size(); col++) {
                assertEquals(cellStatesFromFile.get(row).get(col), cellsFromModel.get(row).get(col).getState());
            }
        }
    }
    @Test
    public void testException() {
        ConfigReader data = new ConfigReader("BAD_FILE" + SimController.CONFIG_FILE_SUFFIX);
//        List<List<Cell>> cellsFromFile = data.getCellList();
//        assertEquals("Could not write Exception to file",data.getCellList());
        assertThrows(java.lang.Exception.class, () -> data.getCellList());

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
    @Test
    public void testGOLReset() {
        startApplication();
        // begin GOL simulation from beginning
        fireButtonEvent(lookup("#GOLSimButton").query());
        fireButtonEvent(lookup("#restartBttn").query());
        // get initial configuration
        List<List<Cell>> initialCells = myMainController.getCurSimController().getModel().getCells();
        // store initial states
        List<List<String>> initialCellStates = getListOfCellStates(initialCells);
        // update simulation
        step();
        // exit simulation
        fireButtonEvent(lookup("#exitBttn").query());
        // step to properly present GOL simulation button
        step();
        // begin GOL simulation from beginning
        fireButtonEvent(lookup("#GOLSimButton").query());
        fireButtonEvent(lookup("#restartBttn").query());
        // get new model's cells
        List<List<Cell>> newCells = myMainController.getCurSimController().getModel().getCells();
        // check that new cells are the same as initial configuration cells after reset
        for (int row = 0; row < initialCells.size(); row++) {
            System.out.println();
            for (int col = 0; col < initialCells.get(0).size(); col++) {
                assertEquals(newCells.get(row).get(col).getState(), initialCellStates.get(row).get(col));
            }
        }
    }
     */

    @Test
    public void testIntroPaneCreation() {
        // assert that intro pane is not present
        assertThrows(org.testfx.service.query.EmptyNodeQueryException.class, () -> lookup("#introPane").query());
        startApplication();
        // assert that intro pane is now present
        assertNotNull(lookup("#introPane").query());
    }

    @Test
    public void testStartSimulationButton() {
        // assert that start GOL simulation button is not present
        assertThrows(org.testfx.service.query.EmptyNodeQueryException.class, () -> lookup("#GOLSimButton").query());
        startApplication();
        // assert that start GOL simulation button is now present
        assertNotNull(lookup("#GOLSimButton").query());
        // assert that no cell view is present (here we look at the first one created: cellView0)
        assertThrows(org.testfx.service.query.EmptyNodeQueryException.class, () -> lookup("#cellView0").query());

        Button GOLSimButton = lookup("#GOLSimButton").query();
        fireButtonEvent(GOLSimButton);

        // assert that the cell view is now presented
        assertNotNull(lookup("#cellView0").query());
    }

    @Test
    public void testExitSimulationButton() {
        startApplication();
        // assert that exit button is not present before simulation is started
        assertThrows(org.testfx.service.query.EmptyNodeQueryException.class, () -> lookup("#exitBttn").query());

        Button GOLSimButton = lookup("#GOLSimButton").query();
        fireButtonEvent(GOLSimButton);

        // assert button is now present
        assertNotNull(lookup("#exitBttn").query());

        Button exitBttn = lookup("#exitBttn").query();

        // press button to exit
        fireButtonEvent(exitBttn);
        // let controller clear root by stepping
        step();
        // assert that button is no longer present
        assertThrows(org.testfx.service.query.EmptyNodeQueryException.class, () -> lookup("#exitBttn").query());
    }

    // NOTE: only works when run by itself; we are still looking into how to fix this.
    @Test
    public void testContinueSimulationButton() {
        startApplication();
        // begin GOL simulation from beginning
        fireButtonEvent(lookup("#GOLSimButton").query());
        mySimModel = myMainController.getCurSimController().getModel();
        // step to update cells
        step();
        // collect updated cells
        List<List<Cell>> updatedCellsFromModel = mySimModel.getCells();
        // exit simulation and then choose to continue it
        fireButtonEvent(lookup("#exitBttn").query());
        // need to step to give buttons time to present themselves
        step();
        fireButtonEvent(lookup("#GOLSimButton").query());
        fireButtonEvent(lookup("#continueBttn").query());
        mySimModel = myMainController.getCurSimController().getModel();
        // collect cells after continuing
        List<List<Cell>> continuedCellsFromModel = mySimModel.getCells();
        // check that cells after continuing are the same as the cells before exiting above
        for (int row = 0; row < updatedCellsFromModel.size(); row++) {
            for (int col = 0; col < updatedCellsFromModel.get(0).size(); col++) {
                assertEquals(updatedCellsFromModel.get(row).get(col).getState(), continuedCellsFromModel.get(row).get(col).getState());
            }
        }
    }

    @Test
    public void testLogErrorFunction() {
        // create a new config reader (doesn't matter what the file is called since constructor doesn't do any reading in)
        ConfigReader data = new ConfigReader("FILE_ID" + SimController.CONFIG_FILE_SUFFIX);
        // get initial number of lines in error log
        long initialNumLines = getNumLinesFromFile(ConfigReader.ERROR_LOG);
        // write an exception to error log
        data.logError(new Exception("My test exception"));
        // get the updated number of lines in error log
        long updatedNumLines = getNumLinesFromFile(ConfigReader.ERROR_LOG);
        // check that the line counter did not fail (if it did, it returns -1)
        assertTrue(updatedNumLines > 0 && initialNumLines > 0);
        // check that the error log has been updated with the new error (and, therefore, has more lines)
        assertTrue(updatedNumLines > initialNumLines);
    }

    // TODO: refactor
    @Test
    public void testPauseAndPlaySimulationButtons() {
        startApplication();
        // assert that pause and play buttons are not yet present
        assertThrows(org.testfx.service.query.EmptyNodeQueryException.class, () -> lookup("#pauseBttn").query());
        assertThrows(org.testfx.service.query.EmptyNodeQueryException.class, () -> lookup("#playBttn").query());
        // begin GOL simulation from beginning
        fireButtonEvent(lookup("#GOLSimButton").query());
        fireButtonEvent(lookup("#restartBttn").query());
        // assert that pause and play buttons are now present
        assertNotNull(lookup("#pauseBttn").query());
        assertNotNull(lookup("#playBttn").query());

        Button pauseButton = lookup("#pauseBttn").query();
        Button playButton = lookup("#playBttn").query();

        // check that simulation is active before pause
        assertTrue(myMainController.getCurSimController().isActive());
        // collect cells
        List<List<Cell>> cells = myMainController.getCurSimController().getModel().getCells();
        List<List<String>> prePauseCellStates = getListOfCellStates(cells);
        // pause and check that simulation is inactive
        fireButtonEvent(pauseButton);
        assertFalse(myMainController.getCurSimController().isActive());

        // check that cells during the pause are the same as the cells before the pause
        for (int row = 0; row < cells.size(); row++) {
            System.out.println();
            for (int col = 0; col < cells.get(0).size(); col++) {
                assertEquals(prePauseCellStates.get(row).get(col), cells.get(row).get(col).getState());
            }
        }

        // play and check that simulation is active
        fireButtonEvent(playButton);
        assertTrue(myMainController.getCurSimController().isActive());
        step();

        boolean notTheSame = false;
        for (int row = 0; row < cells.size(); row++) {
            System.out.println();
            for (int col = 0; col < cells.get(0).size(); col++) {
                if (!prePauseCellStates.get(row).get(col).equals(cells.get(row).get(col).getState())) {
                    notTheSame = true;
                    break;
                }
            }
        }
        assertTrue(notTheSame);
    }

    /*

    Below are the initial configuration tests for Game of Life.
    There are 5 Still Lifes, 1 Oscillator (Blinker), and 1 Spaceship (Glider)
    
     */
    @Test
    public void testGOLBeehiveConfig() {
        SimModel simModel = createModelForInitialConfigUpdateTests(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "beehive" + GOL_CONFIG_TESTS_EXTENSION);
        testGOLStillLifes(simModel);
    }

    @Test
    public void testGOLBlockConfig() {
        SimModel simModel = createModelForInitialConfigUpdateTests(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "block" + GOL_CONFIG_TESTS_EXTENSION);
        testGOLStillLifes(simModel);
    }

    @Test
    public void testGOLBoatConfig() {
        SimModel simModel = createModelForInitialConfigUpdateTests(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "boat" + GOL_CONFIG_TESTS_EXTENSION);
        testGOLStillLifes(simModel);
    }

    @Test
    public void testGOLLoafConfig() {
        SimModel simModel = createModelForInitialConfigUpdateTests(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "loaf" + GOL_CONFIG_TESTS_EXTENSION);
        testGOLStillLifes(simModel);
    }

    @Test
    public void testGOLTubConfig() {
        SimModel simModel = createModelForInitialConfigUpdateTests(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "tub" + GOL_CONFIG_TESTS_EXTENSION);
        testGOLStillLifes(simModel);
    }

    @Test
    public void testGOLBlinkerConfig() {
        SimModel simModel1 = createModelForInitialConfigUpdateTests(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "blinker1" + GOL_CONFIG_TESTS_EXTENSION);
        SimModel simModel2 = createModelForInitialConfigUpdateTests(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "blinker2" + GOL_CONFIG_TESTS_EXTENSION);
        testGOLStateLoops(List.of(simModel1, simModel2));
    }

    @Test
    public void testGOLGliderConfig() {
        SimModel simModel1 = createModelForInitialConfigUpdateTests(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "glider1" + GOL_CONFIG_TESTS_EXTENSION);
        SimModel simModel2 = createModelForInitialConfigUpdateTests(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "glider2" + GOL_CONFIG_TESTS_EXTENSION);
        SimModel simModel3 = createModelForInitialConfigUpdateTests(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "glider3" + GOL_CONFIG_TESTS_EXTENSION);
        SimModel simModel4 = createModelForInitialConfigUpdateTests(GOLSimModel.class,
                GOL_CONFIG_TESTS_PATH + "glider4" + GOL_CONFIG_TESTS_EXTENSION);
        SimModel simModel5 = createModelForInitialConfigUpdateTests(GOLSimModel.class,
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
            List<List<Cell>> referenceModelCells = referenceModel.getCells();
            List<List<Cell>> expectedModelCells = models.get(modelNum).getCells();
            for (int row = 0; row < referenceModelCells.size(); row++) {
                for (int col = 0; col < referenceModelCells.get(0).size(); col++) {
                    assertEquals(referenceModelCells.get(row).get(col).getState(), expectedModelCells.get(row).get(col).getState());
                }
            }
        }
    }

    private void testGOLStillLifes(SimModel simModel) {
        List<List<Cell>> cells = simModel.getCells();
        List<List<String>> initialCellStates = getListOfCellStates(cells);
        // update model and its cells
        simModel.update();

        // check that cells after updating are the same as the initial cells
        for (int row = 0; row < cells.size(); row++) {
            for (int col = 0; col < cells.get(0).size(); col++) {
                assertEquals(initialCellStates.get(row).get(col), cells.get(row).get(col).getState());
            }
        }
    }

    private SimModel createModelForInitialConfigUpdateTests(Class simTypeClassName, String initialConfigFile) {
        final SimController[] simController = new SimController[1];
        javafxRun(() -> {
            simController[0] = new SimController(GOLSimModel.class, new MainController(), initialConfigFile);
        });
        return simController[0].getModel();
    }

    private List<List<String>> getListOfCellStates(List<List<Cell>> cells) {
        List<List<String>> cellStates = new ArrayList<>();
        // store initial states
        for (int row = 0; row < cells.size(); row++) {
            cellStates.add(new ArrayList<>());
            for (int col = 0; col < cells.get(0).size(); col++) {
                cellStates.get(row).add(cells.get(row).get(col).getState());
            }
        }
        return cellStates;
    }

    private void step() {
        javafxRun(() -> myMainController.step(MainController.SECOND_DELAY));
    }

    private void testGOLCellStateChange(int row, int col, String initialState, String updatedStated) {
        createModelFromStart(GOLSimModel.class);
        List<List<Cell>> cells = mySimModel.getCells();

        // get a cell
        Cell loneCell = cells.get(row).get(col);
        // assert that this cell's initial state is correct
        assertEquals(initialState, loneCell.getState());
        // update simulation (one step)
        mySimModel.update();
        // assert that this cell's updated state is correct
        assertEquals(updatedStated, loneCell.getState());
    }

    private void testGOLCellDeath(int row, int col) {
        testGOLCellStateChange(row, col, GOLCell.ALIVE, GOLCell.DEAD);
    }

    private <T extends SimModel> SimModel createModelFromStart(Class<T> simTypeClassName) {
        javafxRun(() -> {
            SimController mySimController = new SimController(simTypeClassName, new MainController(),
                    "configs/" + ConfigSaver.SIM_CLASS_NAME_TO_DIRECTORY.get(simTypeClassName) + "/GOLConfig/GOLConfig.csv");
            mySimModel = mySimController.getModel();
        });
        return mySimModel;
    }

    private void startApplication() {
        // start application
        myMainController = new MainController();
        javafxRun(() -> myMainController.start(new Stage()));
    }

    private void fireButtonEvent(Button button) {
        javafxRun(() -> button.fireEvent(new ActionEvent()));
    }

    private long getNumLinesFromFile(String fileName) {
        try {
            long lineCount = Files.lines(Paths.get(ConfigReader.ERROR_LOG)).count();
            return lineCount;
        } catch (IOException e) {
            //Assert.fail("Exception " + e);
        }
        return -1;
    }
}
