package cellsociety.cell;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Initial class for cellView assuming square cells
 */
public class CellView extends Rectangle {
    private int size;

    public CellView(int size, int row, int col) {
        super(row * size, col * size, size, size);
    }

    public void updateCellColor(String state){
        if(state.equals("ALIVE")) { //switch this to a constant later
            setFill(Color.BLACK);
        } else {
            setFill(Color.WHITE);
        }
    }

}
