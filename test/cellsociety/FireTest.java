package cellsociety;

import cellsociety.cell.Cell;
import cellsociety.config.ConfigReader;
import cellsociety.config.ConfigSaver;
import cellsociety.grid.Grid;
import cellsociety.simulation.FireSimModel;
import cellsociety.simulation.GOLSimModel;
import cellsociety.simulation.WaTorSimModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FireTest extends CellSocietyTest {

    @Test
    public void testFireGridPopulation() {
        testGridPopulation(FireSimModel.class, "FireConfig");
    }

}
