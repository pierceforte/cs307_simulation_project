package cellsociety.grid;

import cellsociety.cell.Cell;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class GridModel<T extends Cell> {
    private List<List<T>> cells;

    public GridModel(List<List<T>> cells) {
        this.cells = cells;
    }

    public List<List<T>> getCells() {
        return cells;
    }

    public List<T> getNeighbors(T cell) {
        List<T> neighbors = new ArrayList<>();
        int row = cell.getRow();
        int col = cell.getCol();
        int topRow = cells.size()-1;
        int topCol = cells.get(0).size()-1;

        if (row != 0) {
            neighbors.add(cells.get(row-1).get(col));
        }
        if (col != 0) {
            neighbors.add(cells.get(row).get(col-1));
        }
        if (row != 0 && col != 0) {
            neighbors.add(cells.get(row-1).get(col-1));
        }
        if (row != topRow) {
            neighbors.add(cells.get(row+1).get(col));
        }
        if (col != topCol) {
            neighbors.add(cells.get(row).get(col+1));
        }
        if (row != topRow && col != 0) {
            neighbors.add(cells.get(row+1).get(col-1));
        }
        if (row != 0 && col != topCol) {
            neighbors.add(cells.get(row-1).get(col+1));
        }
        if (row != topRow && col != topCol) {
            neighbors.add(cells.get(row+1).get(col+1));
        }

        return neighbors;
    }
}
