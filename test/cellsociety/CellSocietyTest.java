package cellsociety;

import cellsociety.cell.Cell;
import cellsociety.cell.GOL.GOLCell;
import cellsociety.config.ConfigReader;
import cellsociety.config.ConfigSaver;
import cellsociety.grid.Grid;
import cellsociety.simulation.GOLSimModel;
import cellsociety.simulation.SimController;
import cellsociety.simulation.SimModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
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
    public void testException() {
        ConfigReader data = new ConfigReader("BAD_FILE" + SimController.CONFIG_FILE_SUFFIX);
        assertThrows(java.lang.Exception.class, () -> data.getCellList());
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

    // TODO: remove this test (only here because it may have useful code)
    /*@Test
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
    }*/

    @Test
    public void testExitSimulationButton() {
        // assert that exit button is not present before simulation is started
        assertThrows(org.testfx.service.query.EmptyNodeQueryException.class, () -> lookup("#exitBttn").query());

        startApplicationFromFile(GOLSimModel.class, "resources/configs/GOL/GOLConfig/GOLConfig.csv");

        // assert button is now present
        assertNotNull(lookup("#exitBttn").query());
        Button exitBttn = lookup("#exitBttn").query();

        // assert that exit simulation prompts are not present before pressing exit button
        assertThrows(org.testfx.service.query.EmptyNodeQueryException.class, () -> lookup("#inputPane").query());
        // press button to exit
        fireButtonEvent(exitBttn);
        // assert that exit simulation prompts are now present
        assertNotNull(lookup("#inputPane").query());
    }

    // TODO: remove this test (only here because it may have useful code)
    /*@Test
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
    }*/

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
        // assert that pause and play buttons are not yet present
        assertThrows(org.testfx.service.query.EmptyNodeQueryException.class, () -> lookup("#pauseBttn").query());
        assertThrows(org.testfx.service.query.EmptyNodeQueryException.class, () -> lookup("#playBttn").query());
        // begin a simulation (here we use a GOL simulation)
        startApplicationFromFile(GOLSimModel.class, "resources/configs/GOL/GOLConfig/GOLConfig.csv");
        // assert that pause and play buttons are now present
        assertNotNull(lookup("#pauseBttn").query());
        assertNotNull(lookup("#playBttn").query());

        Button pauseButton = lookup("#pauseBttn").query();
        Button playButton = lookup("#playBttn").query();

        // check that simulation is active before pause
        assertTrue(myMainController.getCurSimController().isActive());
        // collect cells
        Grid<GOLCell> grid = myMainController.getCurSimController().getModel().getGrid();
        List<List<String>> prePauseCellStates = getListOfCellStates(grid);
        // pause and check that simulation is inactive
        fireButtonEvent(pauseButton);
        assertFalse(myMainController.getCurSimController().isActive());

        // check that cells during the pause are the same as the cells before the pause
        for (int row = 0; row < grid.getNumRows(); row++) {
            System.out.println();
            for (int col = 0; col < grid.getNumCols(); col++) {
                assertEquals(prePauseCellStates.get(row).get(col), grid.get(row, col).getState());
            }
        }

        // play and check that simulation is active
        fireButtonEvent(playButton);
        assertTrue(myMainController.getCurSimController().isActive());
        step();

        boolean notTheSame = false;
        for (int row = 0; row < grid.getNumRows(); row++) {
            System.out.println();
            for (int col = 0; col < grid.getNumCols(); col++) {
                if (!prePauseCellStates.get(row).get(col).equals(grid.get(row, col).getState())) {
                    notTheSame = true;
                    break;
                }
            }
        }
        assertTrue(notTheSame);
    }

    protected SimModel createModelFromFile(Class simTypeClassName, String initialConfigFile) {
        final SimController[] simController = new SimController[1];
        javafxRun(() -> {
            simController[0] = new SimController(GOLSimModel.class, new MainController(), initialConfigFile);
        });
        return simController[0].getModel();
    }

    protected <T extends Cell> List<List<String>> getListOfCellStates(Grid<T> grid) {
        List<List<String>> cellStates = new ArrayList<>();
        // store initial states
        for (int row = 0; row < grid.getNumRows(); row++) {
            cellStates.add(new ArrayList<>());
            for (int col = 0; col < grid.getNumCols(); col++) {
                cellStates.get(row).add(grid.get(row, col).getState());
            }
        }
        return cellStates;
    }

    protected void step() {
        javafxRun(() -> myMainController.step(MainController.SECOND_DELAY));
    }

    protected <T extends SimModel> SimModel createModelFromStart(Class<T> simTypeClassName) {
        javafxRun(() -> {
            String simName = ConfigSaver.SIM_CLASS_NAME_TO_DIRECTORY.get(simTypeClassName);
            String simConfig = simName + "Config";
            SimController mySimController = new SimController(simTypeClassName, new MainController(),
                    "configs/" + simName + "/" + simConfig + "/" + simConfig + ".csv" );
            mySimModel = mySimController.getModel();
        });
        return mySimModel;
    }

    protected void startApplication() {
        // start application
        myMainController = new MainController();
        javafxRun(() -> myMainController.start(new Stage()));
    }

    protected <T extends SimModel> void startApplicationFromFile(Class<T> simModelClass, String csvFilePath) {
        // start application
        myMainController = new MainController(){
            @Override
            public void start(Stage stage) {
                setMyStage(stage);
                getMyStage().show();
                setMyAnimation(getMyStage());
                myMainController.beginSimulation(simModelClass, csvFilePath);
            }
        };
        javafxRun(() -> myMainController.start(new Stage()));
    }

    protected void fireButtonEvent(Button button) {
        javafxRun(() -> button.fireEvent(new ActionEvent()));
    }

    protected long getNumLinesFromFile(String fileName) {
        try {
            long lineCount = Files.lines(Paths.get(ConfigReader.ERROR_LOG)).count();
            return lineCount;
        } catch (IOException e) {
            //Assert.fail("Exception " + e);
        }
        return -1;
    }

    protected SimModel getMySimModel() {
        return mySimModel;
    }

    protected void setMySimModel(SimModel simModel) {
        mySimModel = simModel;
    }


    protected void testCellChangeState(int row, int col, String initialState, String updatedState, SimModel model) {
        Grid cells = model.getGrid();
        // get a cell
        Cell loneCell = cells.get(row, col);
        // assert that this cell's initial state is correct
        assertEquals(initialState, loneCell.getState());
        // update simulation (one step)
        model.update();
        // assert that this cell's updated state is correct
        assertEquals(updatedState, loneCell.getState());
    }
}
