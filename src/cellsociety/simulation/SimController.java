package cellsociety.simulation;


import javafx.scene.Scene;

public class SimController {

    private SimModel model;
    private SimView view;
    private boolean isActive;

    public SimController(SimModel model) {
        this.model = model;
        view = new SimView(this, model);
        view.createControls();
        isActive = true;
    }

    public void play() {
        if (isActive) {
            model.update();
        }
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
