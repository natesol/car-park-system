package net.cps.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.cps.client.events.MessageEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

/**
 * JavaFX GUI Application.
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
        System.out.println("[CLIENT] application lunched successfully.");
    }
    
    @Override
    public void start(Stage stage) throws IOException {
        // EventBus.getDefault().register(this);
        
        client = CPSClient.getClient();
        client.openConnection();
        scene = new Scene(loadFXML("start"));
        stage.setScene(scene);
        stage.show();
    }
    
    @Override
    public void stop() throws Exception {
        // EventBus.getDefault().unregister(this);
        super.stop();
    }
  
}