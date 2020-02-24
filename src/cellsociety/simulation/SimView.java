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
    private BorderPane bPane;


    Button startBttn;
    Button pauseBttn;

    public SimView(SimController controller){
        this.controller = controller;
        bPane = new BorderPane();
        createControls();
    }

    public Node getSimScene(){
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




}
