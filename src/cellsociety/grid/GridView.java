package cellsociety.grid;

import cellsociety.cell.Cell;
import cellsociety.cell.CellView;
import javafx.scene.Group;
import javafx.scene.Node;

import java.util.List;

public class GridView {

    private Group root;
    private int size;

    public GridView(int gridSize) {
        this.size = gridSize;
        root = new Group();
    }

    public Node getRoot(){
        return root;
    }

    public void update(List<List<Cell>> cells) {
        root.getChildren().removeAll();

        for (List<Cell> row : cells) {
            for (Cell cell : row) {
                CellView cellView = new CellView(size/row.size(), cells.indexOf(row), row.indexOf(cell));
                cellView.updateCellColor(cell.getState());
                root.getChildren().add(cellView);
            }
        }

    }

}
