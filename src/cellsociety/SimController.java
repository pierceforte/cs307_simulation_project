package cellsociety;

import cellsociety.config.ConfigReader;
import cellsociety.config.ConfigSaver;
import cellsociety.frontend.SimView;
import cellsociety.backend.*;
import javafx.scene.Node;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * This class provides the interface between the simulation backend and frontend, controlling how
 * the simulation updates on both ends and interacts with the main controller, which has the step
 * method.
 *
 * @author Pierce Forte
 * @author Mary Jiang
 */
public class SimController {
    public static final String CONFIG_FILE_SUFFIX = "Config.csv";

    //for modifying animation timeline
    public static final int FRAMES_PER_SECOND = 5;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

    //TODO: find a way to include this info in the specific SimModel
    public static final Map<Class, String> CLASS_TO_FILE_ID = Map.of(
            GOLModel.class, GOLModel.CONFIG_FILE_PREFIX,
            WaTorModel.class, WaTorModel.CONFIG_FILE_PREFIX,
            SegregationModel.class, SegregationModel.CONFIG_FILE_PREFIX,
            PercolationModel.class, PercolationModel.CONFIG_FILE_PREFIX);

    //TODO: not sure if we want this dependency
    private MainController mainController;
    // TODO: not sure if we want bundles here (only here to reduce duplication)
    private ResourceBundle myDefaultResources;
    private ResourceBundle mySimResources;
    private SimModel model;
    private SimView view;
    private boolean isActive;
    private boolean isEnded;
    private String filePath;

    /**
     * The constructor to create a SimController.
     * @param simTypeClassName The class of the SimModel implementation
     * @param mainController The MainController for the application
     * @param csvFilePath The path to the initial CSV config
     * @param <T> The type of simulation's class
     */
    public <T extends SimModel> SimController(Class<T> simTypeClassName, MainController mainController, String csvFilePath) {
        this.mainController = mainController;
        ConfigReader data = new ConfigReader(csvFilePath);
        List<List<String>> listOfCells = data.getCellList();
        try {
            Constructor<?> constructor = simTypeClassName.getConstructor(List.class, SimController.class);
            this.model = (SimModel) constructor.newInstance(listOfCells, this);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.exit(0);
        }
        initializeResources(csvFilePath);
        view = new SimView(this, myDefaultResources, mySimResources);
        isActive = true;
    }

    /**
     * Begin the simulation.
     */
    public void start() {
        isActive = true;
    }

    /**
     * Pause the simulation.
     */
    public void pause(){
        isActive = false;
    }

    /**
     * Initiate a single step.
     */
    public void stepFrame(){ update(true);pause(); }

    /**
     * Change the animation speed.
     * @param change The factor to change the speed by
     */
    public void changeRate(double change){
        mainController.changeAnimationSpeed(change);
    }

    /**
     * Update the simulation.
     * @param overrideActiveStatus Whether the simulation should update even if it is not active
     */
    public void update(boolean overrideActiveStatus) {
        if (isActive || overrideActiveStatus){
            model.update();
            view.update(model.getGrid());
        }
    }

    /**
     * Handle a cell click on the screen.
     * @param row The row of the clicked cell
     * @param col The column of the clicked cell
     */
    public void handleClick(int row, int col){
        model.clickResponse(row, col);
        view.update(model.getGrid());
    }

    /**
     * Get the Node that is the frontend's root.
     * @return The Node that is the frontend's root
     */
    public Node getViewRoot(){
        return view.getRoot();
    }

    /**
     * Get the backend for the simulation.
     * @return The backend for the simulation
     */
    public SimModel getModel() {
        return model;
    }

    /**
     * Determine whether the simulation is active.
     * @return Whether the simulation is active
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Set whether the simulation has been ended.
     * @param isEnded Whether the simulation has been ended
     */
    public void setIsEnded(boolean isEnded) {
        this.isEnded = isEnded;
    }

    /**
     * Determine whether the simulation has been ended.
     * @return Whether the simulation has been ended
     */
    public boolean isEnded() {
        return isEnded;
    }

    /**
     * The constructor to create a ConfigSaver.
     * @param fileName The name of the file to be saved
     * @param author The author of this grid
     * @param description The description for this grid
     * @param stateColors The colors of each cell type
     */
    public void saveConfig(String fileName, String author, String description, Map<String, String> stateColors) {
        new ConfigSaver<>(model.getGrid(), fileName, author, description, model.getClass(), mySimResources, stateColors);
    }

    /**
     * Get the resource bundle that is specific to the current simulation.
     * @return The resource bundle that is specific to the current simulation
     */
    public ResourceBundle getSimResources() {
        return mySimResources;
    }

    private void initializeResources(String csvFilePath) {
        filePath = csvFilePath.substring(0, csvFilePath.length()-4);
        Locale locale = new Locale("en", "US");
        myDefaultResources = ResourceBundle.getBundle("default", locale);
        mySimResources = ResourceBundle.getBundle(filePath, locale);
    }
}
