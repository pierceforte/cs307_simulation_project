package cellsociety;
import cellsociety.cell.Cell;
import cellsociety.simulation.GameOfLifeSimModel;
import cellsociety.simulation.SimController;
import cellsociety.simulation.SimModel;
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
    public static final int HEIGHT = 600;
    public static final String SIMULATION_BUTTON_PREFIX = "Simulation ";
    public static final Paint BACKGROUND = Color.BEIGE;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final String INTRO_SCREEN_IMG_NAME = "StartScreen.jpg";
    public static final int DEFAULT_FONT_SIZE = 20;
    public static final String DEFAULT_FONT = "Verdana";
    public static final DecimalFormat df2 = new DecimalFormat("#.##");
    public static final String STARTING_MESSAGE = "  ";

    private Scene myScene;
    private Stage myStage;
    private Pane myIntroPane = new Pane();
    private Timeline myAnimation;
    private Text myPressToBeginText;
    private Text myTimeText;
    private double myTime;
    private SimModel mySimModel;
    private SimController mySimController;

    @Override
    public void start(Stage stage) {

        Image introScreenImage = new Image(getClass().getClassLoader().getResourceAsStream(INTRO_SCREEN_IMG_NAME));
        ImageView introScreenNode = new ImageView(introScreenImage);
        introScreenNode.setFitHeight(HEIGHT);
        introScreenNode.setFitWidth(WIDTH);
        Scene introScene = new Scene(myIntroPane, WIDTH, HEIGHT);
        myIntroPane.getChildren().add(introScreenNode);

        //read configuration files

        Scene simulation1Scene = setupSimulation(WIDTH, HEIGHT, BACKGROUND,"GOL");
        Scene simulation2Scene = setupSimulation(WIDTH, HEIGHT, BACKGROUND,"SIM2");
        Button simulation1Button = makeButton(stage, simulation1Scene, "Simulation 1", 180, 350);
        Button simulation2Button = makeButton(stage, simulation2Scene, "Simulation 2", 360, 350);

        myIntroPane.getChildren().addAll(simulation1Button, simulation2Button);


        myStage = stage;
        stage.setScene(introScene);
        stage.setTitle(TITLE);
        stage.show();
        setMyAnimation(stage);
    }

    private Button makeButton(Stage stage, Scene simulation1Scene, String buttonName, int xLocation, double yLocation) {
        Button simulationButton = new Button(buttonName);
        simulationButton.setOnAction(e -> stage.setScene(simulation1Scene));
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


    public Scene setupSimulation(int width, int height, Paint background, String simulationName) {
        Group root = new Group();
        myScene = new Scene(root, width, height, background);
        ConfigReader data = new ConfigReader(simulationName + "Config.csv");

        List<List<Cell>> listOfCells = data.getCellList();
        mySimModel = new GameOfLifeSimModel(listOfCells);
        mySimController = new SimController(mySimModel);

        myTimeText = screenMessage(1 * WIDTH/7, 30, "Time: " + myTime);
        myPressToBeginText = screenMessage(WIDTH / 3,  2 * HEIGHT / 3, STARTING_MESSAGE);
        addToRoot(root);
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode(), root));
        return myScene;
    }

    private void addToRoot(Group root) {

        root.getChildren().add(myTimeText);
    }

    public void step(double elapsedTime) {
        mySimController.play();
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


//    Scene configScene() {
//        Group root = new Group();
//
//    }


    private void addSimulationButtonToScene(Scene scene, int simNumber, Stage stage, double xPos, double yPos) {
        Button simulationButton1 = makeButton(stage, scene, SIMULATION_BUTTON_PREFIX + simNumber, (int) xPos, yPos);
        myIntroPane.getChildren().add(simulationButton1);
    }

    public static void main(String[] args)
    {
        launch(args);
    }

}
