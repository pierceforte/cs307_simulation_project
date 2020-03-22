package cellsociety.cell;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;

/**
 * This class is used to create each cell's frontend, and it extends the class Polygon.
 *
 * It provides the simple functionality of allowing a Cell to have an image as its fill pattern.
 * Beyond this simple functionality, a CellView is merely a polygon.
 *
 * @author Pierce Forte
 */
public class CellView extends Polygon {
    public static final int DEFAULT_STROKE_WIDTH = 1;

    /**
     * The constructor to create a CellView.
     */
    public CellView() {

    }

    /**
     * Overload the setFill method of the Polygon class to allow for setting
     * the fill to an image.
     * @param image The image to be used for the fill pattern
     */
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
