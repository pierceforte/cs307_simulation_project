package cellsociety.config;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConfigReader {
    public static final int NUM_ROWS_INDEX = 0;
    public static final int NUM_COLS_INDEX = 1;
    public static final String SPLIT_REGEX = ",";
    public static final String ERROR_LOG = "error_log.txt";

    private final String simulationInitialLayout;
    private int quantityOfRows;
    private int quantityOfColumns;
    private int manualQuantityOfRows;
    private int manualQuantityOfColumns;

    public ConfigReader(String fileOfCells) {
        simulationInitialLayout = fileOfCells;
    }

    @SuppressWarnings("ThrowablePrintedToSystemOut")
    public List<List<String>> getCellList() {
        try {
            File file = new File(this.getClass().getClassLoader().getResource(simulationInitialLayout).getFile());
            return buildListOfCellLists(file);
        } catch (FileNotFoundException e)
        {
            //System.out.println("Could not write Exception to file");
            logError(e);
            System.exit(0);
        }
        return null;
    }

    private List<List<String>> buildListOfCellLists(File file) throws FileNotFoundException {
        List<List<String>> results = new ArrayList<>();
        Scanner input = new Scanner(file);
        String[] dimensions = input.next().split(SPLIT_REGEX);
        quantityOfRows = Integer.valueOf(dimensions[NUM_ROWS_INDEX]);
        quantityOfColumns = Integer.valueOf(dimensions[NUM_COLS_INDEX]);
        for(int r = 0; r < quantityOfRows; r++) {
            List<String> row = getRowInfo(input.next(), r);
            results.add(row);
        }
        manualQuantityOfRows = results.size();
        manualQuantityOfColumns = results.get(0).size();
        return results;

    }

    public int getQuantityOfColumns(){
        return quantityOfColumns;
    }

    public int getQuantityOfRows(){
        return quantityOfRows;
    }

    public int getManualQuantityOfRows(){ return manualQuantityOfRows; }

    public int getManualQuantityOfColumns(){ return manualQuantityOfColumns; }

    private List<String> getRowInfo(String row, int r) {
        String[] arrayOfInfo = row.split(SPLIT_REGEX);
        return makeCellObjects(arrayOfInfo, r);
    }

    private List<String> makeCellObjects(String[] rowArray, int r) {
        List<String> cellRow = new ArrayList<>();
        for(int c = 0; c < rowArray.length; c++) {
            cellRow.add(rowArray[c]);
        }
        return cellRow;
    }

    public void logError(Exception e) {
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
