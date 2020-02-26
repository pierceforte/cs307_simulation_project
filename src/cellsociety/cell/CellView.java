package cellsociety.cell;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Initial class for cellView assuming square cells
 */
public class CellView extends Rectangle {
    private int size;

    public CellView(Cell cell, int size) {
        super(cell.getRow() * size, cell.getCol() * size, size, size);
        if(cell.getState().equals("1")) { //switch this to a constant later
            setFill(Color.BLACK);
        } else {
            setFill(Color.WHITE);
        }
        setStroke(Color.STEELBLUE);
    }


}
