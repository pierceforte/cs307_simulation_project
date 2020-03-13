package cellsociety.gridview;

import cellsociety.SimController;
import cellsociety.cell.Cell;
import cellsociety.frontend.ColorControlsGUI;
import cellsociety.grid.Grid;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;

import java.util.List;

public class TriangleGridView <T extends Cell> extends GridView {
    public static final int NUM_SIDES = 3;
    public static final double UP_ROTATION = Math.PI;
    public static final double DOWN_ROTATION = Math.PI * 2;
    public static final double CELL_SIZE_FACTOR = 1.2;
    public static final double HEIGHT_FACTOR = Math.sqrt(0.75);
    public static final int COL_SPAN = 3;
    public static final int ROW_SPAN = 2;

    private List<Double> upPoints;
    private List<Double> downPoints;

    public TriangleGridView(Grid<T> grid, SimController simController, ColorControlsGUI colorControlsGUI) {
        super(grid, simController, colorControlsGUI, CELL_SIZE_FACTOR, HEIGHT_FACTOR);
    }

    @Override
    protected void setCellShapeAndAddToGridView(Cell cell, int row, int col) {
        upPoints = createPoints(NUM_SIDES, UP_ROTATION);
        downPoints = createPoints(NUM_SIDES, DOWN_ROTATION);
        if (row % 2 != 0 ^ col % 2 == 0) {
            addPointsToCellView(cell, upPoints);
        } else {
            addPointsToCellView(cell, downPoints);
        }
        getGridPane().add(cell.getView(), 2 * col, 2 * row, COL_SPAN, ROW_SPAN);
    }

    @Override
    protected void addConstraints() {
        RowConstraints finalRowConstraints = addRowConstraintsAndSetFillHeight(getCellHeight() / 2.2);
        addRowConstraintsAndSetFillHeight(getCellHeight() / 2.2);
        ColumnConstraints finalColConstraints = addColConstraintsAndSetFillWidth(getCellSize() / 4.0);
        addColConstraintsAndSetFillWidth(getCellSize() / 4.8);

        setFinalRowConstraints(finalRowConstraints);
        setFinalColConstraints(finalColConstraints);


    }
}
