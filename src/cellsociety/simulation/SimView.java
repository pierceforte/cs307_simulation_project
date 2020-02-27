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
import java.util.Locale;
import java.util.ResourceBundle;

public class SimView {
    public ResourceBundle myResources;
    public static final int GRID_SIZE = 400;
    public static final Color BACKGROUND = Color.WHEAT;
    private SimController controller;
    private BorderPane bPane;
    private GridPane gridPane;
    private Button playBttn;
    private Button pauseBttn;
    private Button stepBttn;
    private Button exitBttn;

    public SimView(SimController controller){
        Locale locale = new Locale("en", "US");
        myResources = ResourceBundle.getBundle("default", locale);
        this.controller = controller;
        bPane = new BorderPane();
        createControls();
    }

    public Node getRoot(){
        return bPane;
    }

    private void createControls(){
        playBttn = new Button(myResources.getString("PlayBttn"));
        playBttn.setId("playBttn");
        pauseBttn = new Button(myResources.getString("PauseBttn"));
        pauseBttn.setId("pauseBttn");
        stepBttn = new Button(myResources.getString("StepBttn"));
        stepBttn.setId("stepBttn");
        exitBttn = new Button(myResources.getString("ExitBttn"));
        exitBttn.setId("exitBttn");
        gridPane = new GridPane();
        gridPane.add(playBttn, 1, 0);
        gridPane.add(pauseBttn, 2, 0);
        gridPane.add(stepBttn, 3, 0);
        gridPane.add(exitBttn, 4, 0);
        bPane.setBottom(gridPane);

        playBttn.setOnAction(event -> handleButtonClick(event));
        pauseBttn.setOnAction(event -> handleButtonClick(event));
        stepBttn.setOnAction(event -> handleButtonClick(event));
        exitBttn.setOnAction(event -> handleButtonClick(event));
    }
    //TODO: cleanup this code
    public boolean userRestartedSimulation() {
        Stage input = new Stage();
        input.setTitle(myResources.getString("StartSim"));
        final boolean[] ret = {false};
        Button restartBttn = createButton(myResources.getString("RestartBttn"), 0, 0, 100, 30);
        restartBttn.setId("restartBttn");
        Button continueBttn = createButton(myResources.getString("ContinueBttn"), 100, 0, 100, 30);
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

        // divide by the large dimension so everything fits on screen
        double size = ((double) MainController.WIDTH) / Math.max(cells.get(0).size(), cells.size());
        // TODO: get these to work (the calculations are correct, but changing xPos and yPos in cellView
        //  doesn't do work
        double xOffset = size * Math.max(0, cells.size() - cells.get(0).size())/2;
        double yOffset = size * Math.max(0, cells.get(0).size() - cells.size())/2;

        int cellViewIdNum = 0;
        for (List<Cell> row : cells) {
            for (Cell cell : row) {
                CellView cellView = new CellView(cell,size, xOffset, yOffset, cellViewIdNum);
                cellViewIdNum++;
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
