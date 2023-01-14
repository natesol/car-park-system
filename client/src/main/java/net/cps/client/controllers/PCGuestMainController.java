package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import net.cps.client.App;
import net.cps.client.utils.ResourcesLoader;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class PCGuestMainController extends PageController {
    @FXML
    public MFXButton menuBtnHome;
    @FXML
    public MFXButton menuBtnReservations;
    @FXML
    public MFXButton menuBtnComplaints;
    @FXML
    public MFXButton menuBtnProfile;
    @FXML
    public MFXButton menuBtnSignUp;
    @FXML
    public Pane subPageWrapper;
    @FXML
    public HBox subPageBody;
    @FXML
    public VBox dashboardLeft;
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        Platform.runLater(() -> {
            try {
                setSubPage("PCGuestMainHome.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            
            activateMenuBtn(menuBtnHome);
        });
    }
    
    
    /* ----- Event Handlers Methods ----------------------- */
    
    public void menuBtnHomeClickHandler (MouseEvent mouseEvent) throws IOException {
        if (menuBtnHome.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                setSubPage("PCGuestMainHome.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            activateMenuBtn(menuBtnHome);
        });
    }
    
    public void menuBtnReservationsClickHandler (MouseEvent mouseEvent) throws IOException {
        if (menuBtnReservations.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                setSubPage("PCGuestMainReservations.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            activateMenuBtn(menuBtnReservations);
        });
    }
    
    public void menuBtnComplaintsClickHandler (MouseEvent mouseEvent) throws IOException {
        if (menuBtnComplaints.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                setSubPage("PCGuestMainComplaints.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            activateMenuBtn(menuBtnComplaints);
        });
    }
    
    public void menuBtnProfileClickHandler (MouseEvent mouseEvent) throws IOException {
        if (menuBtnProfile.getStyleClass().contains("active")) return;
        
        Platform.runLater(() -> {
            try {
                setSubPage("PCGuestMainProfile.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            activateMenuBtn(menuBtnProfile);
        });
    }
    
    public void menuBtnSignUpClickHandler (MouseEvent mouseEvent) throws IOException {
        Platform.runLater(() -> {
            try {
                App.setPage("PCSignUp.fxml");
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    
    /* ----- Utility Methods ----------------------- */
    
    public void setSubPage (String fxml) throws IOException {
        Platform.runLater(() -> {
            try {
                subPageWrapper.getChildren().clear();
                subPageWrapper.getChildren().add(ResourcesLoader.loadFXML(fxml));
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    
    public void setSubPage (Pane pane) {
        Platform.runLater(() -> {
            subPageWrapper.getChildren().clear();
            subPageWrapper.getChildren().add(pane);
        });
    }
    
    public void activateMenuBtn (@NotNull MFXButton btn) {
        Platform.runLater(() -> {
            dashboardLeft.lookupAll(".dashboard-menu-button.active").forEach(node -> node.getStyleClass().remove("active"));
            btn.getStyleClass().add("active");
        });
    }
}
