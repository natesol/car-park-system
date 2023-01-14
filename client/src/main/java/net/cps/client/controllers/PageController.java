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
import org.jetbrains.annotations.NotNull;

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
    public VBox dialogCustomContent;
    @FXML
    public HBox dialogAction;
    public Dialog dialog = new Dialog();
    @FXML
    void closeDialog() { dialog.close(); }
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {}
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    /**
     * Inner utility class for dialog management.
     * <p>
     * {@code @method} open() opens the dialog.
     * {@code @method} close() closes the dialog.
     * {@code @method} setTitleText() sets the dialog title.
     * {@code @method} setBodyText() sets the dialog body content.
     * {@code @method} setCustomContent() add a custom content to the dialog (after the body content).
     * {@code @method} setActionButtons() add a custom action buttons to the dialog (as the last elements).
     * {@code @method} addStyleClass() add a CSS style class to the dialog control root.
     * {@code @method} removeStyleClass() remove a CSS style class from the dialog control root.
     * {@code @method} setWidth() set the dialog width (from a predefined list of values).
     * </p>
     */
    public class Dialog {
        public static enum Width {
            EXTRA_SMALL, SMALL, MEDIUM, LARGE, EXTRA_LARGE
        }
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
        public void setBodyText (String @NotNull ... content) {
            dialogContent.getChildren().clear();
            for (String str : content) {
                dialogContent.getChildren().add(new Text(str));
            }
        }
        public void setBodyText (@NotNull List<Node> content) {
            dialogContent.getChildren().clear();
            for (Node node : content) {
                dialogContent.getChildren().add(node);
            }
        }
        public void setCustomContent (Node @NotNull ... nodes) {
            dialogCustomContent.getChildren().clear();
            for (Node node : nodes) {
                dialogCustomContent.getChildren().add(node);
            }
        }
        public void setActionButtons (Node @NotNull ... buttons) {
            dialogAction.getChildren().clear();
            for (Node button : buttons) {
                dialogAction.getChildren().add(button);
            }
        }
        public void addStyleClass (String @NotNull ... classes) {
            for (String styleClass : classes) {
                dialogControl.getStyleClass().add(styleClass);
            }
        }
        public void removeStyleClass (String @NotNull ... classes) {
            for (String styleClass : classes) {
                dialogControl.getStyleClass().remove(styleClass);
            }
        }
        public void setWidth (@NotNull String width) {
            // check if width equals "xs", "sm", "md", "lg" or "xl"
            if (!(Objects.equals(width, "xs") || Objects.equals(width, "sm") || Objects.equals(width, "md") || Objects.equals(width, "lg") || Objects.equals(width, "xl"))) {
                throw new IllegalArgumentException("Dialog width must be one of the following: xs, sm, md, lg, xl");
            }
            
            dialogControl.getStyleClass().removeIf(styleClass -> styleClass.contains("w-"));
            dialogControl.getStyleClass().add("dialog-w-" + width);
        }
        public void setWidth (@NotNull Width width) {
            switch (width) {
                case EXTRA_SMALL -> setWidth("xs");
                case SMALL -> setWidth("sm");
                case MEDIUM -> setWidth("md");
                case LARGE -> setWidth("lg");
                case EXTRA_LARGE -> setWidth("xl");
            }
        }
    }
}
