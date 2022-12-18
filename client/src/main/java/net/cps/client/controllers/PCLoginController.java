package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.cell.MFXNotificationCell;
import io.github.palexdev.materialfx.enums.NotificationPos;
import io.github.palexdev.materialfx.enums.NotificationState;
import io.github.palexdev.materialfx.factories.InsetsFactory;
import io.github.palexdev.materialfx.font.FontResources;
import io.github.palexdev.materialfx.font.MFXFontIcon;
import io.github.palexdev.materialfx.notifications.MFXNotificationCenterSystem;
import io.github.palexdev.materialfx.notifications.MFXNotificationSystem;
import io.github.palexdev.materialfx.notifications.base.INotification;
import io.github.palexdev.materialfx.utils.RandomUtils;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.cps.client.App;

import java.io.IOException;


public class PCLoginController {
    
    @FXML
    private MFXTextField emailField;
    
    @FXML
    private MFXPasswordField passwordField;
    
    @FXML
    private MFXButton loginBtn;
    
    @FXML
    void loginBtnClickHandler(ActionEvent event) throws IOException {
        System.out.println("login !");
    
        if (emailField.getText().equals("test") && passwordField.getText().equals("test")) {
            App.setScene("pc-home");
        }
        else {
            MFXNotificationSystem.instance()
                    .setPosition(NotificationPos.TOP_CENTER)
                    .publish(createNotification());
        }
    }
    
    /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */
    /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */
    
    public PCLoginController(Stage stage) {
        Platform.runLater(() -> {
            MFXNotificationSystem.instance().initOwner(stage);
            MFXNotificationCenterSystem.instance().initOwner(stage);

            MFXNotificationCenter center = MFXNotificationCenterSystem.instance().getCenter();
            center.setCellFactory(notification -> new MFXNotificationCell(center, notification) {
                {
                    setPrefHeight(400);
                }
            });
        });
    }
    
    private INotification createNotification() {
        PCLoginController.ExampleNotification notification = new PCLoginController.ExampleNotification();
        notification.setContentText("aaa");
        return notification;
    }
    
    private static class ExampleNotification extends MFXSimpleNotification {
        private final StringProperty headerText = new SimpleStringProperty("Notification Header");
        private final StringProperty contentText = new SimpleStringProperty();
        
        public ExampleNotification() {
            MFXIconWrapper icon = new MFXIconWrapper(RandomUtils.randFromArray((new FontResources[]{
                    FontResources.BELL, FontResources.BELL_ALT,
                    FontResources.CALENDAR_ALT_DARK, FontResources.CALENDAR_ALT_SEMI_DARK,
                    FontResources.CHART_PIE, FontResources.CIRCLE, FontResources.CIRCLE_EMPTY,
                    FontResources.EXCLAMATION_CIRCLE, FontResources.EXCLAMATION_TRIANGLE,
                    FontResources.GEAR, FontResources.GOOGLE_DRIVE, FontResources.HOME,
                    FontResources.INFO_CIRCLE, FontResources.MUSIC,
                    FontResources.USER, FontResources.USERS, FontResources.VIDEO,
                    FontResources.X_CIRCLE
            })).getDescription(), 16, 32);
            Label headerLabel = new Label();
            headerLabel.textProperty().bind(headerText);
            MFXIconWrapper readIcon = new MFXIconWrapper("mfx-eye", 16, 32);
            ((MFXFontIcon) readIcon.getIcon()).descriptionProperty().bind(Bindings.createStringBinding(
                    () -> (getState() == NotificationState.READ) ? "mfx-eye" : "mfx-eye-slash",
                    notificationStateProperty()
            ));
            StackPane.setAlignment(readIcon, Pos.CENTER_RIGHT);
            StackPane placeHolder = new StackPane(readIcon);
            placeHolder.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(placeHolder, Priority.ALWAYS);
            HBox header = new HBox(10, icon, headerLabel, placeHolder);
            header.setAlignment(Pos.CENTER_LEFT);
            header.setPadding(InsetsFactory.of(5, 0, 5, 0));
            header.setMaxWidth(Double.MAX_VALUE);
            
            
            Label contentLabel = new Label();
            contentLabel.getStyleClass().add("content");
            contentLabel.textProperty().bind(contentText);
            contentLabel.setWrapText(true);
            contentLabel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            contentLabel.setAlignment(Pos.TOP_LEFT);
            
            MFXButton action1 = new MFXButton("Action 1");
            MFXButton action2 = new MFXButton("Action 2");
            HBox actionsBar = new HBox(15, action1, action2);
            actionsBar.getStyleClass().add("actions-bar");
            actionsBar.setAlignment(Pos.CENTER_RIGHT);
            actionsBar.setPadding(InsetsFactory.all(5));
            
            BorderPane container = new BorderPane();
            container.getStyleClass().add("notification");
            container.setTop(header);
            container.setCenter(contentLabel);
            container.setBottom(actionsBar);
            container.getStylesheets().add(App.class.getResource("styles/notification.css").toString());
            container.setMinHeight(200);
            container.setMaxWidth(400);
            
            setContent(container);
        }
        
        public String getHeaderText() {
            return headerText.get();
        }
        
        public StringProperty headerTextProperty() {
            return headerText;
        }
        
        public void setHeaderText(String headerText) {
            this.headerText.set(headerText);
        }
        
        public String getContentText() {
            return contentText.get();
        }
        
        public StringProperty contentTextProperty() {
            return contentText;
        }
        
        public void setContentText(String contentText) {
            this.contentText.set(contentText);
        }
    }
    /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */
    /* !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! */
}
