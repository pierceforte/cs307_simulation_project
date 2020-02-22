package cellsociety.grid;

import cellsociety.cell.Cell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GridModel{
    private List<List<Cell>> grid;

    public GridModel(Collection<Collection<Object>> grid) {
        this.grid = new ArrayList<>();
        for (Collection row : grid) {
            List<Cell> cellRow = new ArrayList<>();
            for (Object obj : grid) {
                cellRow.add(new Cell());
            }
            this.grid.add(cellRow);
        }
    }

    public List<List<Cell>> getCells() {
        return grid;
    }

    public List<Cell> getNeighbors(int row, int col) {
        List<Cell> neighbors = new ArrayList<>();
        int topRow = grid.size()-1;
        int topCol = grid.get(0).size()-1;

        if (row != 0) {
            neighbors.add(grid.get(row-1).get(col));
        }
        if (col != 0) {
            neighbors.add(grid.get(row).get(col-1));
        }
        if (row != 0 && col != 0) {
            neighbors.add(grid.get(row-1).get(col-1));
        }
        if (row != topRow) {
            neighbors.add(grid.get(topRow).get(col));
        }
        if (col != topCol) {
            neighbors.add(grid.get(row).get(topCol));
        }
        if (row != topRow && col != 0) {
            neighbors.add(grid.get(topRow).get(col-1));
        }
        if (row != 0 && col != topCol) {
            neighbors.add(grid.get(row-1).get(topCol));
        }
        if (row != topRow && col != topCol) {
            neighbors.add(grid.get(topRow).get(topCol));
        }
        return neighbors;
    }
}
