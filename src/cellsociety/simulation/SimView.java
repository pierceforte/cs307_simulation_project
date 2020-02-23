package cellsociety.simulation;

import javafx.scene.Group;
import javafx.scene.Scene;
import cellsociety.cell.Cell;
import cellsociety.cell.CellView;
import cellsociety.grid.GridModel;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class SimView {

    public static final Color BACKGROUND = Color.WHEAT;
    private SimModel sim;
    private GridModel grid;
    private Scene myScene;
    private Group root;
    private List<CellView> cellViews;

    private int size; //of grid

    public SimView(){
        root = new Group();
    }

    public Scene getSimScene(){
        return new Scene(root, size, size, BACKGROUND);
    }

    public void updateCellGrid(List<List<Cell>> cells) {
        cellViews = new ArrayList<>();
        for (List<Cell> row : cells) {
            for (Cell cell : row) {
                CellView cellView = new CellView(size/row.size(), cells.indexOf(row), row.indexOf(cell));
                cellViews.add(cellView);
                if(cell.getState().equals("ALIVE")){ //change this later
                    cellView.setFill(Color.BLACK);
                } else {
                    cellView.setFill(Color.WHITE);
                }
                root.getChildren().add(cellView);
            }
        }
    }

    public void setGridSize(int size){
        this.size = size;
    }





}
