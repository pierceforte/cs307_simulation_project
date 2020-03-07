package cellsociety.simulation;

import cellsociety.InputStage;
import cellsociety.cell.FileNameVerifier;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Initializes stage components that prompt for user input/control of simulations
 * TODO: Implement inheritance hierarchy
 */
public class UserGUI {
    private SimController controller;
    private ResourceBundle myResources;

    public UserGUI(SimController controller){
        this.controller = controller;
        Locale locale = new Locale("en", "US");
        myResources = ResourceBundle.getBundle("default", locale);
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

    protected void handleExitRequest() {
        controller.pause();
        InputStage stage = new InputStage(myResources.getString("ExitPaneTitle"), InputStage.DEFAULT_WIDTH, 300, "exitRequestPane");
        Button beginSaveBttn = createButton(myResources.getString("Save"), "beginSaveBttn", 300/2 - 100/2, 80, 100, 30);
        Button cancelExitBttn = createButton(myResources.getString("Cancel"), "cancelExitBttn",300/2 - 100/2, 120, 100, 30);
        Button noSaveBttn = createButton(myResources.getString("Quit"), "noSaveBttn", 300/2 - 100/2, 160, 100, 30);


        beginSaveBttn.setOnAction(t -> {
            letUserSaveConfig();
            stage.close();
        });
        cancelExitBttn.setOnAction(t -> {
            controller.start();
            stage.close();
        });
        noSaveBttn.setOnAction(t -> {
            ensureUserWantsToQuit();
            stage.close();
        });

        stage.addNodesToPane(List.of(beginSaveBttn, cancelExitBttn, noSaveBttn));
        stage.showAndWait();
    }

    private void letUserSaveConfig() {
        InputStage stage = new InputStage(myResources.getString("SaveConfig"), InputStage.DEFAULT_WIDTH,
                InputStage.DEFAULT_HEIGHT, "saveConfigPane");

        stage.addTextToCenterX(myResources.getString("FileName"), 50);
        TextField fileNameField = stage.addTextFieldToCenterX(75);
        fileNameField.setId("fileNameField");

        stage.addTextToCenterX(myResources.getString("Author"), 150);
        TextField authorField = stage.addTextFieldToCenterX(175);

        stage.addTextToCenterX(myResources.getString("Description"), 250);
        TextArea descriptionField  = stage.addTextAreaToCenterX(275);

        Button saveBttn = createButton(myResources.getString("Save"), "saveBttn", 300/2 - 100/2, 500, 100, 30);
        Button cancelSaveBttn = createButton(myResources.getString("Cancel"), "cancelSaveBttn", 300/2 - 100/2, 540, 100, 30);

        saveBttn.setOnAction(t -> {
            FileNameVerifier fileNameVerifier = new FileNameVerifier(fileNameField.getText(), controller.getModel().getClass());
            stage.removeErrorMessage();
            String errorMessage = fileNameVerifier.verify();
            if (errorMessage.equals(FileNameVerifier.NAME_IS_VALID)) {
                controller.saveConfig(fileNameField.getText(), authorField.getText(), descriptionField.getText());
                ensureUserWantsToQuit();
                stage.close();
            }
            else {
                stage.addErrorMessageToCenterX(errorMessage, 590);
            }
        });
        cancelSaveBttn.setOnAction(t -> {
            controller.start();
            stage.close();
        });

        stage.addNodesToPane(List.of(saveBttn, cancelSaveBttn));
        stage.showAndWait();
    }

    private void ensureUserWantsToQuit() {
        InputStage stage = new InputStage(myResources.getString("QuitOrNot"), InputStage.DEFAULT_WIDTH, 300, "ensureQuitPane");

        stage.addTextToCenterX(myResources.getString("ConfirmQuit"), 100);

        Button resumeBttn = createButton(myResources.getString("Resume"), "resumeBttn", 300/2 - 100/2, 150, 100, 30);
        Button quitBttn = createButton(myResources.getString("Quit"), "quitBttn", 300/2 - 100/2, 190, 100, 30);

        resumeBttn.setOnAction(t -> {
            controller.start();
            stage.close();
        });
        quitBttn.setOnAction(t -> {
            controller.setIsEnded(true);
            stage.close();
        });
        stage.addNodesToPane(List.of(resumeBttn, quitBttn));
        stage.showAndWait();
    }
}
