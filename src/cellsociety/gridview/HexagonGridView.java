package cellsociety.gridview;

import cellsociety.SimController;
import cellsociety.cell.Cell;
import cellsociety.frontend.ColorControlsGUI;
import cellsociety.grid.Grid;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;

import java.util.List;

public class HexagonGridView <T extends Cell> extends GridView {
    public static final int NUM_SIDES = 6;
    public static final double ROTATION = Math.PI / 2;
    public static final double CELL_SIZE_FACTOR = 1.2;
    public static final double HEIGHT_FACTOR = Math.sqrt(0.75);
    public static final int COL_SPAN = 3;
    public static final int ROW_SPAN = 2;

    private List<Double> points;

    public HexagonGridView(Grid<T> grid, SimController simController, ColorControlsGUI colorControlsGUI) {
        super(grid, simController, colorControlsGUI, CELL_SIZE_FACTOR, HEIGHT_FACTOR);
    }

    @Override
    protected void setCellShapeAndAddToGridView(Cell cell, int row, int col, double strokeWidth) {
        points = createPoints(NUM_SIDES, ROTATION);
        clearPointsFromCellView(cell);
        addPointsToCellView(cell, points);
        cell.getView().setStrokeWidth(strokeWidth);
        int offset = col % 2;
        getGridPane().add(cell.getView(), COL_INDEX_FACTOR*col, ROW_INDEX_FACTOR*row + offset, COL_SPAN, ROW_SPAN);
    }

    @Override
    protected void addConstraints() {
        addRowConstraintsAndSetFillHeight(getCellHeight() / 2.0);
        addRowConstraintsAndSetFillHeight(getCellHeight() / 2.0);
        addColConstraintsAndSetFillWidth(getCellSize() / 4.0);
        addColConstraintsAndSetFillWidth(getCellSize() / 2.0);
    }
}
