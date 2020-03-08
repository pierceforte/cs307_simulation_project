package cellsociety.frontend;

import cellsociety.MainController;
import cellsociety.grid.Grid;
import cellsociety.SimController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import cellsociety.cell.Cell;
import cellsociety.cell.CellView;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;

public class SimView {
    public ResourceBundle myDefaultResources;
    public ResourceBundle mySimResources;
    public static final int GRID_SIZE = 400;
    private SimController controller;
    private UserGUI userGUI;
    private ColorControlsGUI colorControlsGUI;
    private BorderPane bPane;
    private Button playBttn;
    private Button pauseBttn;
    private Button stepBttn;
    private Button exitBttn;
    private Button detailsBttn;
    private Group cellViewsRoot;

    public SimView(SimController controller, ResourceBundle defaultResources, ResourceBundle simResources){
        myDefaultResources = defaultResources;
        mySimResources = simResources;
        this.controller = controller;
        userGUI = new UserGUI(controller, this, myDefaultResources, mySimResources);

        Map<String, Class> orderedCellTypesMap = controller.getModel().getOrderedCellTypesMap();
        colorControlsGUI = new ColorControlsGUI(myDefaultResources, mySimResources, orderedCellTypesMap);
        bPane = new BorderPane();

        createControls(orderedCellTypesMap);
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
                colorControlsGUI.setCellFill(cell, cellView);
                cellViewIdNum++;
                root.getChildren().add(cellView);
                cellView.setOnMouseClicked(event -> controller.handleClick(cellView.getRow(), cellView.getCol()));
            }
        }
        bPane.setCenter(root);
        cellViewsRoot = root;
        return root;
    }

    public BorderPane getRoot(){
        return bPane;
    }

    // TODO: idk if this is the best way to do this
    public Map<String, String> getCellFills() {
        return colorControlsGUI.getCellFills();
    }

    private void createControls(Map<String, Class> cellTypesMap){
       VBox vbox = new VBox(5);
       vbox.getChildren().addAll(createButtonControls(), colorControlsGUI.createColorControls(cellTypesMap));
       bPane.setBottom(vbox);
    }

    // TODO: refactor
    private HBox createButtonControls(){
        playBttn = new Button(myDefaultResources.getString("PlayBttn"));
        playBttn.setId("playBttn");
        pauseBttn = new Button(myDefaultResources.getString("PauseBttn"));
        pauseBttn.setId("pauseBttn");
        stepBttn = new Button(myDefaultResources.getString("StepBttn"));
        stepBttn.setId("stepBttn");
        exitBttn = new Button(myDefaultResources.getString("ExitBttn"));
        exitBttn.setId("exitBttn");
        Label speedLabel = new Label(myDefaultResources.getString("ChangeSpeed"));
        Slider speedSlider = new Slider(0, 2, 1);
        speedSlider.setBlockIncrement(0.1);
        detailsBttn = new Button(myDefaultResources.getString("DetailsBttn"));
        detailsBttn.setId("detailsBttn");

        HBox hbox = new HBox(5);
        hbox.getChildren().addAll(playBttn, pauseBttn, stepBttn, exitBttn, speedLabel, speedSlider, detailsBttn);

        playBttn.setOnAction(event -> controller.start());
        pauseBttn.setOnAction(event -> controller.pause());
        stepBttn.setOnAction(event -> controller.stepFrame());
        exitBttn.setOnAction(event -> userGUI.handleExitRequest());
        speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, //
                                Number oldValue, Number newValue) {
                controller.changeRate((Double) newValue);
            }});
        detailsBttn.setOnAction(event -> userGUI.createDetailsPane());
        return hbox;
    }
}
