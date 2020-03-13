package cellsociety.frontend;

import cellsociety.grid.Grid;
import cellsociety.SimController;
import cellsociety.gridview.GridView;
import cellsociety.gridview.HexagonGridView;
import cellsociety.gridview.SquareGridView;
import cellsociety.gridview.TriangleGridView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import cellsociety.cell.Cell;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.*;

public class SimView {
    public ResourceBundle myDefaultResources;
    public ResourceBundle mySimResources;
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
    private GridView gridView;

    public SimView(SimController controller, ResourceBundle defaultResources, ResourceBundle simResources) {
        myDefaultResources = defaultResources;
        mySimResources = simResources;
        this.controller = controller;
        userGUI = new UserGUI(controller, this, myDefaultResources, mySimResources);

        Map<String, Class> orderedCellTypesMap = controller.getModel().getOrderedCellStatesMap();
        colorControlsGUI = new ColorControlsGUI(myDefaultResources, mySimResources, orderedCellTypesMap);
        bPane = new BorderPane();

        createControls(orderedCellTypesMap);
    }

    public <T extends Cell> Group update(Grid<T> grid) {
        bPane.getChildren().remove(cellViewsRoot);
        Group root = new Group();

        //gridView = new TriangleGridView(grid, controller, colorControlsGUI);
        //gridView = new SquareGridView<>(grid, controller, colorControlsGUI);
        gridView = new HexagonGridView(grid, controller, colorControlsGUI);

        GridPane gridPane = gridView.getGridPane();
        root.getChildren().add(gridPane);
        bPane.setCenter(root);
        cellViewsRoot = root;
        return root;
    }

    public BorderPane getRoot() {
        return bPane;
    }

    // TODO: idk if this is the best way to do this
    public Map<String, String> getCellFills() {
        return colorControlsGUI.getCellFills();
    }

    private void createControls(Map<String, Class> cellTypesMap) {
        VBox vbox = new VBox(5);
        vbox.getChildren().addAll(createButtonControls(), colorControlsGUI.createColorControls(cellTypesMap));
        bPane.setBottom(vbox);
    }

    // TODO: refactor
    private HBox createButtonControls() {
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
        speedSlider.setPrefWidth(267);
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
            }
        });
        detailsBttn.setOnAction(event -> userGUI.createDetailsPane());
        return hbox;
    }
}