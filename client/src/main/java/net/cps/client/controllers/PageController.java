package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;


public class PageController implements Initializable {
    @FXML
    public AnchorPane root;
    @FXML
    public MFXScrollPane rootScroll;
    @FXML
    public VBox body;
    @FXML
    public VBox dialogRoot;
    @FXML
    public MFXGenericDialog dialogControl;
    @FXML
    public TextFlow dialogContent;
    @FXML
    public HBox dialogAction;
    public Dialog dialog = new Dialog();
    @FXML
    void closeDialog() { dialog.close(); }
    
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {}
    
    public class Dialog {
        public void open () {
            dialogRoot.setVisible(true);
            dialogRoot.setDisable(false);
            body.setEffect(new GaussianBlur(8));
        }
        public void close () {
            dialogRoot.setVisible(false);
            dialogRoot.setDisable(true);
            body.setEffect(null);
            
            dialogAction.getChildren().clear();
            dialogContent.getChildren().clear();
            dialogControl.getStyleClass().clear();
            dialogControl.getStyleClass().add("dialog");
            dialogControl.getStyleClass().add("dialog-w-xs");
        }
        public void setTitleText (String title) {
            dialogControl.setHeaderText(title);
        }
        public void setBodyText (String content) {
            dialogContent.getChildren().clear();
            dialogContent.getChildren().add(new Text(content));
        }
        public void setBodyText (List<Node> content) {
            dialogContent.getChildren().clear();
            for (Node node : content) {
                dialogContent.getChildren().add(node);
            }
        }
        public void setActionButtons (Node... buttons) {
            dialogAction.getChildren().clear();
            for (Node button : buttons) {
                dialogAction.getChildren().add(button);
            }
        }
        public void addStyleClass (String... classes) {
            for (String styleClass : classes) {
                dialogControl.getStyleClass().add(styleClass);
            }
        }
        public void removeStyleClass (String... classes) {
            for (String styleClass : classes) {
                dialogControl.getStyleClass().remove(styleClass);
            }
        }
        public void setWidth (String width) {
            // check if width equals "xs", "sm", "md", "lg" or "xl"
            if (!(Objects.equals(width, "xs") || Objects.equals(width, "sm") || Objects.equals(width, "md") || Objects.equals(width, "lg") || Objects.equals(width, "xl"))) {
                throw new IllegalArgumentException("Dialog width must be one of the following: xs, sm, md, lg, xl");
            }
            
            dialogControl.getStyleClass().removeIf(styleClass -> styleClass.contains("w-"));
            dialogControl.getStyleClass().add("dialog-w-" + width);
        }
    }
}
