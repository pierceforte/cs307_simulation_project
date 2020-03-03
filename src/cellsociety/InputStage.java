package cellsociety;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class InputStage {
    public static final Color BACKGROUND = Color.LIGHTBLUE;
    public static final double DEFAULT_WIDTH = 300;
    public static final double DEFAULT_HEIGHT = 650;
    public static final double DEFAULT_TEXT_FIELD_WIDTH = 100;
    public static final double DEFAULT_TEXT_AREA_WIDTH = 200;

    private Stage myStage;
    private Pane myPane;
    private Scene myScene;
    private double width;
    private double height;
    private Text myErrorMessage;

    public InputStage(String title, double width, double height) {
        myStage = new Stage();
        myStage.setTitle(title);
        myPane = new Pane();
        myPane.setBackground(new Background(new BackgroundFill(BACKGROUND, CornerRadii.EMPTY, Insets.EMPTY)));
        this.width = width;
        this.height = height;
        myScene = new Scene(myPane, width, height);
        myStage.setScene(myScene);
    }

    public Text addTextToCenterX(String textString, double yPos) {
        Text text = new Text(textString);
        text.setX(width/2 - text.getLayoutBounds().getWidth()/2);
        text.setY(yPos);
        addNodeToPane(text);
        return text;
    }

    public TextField addTextFieldToCenterX(double yPos) {
        TextField textField = new TextField();
        textField.setPrefWidth(DEFAULT_TEXT_FIELD_WIDTH);
        textField.setLayoutX(width/2 - textField.getPrefWidth()/2);
        textField.setLayoutY(yPos);
        addNodeToPane(textField);
        return textField;
    }

    public TextArea addTextAreaToCenterX(double yPos) {
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setPrefWidth(DEFAULT_TEXT_AREA_WIDTH);
        textArea.setLayoutX(width/2 - textArea.getPrefWidth()/2);
        textArea.setLayoutY(yPos);
        addNodeToPane(textArea);
        return textArea;
    }

    public void addErrorMessageToCenterX(String errorMessage, double yPos) {
        myErrorMessage = addTextToCenterX(errorMessage, yPos);
        myErrorMessage.setTextAlignment(TextAlignment.CENTER);
        myErrorMessage.setWrappingWidth(200);

        if (myErrorMessage.getLayoutBounds().getWidth() >= 200) {
            myErrorMessage.setX(width/2 - myErrorMessage.getWrappingWidth()/2);
        }
    }

    public void removeErrorMessage() {
        if (myPane.getChildren().contains(myErrorMessage)) {
            removeNodeFromPane(myErrorMessage);
        }
    }

    public void showAndWait() {
        myStage.showAndWait();
    }

    public <T extends Node> void addNodeToPane(T node) {
        myPane.getChildren().add(node);
    }

    public void close() {
        myStage.close();
    }

    private  <T extends Node> void removeNodeFromPane(T node) {
        myPane.getChildren().remove(node);
    }
}
