package cellsociety.grid;

import cellsociety.cell.Cell;
import cellsociety.cell.CellView;
import javafx.scene.Group;

import java.util.List;

public class GridView {

    private Group root;
    private GridModel model;
    private int size;

    public GridView(int gridSize) {
        this.size = gridSize;
        root = new Group();
    }

    public void setModel(GridModel model){
        this.model = model;
    }

    public void update() {
        root.getChildren().removeAll();

        for (List<Cell> row : model.getCells()) {
            for (Cell cell : row) {
                CellView cellView = new CellView(size/row.size(), model.getCells().indexOf(row), row.indexOf(cell));
                cellView.updateCellColor(cell.getState());
                root.getChildren().add(cellView);
            }
        }

    }

}
