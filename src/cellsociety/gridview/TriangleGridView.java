package cellsociety.gridview;

import cellsociety.SimController;
import cellsociety.cell.Cell;
import cellsociety.frontend.ColorControlsGUI;
import cellsociety.grid.Grid;

import java.util.List;

public class TriangleGridView <T extends Cell> extends GridView {
    public static final int NUM_SIDES = 3;
    public static final double UP_ROTATION = Math.PI;
    public static final double DOWN_ROTATION = Math.PI * 2;
    public static final double CELL_SIZE_FACTOR = 1.4;
    public static final double HEIGHT_FACTOR = Math.sqrt(0.75);
    public static final int COL_SPAN = 3;
    public static final int ROW_SPAN = 2;

    private List<Double> upPoints;
    private List<Double> downPoints;

    public TriangleGridView(Grid<T> grid, SimController simController, ColorControlsGUI colorControlsGUI) {
        super(grid, simController, colorControlsGUI, CELL_SIZE_FACTOR, HEIGHT_FACTOR);
    }

    @Override
    protected void setCellShapeAndAddToGridView(Cell cell, int row, int col, double strokeWidth) {
        upPoints = createPoints(NUM_SIDES, UP_ROTATION);
        downPoints = createPoints(NUM_SIDES, DOWN_ROTATION);
        clearPointsFromCellView(cell);
        addPointsToTriangles(cell, row, col);
        cell.getView().setStrokeWidth(strokeWidth);
        getGridPane().add(cell.getView(), COL_INDEX_FACTOR*col, ROW_INDEX_FACTOR*row, COL_SPAN, ROW_SPAN);
    }

    @Override
    protected void addConstraints() {
        addRowConstraintsAndSetFillHeight(getCellHeight() / 2.4);
        addRowConstraintsAndSetFillHeight(getCellHeight() / 2.4);
        addColConstraintsAndSetFillWidth(getCellSize() / 4.0);
        addColConstraintsAndSetFillWidth(getCellSize() / 7.0);
    }

    private void addPointsToTriangles(Cell cell, int row, int col) {
        if (isTriangleUpright(row, col)) {
            addPointsToCellView(cell, upPoints);
        }
        else {
            addPointsToCellView(cell, downPoints);
        }
    }

    private boolean isTriangleUpright(int row, int col) {
        return row % 2 != 0 ^ col % 2 == 0;
    }
}
