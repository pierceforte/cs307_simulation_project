package cellsociety.simulation;

import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import cellsociety.cell.Cell;
import cellsociety.cell.CellView;
import cellsociety.grid.GridModel;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class SimView {

    public static final Color BACKGROUND = Color.WHEAT;
    private SimModel model;
    private SimController controller;
    private Group root;
    private int size; //of entire grid

    public SimView(){
        root = new Group();
    }

    public Scene getSimScene(){
        return new Scene(root, size, size, BACKGROUND);
    }

    public void createSimScene(){
        Button startBttn = new Button("Start");
        Button pauseBttn = new Button("Stop");
        root.getChildren().addAll(startBttn, pauseBttn);
    }

//    private void handleButtonClick(ActionEvent event){
//        if(event.getSource() == startBttn){
//
//        }
//
//    }

    public void updateCellGrid(List<List<Cell>> cells) {
        root.getChildren().removeAll();
        for (List<Cell> row : cells) {
            for (Cell cell : row) {
                CellView cellView = new CellView(size/row.size(), cells.indexOf(row), row.indexOf(cell));
                cellView.updateCellColor(cell.getState());
                root.getChildren().add(cellView);
            }
        }
    }

    public void setGridSize(int size){
        this.size = size;
    }





}
