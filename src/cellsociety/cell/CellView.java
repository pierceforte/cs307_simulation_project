package cellsociety.cell;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;

/**
 * Initial class for cellView
 */
public class CellView extends Polygon {
    public static final int DEFAULT_STROKE_WIDTH = 1;

    public CellView() {

    }

    public void setFill(Image image) {
        if (image == null) {
            setFill(Color.BLACK);
        }
        else {
            ImagePattern imagePattern = new ImagePattern(image);
            setFill(imagePattern);
        }
    }

}
