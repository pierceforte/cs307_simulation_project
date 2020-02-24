package cellsociety;

import cellsociety.cell.Cell;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

import static javafx.application.Application.launch;

/**
 * Feel free to completely change this code or delete it entirely. 
 */
public class Main {//extends Application {
    /**
     * Start of the program.
     */
    public static final String TITLE = "Simulation";
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    public static final Paint BACKGROUND = Color.BEIGE;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    public static final String STARTING_MESSAGE = "Press SPACE to Start\nPress ENTER to Pause\nPress SPACE to Resume\nPress L to Add Lives\nPress S to slow the Ball\nPress F to speed up the Ball";
    private Scene myScene;
    private Text time;
    private double timeSoFar;
    private int FONT_SIZE = 20;
    private Text pressToBegin;
    public Timeline animation;
    private Pane intro= new Pane();


    @Override
    public void start(Stage stage) {

        Image introScreen = new Image(getClass().getClassLoader().getResourceAsStream("StartScreen.jpg")); // image to the intro screen of the game
        ImageView introScreenNode = new ImageView(introScreen);
        introScreenNode.setImage(introScreen); // set the image of the title/intro screen
        introScreenNode.setFitHeight(HEIGHT);
        introScreenNode.setFitWidth(WIDTH);
        Scene introScene =new Scene(intro, WIDTH, HEIGHT);


        Scene simulation1Scene = setupSimulation(WIDTH, HEIGHT, BACKGROUND,"level1");
        Scene simulation2Scene = setupSimulation(WIDTH, HEIGHT, BACKGROUND,"level2");
        Scene simulation3Scene = setupSimulation(WIDTH, HEIGHT, BACKGROUND,"level3");
        Scene simulation4Scene = setupSimulation(WIDTH, HEIGHT, BACKGROUND,"level2");
        Scene simulation5Scene = setupSimulation(WIDTH, HEIGHT, BACKGROUND,"level3");

        Button simulation1Button = new Button ("Simulation 1");
        simulation1Button.setOnAction(e -> stage.setScene(simulation1Scene));
        simulation1Button.setTranslateX(220);
        simulation1Button.setTranslateY(350);

        Button simulation2Button = new Button ("Simulation 2");
        simulation2Button.setOnAction(e -> stage.setScene(simulation2Scene));
        simulation2Button.setTranslateX(160);
        simulation2Button.setTranslateY(550);

        Button simulation3Button = new Button ("Simulation 3");
        simulation3Button.setOnAction(e -> stage.setScene(simulation3Scene));
        simulation3Button.setTranslateX(260);
        simulation3Button.setTranslateY(550);

        Button simulation4Button = new Button ("Simulation 4");
        simulation4Button.setOnAction(e -> stage.setScene(simulation4Scene));
        simulation4Button.setTranslateX(360);
        simulation4Button.setTranslateY(550);

        Button simulation5Button = new Button ("Simulation 5");
        simulation5Button.setOnAction(e -> stage.setScene(simulation5Scene));
        simulation5Button.setTranslateX(360);
        simulation5Button.setTranslateY(550);

        intro.getChildren().addAll(introScreenNode, simulation2Button, simulation3Button,simulation4Button,simulation2Button,simulation5Button);

        stage.setScene(introScene);
        stage.setTitle(TITLE);
        stage.show();
        setAnimation(stage);

    }

    public void setAnimation(Stage s) {
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
    }


    public Scene setupSimulation(int width, int height, Paint background, String simulationName) {
        Group root = new Group();
        myScene = new Scene(root, width, height, background);
        ConfigReader data = new ConfigReader(simulationName);
        List<List<Cell>> listOfBricks= data.getCellList();
        time = screenMessage(time, 1 * WIDTH/7, 30, "Time: " + timeSoFar);
        pressToBegin = screenMessage(pressToBegin, WIDTH / 3,  2 * HEIGHT / 3, STARTING_MESSAGE);
        addToRoot(root);
        myScene.setOnKeyPressed(e -> handleKeyInput(e.getCode(), root));
        return myScene;
    }

    private void addToRoot(Group root) {
        root.getChildren().add(time);
    }

    public void step(double elapsedTime) {

    }

    private void handleKeyInput(KeyCode code, Group root) {


    }

    private Text screenMessage(Text message, double x, double y, String words) {
        message = new Text();
        message.setX(x);
        message.setY(y);
        message.setFont(Font.font("Verdana", FONT_SIZE));
        message.setText(words);
        message.setFill(Color.BLACK);
        return message;
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
