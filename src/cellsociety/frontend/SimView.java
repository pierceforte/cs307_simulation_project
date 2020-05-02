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

/**
 * This class is used to create the frontend for each simulation, along with a user interface.
 *
 * It is important to note that the SimView is entirely independent from the simulation backend, so
 * only one implementation is needed for any type of simulation.
 *
 * This class is dependent on the resource bundles for all simulations and the specific simulation, the
 * MainController for the application, and the SimController for the current simulation.
 *
 * Note that if this project had not been ended early due to COVID-19, a high priority next step would have
 * been to refactor this class.
 *
 * @author Pierce Forte
 * @author Mary Jiang
 */
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

    /**
     * The constructor to create a SimView.
     * @param controller The controller that handles the interaction with the backend of the simulation
     * @param defaultResources The default resources used for all simulations
     * @param simResources The resources specific to the current simulation
     */
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

    /**
     * Update the front end.
     * @param grid The grid of cells to be displayed
     * @param <T> The type of cells in the grid
     * @return The Group that contains the grid to be placed on screen
     */
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

    /**
     * Get the root for the SimView
     * @return The root for the SimView
     */
    public BorderPane getRoot() {
        return bPane;
    }

    /**
     * Get the cell fills for the cells in the grid.
     * @return The cell fills for the cells in the grid.
     * TODO: determine if there is a better way to access these cell fills
     */
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