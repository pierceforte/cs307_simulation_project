package cellsociety;

import cellsociety.simulation.WaTorSimModel;
import org.junit.jupiter.api.Test;

public class WaTorTest extends CellSocietyTest {

    @Test
    public void testWaTorGridPopulation() {
        testGridPopulation(WaTorSimModel.class, "WaTorConfig");
    }
}
