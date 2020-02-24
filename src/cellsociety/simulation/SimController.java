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
            System.out.println("\n\n\n\n");
            for (List<Cell> row : cells) {
                for (Cell cell : row) {
                    System.out.print(cell.getState() + ",");
                }
                System.out.println();
            }
            System.out.println("\n\n\n\n");
            model.update();
        }
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
        view.updateCellGrid(model.getCells());
    }
}
