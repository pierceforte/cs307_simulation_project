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
        InputStage stage = new InputStage("Exit", InputStage.DEFAULT_WIDTH, InputStage.DEFAULT_HEIGHT, "exitRequestPane");
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
                ensureUserWantsToQuit();
                stage.close();
            }
        });

        stage.addNodeToPane(beginSaveBttn);
        stage.addNodeToPane(noSaveBttn);
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

        Button saveBttn = createButton(myResources.getString("SaveConfigBttn"), "saveBttn", 300/2 - 100/2, 500, 100, 30);
        Button cancelSaveBttn = createButton(myResources.getString("CancelBttn"), "cancelSaveBttn", 300/2 - 100/2, 540, 100, 30);

        saveBttn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent t) {
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
            }
        });
        cancelSaveBttn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent t) {
                controller.start();
                stage.close();
            }
        });

        stage.addNodeToPane(saveBttn);
        stage.addNodeToPane(cancelSaveBttn);

        stage.showAndWait();
    }

    private void ensureUserWantsToQuit() {
        InputStage stage = new InputStage(myResources.getString("QuitOrNot"), InputStage.DEFAULT_WIDTH, InputStage.DEFAULT_HEIGHT, "ensureQuitPane");

        stage.addTextToCenterX(myResources.getString("ConfirmQuit"), 150);

        Button resumeBttn = createButton(myResources.getString("Resume"), "resumeBttn", 300/2 - 100/2, 400, 100, 30);
        Button quitBttn = createButton(myResources.getString("Quit"), "quitBttn", 300/2 - 100/2, 440, 100, 30);

        resumeBttn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent t) {
                controller.start();
                stage.close();
            }
        });
        quitBttn.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent t) {
                controller.setIsEnded(true);
                stage.close();
            }
        });
        stage.addNodeToPane(resumeBttn);
        stage.addNodeToPane(quitBttn);
        stage.showAndWait();
    }
}
