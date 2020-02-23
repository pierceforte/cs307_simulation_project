package cellsociety;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

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

    private Stage myStage;

    @Override
    public void start(Stage stage) {

        Image introScreenImage = new Image(getClass().getClassLoader().getResourceAsStream(INTRO_SCREEN_IMG_NAME));
        //read configuration files

        addSimulationButtonToScene("GOL", 1, myStage, 220, 350);
        addSimulationButtonToScene("Simulation 2", 2, myStage, 160, 350);
        addSimulationButtonToScene("Simulation 3", 3, myStage, 260, 550);
        addSimulationButtonToScene("Simulation 4", 4, myStage, 360, 550);
        addSimulationButtonToScene("Simulation 5", 5, myStage, 360, 550);

        myStage = stage;
        stage.setScene(introScene);
        stage.setTitle(TITLE);
        stage.show();
        setAnimation(stage);
    }


//    Scene configScene() {
//        Group root = new Group();
//
//    }

    private void addSimulationButtonToScene(String name, int simNumber, Stage stage, double xPos, double yPos) {
        Scene simulationScene = setupSimulation(WIDTH, HEIGHT, BACKGROUND,name);
        Button simulationButton = new Button (SIMULATION_BUTTON_PREFIX + simNumber);
        simulationButton.setOnAction(e -> stage.setScene(simulationScene));
        simulationButton.setTranslateX(xPos);
        simulationButton.setTranslateY(yPos);
        intro.getChildren().addAll(simulationButton);
    }


}
