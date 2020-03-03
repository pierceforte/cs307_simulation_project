package cellsociety.cell;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Initial class for cellView assuming square cells
 */
public class CellView <T extends Cell> extends Rectangle {
    private int size;


    public CellView(T cell, double size, double xOffset, double yOffset, int idNum) {
        super(xOffset + cell.getCol() * size, yOffset + cell.getRow() * size, size, size);
        setId("cellView" + idNum);
        getStyleClass().add("cell");
        getStyleClass().add("state" + cell.getState());
    }

}
