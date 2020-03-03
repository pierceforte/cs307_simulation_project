package cellsociety.cell.config;

import cellsociety.cell.Cell;
import cellsociety.simulation.GOLSimModel;
import cellsociety.simulation.SegregationSimModel;
import cellsociety.simulation.SimModel;
import cellsociety.simulation.WaTorSimModel;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class ConfigSaver<T extends Cell> {
    public static final String PATH_TO_CONFIGS = "resources/configs/";
    public static final String CSV_EXTENSION = ".csv";
    public static final Map<Class, String> CLASS_NAME_TO_SAVE_FOLDER = Map.of(
            GOLSimModel.class, GOLSimModel.CONFIG_FILE_PREFIX,
            WaTorSimModel.class, WaTorSimModel.CONFIG_FILE_PREFIX,
            SegregationSimModel.class, SegregationSimModel.CONFIG_FILE_PREFIX);

    public ConfigSaver(List<List<T>> cells, String fileName, String author, String description, Class modelClass) {
        String directory = CLASS_NAME_TO_SAVE_FOLDER.get(modelClass);
        saveCSV(cells, fileName, directory);
        saveProperties(fileName, author, description, directory);
    }
    
    // TODO: the saved config file will only contain the state of the cell. We need to determine if this is okay; for a simulation
    //  like Wa-Tor, cells also have information like energy and a reproduction timer – if we save a file and then restart from that
    //  file, this information will reset and the simulation won't actually "continue"

    // TODO: throw exceptions in method signature

   private void saveCSV(List<List<T>> cells, String fileName, String directory) {
        try {
            PrintWriter pw = new PrintWriter("resources/configs/" + directory + "/" + fileName + CSV_EXTENSION);
            int rows = cells.size();
            int cols = cells.get(0).size();
            pw.println(rows + ConfigReader.SPLIT_REGEX + cols);

            for (int row = 0; row < rows; row++) {
                if (cols == 0) {
                    break;
                }
                String line = cells.get(row).get(0).getState();
                for (int col = 1; col < cols; col++) {
                    line += ConfigReader.SPLIT_REGEX + cells.get(row).get(col).getState();
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

    private void saveProperties(String fileName, String author, String description, String directory) {

    }
}
