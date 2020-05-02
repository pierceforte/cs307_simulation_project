package cellsociety;


import cellsociety.cell.Cell;
import cellsociety.grid.Grid;
import cellsociety.backend.RPSModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RPSTest extends CellSocietyTest {

    @Test
    public void testGOLGridPopulation() {
        testGridPopulation(RPSModel.class, "RPSConfig");
    }

    @Test
    public void teststate(){
        testRPSCellStateChange(4,4,"7","7");

        testRPSCellStateChange(2,1,"2","2");
    }
    private void testRPSCellStateChange(int row, int col, String initialState, String updatedStated) {
        createModelFromStart(RPSModel.class);
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

}


