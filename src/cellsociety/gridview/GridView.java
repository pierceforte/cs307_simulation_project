package cellsociety.gridview;

import cellsociety.MainController;
import cellsociety.SimController;
import cellsociety.cell.Cell;
import cellsociety.cell.CellView;
import cellsociety.frontend.ColorControlsGUI;
import cellsociety.grid.Grid;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.List;

public abstract class GridView<T extends Cell>{
    public static final int GRID_SIZE = MainController.WIDTH;
    public static final double CELL_GAP = 0;

    private GridPane gridPane;
    private int rows;
    private int cols;
    private double cellSize;
    private double cellHeight;
    private List<RowConstraints> rowConstraints = new ArrayList<>();
    private List<ColumnConstraints> colConstraints = new ArrayList<>();
    private ColumnConstraints finalColConstraints;
    private RowConstraints finalRowContraints;
    private SimController simController;
    private ColorControlsGUI colorControlsGUI;

    public GridView(Grid<T> grid, SimController simController, ColorControlsGUI colorControlsGUI, double cellSizeFactor, double heightFactor) {
        this.rows = grid.getNumRows();
        this.cols = grid.getNumCols();
        this.simController = simController;
        this.colorControlsGUI = colorControlsGUI;
        gridPane = new GridPane();
        gridPane.setHgap(CELL_GAP);
        gridPane.setVgap(CELL_GAP);
        cellSize = ((double) GRID_SIZE) / ((Math.max(cols, rows) + CELL_GAP)) - CellView.STROKE_WIDTH;
        adjustCellSize(cellSizeFactor);
        setCellHeight(heightFactor);
        addConstraints();
        setConstraintsInGridView();
        createView(grid);
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    protected double getCellSize() {
        return cellSize;
    }

    protected double getCellHeight() {
        return cellHeight;
    }

    protected RowConstraints addRowConstraintsAndSetFillHeight(double height) {
        RowConstraints rc = new RowConstraints(height);
        rc.setFillHeight(true);
        rowConstraints.add(rc);
        return rc;
    }

    protected ColumnConstraints addColConstraintsAndSetFillWidth(double width) {
        ColumnConstraints cc = new ColumnConstraints(width);
        cc.setFillWidth(true);
        colConstraints.add(cc);
        return cc;
    }

    protected void setConstraintsInGridView() {
        for (int row = 0; row < rows; row++) {
            gridPane.getRowConstraints().addAll(rowConstraints);
        }
        for (int col = 0; col < cols; col++) {
            gridPane.getColumnConstraints().addAll(colConstraints);
        }
    }

    protected void setFinalRowConstraints(RowConstraints rc) {
        finalRowContraints = rc;
    }

    protected void setFinalColConstraints(ColumnConstraints cc) {
        finalColConstraints = cc;
    }

    protected List<Double> createPoints(int numSides, double rotation) {
        List<Double> points = new ArrayList<>();
        for (int i = 0; i < numSides * 2; i += 2) {
            double angle = Math.PI * (0.5 + i / (double) numSides) + rotation;
            double adjustedSize = cellSize / 2.0;
            points.add(Math.cos(angle) * adjustedSize);
            points.add(Math.sin(angle) * adjustedSize);
        }
        return points;
    }

    protected void createView(Grid<T> grid) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                T cell = grid.get(row, col);
                setCellShapeAndAddToGridView(cell, row, col);
                updateGUI(cell);
            }
        }
        gridPane.getRowConstraints().add(finalRowContraints);
        gridPane.getColumnConstraints().add(finalColConstraints);
    }

    protected void updateGUI(T cell) {
        cell.getView().setOnMouseClicked(event -> simController.handleClick(cell.getRow(), cell.getCol()));
        colorControlsGUI.setCellFill(cell);
        colorControlsGUI.setCellBorder(cell);
    }

    protected void adjustCellSize(double factor) {
        cellSize *= factor;
    }

    protected void setCellHeight(double factor) {
        cellHeight = cellSize * factor;
    }

    protected void addPointsToCellView(T cell, List<Double> points) {
        CellView cellView = cell.getView();
        cellView.getPoints().addAll(points);
    }

    protected abstract void setCellShapeAndAddToGridView(T cell, int row, int col);

    protected abstract void addConstraints();

}
