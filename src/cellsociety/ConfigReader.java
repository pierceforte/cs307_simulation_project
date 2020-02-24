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
    public static final int NUM_COLS_INDEX = 1;
    public static final String DATA_REGEX = ",";

    private String simulationInitialLayout;
    private static int quantityOfRows;
    private static int quantityOfColumns;
    private List<List<Cell>> BigList = new ArrayList<>();
    int manualquantityOfRows;
    int manualquantityOfColumns;



    public  ConfigReader(String fileOfCells) {
        simulationInitialLayout = fileOfCells;
    }




    @SuppressWarnings("ThrowablePrintedToSystemOut")
    public List<List<Cell>> getCellList() {
        try {
            File file = new File(Objects.requireNonNull(Main.class.getClassLoader().getResource(simulationInitialLayout)).getFile());
            buildListOfCellLists(file);
        } catch (FileNotFoundException e) {
            System.out.println(e);
            System.exit(0);
        }
        return null;
    }

    void  buildListOfCellLists(File file) throws FileNotFoundException {
        List<List<Cell>> results = new ArrayList<>();
        Scanner input = new Scanner(file);
        String[] dimensions = input.next().split(DATA_REGEX);
        quantityOfRows = Integer.valueOf(dimensions[NUM_ROWS_INDEX]);
        quantityOfColumns = Integer.valueOf(dimensions[NUM_COLS_INDEX]);
        while(input.hasNext()) {
            for (int r = 0; r < quantityOfRows; r++) {
                List<Cell> row = getRowInfo(input.next(), r);
                results.add(row);
            }
        }
         manualquantityOfRows= results.size();
         manualquantityOfColumns= results.get(0).size();
        BigList = results;
       // return results;
    }

    int getQuantityOfColumns(){
        return quantityOfColumns;
    }

    int getQuantityOfRows(){
        return quantityOfRows;
    }

    int getManualquantityOfRows(){ return manualquantityOfRows; }

    int getManualquantityOfColumns(){ return manualquantityOfColumns; }


    private static List<Cell> getRowInfo(String row, int r) {
        String[] arrayOfInfo = row.split(DATA_REGEX);
        return makeCellObjects(arrayOfInfo, r);
    }

    private static List<Cell> makeCellObjects(String[] rowArray, int r) {
        List<Cell> cellRow = new ArrayList<>();
        for (int c = 0; c < rowArray.length;c++ ) {
            cellRow.add(new Cell(rowArray[c],r,c));
        }
        return cellRow;
    }
}

