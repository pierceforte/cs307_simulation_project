package cellsociety;

import cellsociety.cell.Cell;
import cellsociety.simulation.GameOfLifeSimModel;
import cellsociety.simulation.SimController;
import cellsociety.simulation.SimModel;
import javafx.scene.Group;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class CellSocietyTest extends DukeApplicationTest {

    @Test
    public void testGOLGridPopulation() {
        SimModel myModel = createModel(GameOfLifeSimModel.class);
        List<List<Cell>> cellsFromModel = myModel.getCells();

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
        SimModel myModel = createModel(GameOfLifeSimModel.class);
        List<List<Cell>> cells = myModel.getCells();

        // get a cell
        Cell loneCell = cells.get(row).get(col);
        // assert that this cell's initial state is correct
        assertEquals(initialState, loneCell.getState());
        // update simulation (one step)
        myModel.update();
        // assert that this cell's updated state is correct
        assertEquals(updatedStated, loneCell.getState());
    }

    private void testGOLCellDeath(int row, int col) {
        testGOLCellStateChange(row, col, GameOfLifeSimModel.ALIVE, GameOfLifeSimModel.DEAD);
    }

    private <T extends SimModel> SimModel createModel(Class<T> simTypeClassName) {
        // TODO: implement handling for Continue/Restart prompt when starting a simulation from test


        SimController mySimController = new SimController(simTypeClassName, new MainController());
        SimModel myModel = mySimController.getModel();
        return myModel;
    }
}
