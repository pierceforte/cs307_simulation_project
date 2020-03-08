package cellsociety;

import cellsociety.config.ConfigSaver;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.ResourceBundle;

public class SimSelector {

    private MainController myMainController;
    private Button mySimSelectorButton;
    private ResourceBundle myDefaultResources;

    public SimSelector(MainController mainController) {
        myMainController = mainController;
        Locale locale = new Locale("en", "US");
        myDefaultResources = ResourceBundle.getBundle("default", locale);
    }

    public Button createSelectorButton() {
        mySimSelectorButton = new Button(myDefaultResources.getString("SelectSim"));
        mySimSelectorButton.setId("fileSelectorButton");
        mySimSelectorButton.setPrefWidth(150);
        mySimSelectorButton.setPrefHeight(30);
        mySimSelectorButton.setTranslateX(MainController.WIDTH/2 - mySimSelectorButton.getPrefWidth()/2);
        mySimSelectorButton.setTranslateY(MainController.HEIGHT/2 - mySimSelectorButton.getPrefHeight()/2);
        mySimSelectorButton.setOnAction(e -> {
            activateMySimSelectorButton();
        });
        return mySimSelectorButton;
    }

    private void activateMySimSelectorButton() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        String initialPath = Paths.get("resources/configs/").toAbsolutePath().normalize().toString();
        directoryChooser.setInitialDirectory(new File(initialPath));
        File directoryChosen = directoryChooser.showDialog(new Stage());
        if (directoryChosen == null) {
            // TODO: handle error
            return;
        }
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
        InputStage errorStage = new InputStage(myDefaultResources.getString("InvalidDir"), InputStage.DEFAULT_WIDTH, InputStage.DEFAULT_HEIGHT,
                "invalidDirectoryPane");
        String message;
        if (!ConfigSaver.DIRECTORY_TO_SIM_CLASS.containsKey(simTypeDirectory)) {
            message = myDefaultResources.getString("DoesNotContainClass");
            for (String key : ConfigSaver.DIRECTORY_TO_SIM_CLASS.keySet()) {
                message += "resources/configs/" + key + "/, ";
            }
            message = message.substring(0, message.length()-2);
        }
        else {
            message = myDefaultResources.getString("MustHaveSameNames");
        }
        errorStage.addErrorMessageToCenterX(message, 100);

        Button okButton = new Button(myDefaultResources.getString("Ok"));
        okButton.setId(myDefaultResources.getString("Ok"));
        okButton.setPrefWidth(100);
        okButton.setPrefHeight(30);
        okButton.setTranslateX(InputStage.DEFAULT_WIDTH/2 - okButton.getPrefWidth()/2);
        okButton.setTranslateY(250);

        okButton.setOnAction(t -> errorStage.close());

        errorStage.addNodeToPane(okButton);

        errorStage.showAndWait();
    }
}
