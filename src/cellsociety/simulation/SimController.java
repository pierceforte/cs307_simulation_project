package cellsociety.simulation;

import cellsociety.ConfigReader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import cellsociety.cell.Cell;
import javafx.scene.layout.Pane;

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

    public <T extends SimModel> SimController(Class<T> simTypeClassName, Group root) {
        ConfigReader data = new ConfigReader(GOL_FILE_IDENTIFIER + CONFIG_FILE_SUFFIX);
        List<List<Cell>> listOfCells = data.getCellList();

        try {
            Constructor<?> constructor = simTypeClassName.getConstructor(List.class);
            this.model = (SimModel) constructor.newInstance(listOfCells);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            //logError(e);
            System.exit(0);
        }
        view = new SimView(this);
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


    //called in step to call update() in SimModel
    public void updateCellStates(){
        if (isActive){
            model.update();
        }
    }

    //called in step after model is updated
    public void updateCellViews(){
        if (isActive){
            view.update(model.getCells());
        }
    }

    public Node getView(){
        return view.getRoot();
    }
}
