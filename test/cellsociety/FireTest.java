package cellsociety;


import cellsociety.simulation.FireSimModel;
import org.junit.jupiter.api.Test;

public class FireTest extends CellSocietyTest {

    @Test
    public void testFireGridPopulation() {
        testGridPopulation(FireSimModel.class, "FireConfig");
    }

}
