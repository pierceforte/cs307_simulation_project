package cellsociety.cell.percolation;

import cellsociety.cell.Cell;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class PercolationCell extends Cell{
    public static final String OPEN = "0";
    public static final String CLOSED = "1";
    public static final String FULL = "2";

    private Map<String, Consumer<List<cellsociety.cell.percolation.PercolationCell>>> handleCell = Map.of(
            OPEN, (neighbors) -> handleOPENCell(neighbors),
            CLOSED, (neighbors) -> handleCLOSEDCell(neighbors),
            FULL, (neighbors) -> handleFULLCell(neighbors));

    public PercolationCell(String state, int row, int col) {
        super(state, row, col);
    }

    public void setWhatToDoNext(List<cellsociety.cell.percolation.PercolationCell> neighbors){
        handleCell.get(getState()).accept(neighbors);
    }

    private void handleCLOSEDCell(List<cellsociety.cell.percolation.PercolationCell> neighbors) {
        setNextState(CLOSED);
        return;
    }
    private void handleOPENCell(List<cellsociety.cell.percolation.PercolationCell> neighbors) {
        if(this.getRow() == 0){
            this.setNextState(FULL);
            return;
        }
        for (Cell neighbor: neighbors) {
            if(neighbor.getState().equals(FULL)){
                setNextState(FULL);
                return;
            }
        }
        setNextState(OPEN);
    }

    private void handleFULLCell(List<cellsociety.cell.percolation.PercolationCell> neighbors) {
        setNextState(FULL);
        return;
    }

}
