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
    private Slider speedSlider;

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
       VBox vbox = new VBox(5);
       vbox.getChildren().addAll(createButtonControls(), createColorControls());
       bPane.setBottom(vbox);
    }

    private HBox createButtonControls(){
        playBttn = new Button(myResources.getString("PlayBttn"));
        pauseBttn = new Button(myResources.getString("PauseBttn"));
        stepBttn = new Button(myResources.getString("StepBttn"));
        exitBttn = new Button(myResources.getString("ExitBttn"));
        Label speedLabel = new Label(myResources.getString("ChangeSpeed"));
        Slider speedSlider = new Slider(0, 2, 1);
        speedSlider.setBlockIncrement(0.2);

        HBox hbox = new HBox(5);
        hbox.getChildren().addAll(playBttn, pauseBttn, stepBttn, exitBttn, speedLabel, speedSlider);

        playBttn.setOnAction(event -> controller.start());
        pauseBttn.setOnAction(event -> controller.pause());
        stepBttn.setOnAction(event -> controller.stepFrame());
        exitBttn.setOnAction(event -> handleExitRequest());
        speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, //
                                Number oldValue, Number newValue) {
                controller.changeRate((Double) newValue);
            }
        });

        return hbox;
    }

    private HBox createColorControls(){
        HBox hbox = new HBox(5);
        ColorPicker colorPicker0 = new ColorPicker();
        ColorPicker colorPicker1 = new ColorPicker();
        ColorPicker colorPicker2 = new ColorPicker();

        return hbox;
    }



    //TODO: cleanup this code
    public boolean userRestartedSimulation() {
        Stage input = new Stage();
        input.setTitle(myResources.getString("StartSim"));
        final boolean[] ret = {false};
        Button restartBttn = createButton(myResources.getString("RestartBttn"),"restartBttn", 0, 0, 100, 30);
        Button continueBttn = createButton(myResources.getString("ContinueBttn"), "restartBttn", 100, 0, 100, 30);

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


    public <T extends Cell> void update(Grid<T> grid) {
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
                cellViewIdNum++;
                root.getChildren().add(cellView);
                cellView.setOnMouseClicked(event -> controller.handleClick(cellView.getRow(), cellView.getCol()));
            }
        }
        bPane.setCenter(root);
    }


    private void setColors(){

    }

    private Button createButton(String text, String id, double xPos, double yPos, double width, double height) {
        Button button = new Button(text);
        button.setTranslateX(xPos);
        button.setTranslateY(yPos);
        button.setPrefWidth(width);
        button.setPrefHeight(height);
        button.setId(id);
        return button;
    }

    // TODO: refactor everything below

    private void getCellCustomizer(){
        controller.pause();

        InputStage stage = new InputStage("Pick custom colors", InputStage.DEFAULT_WIDTH, InputStage.DEFAULT_HEIGHT);
        GridPane pane = new GridPane();

        ColorPicker colorPicker0 = new ColorPicker();
        ColorPicker colorPicker1 = new ColorPicker();
        ColorPicker colorPicker2 = new ColorPicker();



    }
    private void handleExitRequest() {
        controller.pause();

        InputStage stage = new InputStage("Exit", InputStage.DEFAULT_WIDTH, InputStage.DEFAULT_HEIGHT);

        Button beginSaveBttn = createButton("Save", "beginSaveBttn", 300/2 - 100/2, 100, 100, 30);
        Button noSaveBttn = createButton("Quit", "noSaveBttn", 300/2 - 100/2, 140, 100, 30);

        beginSaveBttn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent t) {
                letUserSaveConfig();
                stage.close();
            }
        });
        noSaveBttn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent t) {
                stage.close();
                ensureUserWantsToQuit();
            }
        });

        stage.addNodeToPane(beginSaveBttn);
        stage.addNodeToPane(noSaveBttn);
        stage.showAndWait();
    }

    private void letUserSaveConfig() {
        InputStage stage = new InputStage("Save Current Configuration", InputStage.DEFAULT_WIDTH, InputStage.DEFAULT_HEIGHT);

        stage.addTextToCenterX("Configuration File Name", 50);
        TextField fileNameField = stage.addTextFieldToCenterX(75);

        stage.addTextToCenterX("Author", 150);
        TextField authorField = stage.addTextFieldToCenterX(175);

        stage.addTextToCenterX("Description", 250);
        TextArea descriptionField  = stage.addTextAreaToCenterX(275);

        Button saveBttn = createButton("Save Configuration", "saveBttn", 300/2 - 100/2, 500, 100, 30);
        Button cancelSaveBttn = createButton("Cancel", "cancelSaveBttn", 300/2 - 100/2, 540, 100, 30);

        saveBttn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent t) {
                FileNameVerifier fileNameVerifier = new FileNameVerifier(fileNameField.getText(), controller.getModel().getClass());
                stage.removeErrorMessage();

                String errorMessage = fileNameVerifier.verify();
                if (errorMessage.equals(FileNameVerifier.NAME_IS_VALID)) {
                    stage.close();
                    controller.saveConfig(fileNameField.getText(), authorField.getText(), descriptionField.getText());
                    ensureUserWantsToQuit();
                }
                else {
                    stage.addErrorMessageToCenterX(errorMessage, 590);
                }
            }
        });
        cancelSaveBttn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent t) {
                stage.close();
                controller.start();
            }
        });

        stage.addNodeToPane(saveBttn);
        stage.addNodeToPane(cancelSaveBttn);

        stage.showAndWait();
    }

    private void ensureUserWantsToQuit() {
        InputStage stage = new InputStage("Resume or Quit", InputStage.DEFAULT_WIDTH, InputStage.DEFAULT_HEIGHT);

        stage.addTextToCenterX("Are you sure you want to Quit the simulation?", 150);

        Button resumeBttn = createButton("Resume", "resumeBttn", 300/2 - 100/2, 400, 100, 30);
        Button quitBttn = createButton("Quit", "quitBttn", 300/2 - 100/2, 440, 100, 30);

        resumeBttn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent t) {
                stage.close();
                controller.start();
            }
        });
        quitBttn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent t) {
                stage.close();
                controller.setIsEnded(true);
            }
        });

        stage.addNodeToPane(resumeBttn);
        stage.addNodeToPane(quitBttn);

        stage.showAndWait();
    }





}
