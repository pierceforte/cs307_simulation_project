package cellsociety.cell;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Initial class for cellView assuming square cells
 */
public class CellView extends Rectangle {
    private int size;
    private Color color;

    public CellView(int size, Color color, int row, int col) {
        super(row * size, col * size, size, size);
        setFill(color);
    }


}
