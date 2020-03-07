package cellsociety;


import cellsociety.simulation.RPSModel;
import org.junit.jupiter.api.Test;

public class RPSTest extends CellSocietyTest {

    @Test
    public void testGOLGridPopulation() {
        testGridPopulation(RPSModel.class, "RPSConfig");
    }
}


