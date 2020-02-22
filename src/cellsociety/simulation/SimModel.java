package cellsociety.simulation;

public abstract class SimModel {

    private SimView simView;
    private SimController simController;

    public SimModel() {
        simView = new SimView();
        simController = new SimController();
    }
}
