package cellsociety;

import cellsociety.cell.Cell;
import cellsociety.simulation.GameOfLifeSimModel;
import cellsociety.simulation.SimController;
import cellsociety.simulation.SimModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;


public class CellSocietyTest extends DukeApplicationTest {

    private MainController myMainController;
    private SimModel mySimModel;

    @Test
    public void testGOLGridPopulation() {
        mySimModel = createModelFromStart(GameOfLifeSimModel.class);
        List<List<Cell>> cellsFromModel = mySimModel.getCells();

        ConfigReader data = new ConfigReader(SimController.GOL_FILE_IDENTIFIER + SimController.CONFIG_FILE_SUFFIX);
        List<List<Cell>> cellsFromFile = data.getCellList();

        for (int row = 0; row < cellsFromModel.size(); row++) {
            for (int col = 0; col < cellsFromModel.get(0).size(); col++) {
                assertEquals(cellsFromFile.get(row).get(col).getState(), cellsFromModel.get(row).get(col).getState());
            }
        }
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
        testGOLCellStateChange(0, 7, GameOfLifeSimModel.DEAD, GameOfLifeSimModel.ALIVE);
    }

    @Test
    public void testGOLCellStaysAlive() {
        // test a cell with 2 neighbors
        testGOLCellStateChange(0, 10, GameOfLifeSimModel.ALIVE, GameOfLifeSimModel.ALIVE);
        // test a cell with 3 neighbors
        testGOLCellStateChange(11, 3, GameOfLifeSimModel.ALIVE, GameOfLifeSimModel.ALIVE);
    }

    @Test
    public void testGOLCellStaysDead() {
        // test a dead cell with less than 3 neighbors (in this case, 2)
        testGOLCellStateChange(0, 5, GameOfLifeSimModel.DEAD, GameOfLifeSimModel.DEAD);
        // test a dead cell with more than 3 neighbors (in this case, 4)
        testGOLCellStateChange(0, 9, GameOfLifeSimModel.DEAD, GameOfLifeSimModel.DEAD);
    }

    @Test
    public void testGOLReset() {
        mySimModel = createModelFromStart(GameOfLifeSimModel.class);
        List<List<Cell>> initialCellsFromModel = mySimModel.getCells();

        // update simulation
        mySimModel.update();

        // exit simulation
        javafxRun(() -> lookup("#exitBttn").query().fireEvent(new ActionEvent()));

        // start simulation again and restart it
        mySimModel = createModelFromStart(GameOfLifeSimModel.class);
        List<List<Cell>> restartedCellsFromModel = mySimModel.getCells();

        for (int row = 0; row < initialCellsFromModel.size(); row++) {
            for (int col = 0; col < initialCellsFromModel.get(0).size(); col++) {
                assertEquals(initialCellsFromModel.get(row).get(col).getState(), restartedCellsFromModel.get(row).get(col).getState());
            }
        }
    }

    @Test
    public void testIntroPaneCreation() {
        // assert that intro pane is not present
        assertThrows(org.testfx.service.query.EmptyNodeQueryException.class, () -> lookup("#introPane").query());
        startApplication();
        // assert that intro pane is now present
        assertNotNull(lookup("#introPane").query());

        /*
        //mySimModel = createModelFromStart(GameOfLifeSimModel.class);
        sleep(3, TimeUnit.SECONDS);
        //mySimModel.getSimController().update(true);
        sleep(3, TimeUnit.SECONDS);
        // assert that exit button is present
        javafxRun(() -> assertNotNull(lookup("#exitBttn").query()));


        //javafxRun(() -> lookup("#exitBttn").query().fireEvent(new ActionEvent()));
        */
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

        Button restartSimButton = lookup("#restartBttn").query();
        fireButtonEvent(restartSimButton);


        sleep(3, TimeUnit.SECONDS);
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

        Button restartSimButton = lookup("#restartBttn").query();
        fireButtonEvent(restartSimButton);

        // assert button is now present
        assertNotNull(lookup("#exitBttn").query());

        Button exitBttn = lookup("#exitBttn").query();

        // press button to exit
        fireButtonEvent(exitBttn);
        // assert that button is no longer present
        assertThrows(org.testfx.service.query.EmptyNodeQueryException.class, () -> lookup("#exitBttn").query());
    }

    @Test
    public void testContinueSimulationButton() {
        startApplication();
        // begin GOL simulation from beginning
        fireButtonEvent(lookup("#GOLSimButton").query());
        fireButtonEvent(lookup("#restartBttn").query());
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

    private void step() {
        javafxRun(() -> myMainController.step(MainController.SECOND_DELAY));
    }

    private void testGOLCellStateChange(int row, int col, String initialState, String updatedStated) {
        createModelFromStart(GameOfLifeSimModel.class);
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
        testGOLCellStateChange(row, col, GameOfLifeSimModel.ALIVE, GameOfLifeSimModel.DEAD);
    }

    private <T extends SimModel> SimModel createModelFromStart(Class<T> simTypeClassName) {
        javafxRun(() -> {
            SimController mySimController = new SimController(simTypeClassName, new MainController());
            mySimModel = mySimController.getModel();
        });
        // choose to restart simulation from initial configuration
        try {
            javafxRun(() -> fireButtonEvent(lookup("#restartBttn").query()));
        }
        catch (NullPointerException e) {
            // logError(e);
            // if button not presented, we don't have to do anything
        }
        return mySimModel;
    }

    private void startApplication() {
        // start application
        myMainController= new MainController();
        javafxRun(() -> myMainController.start(new Stage()));
    }

    private void fireButtonEvent(Button button) {
        javafxRun(() -> button.fireEvent(new ActionEvent()));
    }
}
