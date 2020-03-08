package cellsociety.simulation;

import cellsociety.MainController;
import cellsociety.grid.Grid;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import cellsociety.cell.Cell;
import cellsociety.cell.CellView;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class SimView {
    public ResourceBundle myDefaultResources;
    public ResourceBundle mySimResources;
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
    private Button detailsBttn;
    private Slider speedSlider;
    private HashMap<String, Color> cellColors;
    private Group cellViewsRoot;

    public SimView(SimController controller, ResourceBundle defaultResources, ResourceBundle simResources){
        Locale locale = new Locale("en", "US");
        myDefaultResources = defaultResources;
        mySimResources = simResources;
        this.controller = controller;
        gui = new UserGUI(controller, myDefaultResources, mySimResources);
        bPane = new BorderPane();
        createControls(controller.getModel().getOrderedCellTypesMap());
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
                    cellView.setFill(getCustomColor(cell));
                } else {
                    Color color = getColorFromSimProperties(cell.getState());
                    cellView.setFill(color);
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

    public BorderPane getRoot(){
        return bPane;
    }

    private void createControls(Map<String, Class> cellTypesMap){
       VBox vbox = new VBox(5);
       cellColors = new HashMap<>();
       vbox.getChildren().addAll(createButtonControls(), createColorControls(cellTypesMap));
       bPane.setBottom(vbox);
    }

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
        speedSlider.setBlockIncrement(0.2);
        detailsBttn = new Button(myDefaultResources.getString("DetailsBttn"));
        detailsBttn.setId("detailsBttn");

        HBox hbox = new HBox(5);
        hbox.getChildren().addAll(playBttn, pauseBttn, stepBttn, exitBttn, speedLabel, speedSlider, detailsBttn);

        playBttn.setOnAction(event -> controller.start());
        pauseBttn.setOnAction(event -> controller.pause());
        stepBttn.setOnAction(event -> controller.stepFrame());
        exitBttn.setOnAction(event -> gui.handleExitRequest());
        speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, //
                                Number oldValue, Number newValue) {
                controller.changeRate((Double) newValue);
            }});
        detailsBttn.setOnAction(event -> gui.createDetailsPane());
        return hbox;
    }

    //TODO: maybe eliminate the dependency on cellTypesMap and find a way to use properties file.
    //  Also need to add option to add image
    private HBox createColorControls(Map<String, Class> cellTypesMap){
        HBox hbox = new HBox(5);

        for (String state : cellTypesMap.keySet()) {
            Color color = getColorFromSimProperties(state);
            ColorPicker picker = new ColorPicker(color);
            picker.setStyle("-fx-color-label-visible: false;");
            Text cellType = new Text(mySimResources.getString("State" + state + "Title") + ":");
            hbox.getChildren().addAll(cellType, picker);

            picker.setOnAction(event -> cellColors.put(state, picker.getValue()));
        }

        return hbox; }

    private Color getCustomColor(Cell cell){
        return cellColors.get(cell.getState());
    }

    private Color getColorFromSimProperties(String state) {
        Color color;
        try {
            color = Color.web(mySimResources.getString("State" + state));
        } catch (Exception e) {
            System.out.println("here");
            color = null; // Not defined
        }
        return color;
    }

}
