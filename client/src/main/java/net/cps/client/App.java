package net.cps.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.cps.client.utils.ResourcesLoader;
import net.cps.common.utils.AbstractUser;
import net.cps.common.utils.Entities;
import net.cps.common.utils.RequestType;

import java.io.IOException;

/**
 * Main JavaFX GUI Application.
 **/
public class App extends Application {
    private static Scene scene;
    
    private static CPSClient client = null;
    private static Object entity = null;
    
    
    @Override
    public void start (Stage stage) throws IOException {
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
    
    public static void setPage (String fxml) throws IOException {
        scene.setRoot(ResourcesLoader.loadFXML(fxml));
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    public static void render (CPSClient client) {
        App.client = client;
        launch();
    }
    
}