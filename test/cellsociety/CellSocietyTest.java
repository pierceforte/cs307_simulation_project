package cellsociety;

import cellsociety.cell.Cell;
import cellsociety.simulation.GameOfLifeSimModel;
import cellsociety.simulation.SimController;
import cellsociety.simulation.SimModel;
import javafx.event.ActionEvent;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CellSocietyTest extends DukeApplicationTest {

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
            javafxRun(() -> lookup("#restartBttn").query().fireEvent(new ActionEvent()));
        }
        catch (NullPointerException e) {
            // logError(e);
            // if button not presented, we don't have to do anything
        }
        return mySimModel;
    }
}
