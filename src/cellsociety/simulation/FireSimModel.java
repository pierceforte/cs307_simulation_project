package cellsociety.simulation;

import cellsociety.cell.Fire.BurningCell;
import cellsociety.cell.Fire.EmptyCell;
import cellsociety.cell.Fire.FireCell;
import cellsociety.cell.Fire.TreeCell;
import java.util.ArrayList;
import java.util.List;

public class FireSimModel extends SimModel<FireCell> {
    // TODO: make probCatch and probTree adjustable/initializable
    public static final String CONFIG_FILE_PREFIX = "Fire";


    public FireSimModel(List<List<String>> cells, SimController simController){
        super(cells, simController);
    }

    @Override
    protected List<List<FireCell>> createGrid(List<List<String>> cellStates) {
        List<List<FireCell>> grid = new ArrayList<>();
        for (int row = 0; row < cellStates.size(); row++) {
            grid.add(new ArrayList<>());
            for (int col = 0; col < cellStates.size(); col++) {
                FireCell cell;
                if (cellStates.get(row).get(col).equals(FireCell.EMPTY)) {
                    cell = new EmptyCell(row, col);
                } else if (cellStates.get(row).get(col).equals(FireCell.TREE)) {
                    cell = new TreeCell(row, col);
                }
                // TODO: throw error if invalid state
                else {
                    cell = new BurningCell(row, col);
                }
                grid.get(row).add(cell);
            }
        }
        return grid;

    }

    @Override
    protected void setNextStates(List<List<FireCell>> cells) {
        for (List<FireCell> row : cells) {
            for (FireCell cell : row) {
                determineAndSetNextState(cell, getNeighbors(cell));
            }
        }

    }

    @Override
    protected void determineAndSetNextState(FireCell cell, List<FireCell> neighbors) {
        cell.setWhatToDoNext(neighbors);
    }

    @Override
    protected void updateStates(List<List<FireCell>> cells) {
        for (List<FireCell> row : cells) {
            for (FireCell cell : row) {
                cell.updateState();
            }
        }
    }

    @Override
    protected String getConfigFileIdentifier() {
        return CONFIG_FILE_PREFIX;
    }


    @Override
    protected List<FireCell> getNeighbors(FireCell cell) {
        return getGridModel().getCardinalNeighbors(cell);
    }
}
