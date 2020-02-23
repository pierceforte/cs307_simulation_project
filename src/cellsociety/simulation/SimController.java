package cellsociety.simulation;

import javafx.scene.Scene;
import cellsociety.cell.Cell;

import java.util.List;

public class SimController {

    private SimModel model;
    private SimView view;
    private boolean isActive;

    public SimController(SimModel model) {
        this.model = model;
        view = new SimView(this);
        view.createControls();
        isActive = true;
    }

    public void play() {
        if (isActive) {
            List<List<Cell>> cells = model.getCells();
            for (List<Cell> row : cells) {
                for (Cell cell : row) {
                    System.out.println(cell.getState());
                }
            }
            model.update();
        }
    }

    public Scene getSimScene(){
        return view.getSimScene();
    }


    public void togglePause() {
        isActive = !isActive;
    }


    //called in step to call update() in SimModel
    public void updateCellStates(){
        model.update();
    }

    //called in step after model is updated
    public void updateCellViews(){
        view.updateCellGrid(model.getCells());
    }
}
