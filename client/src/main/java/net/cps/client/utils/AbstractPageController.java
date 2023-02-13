package net.cps.client.utils;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.controls.MFXProgressSpinner;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import net.cps.client.App;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;


public abstract class AbstractPageController implements Initializable {
    /* Page Main Controllers */
    @FXML
    public AnchorPane root;
    @FXML
    public MFXScrollPane rootScroll;
    @FXML
    public VBox body;
    
    /* Theme Toggle Button */
    @FXML
    public MFXButton toggleThemeBtn;
    @FXML
    public void toggleThemeBtnClickHandler (ActionEvent actionEvent) {
        Platform.runLater(App::toggleTheme);
    }
    
    /* Dialogs */
    @FXML
    public VBox dialogRoot;
    @FXML
    public MFXGenericDialog dialogControl;
    @FXML
    public VBox dialogContent;
    @FXML
    public VBox dialogCustomContent;
    @FXML
    public HBox dialogAction;
    @FXML
    void closeDialog() { dialog.close(); }
    public Dialog dialog = new Dialog();
    
    /* Loader */
    @FXML
    public VBox loaderRoot;
    @FXML
    public MFXProgressSpinner loaderSpinner;
    public Loader loader = new Loader();
    
    /* Utility Constants */
    public static final Integer DEFAULT_BLUR_RADIUS = 8;
    public static final Integer DEFAULT_TRANSITION_DURATION = 120;
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {}
    
    
    /* ----- Utility Classes ---------------------------------------- */
    
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
     **/
    public class Dialog {
        public static enum Width {
            EXTRA_SMALL, SMALL, MEDIUM, LARGE, EXTRA_LARGE
        }
        public void open () {
            dialogRoot.setDisable(false);
            dialogRoot.setVisible(true);
            body.setEffect(new GaussianBlur(DEFAULT_BLUR_RADIUS));
    
            FadeTransition dialogFadeIn = new FadeTransition();
            dialogFadeIn.setDuration(Duration.millis(DEFAULT_TRANSITION_DURATION));
            dialogFadeIn.setInterpolator(Interpolator.EASE_BOTH);
            dialogFadeIn.setFromValue(0);
            dialogFadeIn.setToValue(1);
            dialogFadeIn.setNode(dialogRoot);
            dialogFadeIn.play();
        }
        public void clear () {
            dialogContent.getChildren().clear();
            dialogCustomContent.getChildren().clear();
            dialogAction.getChildren().clear();
            dialogControl.getStyleClass().clear();
            dialogControl.getStyleClass().add("dialog");
            dialogControl.getStyleClass().add("dialog-w-xs");
        }
        public void close () {
            FadeTransition dialogFadeOut = new FadeTransition();
            dialogFadeOut.setDuration(Duration.millis(DEFAULT_TRANSITION_DURATION));
            dialogFadeOut.setInterpolator(Interpolator.EASE_BOTH);
            dialogFadeOut.setFromValue(1);
            dialogFadeOut.setToValue(0);
            dialogFadeOut.setNode(dialogRoot);
            dialogFadeOut.setOnFinished(e -> {
                dialogRoot.setVisible(false);
                dialogRoot.setDisable(true);
                body.setEffect(null);
                
                clear();
            });
            dialogFadeOut.play();
        }
        public void setTitleText (String title) {
            dialogControl.setHeaderText(title);
        }
        public void setBodyText (String @NotNull ... content) {
            dialogContent.getChildren().clear();
            VBox contentBox = new VBox();
            for (String str : content) {
                contentBox.getChildren().add(new TextFlow(new Text(str)));
            }
            dialogContent.getChildren().add(contentBox);
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
    
    
    /**
     * Inner utility class for loader management.
     * <p>
     * {@code @method} show() shows the loader.
     * {@code @method} hide() hides the loader.
     * </p>
     **/
    public class Loader {
        public void show () {
            loaderRoot.setVisible(true);
            loaderRoot.setDisable(false);
            body.setEffect(new GaussianBlur(DEFAULT_BLUR_RADIUS));
    
            FadeTransition loaderFadeIn = new FadeTransition();
            loaderFadeIn.setDuration(Duration.millis(DEFAULT_TRANSITION_DURATION));
            loaderFadeIn.setInterpolator(Interpolator.EASE_BOTH);
            loaderFadeIn.setFromValue(0);
            loaderFadeIn.setToValue(1);
            loaderFadeIn.setNode(loaderRoot);
            loaderFadeIn.play();
        }
    
        public void hide () {
            FadeTransition loaderFadeOut = new FadeTransition();
            loaderFadeOut.setDuration(Duration.millis(DEFAULT_TRANSITION_DURATION));
            loaderFadeOut.setInterpolator(Interpolator.EASE_BOTH);
            loaderFadeOut.setFromValue(1);
            loaderFadeOut.setToValue(0);
            loaderFadeOut.setNode(loaderRoot);
            loaderFadeOut.setOnFinished(e -> {
                loaderRoot.setVisible(false);
                loaderRoot.setDisable(true);
                body.setEffect(null);
            });
            loaderFadeOut.play();
        }
    }
}
