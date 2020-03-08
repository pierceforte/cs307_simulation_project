package cellsociety.frontend;

import cellsociety.cell.Cell;
import cellsociety.cell.CellView;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ColorControlsGUI {
    public static final double MENU_ICON_SIZE = 25;
    private ResourceBundle myDefaultResources;
    private ResourceBundle mySimResources;
    private HashMap<String, String> cellFills;
    // helps with runtime (we only have to load in an image once with this)
    private HashMap<String, Image> stateToImageMap = new HashMap<>();
    private Stage setColorStage = new Stage();
    private Map<String, MenuButton> stateToMenuMap = new HashMap<>();

    public ColorControlsGUI(ResourceBundle defaultResources, ResourceBundle simResources, Map<String, Class> orderedCellTypesMap){
        myDefaultResources = defaultResources;
        mySimResources = simResources;
        initializeCellFills(orderedCellTypesMap);
    }

    //TODO: maybe eliminate the dependency on cellTypesMap and find a way to use properties file.
    //  Not that hard, but might be even more brittle (have to look through all the keys and check
    //  for ones with a certain prefix (currently "State"))
    public HBox createColorControls(Map<String, Class> cellTypesMap){
        HBox hbox = new HBox(5);
        for (String state : cellTypesMap.keySet()) {
            hbox.getChildren().add(buildMenuButton(state));
        }
        return hbox;
    }

    public void setCellFill(Cell cell, CellView cellView) {
        String state = cell.getState();
        String fill = cellFills.get(state);
        Color color;
        try {
            if (isFillAnImage(fill)) {
                Image image = getImageAndAddToMap(state, fill);
                //System.out.println(image.getUrl());
                cellView.setFill(image);
                return;
            }
            else {
                color = Color.web(fill);
            }
        } catch (Exception e) {
            System.out.println("here");
            color = getRandomColor();
        }
        cellView.setFill(color);
    }

    public HashMap<String, String> getCellFills() {
        return cellFills;
    }

    private MenuButton buildMenuButton(String state) {
        String cellType = mySimResources.getString("State" + state + "Title");
        MenuButton menu = new MenuButton(cellType);
        stateToMenuMap.put(state, menu);
        setMenuIconFill(state);
        ColorPicker picker = createColorPicker(state);
        return addEventHandlersToMenuButton(menu, picker, state);
    }

    private MenuButton addEventHandlersToMenuButton(MenuButton menu, ColorPicker picker, String state) {
        MenuItem setColor = new MenuItem(myDefaultResources.getString("SetColor"));
        setColor.setOnAction(event -> {
            handleSetColorRequest(picker);
        });

        MenuItem setImage = new MenuItem(myDefaultResources.getString("SetImage"));
        setImage.setOnAction(event -> {
            handleSetImageRequest(state);
        });

        menu.getItems().addAll(setColor, setImage);
        return menu;
    }

    private Color getInitColorFromProperties(String state) {
        Color color;
        try {
            color = Color.web(cellFills.get(state));
        } catch (Exception e) {
            color = Color.BLACK; // Set to black if not defined
        }
        return color;
    }

    private void initializeCellFills(Map<String, Class> orderedCellTypesMap) {
        cellFills = new HashMap<>();
        for (String state : orderedCellTypesMap.keySet()) {
            String fill = mySimResources.getString("State" + state);
            try {
                if (isFillAnImage(fill)) {
                    getImageAndAddToMap(state, fill);
                }
                else {
                    convertFillPropertyToColor(fill);
                }
            } catch (Exception e) {
                fill = getRandomColor().toString();
            }
            cellFills.put(state, fill);
        }
    }

    private void setMenuIconFill(String state) {
        MenuButton menu = stateToMenuMap.get(state);
        String fill = cellFills.get(state);
        Color color;
        try {
            if (isFillAnImage(fill)) {
                Image image = getImageAndAddToMap(state, fill);
                ImageView icon = new ImageView(image);
                icon.setFitWidth(MENU_ICON_SIZE);
                icon.setFitHeight(MENU_ICON_SIZE);
                menu.setGraphic(icon);
                return;
            }
            else {
                color = Color.web(fill);
            }
        } catch (Exception e){
            color = getRandomColor();
        }
        Rectangle icon = new Rectangle(MENU_ICON_SIZE, MENU_ICON_SIZE);
        icon.setFill(color);
        menu.setGraphic(icon);
    }

    private boolean isFillAnImage(String fill) {
        return fill.substring(0,6).equals("image:");
    }

    private Image convertFillPropertyToImage(String fill) {
        return new Image(fill.substring(6));
    }

    private Color convertFillPropertyToColor(String fill) {
        return Color.web(fill);
    }

    private Image getImageAndAddToMap(String state, String fill) {
        if (stateToImageMap.get(state) == null) {
            stateToImageMap.put(state, convertFillPropertyToImage(fill));
        }
        return stateToImageMap.get(state);
    }

    private ColorPicker createColorPicker(String state) {
        Color color = getInitColorFromProperties(state);
        ColorPicker picker = new ColorPicker(color);
        picker.setPrefWidth(150);
        picker.setPrefHeight(35);
        picker.setOnAction(event -> {
            handleColorPickerAction(picker, state);
        });
        return picker;
    }

    private void handleColorPickerAction(ColorPicker picker, String state) {
        setColorStage.close();
        cellFills.put(state, picker.getValue().toString());
        setMenuIconFill(state);
    }

    private void handleSetImageRequest(String state) {
        FileChooser imageChooser = new FileChooser();
        String fileTypes = myDefaultResources.getString("ImageFileTypes");
        List<String> validExtensionStrings = List.of("*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp");
        FileChooser.ExtensionFilter fileExtensions = new FileChooser.ExtensionFilter(fileTypes, validExtensionStrings);
        imageChooser.getExtensionFilters().add(fileExtensions);

        File imageFile = imageChooser.showOpenDialog(new Stage());
        if (imageFile == null) {
            // TODO: handle error
            // for now, just don't change the image –– we might want to throw an error message, though
            return;
        }
        String imagePath = imageFile.toURI().toString();
        stateToImageMap.put(state, new Image(imagePath));
        cellFills.put(state, "image:"+imagePath);
        setMenuIconFill(state);
    }

    private void handleSetColorRequest(ColorPicker picker) {
        setColorStage = new Stage();
        Pane pane = new Pane();
        Scene scene = new Scene(pane, picker.getPrefWidth(), picker.getPrefHeight());
        setColorStage.setScene(scene);
        pane.getChildren().add(picker);
        setColorStage.show();
    }

    private Color getRandomColor() {
        return Color.color(Math.random(), Math.random(), Math.random());
    }

}
