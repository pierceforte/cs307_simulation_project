package cellsociety;

import cellsociety.cell.config.ConfigSaver;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;

public class SimSelector {

    private MainController myMainController;
    private Button mySimSelectorButton;

    public SimSelector(MainController mainController) {
        myMainController = mainController;
    }

    public Button createSelectorButton() {
        mySimSelectorButton = new Button("Select Simulation");
        mySimSelectorButton.setPrefWidth(150);
        mySimSelectorButton.setPrefHeight(30);
        mySimSelectorButton.setTranslateX(MainController.WIDTH/2 - mySimSelectorButton.getPrefWidth()/2);
        mySimSelectorButton.setTranslateY(MainController.HEIGHT/2 - mySimSelectorButton.getPrefHeight()/2);
        mySimSelectorButton.setOnAction(e -> {
            activateMySimSelectorButton();
        });
        mySimSelectorButton.setId("FileSelectorButton");
        return mySimSelectorButton;
    }

    private void activateMySimSelectorButton() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        String initialPath = Paths.get("resources/configs/").toAbsolutePath().normalize().toString();
        directoryChooser.setInitialDirectory(new File(initialPath));
        File directoryChosen = directoryChooser.showDialog(new Stage());
        String [] directoryChosenPath = directoryChosen.getPath().split("/");
        String simTypeDirectory = directoryChosenPath[directoryChosenPath.length - 2];

        File csvFile = new File(directoryChosen.getPath() + "/"
                + directoryChosen.getName() + ConfigSaver.CSV_EXTENSION);

        //File propertiesFile = new File(directoryChosen.getPath() + directoryChosen.getName() + ConfigSaver.PROPERTIES_EXTENSION);

        if (!ConfigSaver.DIRECTORY_TO_SIM_CLASS.containsKey(simTypeDirectory) || !csvFile.isFile() ) { //|| !propertiesFile.isFile()) {
            handleInvalidDirectory(simTypeDirectory);
        }
        else {
            myMainController.beginSimulation(ConfigSaver.DIRECTORY_TO_SIM_CLASS.get(simTypeDirectory), csvFile.getPath());
        }
    }

    private void handleInvalidDirectory(String simTypeDirectory) {
        InputStage errorStage = new InputStage("Invalid Directory Selection", InputStage.DEFAULT_WIDTH, InputStage.DEFAULT_HEIGHT);
        String message;
        if (!ConfigSaver.DIRECTORY_TO_SIM_CLASS.containsKey(simTypeDirectory)) {
            message = "The directory selected must be in one of the following directories: ";
            for (String key : ConfigSaver.DIRECTORY_TO_SIM_CLASS.keySet()) {
                message += "resources/configs/" + key + "/, ";
            }
            message = message.substring(0, message.length()-2);
        }
        else {
            message = "A directory with one csv and one properties file of the same names must be chosen.";
        }
        errorStage.addErrorMessageToCenterX(message, 100);

        Button okButton = new Button("OK");
        okButton.setId("okBttn");
        okButton.setPrefWidth(100);
        okButton.setPrefHeight(30);
        okButton.setTranslateX(InputStage.DEFAULT_WIDTH/2 - okButton.getPrefWidth()/2);
        okButton.setTranslateY(250);

        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent t) {
                errorStage.close();
            }
        });

        errorStage.addNodeToPane(okButton);

        errorStage.showAndWait();
    }
}
