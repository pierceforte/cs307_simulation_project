package cellsociety.simulation;

import cellsociety.MainController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import cellsociety.cell.Cell;
import cellsociety.cell.CellView;
import cellsociety.grid.GridModel;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SimView {

    public static final int GRID_SIZE = 400;
    public static final Color BACKGROUND = Color.WHEAT;
    private SimController controller;
    private BorderPane bPane;
    private Button startBttn;
    private Button pauseBttn;
    private Button backBttn;

    public SimView(SimController controller){
        this.controller = controller;
        bPane = new BorderPane();
        createControls();
    }

    public Node getRoot(){
        return bPane;
    }

    private void createControls(){
        startBttn = new Button("Start");
        pauseBttn = new Button("Stop");
        backBttn = new Button("Back");
        GridPane grid = new GridPane();
        grid.add(startBttn, 1, 0);
        grid.add(pauseBttn, 2, 0);
        grid.add(backBttn, 3, 0);
        bPane.setBottom(grid);


        startBttn.setOnAction(event -> handleButtonClick(event));
        pauseBttn.setOnAction(event -> handleButtonClick(event));
        backBttn.setOnAction(event -> handleButtonClick(event));

    }

    //TODO: cleanup this code
    public boolean userRestartedSimulation() {
        Stage input = new Stage();
        input.setTitle("Start Simulation");

        final boolean[] ret = {false};
        Button restartBttn = createButton("Restart", 0, 0, 100, 30);
        Button continueBttn = createButton("Continue", 100, 0, 100, 30);

        restartBttn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent t) {
                input.close();
                ret[0] = true;
            }
        });
        continueBttn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent t) {
                input.close();
                ret[0] = false;
            }
        });

        Pane pane = new Pane();
        pane.getChildren().addAll(restartBttn, continueBttn);

        input.setScene(new Scene(pane, 200, 30));
        input.showAndWait();

        return ret[0];
    }


    private void handleButtonClick(ActionEvent event){
        if(event.getSource() == startBttn){
            System.out.println("poop");
            controller.start();
        } else if (event.getSource() == pauseBttn){
            controller.pause();
        }
        else if (event.getSource() == backBttn) {
            controller.setIsEnded(true);
        }
    }

    public void update(List<List<Cell>> cells) {
        Group root = new Group();
        for (List<Cell> row : cells) {
            for (Cell cell : row) {
                CellView cellView = new CellView(cell, MainController.WIDTH /row.size());
                root.getChildren().add(cellView);
            }
        }
        bPane.setCenter(root);
    }

    private Button createButton(String text, double xPos, double yPos, double width, double height) {
        Button button = new Button(text);
        button.setTranslateX(xPos);
        button.setTranslateY(yPos);
        button.setPrefWidth(width);
        button.setPrefHeight(height);
        return button;
    }



}
