package cellsociety.simulation;

import cellsociety.InputStage;
import cellsociety.MainController;
import cellsociety.cell.FileNameVerifier;
import cellsociety.grid.Grid;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import cellsociety.cell.Cell;
import cellsociety.cell.CellView;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class SimView {
    public ResourceBundle myResources;
    public static final int GRID_SIZE = 400;
    public static final Color BACKGROUND = Color.WHEAT;
    private SimController controller;
    private UserGUI gui;
    private BorderPane bPane;
    private GridPane gridPane;
    private Button playBttn;
    private Button pauseBttn;
    private Button stepBttn;
    private Button exitBttn;
    private Slider speedSlider;
    private HashMap<String, Color> cellColors;
    private Group cellViewsRoot;

    public SimView(SimController controller){
        Locale locale = new Locale("en", "US");
        myResources = ResourceBundle.getBundle("default", locale);
        this.controller = controller;
        gui = new UserGUI(controller);
        bPane = new BorderPane();
        createControls();
    }

    public BorderPane getRoot(){
        return bPane;
    }


    private void createControls(){
       VBox vbox = new VBox(5);
       vbox.getChildren().addAll(createButtonControls(), createColorControls());
       bPane.setBottom(vbox);
    }

    private HBox createButtonControls(){
        playBttn = new Button(myResources.getString("PlayBttn"));
        playBttn.setId("playBttn");
        pauseBttn = new Button(myResources.getString("PauseBttn"));
        pauseBttn.setId("pauseBttn");
        stepBttn = new Button(myResources.getString("StepBttn"));
        stepBttn.setId("stepBttn");
        exitBttn = new Button(myResources.getString("ExitBttn"));
        exitBttn.setId("exitBttn");
        Label speedLabel = new Label(myResources.getString("ChangeSpeed"));
        Slider speedSlider = new Slider(0, 2, 1);
        speedSlider.setBlockIncrement(0.2);

        HBox hbox = new HBox(5);
        hbox.getChildren().addAll(playBttn, pauseBttn, stepBttn, exitBttn, speedLabel, speedSlider);

        playBttn.setOnAction(event -> controller.start());
        pauseBttn.setOnAction(event -> controller.pause());
        stepBttn.setOnAction(event -> controller.stepFrame());
        exitBttn.setOnAction(event -> gui.handleExitRequest());
        speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, //
                                Number oldValue, Number newValue) {
                controller.changeRate((Double) newValue);
            }
        });

        return hbox;
    }

    //TODO: refactor this method and implement a loop which adds color pickers based on the
    //  properties file for the current simulation
    private HBox createColorControls(){
        cellColors = new HashMap<>();
        HBox hbox = new HBox(5);

        ColorPicker picker0 = new ColorPicker();
        ColorPicker picker1 = new ColorPicker();
        ColorPicker picker2 = new ColorPicker();

        hbox.getChildren().addAll(picker0, picker1, picker2);
        picker0.setOnAction(event -> cellColors.put("0", picker0.getValue()));
        picker1.setOnAction(event -> cellColors.put("1", picker1.getValue()));
        picker2.setOnAction(event -> cellColors.put("2", picker1.getValue()));

        return hbox;
    }




    public <T extends Cell> Group update(Grid<T> grid) {
        bPane.getChildren().remove(cellViewsRoot);

        Group root = new Group();
        int rows = grid.getNumRows();
        int cols = grid.getNumCols();

        // divide by the large dimension so everything fits on screen
        double size = ((double) MainController.WIDTH) / Math.max(cols, rows);
        // TODO: get these to work (the calculations are correct, but changing xPos and yPos in cellView
        //  doesn't do work
        double xOffset = size * Math.max(0, rows - cols)/2;
        double yOffset = size * Math.max(0, cols - rows)/2;

        int cellViewIdNum = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                T cell = grid.get(row, col);
                CellView cellView = new CellView(cell, size, xOffset, yOffset, cellViewIdNum);
                if (cellColors.get(cell.getState()) != null){
                    cellView.getStyleClass().removeAll();
                    cellView.setFill(getCustomColor(cell));
                    System.out.println(cellView.getFill());
                } else {
                    cellView.setDefaultStyle();
                }

                cellViewIdNum++;
                root.getChildren().add(cellView);
                cellView.setOnMouseClicked(event -> controller.handleClick(cellView.getRow(), cellView.getCol()));
            }
        }
        bPane.setCenter(root);
        cellViewsRoot = root;
        return root;
    }

    public Group getCellViewsRoot() {
        return cellViewsRoot;
    }

    private Color getCustomColor(Cell cell){
        return cellColors.get(cell.getState());
    }








}
