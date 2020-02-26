package cellsociety;

import cellsociety.cell.Cell;

import java.io.*;
import java.util.*;

public class ConfigReader {
    public static final int NUM_ROWS_INDEX = 0;
    public static final int NUM_COLS_INDEX = 1;
    public static final String DATA_REGEX = ",";
    public static final String ERROR_LOG = "error_log.txt";

    private final String simulationInitialLayout;
    private int quantityOfRows;
    private int quantityOfColumns;
    private int manualColumnquantity;
    private int manualRowquantity;
   // private List<List<Cell>> myResults;


    public ConfigReader(String fileOfCells) {
        simulationInitialLayout = fileOfCells;
    }
    Random rand = new Random();

    @SuppressWarnings("ThrowablePrintedToSystemOut")
    public List<List<Cell>> getCellList() {
        try {
            File file = new File(Objects.requireNonNull(this.getClass().getClassLoader().getResource(simulationInitialLayout)).getFile());
             buildListOfCellLists(file);
        } catch (FileNotFoundException e)
        {
            logError(e);
            System.exit(0);
        }
        return null;
    }

    protected List<List<Cell>> buildListOfCellLists(File file) throws FileNotFoundException {
        List<List<Cell>> results = new ArrayList<>();
        Scanner input = new Scanner(file);
        String[] dimensions = input.next().split(DATA_REGEX);
        quantityOfRows = Integer.valueOf(dimensions[NUM_ROWS_INDEX]);
        quantityOfColumns = Integer.valueOf(dimensions[NUM_COLS_INDEX]);
        for(int r = 0; r < quantityOfRows; r++) {
            List<Cell> row = getRowInfo(input.next(), r);
            results.add(row);
        }
        manualRowquantity=results.size();
        int randomrow = rand.nextInt(manualRowquantity);
        manualColumnquantity=results.get(0).size();
        return results;

    }

    int getManualquantityOfColumns(){return manualColumnquantity;}

    int getManualquantityOfRows(){return manualRowquantity;}

    int getQuantityOfColumns(){return quantityOfColumns;}

    int getQuantityOfRows(){return quantityOfRows;}

    private List<Cell> getRowInfo(String row, int r) {
        String[] arrayOfInfo = row.split(DATA_REGEX);
        return makeCellObjects(arrayOfInfo, r);
    }

    private List<Cell> makeCellObjects(String[] rowArray, int r) {
        List<Cell> cellRow = new ArrayList<>();
        for(int c = 0; c < rowArray.length; c++) {
            cellRow.add(new Cell(rowArray[c] , r, c));
        }
        return cellRow;
    }

    private void logError(Exception e) {
        try {
            FileWriter fStream = new FileWriter(ERROR_LOG, true);
            BufferedWriter out = new BufferedWriter(fStream);
            PrintWriter pw = new PrintWriter(out, true);
            e.printStackTrace(pw);
            fStream.close();
        }
        catch (Exception ie) {
            throw new RuntimeException("Could not write Exception to file", ie);
        }
    }

}
