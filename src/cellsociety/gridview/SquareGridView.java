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
    public static final double CELL_SIZE_FACTOR = 1.5;
    public static final double HEIGHT_FACTOR = Math.sqrt(0.75);
    public static final double CONSTRAINT_QUOTIENT = 2.9;
    public static final int COL_SPAN = 3;
    public static final int ROW_SPAN = 3;

    private List<Double> points;

    public SquareGridView(Grid<T> grid, SimController simController, ColorControlsGUI colorControlsGUI) {
        super(grid, simController, colorControlsGUI, CELL_SIZE_FACTOR, HEIGHT_FACTOR);
    }

    @Override
    protected void setCellShapeAndAddToGridView(Cell cell, int row, int col, double strokeWidth) {
        points = createPoints(NUM_SIDES, ROTATION);
        clearPointsFromCellView(cell);
        addPointsToCellView(cell, points);
        cell.getView().setStrokeWidth(strokeWidth);
        getGridPane().add(cell.getView(), COL_INDEX_FACTOR*col, ROW_INDEX_FACTOR*row, COL_SPAN, ROW_SPAN);
    }

    @Override
    protected void addConstraints() {
        addRowConstraintsAndSetFillHeight(getCellSize() / CONSTRAINT_QUOTIENT);
        addRowConstraintsAndSetFillHeight(getCellSize() / CONSTRAINT_QUOTIENT);
        addColConstraintsAndSetFillWidth(getCellSize() / CONSTRAINT_QUOTIENT);
        addColConstraintsAndSetFillWidth(getCellSize() / CONSTRAINT_QUOTIENT);
    }
}
