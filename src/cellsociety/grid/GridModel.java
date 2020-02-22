package cellsociety.grid;

import cellsociety.cell.Cell;

import java.util.ArrayList;
import java.util.List;

public class GridModel{
    private List<List<Cell>> cells;

    public GridModel(List<List<String>> grid) {
        this.cells = new ArrayList<>();
        for (int row = 0; row < grid.size(); row++) {
            List<Cell> cellRow = new ArrayList<>();
            for (int col = 0; col < grid.get(0).size(); col++) {
                cellRow.add(new Cell(grid.get(row).get(col), row, col));
            }
            this.cells.add(cellRow);
        }
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
            neighbors.add(cells.get(topRow).get(col));
        }
        if (col != topCol) {
            neighbors.add(cells.get(row).get(topCol));
        }
        if (row != topRow && col != 0) {
            neighbors.add(cells.get(topRow).get(col-1));
        }
        if (row != 0 && col != topCol) {
            neighbors.add(cells.get(row-1).get(topCol));
        }
        if (row != topRow && col != topCol) {
            neighbors.add(cells.get(topRow).get(topCol));
        }
        return neighbors;
    }
}
