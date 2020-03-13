package cellsociety.cell;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;

/**
 * Initial class for cellView
 */
public class CellView <T extends Cell> extends Polygon {
    public static final int STROKE_WIDTH = 1;

    public CellView() {
        setStrokeWidth(STROKE_WIDTH);
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
