package cellsociety;

import cellsociety.cell.Cell;
import cellsociety.cell.WaTorCell;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConfigReader {
    public static final int NUM_ROWS_INDEX = 0;
    public static final int NUM_COLS_INDEX = 1;
    public static final String DATA_REGEX = ",";
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
    public List<List<WaTorCell>> getCellList() {
        try {
            File file = new File(this.getClass().getClassLoader().getResource(simulationInitialLayout).getFile());
            return buildListOfCellLists(file);
        } catch (FileNotFoundException e)
        {
//            System.out.println("Could not write Exception to file");
            logError(e);
            System.exit(0);
        }
        return null;
    }

    private List<List<WaTorCell>> buildListOfCellLists(File file) throws FileNotFoundException {
        List<List<WaTorCell>> results = new ArrayList<>();
        Scanner input = new Scanner(file);
        String[] dimensions = input.next().split(DATA_REGEX);
        quantityOfRows = Integer.valueOf(dimensions[NUM_ROWS_INDEX]);
        quantityOfColumns = Integer.valueOf(dimensions[NUM_COLS_INDEX]);
        for(int r = 0; r < quantityOfRows; r++) {
                List<WaTorCell> row = getRowInfo(input.next(), r);
                results.add(row);
        }
        manualQuantityOfRows = results.size();
        manualQuantityOfColumns = results.get(0).size();
        return results;

    }

    int getQuantityOfColumns(){
        return quantityOfColumns;
    }

    int getQuantityOfRows(){
        return quantityOfRows;
    }

    public int getManualQuantityOfRows(){ return manualQuantityOfRows; }

    public int getManualQuantityOfColumns(){ return manualQuantityOfColumns; }

    private List<WaTorCell> getRowInfo(String row, int r) {
        String[] arrayOfInfo = row.split(DATA_REGEX);
        return makeCellObjects(arrayOfInfo, r);
    }

    private List<WaTorCell> makeCellObjects(String[] rowArray, int r) {
        List<WaTorCell> cellRow = new ArrayList<>();
        for(int c = 0; c < rowArray.length; c++) {
            cellRow.add(new WaTorCell(rowArray[c] , r, c));
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
