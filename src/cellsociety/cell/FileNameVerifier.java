package cellsociety.cell;

import cellsociety.cell.config.ConfigSaver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileNameVerifier {
    public static final String NAME_IS_VALID = "VALID";
    public static final List<String> ILLEGAL_STRINGS =
            List.of("#", "%", "&", "{", "}", "\\", "<", ">", "*", "?", "/", " ", "$", "!", "\'", "\"", ":", ";", "@", "+", "`", "|", "=", ",", ".");

    private String myFileName;
    private Class myModelClass;

    public FileNameVerifier(String fileName, Class modelClass) {
        myFileName = fileName;
        myModelClass = modelClass;
    }

    public String verify() {
        if (myFileName.isEmpty()) {
            return "A file name must be entered.";
        }
        else if (fileExists()) {
            return "A file with the name \"" + myFileName + "\" already exists.";
        }
        else if (containsIllegalCharacters()) {
            return getIllegalCharactersMessage();
        }
        return NAME_IS_VALID;
    }

    private boolean fileExists() {
        File file = new File(ConfigSaver.PATH_TO_CONFIGS + "/" +
                ConfigSaver.CLASS_NAME_TO_SAVE_FOLDER.get(myModelClass) + "/" + myFileName + ConfigSaver.CSV_EXTENSION);
        System.out.println(file.toString());
        return file.isFile();
    }

    private boolean containsIllegalCharacters() {
        for (String str : ILLEGAL_STRINGS) {
            if (myFileName.contains(str)) {
                return true;
            }
        }
        return false;
    }

    private String getIllegalCharactersMessage() {
        List<String> illegalCharactersInName = new ArrayList<>();
        for (String illegalString : ILLEGAL_STRINGS) {
            if (myFileName.contains(illegalString)) {
                if (illegalString.equals(" ")) {
                    illegalString = "spaces";
                }
                illegalCharactersInName.add(illegalString);
            }
        }
        String message = "File name must not include the characters: ";

        for (String illegalString : illegalCharactersInName) {
            message += illegalString + ", ";
        }

        return message.substring(0, message.length() - 2);
    }

}
