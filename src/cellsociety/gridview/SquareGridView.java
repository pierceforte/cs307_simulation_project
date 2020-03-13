package cellsociety.gridview;

import cellsociety.SimController;
import cellsociety.cell.Cell;
import cellsociety.frontend.ColorControlsGUI;
import cellsociety.grid.Grid;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;

import java.util.List;

/* it's a little difficult to make polygons like this. it is very easy to just use
 * rectangles for squares, which we might want to do*/
public class SquareGridView<T extends Cell> extends GridView {
    public static final int NUM_SIDES = 4;
    public static final double ROTATION = Math.PI / 4;
    public static final double CELL_SIZE_FACTOR = 2;
    public static final double HEIGHT_FACTOR = Math.sqrt(0.75);

    private List<Double> points;

    public SquareGridView(Grid<T> grid, SimController simController, ColorControlsGUI colorControlsGUI) {
        super(grid, simController, colorControlsGUI, CELL_SIZE_FACTOR, HEIGHT_FACTOR);
    }

    @Override
    protected void setCellShapeAndAddToGridView(Cell cell, int row, int col) {
        points = createPoints(NUM_SIDES, ROTATION);
        addPointsToCellView(cell, points);
        getGridPane().add(cell.getView(), col, row);
    }

    @Override
    protected void addConstraints() {
        RowConstraints finalRowConstraints = addRowConstraintsAndSetFillHeight(getCellSize() / 2.0);
        ColumnConstraints finalColConstraints = addColConstraintsAndSetFillWidth(getCellSize() / 2.0);

        setFinalRowConstraints(finalRowConstraints);
        setFinalColConstraints(finalColConstraints);
    }
}
