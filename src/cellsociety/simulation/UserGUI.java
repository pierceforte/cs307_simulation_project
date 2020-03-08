package cellsociety.simulation;

import cellsociety.InputStage;
import cellsociety.cell.FileNameVerifier;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.List;
import java.util.ResourceBundle;


/**
 * Initializes stage components that prompt for user input/control of simulations
 * TODO: Implement inheritance hierarchy
 */
public class UserGUI {
    private SimController controller;
    private ResourceBundle myDefaultResources;
    private ResourceBundle mySimResources;

    public UserGUI(SimController controller, ResourceBundle defaultResources, ResourceBundle simResources){
        this.controller = controller;
        myDefaultResources = defaultResources;
        mySimResources = simResources;
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
        InputStage stage = new InputStage(myDefaultResources.getString("ExitPaneTitle"), InputStage.DEFAULT_WIDTH, 300, "exitRequestPane");
        Button beginSaveBttn = createButton(myDefaultResources.getString("Save"), "beginSaveBttn", 300/2 - 100/2, 80, 100, 30);
        Button cancelExitBttn = createButton(myDefaultResources.getString("Cancel"), "cancelExitBttn",300/2 - 100/2, 120, 100, 30);
        Button noSaveBttn = createButton(myDefaultResources.getString("Quit"), "noSaveBttn", 300/2 - 100/2, 160, 100, 30);


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

    protected void createDetailsPane() {
        controller.pause();
        InputStage stage = new InputStage(myDefaultResources.getString("DetailsPaneTitle"), InputStage.DEFAULT_WIDTH, InputStage.DEFAULT_HEIGHT, "exitRequestPane");
        Button closeDetailsBttn = createButton(myDefaultResources.getString("Close"),
                "closeDetailsBttn", InputStage.DEFAULT_WIDTH/2 - 100/2, InputStage.DEFAULT_HEIGHT-75, 100, 30);

        stage.addTextToCenterX(myDefaultResources.getString("FileName"), 50);
        Text fileName = stage.addTextToCenterX(mySimResources.getString("Title"), 75);
        stage.addEllipsisIfNecessary(fileName, 70, 160);
        fileName.setFill(Color.PURPLE);

        stage.addTextToCenterX(myDefaultResources.getString("Author"), 150);
        Text author = stage.addTextToCenterX(mySimResources.getString("Author"), 175);
        stage.addEllipsisIfNecessary(author, 70, 160);
        author.setFill(Color.PURPLE);

        stage.addTextToCenterX(myDefaultResources.getString("Description"), 250);
        Text description = stage.addTextToCenterX(mySimResources.getString("Description"), 275);
        stage.addEllipsisIfNecessary(description, 330, 700);
        description.setFill(Color.PURPLE);

        closeDetailsBttn.setOnAction(t -> {
            controller.start();
            stage.close();
        });

        stage.addNodesToPane(List.of(closeDetailsBttn));
        stage.showAndWait();
    }

    private void letUserSaveConfig() {
        InputStage stage = new InputStage(myDefaultResources.getString("SaveConfig"), InputStage.DEFAULT_WIDTH,
                InputStage.DEFAULT_HEIGHT, "saveConfigPane");

        stage.addTextToCenterX(myDefaultResources.getString("FileName"), 50);
        TextField fileNameField = stage.addTextFieldToCenterX(75);
        fileNameField.setId("fileNameField");

        stage.addTextToCenterX(myDefaultResources.getString("Author"), 150);
        TextField authorField = stage.addTextFieldToCenterX(175);

        stage.addTextToCenterX(myDefaultResources.getString("Description"), 250);
        TextArea descriptionField  = stage.addTextAreaToCenterX(275);

        Button saveBttn = createButton(myDefaultResources.getString("Save"), "saveBttn", 300/2 - 100/2, 500, 100, 30);
        Button cancelSaveBttn = createButton(myDefaultResources.getString("Cancel"), "cancelSaveBttn", 300/2 - 100/2, 540, 100, 30);

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
        InputStage stage = new InputStage(myDefaultResources.getString("QuitOrNot"), InputStage.DEFAULT_WIDTH, 300, "ensureQuitPane");

        stage.addTextToCenterX(myDefaultResources.getString("ConfirmQuit"), 100);

        Button resumeBttn = createButton(myDefaultResources.getString("Resume"), "resumeBttn", 300/2 - 100/2, 150, 100, 30);
        Button quitBttn = createButton(myDefaultResources.getString("Quit"), "quitBttn", 300/2 - 100/2, 190, 100, 30);

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
