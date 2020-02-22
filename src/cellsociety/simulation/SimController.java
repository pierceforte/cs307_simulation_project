package cellsociety.simulation;

public class SimController {

    private boolean isActive;

    public SimController() {
        isActive = true;
    }

    public void play() {
        if (isActive) {

        }
    }

    public void togglePause() {
        isActive = !isActive;
    }
}
