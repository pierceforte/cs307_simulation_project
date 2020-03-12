package cellsociety.cell;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/**
 * Initial class for cellView assuming square cells
 */
public class CellView <T extends Cell> extends Rectangle {
    private int size;
    private T cell;

    public CellView(T cell, double size, double xOffset, double yOffset, int idNum) {
        super(xOffset + cell.getCol() * size, yOffset + cell.getRow() * size, size, size);
        this.cell = cell;
        setId("cellView" + idNum);
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

    public int getRow(){
        return cell.getRow();
    }

    public int getCol(){
        return cell.getCol();
    }

    private Polygon createPolygon() {
        Polygon polygon = new Polygon();

        // comparing to 6 is enough to ensure every angle is used once here
        // since (5/6) * 2 * PI < 6 < 2 * PI
        //for (double rad = 0; rad < 6; rad += HEX_RAD_DELTA) {
            //polygon.getPoints().addAll(Math.cos(rad) * radius + centerX, Math.sin(rad) * radius + centerY);
        //}

        return polygon;

    }







}
