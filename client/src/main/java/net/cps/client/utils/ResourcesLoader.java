package net.cps.client.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import net.cps.client.App;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;

/**
 * Utility class that manage the access to this project's assets.
 * - Helps keeping the assets files structure organized.
 **/
public class ResourcesLoader {
    
    private ResourcesLoader () {}
    
    public static URL loadURL (String path) {
        return ResourcesLoader.class.getResource(path);
    }
    
    public static InputStream loadStream (String name) {
        return ResourcesLoader.class.getResourceAsStream(name);
    }
    
    public static Parent loadFXML (String fileName) throws IOException {
        try {
            return FXMLLoader.load(loadURL("../fxml/" + fileName ));
        }
        catch (Throwable e) {
            System.out.println("[CLIENT] error while loading fxml file '" + fileName + "'.");
            throw new IOException("Failed to load FXML file: " + fileName , e);
        }
    }
    
    public static String loadCSS (String fileName) {
        return loadURL("../styles/" + fileName).toExternalForm();
    }
    
    public static void loadCSSToScene (Scene scene, String fileName) {
        String css = loadURL("../styles/" + fileName + ".css").toExternalForm();
        scene.getStylesheets().add(css);
    }
    
    public static void loadFont (String fontFileName) {
        Font.loadFont(loadURL("../fonts/" + fontFileName).toExternalForm(), 10);
    }
    
    public static Image loadImage (String imageFileName) {
        return new Image(loadStream("../images/" + imageFileName));
    }
}