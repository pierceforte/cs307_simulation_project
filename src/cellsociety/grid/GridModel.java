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

    public List<T> getAllNeighbors(T cell) {
        List<T> neighbors = new ArrayList<>();

        neighbors.addAll(getCardinalNeighbors(cell));
        neighbors.addAll(getDiagonalNeighbors(cell));

        return neighbors;
    }

    // TODO: eliminate duplication of first 5 lines
    public List<T> getCardinalNeighbors(T cell) {
        List<T> cardinalNeighbors = new ArrayList<>();
        int row = cell.getRow();
        int col = cell.getCol();
        int topRow = cells.size()-1;
        int topCol = cells.get(0).size()-1;
        int[] rowdiffarray = {-1,-1,-1,0,0,1,1,1};
        int[] coldiffarray = {-1,0,1,-1,1,-1,0,1};
        int lengthofarrayscol_row = rowdiffarray.length;



        if (row != 0) {
            cardinalNeighbors.add(cells.get(row-1).get(col));
        }
        if (col != 0) {
            cardinalNeighbors.add(cells.get(row).get(col-1));
        }
        if (row != topRow) {
            cardinalNeighbors.add(cells.get(row+1).get(col));
        }
        if (col != topCol) {
            cardinalNeighbors.add(cells.get(row).get(col+1));
        }

        return cardinalNeighbors;
    }

    public List<T> getDiagonalNeighbors(T cell) {
        List<T> diagonalNeighbors = new ArrayList<>();
        int row = cell.getRow();
        int col = cell.getCol();
        int topRow = cells.size()-1;
        int topCol = cells.get(0).size()-1;

        if (row != 0 && col != 0) {
            diagonalNeighbors.add(cells.get(row-1).get(col-1));
        }
        if (row != topRow && col != 0) {
            diagonalNeighbors.add(cells.get(row+1).get(col-1));
        }
        if (row != 0 && col != topCol) {
            diagonalNeighbors.add(cells.get(row-1).get(col+1));
        }
        if (row != topRow && col != topCol) {
            diagonalNeighbors.add(cells.get(row+1).get(col+1));
        }

        return diagonalNeighbors;

    }
}
