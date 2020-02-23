package cellsociety.simulation;


public class SimController {

    private SimModel simModel;
    private SimView simView;
    private boolean isActive;

    public SimController() {
        /*
           Give user option to choose simulation.
           Here we default to Game of Life.
         */
        simModel = new GameOfLifeSimModel();
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
