package cellsociety;
import cellsociety.simulation.*;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.text.DecimalFormat;
import java.util.Arrays;

public class MainController extends Application {
    public static final String STYLESHEET = "style.css";
    public static final String TITLE = "Cell Society";
    public static final int WIDTH = 600;
    public static final int GRID_HEIGHT = 600;
    public static final int HEIGHT = 680;
    public static final Paint BACKGROUND = Color.BEIGE;
    public static final int FRAMES_PER_SECOND = 5;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final String INTRO_SCREEN_IMG_NAME = "StartScreen.jpg";
    public static final int DEFAULT_FONT_SIZE = 5;
    public static final String DEFAULT_FONT = "Verdana";
    public static final DecimalFormat df2 = new DecimalFormat("#.##");

    private Group myRoot = new Group();
    private Scene myScene;
    private Stage myStage;
    private Pane myIntroPane;
    private Timeline myAnimation;
    private Text myPressToBeginText;
    private Text myTimeText;
    private double myTime;
    private SimModel mySimModel;
    private SimController mySimController;
    private boolean isMySimulationActive = false;

    @Override
    public void start(Stage stage) {
        Image introScreenImage = new Image(getClass().getClassLoader().getResourceAsStream(INTRO_SCREEN_IMG_NAME));
        ImageView introScreenNode = new ImageView(introScreenImage);
        introScreenNode.setFitHeight(HEIGHT);
        introScreenNode.setFitWidth(WIDTH);
        myIntroPane = new Pane();
        myIntroPane.setId("introPane");
        Scene introScene = new Scene(myIntroPane, WIDTH, HEIGHT);
        introScene.getStylesheets().add(STYLESHEET);
        myIntroPane.getChildren().add(introScreenNode);

        SimSelector simSelector = new SimSelector(this);
        Button simSelectorButton = simSelector.createSelectorButton();
        myIntroPane.getChildren().add(simSelectorButton);

        myStage = stage;
        myStage.setScene(introScene);
        myStage.setTitle(TITLE);
        myStage.show();
        setMyAnimation(myStage);
    }

    public void setMySimulationActiveStatus(boolean activeStatus) {
        isMySimulationActive = activeStatus;
    }

    public void setMyStage(Stage stage) {
        myStage = stage;
    }

    public Stage getMyStage() {
        return myStage;
    }

    public void setMyAnimation(Stage s) {
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        myAnimation = new Timeline();
        myAnimation.setCycleCount(Timeline.INDEFINITE);
        myAnimation.getKeyFrames().add(frame);
        myAnimation.play();
    }

    public void changeAnimationSpeed(double change){
        myAnimation.setRate(FRAMES_PER_SECOND * change);
    }

    public <T extends SimModel> void beginSimulation(Class<T> simTypeClassName, String csvFilePath) {
        Scene simulationScene = setupSimulation(simTypeClassName, csvFilePath);
        myStage.setScene(simulationScene);
        isMySimulationActive = true;
    }

    private <T extends SimModel> Scene setupSimulation(Class<T> simTypeClassName, String csvFilePath) {
        myRoot = new Group();
        String [] csvFilePathFromResources = csvFilePath.split("/");
        String validCsvFilePath = String.join("/",
                Arrays.copyOfRange(csvFilePathFromResources, csvFilePathFromResources.length-4, csvFilePathFromResources.length));
        mySimController = new SimController(simTypeClassName, this, validCsvFilePath);
        myRoot.getChildren().add(mySimController.getViewRoot());
        myScene = new Scene(myRoot, WIDTH, HEIGHT, BACKGROUND);
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode(), myRoot));
        myStage.setTitle(mySimController.getSimResources().getString("Title"));

        //testing adding of css styles
        myScene.getStylesheets().add(STYLESHEET);
        return myScene;
    }

    public void step(double elapsedTime) {
        if (isMySimulationActive) {
            if (mySimController.isEnded()) {
                returnToIntroScreen();
            }
            else {
                mySimController.update(false);
            }
        }
    }

    public void clearRoot() {
        myRoot.getChildren().clear();
    }

    public <T extends Node> void addToRoot(T node) {
        myRoot.getChildren().add(node);
    }

    public SimController getCurSimController() {
        return mySimController;
    }

    private void returnToIntroScreen() {
        isMySimulationActive = false;
        myRoot.getChildren().clear();
        myAnimation.stop();
        start(myStage);
    }

    private void handleKeyInput(KeyCode code, Group root) {

    }

    public static void main(String[] args)
    {
        launch(args);
    }

}
