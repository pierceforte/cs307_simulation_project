package cellsociety.grid;

import cellsociety.cell.Cell;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Grid<T extends Cell> {
    private List<List<T>> cells = new ArrayList<>();
    private int topRow;
    private int topCol;
    private int numStates;

    public Grid(Grid<T> gridToCopy) {
        for (int row = 0; row < gridToCopy.getNumRows(); row++) {
            cells.add(new ArrayList<>());
            for (int col = 0; col < gridToCopy.getNumCols(); col++) {
                cells.get(row).add(gridToCopy.get(row, col));
            }
        }
        setTopRowAndCol();
        setNumStates();
    }

    public Grid(List<List<String>> cellStates, Map<String, Class> cellTypeMap) {
        for (int row = 0; row < cellStates.size(); row++) {
            cells.add(new ArrayList<>());
            for (int col = 0; col < cellStates.get(0).size(); col++) {
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

    public int getNumStates() {
        return numStates;
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

    public T createCell(String state, Map<String, Class> cellTypeMap, int row, int col) {
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
            // TODO: handle exception properly
            e.printStackTrace();
            //logError(e);
            System.exit(0);
            return null;
        }
    }

    public void shuffle() {
        // 1. Add all values in single dimension list
        List<T> allValues = cells.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        // 2. Shuffle all those values
        Collections.shuffle(allValues);

        // 3. Re-create the multidimensional List
        List<List<T>> shuffledValues = new ArrayList<>();
        for (int i = 0; i < allValues.size(); i = i + getNumCols()) {
            shuffledValues.add(allValues.subList(i, i+getNumCols()));
        }

        cells = shuffledValues;
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

    private void setTopRowAndCol() {
        topRow = getNumRows()-1;
        topCol = getNumCols()-1;
    }

    private void setNumStates() {
        Set<String> states = new HashSet<>();
        for (List<T> row : cells) {
            for (T cell : row) {
                states.add(cell.getState());
            }
        }
        numStates = states.size();
    }

}
