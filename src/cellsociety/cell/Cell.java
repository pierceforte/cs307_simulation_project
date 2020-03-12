package cellsociety.cell;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Cell {
    public static final int ROW_INDEX = 0;
    public static final int COL_INDEX = 1;
    public static final int STROKE_WIDTH = 1;

    private String state, nextState;
    private int row, col;
    private Rectangle cellView;

    public Cell(String state, int row, int col){
        this.state = state;
        this.row = row;
        this.col = col;
        cellView = new Rectangle();
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState(){
        return state;
    }

    public String getNextState(){
        return nextState;
    }

    public void setNextState(String nextState) {
        this.nextState = nextState;
    }


    public void updateState(){
        this.state = this.nextState;
    }

    //makes it easier to compare states
    public boolean isState(String state){
        return this.state.equals(state);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    public void setRow(int row) {
        this.row = row;
    }
    public void setCol(int col) {
        this.col = col;
    }

    /*
    public void createView() {
        cellView = new Rectangle();
    }

    public void createView(double size) {
        cellView = new Rectangle(size, size);
        cellView.maxWidth(size);
        cellView.maxHeight(size);
        cellView.setHeight(size);
        cellView.setWidth(size);
    }*/

    public Rectangle getView() {
        return cellView;
    }

    public void setFill(Color color) {
        cellView.setFill(color);
    }

    public void setFill(Image image) {
        if (image == null) {
            cellView.setFill(Color.BLACK);
        }
        else {
            ImagePattern imagePattern = new ImagePattern(image);
            cellView.setFill(imagePattern);
        }
    }

    public void setStroke(Color color) {
        cellView.setStroke(color);
        cellView.setStrokeWidth(STROKE_WIDTH);
    }

}
