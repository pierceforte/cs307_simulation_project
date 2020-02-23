package cellsociety;

import cellsociety.cell.Cell;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ConfigReader {
    public static final int NUM_ROWS_INDEX = 0;
    public static final int NUM_COLS_INDEX = 0;
    public static final String DATA_REGEX = ",";

    private final String simulationInitialLayout;
    private int quantityOfRows;
    private int quantityOfColumns;
    public ConfigReader(String fileOfCells) {
        simulationInitialLayout = fileOfCells;
    }

    @SuppressWarnings("ThrowablePrintedToSystemOut")
    public List<List<Cell>> getCellList() {
        try {
            File file = new File(Objects.requireNonNull(Main.class.getClassLoader().getResource(simulationInitialLayout)).getFile());
            return buildListOfCellLists(file);
        } catch (FileNotFoundException e)
        {
            System.out.println(e);
            System.exit(0);
        }
        return null;
    }

    private List<List<Cell>> buildListOfCellLists(File file) throws FileNotFoundException {
        List<List<Cell>> results = new ArrayList<>();
        Scanner input = new Scanner(file);
        String[] dimensions = input.next().split(DATA_REGEX);
        quantityOfRows = Integer.valueOf(dimensions[NUM_ROWS_INDEX]);
        quantityOfColumns = Integer.valueOf(dimensions[NUM_COLS_INDEX]);
        for(int r = 0; r < quantityOfRows; r++) {
                List<Cell> row = getRowInfo(input.next(), r);
                results.add(row);
        }
        return results;
    }

    private List<Cell> getRowInfo(String row, int r) {
        String[] arrayOfInfo = row.split(DATA_REGEX);
        return makeCellObjects(arrayOfInfo, r);
    }
    private List<Cell> makeCellObjects(String[] rowArray, int r) {
        List<Cell> cellRow = new ArrayList<>();
        for(int c = 0; c < rowArray.length; ) {
            cellRow.add(new Cell(c, r, Integer.valueOf(rowArray[c])));
        }
        return cellRow;
    }

}
