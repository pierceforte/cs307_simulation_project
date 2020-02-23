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
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class SimView {

    public static final int GRID_SIZE = 400;
    public static final Color BACKGROUND = Color.WHEAT;
    private SimController controller;
    private SimModel model;
    private BorderPane bPane;


    Button startBttn;
    Button pauseBttn;

    public SimView(SimController controller, SimModel model){
        this.controller = controller;
        this.model = model;
        bPane = new BorderPane();
    }

    public Node getView(){
        return bPane;
    }

    public void createControls(){
        Group root = new Group();
        startBttn = new Button("Start");
        pauseBttn = new Button("Stop");
        root.getChildren().addAll(startBttn, pauseBttn);
        bPane.setBottom(root);
    }

    private void handleButtonClick(ActionEvent event){
        if(event.getSource() == startBttn){
            controller.play();
        } else if (event.getSource() == pauseBttn){
            controller.togglePause();
        }
    }

    public void updateCellGrid(List<List<Cell>> cells) {
        Group root = new Group();
        for (List<Cell> row : cells) {
            for (Cell cell : row) {
                CellView cellView = new CellView(GRID_SIZE/row.size(), cells.indexOf(row), row.indexOf(cell));
                cellView.updateCellColor(cell.getState());
                root.getChildren().add(cellView);
            }
        }
        bPane.setCenter(root);
    }


}
