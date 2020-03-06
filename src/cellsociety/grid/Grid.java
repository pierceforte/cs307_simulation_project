package cellsociety.grid;

import cellsociety.cell.Cell;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Grid<T extends Cell> {
    private List<List<T>> cells = new ArrayList<>();
    private int topRow;
    private int topCol;

    public Grid(Grid<T> gridToCopy) {
        for (int row = 0; row < gridToCopy.getNumRows(); row++) {
            cells.add(new ArrayList<>());
            for (int col = 0; col < gridToCopy.getNumCols(); col++) {
                cells.get(row).add(gridToCopy.get(row, col));
            }
        }
        setTopRowAndCol();
    }

    public Grid(List<List<String>> cellStates, Map<String, Class> cellTypeMap) {
        for (int row = 0; row < cellStates.size(); row++) {
            cells.add(new ArrayList<>());
            for (int col = 0; col < cellStates.size(); col++) {
                String state = cellStates.get(row).get(col);
                T cell = createCell(state, cellTypeMap, row, col);
                cells.get(row).add(cell);
            }
        }
        setTopRowAndCol();
    }

    //TODO: delete this method
    public List<List<T>> getCells() {
        return cells;
    }

    public T get(int row, int col){
        return cells.get(row).get(col);
    }

    public <C extends T> void set(int row, int col, C cell){
        cells.get(row).set(col, cell);
    }

    public int getNumRows(){
        return cells.size();
    }

    public int getNumCols(){
        return cells.get(0).size();
    }

    public List<T> getAllNeighbors(T cell) {
        List<T> neighbors = new ArrayList<>();
        neighbors.addAll(getCardinalNeighbors(cell));
        neighbors.addAll(getDiagonalNeighbors(cell));
        return neighbors;
    }

    // TODO: eliminate duplication of first 3 lines
    public List<T> getCardinalNeighbors(T cell) {
        List<T> cardinalNeighbors = new ArrayList<>();
        int row = cell.getRow();
        int col = cell.getCol();

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

    public void executeForAllCells(Consumer<T> lambda) {
        for (List<T> row : cells) {
            for (T cell : row) {
                lambda.accept(cell);
            }
        }
    }

    private boolean constructorHasStateParam(Class cellClass) {
        try {
            cellClass.getConstructor(String.class, int.class, int.class);
            return true;
        }
        catch (NoSuchMethodException e) {
            return false;
        }
    }

    private T createCell(String state, Map<String, Class> cellTypeMap, int row, int col) {
        Class cellClass = cellTypeMap.get(state);
        try {
            Constructor<?> constructor;
            T cell;
            if (constructorHasStateParam(cellClass)) {
                constructor = cellClass.getConstructor(String.class, int.class, int.class);
                cell = (T) constructor.newInstance(state, row, col);
            }
            else {
                constructor = cellClass.getConstructor(int.class, int.class);
                cell = (T) constructor.newInstance(row, col);
            }
            return cell;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            //logError(e);
            System.exit(0);
            return null;
        }
    }

    private void setTopRowAndCol() {
        topRow = getNumRows()-1;
        topCol = getNumCols()-1;
    }

}
