package cellsociety.frontend;

import cellsociety.grid.Grid;
import cellsociety.SimController;
import cellsociety.gridview.GridView;
import cellsociety.gridview.HexagonGridView;
import cellsociety.gridview.SquareGridView;
import cellsociety.gridview.TriangleGridView;
import javafx.scene.Group;
import cellsociety.cell.Cell;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class SimView {
    public static final Class DEFAULT_GRID_VIEW_SUBCLASS = SquareGridView.class;

    private ResourceBundle myDefaultResources;
    private ResourceBundle mySimResources;
    private SimController controller;
    private UserGUI userGUI;
    private ColorControlsGUI colorControlsGUI;
    private BorderPane bPane;
    private Group cellViewsRoot;
    private Class cellShapeClass;

    public SimView(SimController controller, ResourceBundle defaultResources, ResourceBundle simResources) {
        myDefaultResources = defaultResources;
        mySimResources = simResources;
        this.controller = controller;
        userGUI = new UserGUI(controller, this, myDefaultResources, mySimResources);
        Map<String, Class> orderedCellTypesMap = controller.getModel().getOrderedCellStatesMap();
        colorControlsGUI = new ColorControlsGUI(myDefaultResources, mySimResources, orderedCellTypesMap);
        bPane = new BorderPane();
        createControls(orderedCellTypesMap);
        cellShapeClass = DEFAULT_GRID_VIEW_SUBCLASS;
    }

    public <T extends Cell> Group update(Grid<T> grid) {
        bPane.getChildren().remove(cellViewsRoot);
        Group root = new Group();
        GridView gridView = createGridView(grid);
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
        Button playBttn = new Button(myDefaultResources.getString("PlayBttn"));
        playBttn.setId("playBttn");
        Button pauseBttn = new Button(myDefaultResources.getString("PauseBttn"));
        pauseBttn.setId("pauseBttn");
        Button stepBttn = new Button(myDefaultResources.getString("StepBttn"));
        stepBttn.setId("stepBttn");
        Button exitBttn = new Button(myDefaultResources.getString("ExitBttn"));
        exitBttn.setId("exitBttn");
        Label speedLabel = new Label(myDefaultResources.getString("ChangeSpeed"));
        Slider speedSlider = new Slider(0, 2, 1);
        speedSlider.setBlockIncrement(0.2);
        speedSlider.setPrefWidth(167);
        Button detailsBttn = new Button(myDefaultResources.getString("DetailsBttn"));
        detailsBttn.setId("detailsBttn");
        MenuButton cellShapeMenuButton = createCellShapeMenuButton();


        HBox hbox = new HBox(5);
        hbox.getChildren().addAll(playBttn, pauseBttn, stepBttn, exitBttn, speedLabel, speedSlider, cellShapeMenuButton, detailsBttn);

        playBttn.setOnAction(event -> controller.start());
        pauseBttn.setOnAction(event -> controller.pause());
        stepBttn.setOnAction(event -> controller.stepFrame());
        exitBttn.setOnAction(event -> userGUI.handleExitRequest());
        speedSlider.valueProperty().addListener((observable, oldValue, newValue) -> controller.changeRate((Double) newValue));
        detailsBttn.setOnAction(event -> userGUI.createDetailsPane());
        return hbox;
    }

    private MenuButton createCellShapeMenuButton() {
        MenuButton cellShapeBttn = new MenuButton(myDefaultResources.getString("CellShape"));
        cellShapeBttn.setId("cellShapeBttn");
        MenuItem square = new MenuItem(myDefaultResources.getString("Square"));
        square.setOnAction(event -> cellShapeClass = SquareGridView.class);
        MenuItem hexagon = new MenuItem(myDefaultResources.getString("Hexagon"));
        hexagon.setOnAction(event -> cellShapeClass = HexagonGridView.class);
        MenuItem triangle = new MenuItem(myDefaultResources.getString("Triangle"));
        triangle.setOnAction(event -> cellShapeClass = TriangleGridView.class);
        cellShapeBttn.getItems().addAll(square, hexagon, triangle);
        return cellShapeBttn;
    }

    private <T extends Cell> GridView createGridView(Grid<T> grid) {
        try {
            Constructor<?> constructor;
            GridView gridView;
            constructor = cellShapeClass.getConstructor(Grid.class, SimController.class, ColorControlsGUI.class);
            gridView = (GridView) constructor.newInstance(grid, controller, colorControlsGUI);
            return gridView;
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            // TODO: handle exception properly
            e.printStackTrace();
            //logError(e);
            System.exit(0);
            return null;
        }
    }
}