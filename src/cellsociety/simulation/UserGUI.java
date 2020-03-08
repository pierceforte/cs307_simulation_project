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
    private SimView view;
    private ResourceBundle myDefaultResources;
    private ResourceBundle mySimResources;

    public UserGUI(SimController controller, SimView view, ResourceBundle defaultResources, ResourceBundle simResources){
        this.controller = controller;
        this.view = view;
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

    //TODO: Refactor (VERY similar to letUserSaveConfig())
    protected void createDetailsPane() {
        controller.pause();
        InputStage stage = new InputStage(myDefaultResources.getString("DetailsPaneTitle"), 500, 600
                , "exitRequestPane");
        Button closeDetailsBttn = createButton(myDefaultResources.getString("Close"),
                "closeDetailsBttn", 500/2 - 100/2, 600-75, 100, 30);

        stage.addTextToCenterX(myDefaultResources.getString("SimulationType"), 20);
        Text simType = stage.addTextToCenterX(mySimResources.getString("SimType"), 40);
        stage.addEllipsisIfNecessary(simType, 50, 180);
        simType.setFill(Color.PURPLE);

        stage.addTextToCenterX(myDefaultResources.getString("FileName"), 100);
        Text fileName = stage.addTextToCenterX(mySimResources.getString("Title"), 120);
        stage.addEllipsisIfNecessary(fileName, 60, 250);
        fileName.setFill(Color.PURPLE);

        stage.addTextToCenterX(myDefaultResources.getString("Author"), 200);
        Text author = stage.addTextToCenterX(mySimResources.getString("Author"), 220);
        stage.addEllipsisIfNecessary(author, 60, 250);
        author.setFill(Color.PURPLE);

        stage.addTextToCenterX(myDefaultResources.getString("Description"), 300);
        Text description = stage.addTextToCenterX(mySimResources.getString("Description"), 320);
        stage.addEllipsisIfNecessary(description, 120, 850);
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
                controller.saveConfig(fileNameField.getText(), authorField.getText(), descriptionField.getText(), view.getCellColors());
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
