package net.cps.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.cps.client.utils.ResourcesLoader;

import java.io.IOException;

/**
 * Main JavaFX GUI Application.
 **/
public class App extends Application {
    private static Scene scene;
    private static CPSClient client;
    
    public static void setScene (String fxml) throws IOException {
        scene.setRoot(ResourcesLoader.loadFXML(fxml));
    }
    
    public static void run (CPSClient client) {
        App.client = client;
        launch();
    }
    
    @Override
    public void start (Stage stage) throws IOException {
        client = CPSClient.getClient();
        client.openConnection();
        
        scene = new Scene(ResourcesLoader.loadFXML("Index.fxml"));
        stage.getIcons().add(ResourcesLoader.loadImage("parking-icon.png"));
        stage.setTitle("CityPark");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(400);
        //stage.setMaximized(true);
        stage.show();
        System.out.println("[CLIENT] application lunched successfully.");
    }
    
    @Override
    public void stop () throws Exception {
        super.stop();
        client.closeConnection();
        System.out.println("[CLIENT] application closed successfully.");
    }
}