package cellsociety.simulation;


public class SimController {

    private SimModel simModel;
    private boolean isActive;

    public SimController(SimModel simModel) {
        this.simModel = simModel;
        isActive = true;
    }

    public void play() {
        if (isActive) {
            simModel.update();
        }
    }

    public void togglePause() {
        isActive = !isActive;
    }
}
