package net.cps.client.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Utility class which manages the access to this project's assets.
 * - Helps keeping the assets files structure organized.
 **/
public class ResourcesLoader {
    
    private ResourcesLoader() {
    }
    
    public static URL loadURL(String path) {
        return ResourcesLoader.class.getResource(path);
    }
    
    public static InputStream loadStream(String name) {
        return ResourcesLoader.class.getResourceAsStream(name);
    }
    
    private static Parent loadFXML(String fileName) throws IOException {
        return new FXMLLoader(loadURL("fxml/" + fileName + ".fxml")).load();
    }
}