package cellsociety.cell;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Initial class for cellView assuming square cells
 */
public class CellView extends Rectangle {
    private int size;

    public CellView(Cell cell, double size, double xOffset, double yOffset, int idNum) {
        super(xOffset + cell.getCol() * size, yOffset + cell.getRow() * size, size, size);
        setId("cellView" + idNum);
        getStyleClass().add("cell");
        if(cell.getState().equals("1")) { //switch this to a constant later
            getStyleClass().add("alive");
        } else {
            getStyleClass().add("dead");
        }
    }


}
