package net.cps.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Main JavaFX GUI Application.
**/
public class App extends Application {
    private static Scene scene;
    private static CPSClient client;
    
    public static void setScene(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    
    private static Parent loadFXML(String fileName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/" + fileName + ".fxml"));
        return fxmlLoader.load();
    }
    
    public static void run(CPSClient client) {
        App.client = client;
        launch();
    }
    
    @Override
    public void start(Stage stage) throws IOException {
        client = CPSClient.getClient();
        client.openConnection();
        scene = new Scene(loadFXML("index"));
        stage.getIcons().add(new Image(Objects.requireNonNull(App.class.getResourceAsStream("images/parking-icon.png"))));
        stage.setTitle("CityPark");
        stage.setScene(scene);
        stage.show();
        System.out.println("[CLIENT] application lunched successfully.");
    }
    
    @Override
    public void stop() throws Exception {
        super.stop();
        client.closeConnection();
        System.out.println("[CLIENT] application closed successfully.");
    }
}