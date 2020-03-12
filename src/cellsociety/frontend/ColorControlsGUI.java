package cellsociety.frontend;

import cellsociety.cell.Cell;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;
import java.util.function.BiConsumer;

public class ColorControlsGUI {
    public static final double MENU_ICON_SIZE = 25;
    public static final String IMAGE_PROPERTY_ID = "image:";

    private ResourceBundle myDefaultResources;
    private ResourceBundle mySimResources;
    private HashMap<String, String> cellFills;
    private HashMap<String, Color> cellBorders;
    // helps with runtime (we only have to load in an image once with this)
    private HashMap<String, Image> stateToImageMap = new HashMap<>();
    private Stage setColorStage = new Stage();
    private Map<String, MenuButton> stateToMenuMap = new HashMap<>();

    public ColorControlsGUI(ResourceBundle defaultResources, ResourceBundle simResources, Map<String, Class> orderedCellStatesMap){
        myDefaultResources = defaultResources;
        mySimResources = simResources;
        initializeCellStyleMaps(orderedCellStatesMap);
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

    public void setCellFill(Cell cell) {
        String state = cell.getState();
        String fill = cellFills.get(state);
        Color color;
        try {
            if (isFillAnImage(fill)) {
                Image image = getImageAndAddToMap(state, fill);
                cell.setFill(image);
                return;
            }
            else {
                color = Color.web(fill);
            }
        } catch (Exception e) {
            // TODO: handle exception properly
            e.printStackTrace();
            //logError(e);
            color = getRandomColor();
        }
        cell.setFill(color);
    }

    public HashMap<String, String> getCellFills() {
        return cellFills;
    }

    protected void setCellBorder(Cell cell) {
        String state = cell.getState();
        Color borderColor = cellBorders.get(state);
        cell.setStroke(borderColor);
    }

    private MenuButton buildMenuButton(String state) {
        String cellType;
        try {
            cellType = mySimResources.getString("State" + state + "Title");
        } catch (MissingResourceException e) {
            // TODO: handle exception properly
            e.printStackTrace();
            cellType = "Invalid Title: " + state;
        }
        MenuButton menu = new MenuButton(cellType);
        stateToMenuMap.put(state, menu);
        setMenuIconFill(state);
        setMenuIconBorder(state);
        ColorPicker colorPicker = createColorPicker(state, getInitColor(state), (p, c) -> handleColorPickerAction(p, c));
        ColorPicker borderPicker = createColorPicker(state, getInitBorder(state), (p, c) -> handleBorderPickerAction(p, c));
        return addEventHandlersToMenuButton(menu, colorPicker, borderPicker, state);
    }

    private MenuButton addEventHandlersToMenuButton(MenuButton menu, ColorPicker colorPicker, ColorPicker borderPicker, String state) {
        MenuItem setColor = new MenuItem(myDefaultResources.getString("SetColor"));
        setColor.setOnAction(event -> {
            handleSetColorRequest(colorPicker);
        });

        MenuItem setImage = new MenuItem(myDefaultResources.getString("SetImage"));
        setImage.setOnAction(event -> {
            handleSetImageRequest(state);
        });

        MenuItem setBorder = new MenuItem(myDefaultResources.getString("SetBorder"));
        setBorder.setOnAction(event -> {
            handleSetColorRequest(borderPicker);
        });

        menu.getItems().addAll(setColor, setImage, setBorder);
        return menu;
    }

    private Color getInitColor(String state) {
        Color color;
        try {
            color = Color.web(cellFills.get(state));
        } catch (Exception e) {
            // TODO: handle exception properly
            e.printStackTrace();
            color = Color.BLACK; // Set to black if not defined
        }
        return color;
    }

    private Color getInitBorder(String state) {
        return cellBorders.get(state);
    }

    private void initializeCellStyleMaps(Map<String, Class> orderedCellStatesMap) {
        cellFills = new HashMap<>();
        cellBorders = new HashMap<>();
        for (String state : orderedCellStatesMap.keySet()) {
            initializeCellFills(state);
            initializeCellBorders(state);
        }
    }

    private void initializeCellFills(String state) {
        String fill;
        try {
            fill = mySimResources.getString("State" + state);
            if (isFillAnImage(fill)) {
                getImageAndAddToMap(state, fill);
            }
            else {
                convertColorPropertyToColor(fill);
            }
        } catch (Exception e) {
            // TODO: handle exception properly
            e.printStackTrace();
            // log & display error
            fill = getRandomColor().toString();
        }
        cellFills.put(state, fill);
    }

    private void initializeCellBorders(String state) {
        Color borderColor;
        try {
            String borderColorString = mySimResources.getString("State" + state + "Border");
            borderColor = convertColorPropertyToColor(borderColorString);
        } catch (Exception e) {
            // TODO: handle exception properly
            e.printStackTrace();
            // log & display error
            borderColor = Color.BLACK;
        }
        cellBorders.put(state, borderColor);
    }

    private void setMenuIconFill(String state) {
        MenuButton menu = stateToMenuMap.get(state);
        String fill = cellFills.get(state);
        Color color;
        Rectangle icon = new Rectangle(MENU_ICON_SIZE, MENU_ICON_SIZE);
        try {
            if (isFillAnImage(fill)) {
                Image image = getImageAndAddToMap(state, fill);
                ImagePattern imagePattern = new ImagePattern(image);
                icon.setFill(imagePattern);
                menu.setGraphic(icon);
                return;
            }
            else {
                color = Color.web(fill);
            }
        } catch (Exception e){
            // TODO: handle exception properly
            e.printStackTrace();
            color = getRandomColor();
        }
        icon.setFill(color);
        menu.setGraphic(icon);
    }

    private void setMenuIconBorder(String state) {
        MenuButton menu = stateToMenuMap.get(state);
        Rectangle icon = (Rectangle) menu.getGraphic();
        icon.setStroke(cellBorders.get(state));
    }

    private boolean isFillAnImage(String fill) {
        return fill.substring(0,IMAGE_PROPERTY_ID.length()).equals(IMAGE_PROPERTY_ID);
    }

    private Image convertFillPropertyToImage(String fill) {
        return new Image(fill.substring(IMAGE_PROPERTY_ID.length()));
    }

    private Color convertColorPropertyToColor(String fill) {
        return Color.web(fill);
    }

    private Image getImageAndAddToMap(String state, String fill) {
        if (stateToImageMap.get(state) == null) {
            stateToImageMap.put(state, convertFillPropertyToImage(fill));
        }
        return stateToImageMap.get(state);
    }

    private ColorPicker createColorPicker(String state, Color initColor, BiConsumer<ColorPicker, String> action) {
        ColorPicker picker = new ColorPicker(initColor);
        picker.setPrefWidth(150);
        picker.setPrefHeight(35);
        picker.setOnAction(event -> action.accept(picker, state));
        return picker;
    }

    private void handleColorPickerAction(ColorPicker picker, String state) {
        setColorStage.close();
        cellFills.put(state, picker.getValue().toString());
        setMenuIconFill(state);
        setMenuIconBorder(state);
    }

    private void handleBorderPickerAction(ColorPicker picker, String state) {
        setColorStage.close();
        cellBorders.put(state, picker.getValue());
        setMenuIconBorder(state);
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
        cellFills.put(state, IMAGE_PROPERTY_ID+imagePath);
        setMenuIconFill(state);
        setMenuIconBorder(state);
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
