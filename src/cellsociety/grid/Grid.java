package cellsociety.grid;

import cellsociety.cell.Cell;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * This class is used to create the grid's backend for each simulation.
 *
 * It is important to note that the Grid is entirely independent from the simulation backend, so
 * only one implementation is needed for any type of simulation.
 *
 * This class is dependent on the ordered cell types for the given simulation type, which is defined
 * by the specific Cell implementation.
 *
 * Note that if this project had not been ended early due to COVID-19, a high priority next step would have
 * been to refactor this class.
 *
 * @author Pierce Forte
 */
public class Grid<T extends Cell> {
    private List<List<T>> cells = new ArrayList<>();
    private int topRow;
    private int topCol;
    private int numStates;

    /**
     * The constructor to create a Grid.
     * @param gridToCopy The grid to be copied into the new grid
     */
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

    /**
     * The constructor to create a Grid.
     * @param cellStates The cell states to be used to create the grid
     * @param cellTypeMap The map from cell state to the associated class
     */
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

    /**
     * Get a list of all the cells in the grid.
     * @return A list of all the cells in the grid.
     *
     * TODO: delete this method
     */
    public List<List<T>> getCells() {
        return cells;
    }

    /**
     * Get a cell from the grid at a given row and column.
     * @param row The row of the requested cell
     * @param col The column of the requested cell
     * @return The requested cell
     */
    public T get(int row, int col){
        return cells.get(row).get(col);
    }

    /**
     * Set a cell in the grid to a given row and column.
     * @param row The row of the cell to be set
     * @param col The column of the cell to be set
     * @param cell The cell to be set
     * @param <C> The type of cell
     */
    public <C extends T> void set(int row, int col, C cell){
        cells.get(row).set(col, cell);
    }

    /**
     * Get the number of rows in the grid.
     * @return The number of rows in the grid
     */
    public int getNumRows(){
        return cells.size();
    }

    /**
     * Get the number of columns in the grid.
     * @return The number of columns in the grid
     */
    public int getNumCols(){
        return cells.get(0).size();
    }

    /**
     * Get the orthogonal neighbors of a cell.
     * @param cell The cell to get the neighbors for
     * @return The orthogonal neighbors of the cell
     */
    public List<T> getAllNeighbors(T cell) {
        List<T> neighbors = new ArrayList<>();
        neighbors.addAll(getCardinalNeighbors(cell));
        neighbors.addAll(getDiagonalNeighbors(cell));
        return neighbors;
    }

    /**
     * Get the cardinal neighbors of a cell.
     * @param cell The cell to get the neighbors for
     * @return The cardinal neighbors of the cell
     * TODO: eliminate duplication of first 3 lines
     */
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

    /**
     * Get the diagonal neighbors of a cell.
     * @param cell The cell to get the neighbors for
     * @return The diagonal neighbors of the cell
     */
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

    /**
     * Execute a consumer function for all cells in the grid.
     * @param lambda The consumer function to execute
     */
    public void executeForAllCells(Consumer<T> lambda) {
        for (List<T> row : cells) {
            for (T cell : row) {
                lambda.accept(cell);
            }
        }
    }

    /**
     * Create a cell
     * @param state The state of the cell
     * @param cellTypeMap The map that provides the associated class for the cell
     * @param row The row of the cell
     * @param col The column of the cell
     * @return The created cell
     */
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

    /**
     * Randomly shuffle the grid.
     *
     * Note: code was gathered from StackOverflow at
     * https://stackoverflow.com/questions/47995662/shuffle-multidimensional-list-arraylist-linkedlist/47995772#47995772
     */
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
