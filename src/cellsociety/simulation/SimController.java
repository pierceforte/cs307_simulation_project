package cellsociety.simulation;

import cellsociety.MainController;
import cellsociety.config.ConfigReader;
import cellsociety.config.ConfigSaver;
import javafx.scene.Node;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public class SimController {
    public static final String CONFIG_FILE_SUFFIX = "Config.csv";

    //for modifying animation timeline
    public static final int FRAMES_PER_SECOND = 5;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

    //TODO: find a way to include this info in the specific SimModel
    public static final Map<Class, String> CLASS_TO_FILE_ID = Map.of(
            GOLSimModel.class, GOLSimModel.CONFIG_FILE_PREFIX,
            WaTorSimModel.class, WaTorSimModel.CONFIG_FILE_PREFIX,
            RPSModel.class, RPSModel.CONFIG_FILE_PREFIX,
            SegregationSimModel.class, SegregationSimModel.CONFIG_FILE_PREFIX);

    //TODO: not sure if we want this dependency
    private MainController mainController;
    private SimModel model;
    private SimView view;
    private boolean isActive;
    private boolean isEnded;

    //TODO: cleanup constructor
    public <T extends SimModel> SimController(Class<T> simTypeClassName, MainController mainController, String csvFileName) {
        view = new SimView(this);
        this.mainController = mainController;
        System.out.println(csvFileName);
        ConfigReader data = new ConfigReader(csvFileName);
        List<List<String>> listOfCells = data.getCellList();

        try {
            Constructor<?> constructor = simTypeClassName.getConstructor(List.class, SimController.class);
            this.model = (SimModel) constructor.newInstance(listOfCells, this);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            //logError(e);
            System.exit(0);
        }
        isActive = true;
    }


    public void start() {
        isActive = true;
    }

    public void pause(){
        isActive = false;
    }

    public void stepFrame(){
        update(true);
        pause();
    }

    public void changeRate(double change){
        mainController.changeAnimationSpeed(change);
    }

    public void update(boolean overrideActiveStatus) {
        if (isActive || overrideActiveStatus){
            model.update();
            view.update(model.getGrid());
        }
    }

    public void handleClick(int row, int col){
        model.clickResponse(row, col);
        view.update(model.getGrid());
    }

    public Node getViewRoot(){
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

    public void saveConfig(String fileName, String author, String description) {
        new ConfigSaver<>(model.getGrid(), fileName, author, description, model.getClass());
    }
}
