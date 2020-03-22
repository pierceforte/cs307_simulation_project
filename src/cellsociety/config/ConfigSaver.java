package cellsociety.config;

import cellsociety.backend.*;
import cellsociety.cell.Cell;
import cellsociety.grid.Grid;

import java.io.*;
import java.util.*;

/**
 * This class is used to save a simulation's configuration to a CSV and properties file.
 *
 * Note that if this project had not been ended early due to COVID-19, a high priority next step would have
 * been to refactor this class.
 *
 * @author Pierce Forte
 */
public class ConfigSaver<T extends Cell> {
    public static final String PATH_TO_CONFIGS = "resources/configs/";
    public static final String CSV_EXTENSION = ".csv";
    public static final String PROPERTIES_EXTENSION = ".properties";
    public static final Map<Class, String> SIM_CLASS_NAME_TO_DIRECTORY = Map.of(
            GOLModel.class, GOLModel.CONFIG_FILE_PREFIX,
            WaTorModel.class, WaTorModel.CONFIG_FILE_PREFIX,
            SegregationModel.class, SegregationModel.CONFIG_FILE_PREFIX,
            FireModel.class, FireModel.CONFIG_FILE_PREFIX,
            PercolationModel.class, PercolationModel.CONFIG_FILE_PREFIX,
            RPSModel.class, RPSModel.CONFIG_FILE_PREFIX
    );
    public static final Map<String, Class> DIRECTORY_TO_SIM_CLASS = Map.of(
            GOLModel.CONFIG_FILE_PREFIX, GOLModel.class,
            WaTorModel.CONFIG_FILE_PREFIX, WaTorModel.class,
            SegregationModel.CONFIG_FILE_PREFIX, SegregationModel.class,
            FireModel.CONFIG_FILE_PREFIX, FireModel.class,
            PercolationModel.CONFIG_FILE_PREFIX, PercolationModel.class,
            RPSModel.CONFIG_FILE_PREFIX, RPSModel.class
    );

    /**
     * The constructor to create a ConfigSaver.
     * @param grid The grid of cells to be saved
     * @param fileName The name of the file to be saved
     * @param author The author of this grid
     * @param description The description for this grid
     * @param modelClass The class for this grid's simulation type
     * @param copyBundle The bundle to be copied into a new properties file
     * @param stateColors The colors of each cell type
     */
    public ConfigSaver(Grid<T> grid, String fileName, String author, String description, Class modelClass,
                       ResourceBundle copyBundle, Map<String, String> stateColors) {
        String simDirectory = SIM_CLASS_NAME_TO_DIRECTORY.get(modelClass);
        new File(PATH_TO_CONFIGS + simDirectory + "/" + fileName).mkdir();
        saveCSV(grid, fileName, simDirectory);
        saveProperties(fileName, author, description, simDirectory, copyBundle, stateColors);
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
            // TODO: handle exception properly
            e.printStackTrace();
            //logError(e);
            e.printStackTrace();
            System.exit(0);
        }
        catch (NullPointerException e) {
            // TODO: handle exception properly
            e.printStackTrace();
            //logError(e);
            // don't save file
        }
    }

    private void saveProperties(String fileName, String author, String description, String simDirectory,
                                ResourceBundle copyBundle, Map<String, String> stateColors) {
        try {
            Properties propertiesToSave = new Properties();

            for (String key : copyBundle.keySet()) {
                propertiesToSave.put(key, copyBundle.getString(key));
            }

            propertiesToSave.put("Title",fileName);
            propertiesToSave.put("Author",author);
            propertiesToSave.put("Description",description);
            propertiesToSave.put("CSVfile",fileName + CSV_EXTENSION);
            for (String key : stateColors.keySet()) {
                propertiesToSave.put("State" + key, stateColors.get(key));
            }

            String propertiesFileName = "resources/configs/" + simDirectory + "/" + fileName + "/" + fileName + PROPERTIES_EXTENSION;
            propertiesToSave.store(new FileOutputStream(propertiesFileName), null);

        } catch (FileNotFoundException e) {
            // TODO: handle exception properly
            e.printStackTrace();
            //logError(e);
            e.printStackTrace();
            System.exit(0);
        }
        catch (NullPointerException e) {
            // TODO: handle exception properly
            e.printStackTrace();
            //logError(e);
            // don't save file
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: handle exception properly
            e.printStackTrace();
            //logError(e)
            e.printStackTrace();
        }
    }
}
