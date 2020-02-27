package cellsociety.cell;

import cellsociety.simulation.WaTorSimModel;

import java.util.Map;

public class WaTorCell extends Cell {
    public static final int CHRONONS_TO_REPRODUCE = 5;
    public static final Map<String, Integer> DEFAULT_ENERGY_MAP = Map.of(
            WaTorSimModel.EMPTY, 0,
            WaTorSimModel.FISH, 0,
            WaTorSimModel.SHARK, 4
    );

    private int energy;
    private int reproductionTimer = 0;

    public WaTorCell(String state, int row, int col) {
        super(state, row, col);
        energy = DEFAULT_ENERGY_MAP.get(state);
    }

    public void incrementEnergy() {
        energy++;
    }

    public void decrementEnergy() {
        energy--;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }

    public void resetReproductionTimer() {
        reproductionTimer = 0;
    }

    public void incrementReproductionTimer() {
        reproductionTimer++;
    }

    public int getReproductionTimer() {
        return reproductionTimer;
    }

}
