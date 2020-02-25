package cellsociety.simulation;

import cellsociety.MainController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import cellsociety.cell.Cell;
import cellsociety.cell.CellView;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class SimView {

    public static final int GRID_SIZE = 400;
    public static final Color BACKGROUND = Color.WHEAT;
    private SimController controller;
    private BorderPane bPane;
    private Button playBttn;
    private Button pauseBttn;
    private Button stepBttn;
    private Button exitBttn;

    public SimView(SimController controller){
        this.controller = controller;
        bPane = new BorderPane();
        createControls();
    }

    public Node getRoot(){
        return bPane;
    }

    private void createControls(){
        playBttn = new Button("Play");
        playBttn.setId("playBttn");
        pauseBttn = new Button("Pause");
        pauseBttn.setId("pauseBttn");
        stepBttn = new Button("Step");
        stepBttn.setId("stepBttn");
        exitBttn = new Button("Exit");
        stepBttn.setId("exitBttn");
        GridPane grid = new GridPane();
        grid.add(playBttn, 1, 0);
        grid.add(pauseBttn, 2, 0);
        grid.add(stepBttn, 3, 0);
        grid.add(exitBttn, 4, 0);
        bPane.setBottom(grid);

        playBttn.setOnAction(event -> handleButtonClick(event));
        pauseBttn.setOnAction(event -> handleButtonClick(event));
        stepBttn.setOnAction(event -> handleButtonClick(event));
        exitBttn.setOnAction(event -> handleButtonClick(event));

    }

    //TODO: cleanup this code
    public boolean userRestartedSimulation() {
        Stage input = new Stage();
        input.setTitle("Start Simulation");

        final boolean[] ret = {false};
        Button restartBttn = createButton("Restart", 0, 0, 100, 30);
        restartBttn.setId("restartBttn");
        Button continueBttn = createButton("Continue", 100, 0, 100, 30);
        continueBttn.setId("continueBttn");

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
        if(event.getSource() == playBttn){
            controller.start();
        } else if (event.getSource() == pauseBttn){
            controller.pause();
        }
        else if (event.getSource() == stepBttn) {
            controller.update(true);
            controller.pause();
        }
        else if (event.getSource() == exitBttn) {
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
