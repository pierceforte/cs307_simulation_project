package cellsociety.config;

import cellsociety.cell.Cell;
import cellsociety.grid.Grid;
<<<<<<< HEAD
import cellsociety.simulation.*;
=======
import cellsociety.simulation.FireSimModel;
import cellsociety.simulation.GOLSimModel;
import cellsociety.simulation.SegregationSimModel;
import cellsociety.simulation.WaTorSimModel;
import cellsociety.simulation.PercolationSimModel;
>>>>>>> 7f285aac42557aa664c080f2f5c6853cd59c1c35

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class ConfigSaver<T extends Cell> {
    public static final String PATH_TO_CONFIGS = "resources/configs/";
    public static final String CSV_EXTENSION = ".csv";
    public static final String PROPERTIES_EXTENSION = ".properties";
    public static final Map<Class, String> SIM_CLASS_NAME_TO_DIRECTORY = Map.of(
            GOLSimModel.class, GOLSimModel.CONFIG_FILE_PREFIX,
            WaTorSimModel.class, WaTorSimModel.CONFIG_FILE_PREFIX,
            SegregationSimModel.class, SegregationSimModel.CONFIG_FILE_PREFIX,
<<<<<<< HEAD
            RPSModel.class, RPSModel.CONFIG_FILE_PREFIX,
            FireSimModel.class, FireSimModel.CONFIG_FILE_PREFIX
            );
=======
            FireSimModel.class, FireSimModel.CONFIG_FILE_PREFIX,
            PercolationSimModel.class, PercolationSimModel.CONFIG_FILE_PREFIX
    );
>>>>>>> 7f285aac42557aa664c080f2f5c6853cd59c1c35
    public static final Map<String, Class> DIRECTORY_TO_SIM_CLASS = Map.of(
            GOLSimModel.CONFIG_FILE_PREFIX, GOLSimModel.class,
            WaTorSimModel.CONFIG_FILE_PREFIX, WaTorSimModel.class,
            SegregationSimModel.CONFIG_FILE_PREFIX, SegregationSimModel.class,
<<<<<<< HEAD
            RPSModel.CONFIG_FILE_PREFIX, RPSModel.class,
            FireSimModel.CONFIG_FILE_PREFIX, FireSimModel.class);
=======
            FireSimModel.CONFIG_FILE_PREFIX, FireSimModel.class,
            PercolationSimModel.CONFIG_FILE_PREFIX, PercolationSimModel.class);
>>>>>>> 7f285aac42557aa664c080f2f5c6853cd59c1c35

    public ConfigSaver(Grid<T> grid, String fileName, String author, String description, Class modelClass) {
        String simDirectory = SIM_CLASS_NAME_TO_DIRECTORY.get(modelClass);
        new File(PATH_TO_CONFIGS + simDirectory + "/" + fileName).mkdir();
        saveCSV(grid, fileName, simDirectory);
        saveProperties(fileName, author, description, simDirectory);
    }
    
    // TODO: the saved config file will only contain the state of the cell. We need to determine if this is okay; for a simulation
    //  like Wa-Tor, cells also have information like energy and a reproduction timer – if we save a file and then restart from that
    //  file, this information will reset and the simulation won't actually "continue"

    // TODO: throw exceptions in method signature

   private void saveCSV(Grid<T> grid, String fileName, String simDirectory) {
        try {
            PrintWriter pw = new PrintWriter("resources/configs/" + simDirectory + "/" + fileName + "/" + fileName + CSV_EXTENSION);
            int rows = grid.getNumRows();
            int cols = grid.getNumCols();
            pw.println(rows + ConfigReader.SPLIT_REGEX + cols);

            for (int row = 0; row < rows; row++) {
                if (cols == 0) {
                    break;
                }
                String line = grid.get(row, 0).getState();
                for (int col = 1; col < cols; col++) {
                    line += ConfigReader.SPLIT_REGEX + grid.get(row, col).getState();
                }
                pw.println(line);
            }
            pw.close();
        } catch (FileNotFoundException e) {
            //logError(e);
            e.printStackTrace();
            System.exit(0);
        }
        catch (NullPointerException e) {
            //logError(e);
            // don't save file
            e.printStackTrace();
        }
    }

    private void saveProperties(String fileName, String author, String description, String simDirectory) {

    }
}
