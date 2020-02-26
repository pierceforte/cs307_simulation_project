package cellsociety.cell;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Initial class for cellView assuming square cells
 */
public class CellView extends Rectangle {
    private int size;

    public CellView(Cell cell, int size, int idNum) {
        super(cell.getCol() * size, cell.getRow() * size, size, size);
        setId("cellView" + idNum);
        getStyleClass().add("cell");
        if(cell.getState().equals("1")) { //switch this to a constant later
            getStyleClass().add("alive");
        } else {
            getStyleClass().add("dead");
        }
    }


}
