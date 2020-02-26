package cellsociety.grid;

import cellsociety.cell.Cell;

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
        int[] rowdiffarray = {-1,-1,-1,0,0,1,1,1};
        int[] coldiffarray = {-1,0,1,-1,1,-1,0,1};
        int lengthofarrayscol_row = rowdiffarray.length;

//        for(int i =0; i< lengthofarrayscol_row;i++){
//            if(row==0 && col ==0){
//                neighbors.add(cells.get(rowdiffarray[i]+row+1).get(coldiffarray[i]+col));
//            }
//            if(row==0 && col ==0){
//                neighbors.add(cells.get(rowdiffarray[i]+row+1).get(coldiffarray[i]+col));
//            }
//            neighbors.add(cells.get(rowdiffarray[i]+row).get(coldiffarray[i]+col));
//
//        }

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
            neighbors.add(cells.get(row).get(col+1));
        }
        if (col != topCol) {
            neighbors.add(cells.get(row).get(col+1));
        }
        if (row != topRow && col != 0) {
            neighbors.add(cells.get(row+1).get(col-1));
        }
        if (row != 0 && col != topCol) {
            neighbors.add(cells.get(row+1).get(col+1));
        }
        if (row != topRow && col != topCol) {
            neighbors.add(cells.get(row).get(col+1));
        }

        return neighbors;
    }
}
