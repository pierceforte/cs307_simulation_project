package cellsociety.frontend;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
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

import java.util.List;

/**
 * This class is used to create a new stage for user input.
 *
 * This class eliminates many aspects of duplication by providing common methods for
 * building/modifying a stage.
 *
 * @author Pierce Forte
 */
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

    /**
     * Constructor to create an InputStage.
     * @param title The title for the stage
     * @param width The width of the stage
     * @param height The height of the stage
     * @param id The node id of the stage
     */
    public InputStage(String title, double width, double height, String id) {
        myStage = new Stage();
        myStage.setTitle(title);
        myPane = new Pane();
        myPane.setBackground(new Background(new BackgroundFill(BACKGROUND, CornerRadii.EMPTY, Insets.EMPTY)));
        this.width = width;
        this.height = height;
        myScene = new Scene(myPane, width, height);
        myStage.setScene(myScene);
        myPane.setId(id);
    }

    /**
     * Add text to the center x position of the screen.
     * @param textString The text's contents
     * @param yPos The y position of the text
     * @return The Text object that has been added
     */
    public Text addTextToCenterX(String textString, double yPos) {
        Text text = new Text(textString);
        text.setX(width/2 - text.getLayoutBounds().getWidth()/2);
        text.setY(yPos);
        wrapAndAlignText(text);
        addNodeToPane(text);
        return text;
    }

    /**
     * Add a text field to the center x position of the screen.
     * @param yPos The y position of the text field
     * @return The TextField object that has been added
     */
    public TextField addTextFieldToCenterX(double yPos) {
        TextField textField = new TextField();
        textField.setPrefWidth(DEFAULT_TEXT_FIELD_WIDTH);
        textField.setLayoutX(width/2 - textField.getPrefWidth()/2);
        textField.setLayoutY(yPos);
        addNodeToPane(textField);
        return textField;
    }

    /**
     * Add a text area to the center x position of the screen.
     * @param yPos The y position of the text area
     * @return The TextArea object that has been added
     */
    public TextArea addTextAreaToCenterX(double yPos) {
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setPrefWidth(DEFAULT_TEXT_AREA_WIDTH);
        textArea.setLayoutX(width/2 - textArea.getPrefWidth()/2);
        textArea.setLayoutY(yPos);
        addNodeToPane(textArea);
        return textArea;
    }

    /**
     * Add an error message to the center x position of the screen.
     * @param errorMessage The error message's contents
     * @param yPos The y position of the error message
     */
    public void addErrorMessageToCenterX(String errorMessage, double yPos) {
        myErrorMessage = addTextToCenterX(errorMessage, yPos);
        myErrorMessage.setId("errorMessage");
        wrapAndAlignText(myErrorMessage);
    }

    /**
     * Remove the error message from the screen.
     */
    public void removeErrorMessage() {
        if (myPane.getChildren().contains(myErrorMessage)) {
            removeNodeFromPane(myErrorMessage);
        }
    }

    /**
     * Wrap text and align it to the center.
     * @param text The text to be adjusted
     */
    public void wrapAndAlignText(Text text) {
        text.setTextAlignment(TextAlignment.CENTER);
        text.setWrappingWidth(width-50);
        if (text.getLayoutBounds().getWidth() >= width-50) {
            text.setX(width/2 - text.getWrappingWidth()/2);
        }
    }

    /**
     * Add an ellipsis to text if it is too long/high.
     * @param text The text to be adjusted
     * @param maxHeight The max height of the text
     * @param maxCharacters The max number of characters in the text
     */
    public void addEllipsisIfNecessary(Text text, double maxHeight, int maxCharacters) {
        if (maxCharacters > text.getText().length()) {
            return;
        }
        if (text.getLayoutBounds().getHeight() > maxHeight) {
            text.setText(text.getText().substring(0, maxCharacters) + "...");
        }
    }

    /**
     * Display the input stage and wait for user input.
     */
    public void showAndWait() {
        myStage.showAndWait();
    }

    /**
     * Add a node to the input stage's pane.
     * @param node The node to be added
     * @param <T> The specific class of the node
     */
    public <T extends Node> void addNodeToPane(T node) {
        myPane.getChildren().add(node);
    }

    /**
     * Add multiple nodes to the input stage's pane.
     * @param nodes The nodes to be added
     * @param <T> The specific class of the nodes
     */
    public <T extends Node> void addNodesToPane(List<T> nodes) {
        myPane.getChildren().addAll(nodes);
    }

    /**
     * Close the input stage.
     */
    public void close() {
        myStage.close();
    }

    /**
     * Set the height of the input stage.
     * @param height The new height of the input stage.
     */
    public void setHeight(double height) {
        this.height = height;
        myStage.setHeight(height);
    }

    private  <T extends Node> void removeNodeFromPane(T node) {
        myPane.getChildren().remove(node);
    }
}
