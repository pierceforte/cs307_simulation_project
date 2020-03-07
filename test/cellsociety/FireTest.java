package cellsociety;

import cellsociety.cell.Cell;
import cellsociety.config.ConfigReader;
import cellsociety.config.ConfigSaver;
import cellsociety.grid.Grid;
import cellsociety.simulation.FireSimModel;
import cellsociety.simulation.GOLSimModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FireTest extends CellSocietyTest {


    @Test
    public <T extends Cell> void testFireGridPopulation() {
        setMySimModel(createModelFromStart(FireSimModel.class));
        Grid<T> gridFromModel = getMySimModel().getGrid();

        ConfigReader data = new ConfigReader("configs/" + ConfigSaver.SIM_CLASS_NAME_TO_DIRECTORY.get(FireSimModel.class) +
                "/FireConfig/FireConfig.csv");
        List<List<String>> cellStatesFromFile = data.getCellList();

        assertEquals(data.getManualQuantityOfColumns(),data.getQuantityOfColumns());
        assertEquals(data.getManualQuantityOfRows(),data.getQuantityOfRows());
        for (int row = 0; row < gridFromModel.getNumRows(); row++) {
            for (int col = 0; col < gridFromModel.getNumCols(); col++) {
                assertEquals(cellStatesFromFile.get(row).get(col), gridFromModel.get(row, col).getState());
            }
        }
    }
}
