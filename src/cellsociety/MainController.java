package cellsociety;
import cellsociety.cell.Cell;
import cellsociety.simulation.GameOfLifeSimModel;
import cellsociety.simulation.SimController;
import cellsociety.simulation.SimModel;
import cellsociety.simulation.SimView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.util.List;

public class MainController extends Application {
    public static final String TITLE = "Cell Society";
    public static final int WIDTH = 600;
    public static final int GRID_HEIGHT = 600;
    public static final int HEIGHT = 630;
    public static final String SIMULATION_BUTTON_PREFIX = "Simulation ";
    public static final Paint BACKGROUND = Color.BEIGE;
    public static final int FRAMES_PER_SECOND = 5;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final String INTRO_SCREEN_IMG_NAME = "StartScreen.jpg";
    public static final int DEFAULT_FONT_SIZE = 20;
    public static final String DEFAULT_FONT = "Verdana";
    public static final DecimalFormat df2 = new DecimalFormat("#.##");
    public static final String STARTING_MESSAGE = "  ";
    private Group root = new Group();
    private Scene myScene;
    private Stage myStage;
    private Pane myIntroPane;
    private Timeline myAnimation;
    private Text myPressToBeginText;
    private Text myTimeText;
    private double myTime;
    private SimModel mySimModel;
    private SimController mySimController;
    private boolean isSimulationActive = false;

    @Override
    public void start(Stage stage) {
        Image introScreenImage = new Image(getClass().getClassLoader().getResourceAsStream(INTRO_SCREEN_IMG_NAME));
        ImageView introScreenNode = new ImageView(introScreenImage);
        introScreenNode.setFitHeight(HEIGHT);
        introScreenNode.setFitWidth(WIDTH);
        myIntroPane = new Pane();
        myIntroPane.setId("introPane");
        Scene introScene = new Scene(myIntroPane, WIDTH, HEIGHT);
        myIntroPane.getChildren().add(introScreenNode);

        Button GOLSimButton = makeButton(stage, "Game of Life", 180, 350);
        GOLSimButton.setId("GOLSimButton");
        myIntroPane.getChildren().add(GOLSimButton);

        myStage = stage;
        stage.setScene(introScene);
        stage.setTitle(TITLE);
        stage.show();
        setMyAnimation(stage);
    }

    private Button makeButton(Stage stage, String buttonName, int xLocation, double yLocation) {
        Button simulationButton = new Button(buttonName);
        simulationButton.setOnAction(e -> {
            Scene simulation1Scene = setupSimulation(GameOfLifeSimModel.class, WIDTH, HEIGHT, BACKGROUND);
            stage.setScene(simulation1Scene);
            isSimulationActive = true;
        });
        simulationButton.setTranslateX(xLocation);
        simulationButton.setTranslateY(yLocation);
        return simulationButton;
    }

    public void setMyAnimation(Stage s) {
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        myAnimation = new Timeline();
        myAnimation.setCycleCount(Timeline.INDEFINITE);
        myAnimation.getKeyFrames().add(frame);
        myAnimation.play();
    }

    private <T extends SimModel> Scene setupSimulation(Class<T> simTypeClassName, int width, int height, Paint background) {
        root = new Group();
        mySimController = new SimController(simTypeClassName, this);
        //myTimeText = screenMessage(1 * WIDTH/7, 30, "Time: " + myTime);
        //myPressToBeginText = screenMessage(WIDTH / 3,  2 * HEIGHT / 3, STARTING_MESSAGE);
        //root.getChildren.addAll(List.of(myTimeText, myPressToBeginText);
        myScene = new Scene(root, width, height, background);
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode(), root));

        //testing adding of css styles
        myScene.getStylesheets().add("style.css");
        return myScene;
    }

    public void step(double elapsedTime) {
        if (isSimulationActive) {
            if (mySimController.isEnded()) {
                returnToIntroScreen();
            }
            else {
                mySimController.update(false);
            }
        }
    }

    public void clearRoot() {
        root.getChildren().clear();
    }

    public <T extends Node> void addToRoot(T node) {
        root.getChildren().add(node);
    }


    private void returnToIntroScreen() {
        isSimulationActive = false;
        root.getChildren().clear();
        myAnimation.stop();
        start(myStage);
    }

    private void handleKeyInput(KeyCode code, Group root) {

    }

    private Text screenMessage(double x, double y, String words) {
        Text message = new Text();
        message.setX(x);
        message.setY(y);
        message.setFont(Font.font(DEFAULT_FONT, DEFAULT_FONT_SIZE));
        message.setText(words);
        message.setFill(Color.BLACK);
        return message;
    }

    public static void main(String[] args)
    {
        launch(args);
    }

}
