package cellsociety.simulation;

import cellsociety.ConfigReader;
import cellsociety.MainController;
import cellsociety.cell.WaTor.WaTorCell;
import javafx.scene.Node;
import javafx.scene.Node;
import cellsociety.cell.Cell;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SimController {
    public static final String CONFIG_FILE_SUFFIX = "Config.csv";
    public static final String CURRENT_CONFIG_FILE_PREFIX = "current";

    //TODO: find a way to include this info in the specific SimModel
    public static final Map<Class, String> CLASS_TO_FILE_ID = Map.of(
            GOLSimModel.class, GOLSimModel.CONFIG_FILE_PREFIX,
            WaTorSimModel.class, WaTorSimModel.CONFIG_FILE_PREFIX,
            SegregationSimModel.class, SegregationSimModel.CONFIG_FILE_PREFIX);

    //TODO: not sure if we want this dependency
    private MainController mainController;
    private SimModel model;
    private SimView view;
    private boolean isActive;
    private boolean isEnded;

    //TODO: cleanup constructor and add code to choose which simulation identifier
    public <S extends SimModel> SimController(Class<S> simTypeClassName, MainController mainController) {
        this(simTypeClassName, mainController,
                CLASS_TO_FILE_ID.get(simTypeClassName) + CONFIG_FILE_SUFFIX, true);
    }

    public <T extends SimModel> SimController(Class<T> simTypeClassName, MainController mainController, String defaultConfigFileName,
                                              boolean askToRestartOrContinue) {
        view = new SimView(this);
        this.mainController = mainController;
        ConfigReader data;
        // for some reason we need to check if true like this
        if (askToRestartOrContinue == true) {
            String configurationFile = chooseConfigurationFile(CLASS_TO_FILE_ID.get(simTypeClassName));
            data = new ConfigReader(configurationFile);
        }
        else {
            data = new ConfigReader(defaultConfigFileName);
        }
        List<List<String>> listOfCells = data.getCellList();

        try {
            Constructor<?> constructor = simTypeClassName.getConstructor(List.class, SimController.class);
            this.model = (SimModel) constructor.newInstance(listOfCells, this);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            //logError(e);
            System.exit(0);
        }
        isActive = true;
        mainController.addToRoot(view.getRoot());
        //root.getChildren().add(view.getRoot());
        view.update(model.getCells());
        mainController.clearRoot();
        mainController.addToRoot(getView());
    }


    public void start() {
        isActive = true;
    }

    public void pause(){
        isActive = false;
    }

    public void update(boolean overrideActiveStatus) {
        if (isActive || overrideActiveStatus){
            model.update();
            view.update(model.getCells());
            mainController.clearRoot();
            mainController.addToRoot(getView());
        }
    }

    public Node getView(){
        return view.getRoot();
    }

    public SimModel getModel() {
        return model;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setIsEnded(boolean isEnded) {
        this.isEnded = isEnded;
    }

    public boolean isEnded() {
        return isEnded;
    }

    private String chooseConfigurationFile(String fileId) {
        String fileToRead = fileId + CONFIG_FILE_SUFFIX;
        String currentConfigFile = CURRENT_CONFIG_FILE_PREFIX + fileId + CONFIG_FILE_SUFFIX;

        if (this.getClass().getClassLoader().getResource(currentConfigFile) != null) {
            if (doesCurrentConfigHaveCorrectDimensions(fileToRead, currentConfigFile) && !view.userRestartedSimulation()) {
                fileToRead = currentConfigFile;
            }
        }
        return fileToRead;
    }

    private boolean doesCurrentConfigHaveCorrectDimensions(String startFile, String currentFile) {
        String[] startDimensions;
        String[] currentDimensions;

        try {
            Scanner startInput = new Scanner(new File(this.getClass().getClassLoader().getResource(startFile).getPath()));
            startDimensions = startInput.next().split(ConfigReader.DATA_REGEX);

            Scanner currentInput = new Scanner(new File(this.getClass().getClassLoader().getResource(currentFile).getPath()));
            if (currentInput.hasNextLine()) {
                currentDimensions = currentInput.next().split(ConfigReader.DATA_REGEX);
            }
            else return false;
        }
        catch (FileNotFoundException | NullPointerException e) {
            //logError(e)
            return false;
        }

        for (int dimension = 0; dimension < startDimensions.length; dimension++) {
            if (startDimensions.length != currentDimensions.length ||
                    !startDimensions[dimension].equals(currentDimensions[dimension])) {
                return false;
            }
        }
        return true;
    }

}
