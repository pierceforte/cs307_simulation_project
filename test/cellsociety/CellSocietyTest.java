package cellsociety;

import cellsociety.cell.Cell;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CellSocietyTest extends DukeApplicationTest {

    @Test
    public void testnumcells_columns(){
        String simulationInitialLayout =  "testfile.csv";
        ConfigReader con = new ConfigReader(simulationInitialLayout);
        File file = new File(Objects.requireNonNull(Main.class.getClassLoader().getResource(simulationInitialLayout)).getFile());
        List<List<Cell>> ret = null;
        try {
            con.buildListOfCellLists(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assertEquals(con.getManualquantityOfColumns(),con.getQuantityOfColumns());
        assertEquals(con.getManualquantityOfRows(),con.getQuantityOfRows());

    }
    @Test
    public void testGridPopulation() {

    }

    @Test
    public void testGameOfLifeUpdate() {

    }
}
