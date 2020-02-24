package cellsociety.grid;

import cellsociety.cell.Cell;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class GridModel{
    private List<List<Cell>> cells;

    public GridModel(List<List<Cell>> cells) {
        this.cells = cells;
    }

    public List<List<Cell>> getCells() {
        return cells;
    }

    public List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
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
