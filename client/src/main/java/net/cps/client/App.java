package net.cps.client;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.cps.client.utils.ResourcesLoader;
import net.cps.common.entities.Employee;
import net.cps.common.entities.ParkingLot;

import java.io.IOException;

/**
 * Main JavaFX GUI Application.
 **/
public class App extends Application {
    private static Scene scene;
    
    public static CPSClient client = null;
    public static Object entity = null;
    
    public static void run (CPSClient client) {
        App.client = client;
        launch();
    }
    
    public static void setScene (String fxml) throws IOException {
        scene.setRoot(ResourcesLoader.loadFXML(fxml));
    }
    
    public static void setEntity (Object entity) {
        App.entity = entity;
    }
    
    
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
        super.stop();
        
        //if (entity Instanceof Employee) {
        //    client.sendRequestToServer(RequestType.POST, Entities.EMPLOYEE.getTableName(), entity, "logout");
        //} else if (entity Instanceof ParkingLot) {
        //    client.sendRequestToServer(RequestType.POST, Entities.PARKING_LOT.getTableName(), entity, "logout");
        //}
        client.closeConnection();
        System.out.println("[CLIENT] application closed successfully.");
    }
}