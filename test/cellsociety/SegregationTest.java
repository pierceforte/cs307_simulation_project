package cellsociety;

import cellsociety.simulation.GOLSimModel;
import cellsociety.simulation.SegregationSimModel;
import org.junit.jupiter.api.Test;

public class SegregationTest extends CellSocietyTest {

    @Test
    public void testSegregationGridPopulation() {
        testGridPopulation(SegregationSimModel.class, "SegregationConfig");
    }
}
