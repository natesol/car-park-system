package net.cps.client;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.cps.client.utils.ResourcesLoader;
import net.cps.client.utils.Theme;
import net.cps.common.utils.AbstractUser;
import net.cps.common.utils.Entities;
import net.cps.common.utils.RequestType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Main JavaFX GUI Application.
 **/
public class App extends Application {
    private static Scene scene;
    
    private static CPSClient client = null;
    private static Object entity = null;
    private static Theme theme = Theme.LIGHT;
    
    
    @Override
    public void start (@NotNull Stage stage) throws IOException {
        client.openConnection();
        
        scene = new Scene(ResourcesLoader.loadFXML("Index.fxml"));
        stage.getIcons().add(ResourcesLoader.loadImage("parking-icon.png"));
        stage.setTitle("CityPark");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(400);
        stage.show();
        System.out.println("[CLIENT] application lunched successfully.");
    }
    
    @Override
    public void stop () throws Exception {
        // Stop the `JavaFX` GUI application
        super.stop();
        
        // Logout the current user
        if (entity != null && !entity.getClass().equals(Entities.PARKING_LOT.getEntityClass())) {
            CPSClient.sendRequestToServer(RequestType.AUTH, "logout/email=" + ((AbstractUser) entity).getEmail(), "logout on application close.", entity, null);
        }
        
        // Close the connection with the server
        client.closeConnection();
        System.out.println("[CLIENT] application closed successfully.");
    }
    
    public static CPSClient getClient () {
        return client;
    }
    
    public static Object getEntity () {
        return entity;
    }
    
    public static void setEntity (Object entity) {
        App.entity = entity;
    }
    
    public static Theme getTheme () {
        return theme;
    }
    
    public static void setTheme (Theme theme) {
        App.theme = theme;
        
        if (scene != null) {
            AnchorPane root = (AnchorPane) scene.getRoot().lookup("#root");
            
            if (theme == Theme.DARK) {
                root.getStyleClass().remove("theme-light");
                root.getStyleClass().add("theme-dark");
        
                Platform.runLater(() -> {
                    MFXButton btn = (MFXButton) scene.getRoot().lookup("#toggleThemeBtn");
                    ((FontAwesomeIconView) btn.getGraphic()).setGlyphName("SUN_ALT");
                    btn.getTooltip().setText("Switch To Light Theme");
                });
            }
            else {
                root.getStyleClass().remove("theme-dark");
                root.getStyleClass().add("theme-light");
    
                Platform.runLater(() -> {
                    MFXButton btn = (MFXButton) scene.getRoot().lookup("#toggleThemeBtn");
                    ((FontAwesomeIconView) btn.getGraphic()).setGlyphName("MOON_ALT");
                    btn.getTooltip().setText("Switch To Dark Theme");
                });
            }
    
            ObservableList<String> stylesheets = scene.getStylesheets();
            String styles = stylesheets.size() == 0 ? "" : stylesheets.get(0);
            if (styles.isEmpty()) return;
            
            scene.getStylesheets().clear();
            scene.getStylesheets().addAll(styles);
        }
    }
    
    public static void updateAppTheme () {
        setTheme(scene.getRoot().lookup("#root").getStyleClass().contains("theme-dark") ? Theme.DARK : Theme.LIGHT);
    }
    
    public static void updatePageTheme () {
        setTheme(App.theme);
    }
    
    public static void toggleTheme () {
        setTheme(theme == Theme.LIGHT ? Theme.DARK : Theme.LIGHT);
    }
    
    public static void setPage (String fxml) throws IOException {
        scene.setRoot(ResourcesLoader.loadFXML(fxml));
        App.updatePageTheme();
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    public static void render (CPSClient client) {
        App.client = client;
        launch();
    }
}