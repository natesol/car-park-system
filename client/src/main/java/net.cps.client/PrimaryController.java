package net.cps.client;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javafx.scene.control.cell.PropertyValueFactory;
import net.cps.entities.Message;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;


import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.xml.crypto.Data;

public class PrimaryController {
    
    // Inject the UI elements defined in the FXML file
    @FXML
    private Button retrieveDataButton;
    @FXML
    private TableView<Data> dataTable;
    @FXML
    private TableColumn<Data, Integer> idColumn;
    @FXML
    private TableColumn<Data, String> nameColumn;
    
    // Set up OCSF client-server communication
    private CPSClient client;
    
    // Set up Hibernate configuration and session factory
    private Configuration config;
    private SessionFactory factory;
    
//    @FXML
//    public void initialize() {
//        // Initialize OCSF client and Hibernate configuration and session factory
//        client = CPSClient.getClient();
//        config = new Configuration().configure();
//        factory = config.buildSessionFactory();
//
//        // Set up the columns of the TableView component
//        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
//        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
//    }
    
    // This method will be called when the retrieveDataButton is clicked
//    @FXML
//    public void retrieveData() throws IOException {
//        // Connect to the server and send a request to retrieve data from the database
//        client.openConnection();
//        client.sendToServer("RETRIEVE_DATA");
//
//        // Wait for the response from the server
//        String response = (String) client.getResponse();
//
//        // If the response is "SUCCESS", retrieve the data from the database and populate the TableView
//        if (response.equals("SUCCESS")) {
//            // Open a session and begin a transaction
//            Session session = factory.openSession();
//            Transaction transaction = session.beginTransaction();
//
//            // Execute a HQL SELECT statement to retrieve the data from the database
//            List<Data> data = session.createQuery("FROM Data", Data.class).list();
//
//            // Set the items of the TableView to the list of data objects
//            dataTable.setItems(FXCollections.observableArrayList(data));
//
//            // Commit the transaction and close the session
//            transaction.commit();
//            session.close();
//        }
//    }
//
//
    
    @FXML
    private TextField MessageTF;

    @FXML
    private Button SendBtn;

    @FXML
    private TextField DataFromServerTF;

    private int msgId;

    @FXML
    void sendMessage(ActionEvent event) {
        try {
            Message message = new Message(msgId++, MessageTF.getText());
            MessageTF.clear();
            CPSClient.getClient().sendToServer(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Subscribe
    public void setDataFromServerTF(MessageEvent event) {
        DataFromServerTF.setText(event.getMessage().getMessage());
    }

    @Subscribe
    public void getStarterData(NewSubscriberEvent event) {
        try {
            Message message = new Message(msgId, "send Submitters IDs");
            CPSClient.getClient().sendToServer(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Subscribe
    public void errorEvent(ErrorEvent event) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    String.format("Message:\nId: %d\nData: %s\nTimestamp: %s\n",
                            event.getMessage().getId(),
                            event.getMessage().getMessage(),
                            event.getMessage().getTimeStamp().format(dtf))
            );
            alert.setTitle("Error!");
            alert.setHeaderText("Error:");
            alert.show();
        });
    }

    @FXML
    void initialize() {
        EventBus.getDefault().register(this);
        MessageTF.clear();
        DataFromServerTF.clear();
        msgId = 0;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
        try {
            Message message = new Message(msgId, "add client");
            CPSClient.getClient().sendToServer(message);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
