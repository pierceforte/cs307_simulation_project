package cellsociety.simulation;

import cellsociety.ConfigReader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import cellsociety.cell.Cell;
import javafx.scene.layout.Pane;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class SimController {
    public static final String CONFIG_FILE_SUFFIX = "Config.csv";
    public static final String CURRENT_CONFIG_FILE_PREFIX = "current";

    //TODO: find a way to include this info in the specific SimModel
    public static final String GOL_FILE_IDENTIFIER = "GOL";

    private SimModel model;
    private SimView view;
    private boolean isActive;
    private boolean isEnded;

    //TODO: cleanup constructor
    public <T extends SimModel> SimController(Class<T> simTypeClassName, Group root) {
        view = new SimView(this);
        String configurationFile = chooseConfigurationFile();
        ConfigReader data = new ConfigReader(configurationFile);
        List<List<Cell>> listOfCells = data.getCellList();

        try {
            Constructor<?> constructor = simTypeClassName.getConstructor(List.class);
            this.model = (SimModel) constructor.newInstance(listOfCells);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            //logError(e);
            System.exit(0);
        }
        isActive = true;
        root.getChildren().add(view.getRoot());
    }


    public void start() {
        isActive = true;
    }

    public void pause(){
        isActive = false;
    }

    public void togglePause() {
        isActive = !isActive;
    }

    public void update() {
        if (isActive){
            model.update();
            view.update(model.getCells());
        }
    }

    public Node getView(){
        return view.getRoot();
    }

    public SimModel getModel() {
        return model;
    }

    public void setIsEnded(boolean isEnded) {
        this.isEnded = isEnded;
    }

    public boolean isEnded() {
        return isEnded;
    }

    private String chooseConfigurationFile() {
        String fileToRead = GOL_FILE_IDENTIFIER + CONFIG_FILE_SUFFIX;
        String currentConfigFile = CURRENT_CONFIG_FILE_PREFIX + GOL_FILE_IDENTIFIER + CONFIG_FILE_SUFFIX;

        if (this.getClass().getClassLoader().getResource(currentConfigFile) != null) {
            if (!view.userRestartedSimulation()) {
                fileToRead = currentConfigFile;
            }
        }
        return fileToRead;
    }
}
