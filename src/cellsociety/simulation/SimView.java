package cellsociety.simulation;

import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import cellsociety.cell.Cell;
import cellsociety.cell.CellView;
import cellsociety.grid.GridModel;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class SimView {

    public static final int GRID_SIZE = 400;
    public static final Color BACKGROUND = Color.WHEAT;
    private SimController controller;
    private Group root;
//    Button startBttn;
//    Button pauseBttn;

    public SimView(SimController controller){
        this.controller = controller;
        root = new Group();
        //createControls();
    }

    public Group getRoot(){
        return root;
    }

//    private void createControls(){
//        startBttn = new Button("Start");
//        pauseBttn = new Button("Stop");
//        GridPane grid = new GridPane();
//        grid.add(startBttn, 1, 0);
//        grid.add(pauseBttn, 2, 0);
//        bPane.setBottom(grid);
//    }
//
//
//    private void handleButtonClick(ActionEvent event){
//        if(event.getSource() == startBttn){
//            controller.play();
//        } else if (event.getSource() == pauseBttn){
//            controller.togglePause();
//        }
//    }

    public void update(List<List<Cell>> cells) {
        root.getChildren().clear();
        for (List<Cell> row : cells) {
            for (Cell cell : row) {
                CellView cellView = new CellView(cell, GRID_SIZE/row.size());
                root.getChildren().add(cellView);
            }
        }
    }



}
