package cellsociety;

import cellsociety.cell.Cell;
import cellsociety.config.ConfigReader;
import cellsociety.config.ConfigSaver;
import cellsociety.grid.Grid;
import cellsociety.simulation.WaTorSimModel;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WaTorTest extends CellSocietyTest {

    @Test
    public void testWaTorGridPopulation() {
        testGridPopulation(WaTorSimModel.class, "WaTorConfig");
    }
}
