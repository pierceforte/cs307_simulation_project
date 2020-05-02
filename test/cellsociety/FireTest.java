package cellsociety;

import cellsociety.cell.fire.EmptyCell;
import cellsociety.cell.fire.FireCell;
import cellsociety.cell.fire.TreeCell;
import cellsociety.grid.Grid;
import cellsociety.backend.FireModel;
import cellsociety.backend.SimModel;
import org.junit.jupiter.api.Test;

public class FireTest extends CellSocietyTest {

    @Test
    public void testFireSpreadWithBurningNeighbour() {
        SimModel model = createModelFromStart(FireModel.class);
        setMySimModel(model);
        Grid grid = getMySimModel().getGrid();
        TreeCell tree = (TreeCell)grid.get(3, 4); //cell beside initial burning cell
        model.update();
        testCellChangeState(3, 4, tree.getState(), FireCell.BURNING, model);
    }

    @Test
    public void testFireSpreadNoBurningNeighbour() {
        SimModel model = createModelFromStart(FireModel.class);
        setMySimModel(model);
        Grid grid = getMySimModel().getGrid();
        TreeCell tree = (TreeCell)grid.get(1, 1);
        tree.setRndSeed(0); //generates a number that ensures tree will catch fire
        model.update();
        testCellChangeState(1, 1, tree.getState(), FireCell.BURNING, model);
    }

    @Test
    public void testTreeSpawn() {
        SimModel model = createModelFromStart(FireModel.class);
        setMySimModel(model);
        Grid grid = getMySimModel().getGrid();
        EmptyCell empty = (EmptyCell)grid.get(0, 0); //empty cell
        empty.setRndSeed(0);//generates a number that ensures tree will spawn
        model.update();
        testCellChangeState(0, 0, empty.getState(), FireCell.TREE, model);
    }

    @Test
    public void testBurningCellExtinguish(){
        testCellChangeState(4, 4, FireCell.BURNING, FireCell.EMPTY, createModelFromStart(FireModel.class));
    }


    @Test
    public void testFireGridPopulation() {
        testGridPopulation(FireModel.class, "FireConfig");
    }


}
