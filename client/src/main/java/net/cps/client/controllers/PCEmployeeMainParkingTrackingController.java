package net.cps.client.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.application.Platform;
import net.cps.common.entities.ParkingLot;
import java.io.File;
import java.io.IOException;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import java.util.Arrays;
import java.lang.reflect.Field;
import java.util.List;
import javafx.scene.Node;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;
public class PCEmployeeMainParkingTrackingController {

    @FXML
    private VBox C0;

    @FXML
    private VBox C1;

    @FXML
    private VBox C10;

    @FXML
    private VBox C11;

    @FXML
    private VBox C12;

    @FXML
    private VBox C13;

    @FXML
    private VBox C14;

    @FXML
    private VBox C15;

    @FXML
    private VBox C16;

    @FXML
    private VBox C17;

    @FXML
    private VBox C18;

    @FXML
    private VBox C19;

    @FXML
    private VBox C2;

    @FXML
    private VBox C20;

    @FXML
    private VBox C21;

    @FXML
    private VBox C22;

    @FXML
    private VBox C23;

    @FXML
    private VBox C3;

    @FXML
    private VBox C4;

    @FXML
    private VBox C5;

    @FXML
    private VBox C6;

    @FXML
    private VBox C7;

    @FXML
    private VBox C8;

    @FXML
    private VBox C9;

    @FXML
    private MFXButton dialogPDFBtn;

    @FXML
    private MFXFilterComboBox floorComboBox;

    @FXML
    private GridPane gridPaneRoot;

    @FXML
    private MFXFilterComboBox lotComboBox;

    @FXML
    private HBox subPageBody;

    @FXML
    private Text subPageTitle;

    @FXML
    void dialogPDFBtnClickHandler(ActionEvent event) {
        Platform.runLater(() -> {

            WritableImage image = gridPaneRoot.snapshot(new SnapshotParameters(), null);
            File file = new File("chart.png");
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Image");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File pdffile = fileChooser.showSaveDialog(gridPaneRoot.getScene().getWindow());
            PDDocument doc = new PDDocument();
            PDRectangle myPageSize = new PDRectangle(470,200);
            PDPage page = new PDPage(myPageSize);
            PDImageXObject pdimage;
            PDPageContentStream content;
            try {
                pdimage = PDImageXObject.createFromFile("chart.png",doc);
                content = new PDPageContentStream(doc, page);
                content.drawImage(pdimage, 0, 0);
                content.close();
                doc.addPage(page);
                doc.save(pdffile.getAbsolutePath());
                doc.close();
                file.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
    }

    @FXML
    void floorComboBoxHandler(ActionEvent event) {
        initializeMap();

    }

    @FXML
    void lotComboBoxHandler(ActionEvent event) {
        initializeMap();

    }

    @FXML
    void initialize() {
        Platform.runLater(() -> {

            ObservableList<String> lotsList = FXCollections.observableArrayList(
                    "parking #1",
                    "parking #2",
                    "parking #3",
                    "parking #4"
            );
            lotComboBox.setItems(lotsList);
            lotComboBox.selectIndex(0);

            ObservableList<String> floorsList = FXCollections.observableArrayList("1", "2", "3");

            floorComboBox.setItems(floorsList);
            floorComboBox.selectIndex(0);
            initializeMap();

        });
    }

    @FXML
    void initializeMap() {
        Platform.runLater(() -> {

            ObservableList<ParkingLot> parkingLots = FXCollections.observableArrayList();
            parkingLots.add(new ParkingLot("parking #1", "haifa 123", 8, "1,0,1,-1,1,0,0,1,0,-1,1,-1,1,1,0,0,0,1,1,-1,0,1,0,-1,0,1,1,-1,1,0,1,-1,1,0,1,1,0,-1,1,0,0,-1,0,1,-1,0,1,-1,-1,0,1,0,0,-1,0,-1,1,-1,0,0,1,-1,1,0,0,1,1,0,0,-1,0,1,-1"));
            parkingLots.add(new ParkingLot("parking #2", "haifa 61/4", 4, "1,0,1,-1,1,0,-1,1,0,1,-1,0,1,1,0,-1,1,1,0,1,-1,0,1,-1,0,1,1,1,0,-1,1,-1,0,1,-1,1,1,0,-1,0,1,-1,0,1,1,1,-1,0,1,-1,0,1,-1,1,0,1,1,-1,1,0,0,-1,1,-1,0,1,1,0"));
            parkingLots.add(new ParkingLot("parking #3", "tel-aviv 99", 6, "1,1,1,0,0,1,1,1,1,0,0,1,1,0,0,1,0,1,1,1,1,0,0,1,1,1,0,0,1,0,0,1,1,1,0,1,1,0,0,0,0,1,1,0,0,1,1,0,1,0,0,1,0,1,1,0,0,1,1,1,0,1,1,0,0,1,1,0,0,1,1,1,1"));
            parkingLots.add(new ParkingLot("parking #4", "eilat 7", 7, "1,1,1,0,1,-1,1,1,0,0,1,1,1,1,-1,1,1,0,0,1,1,1,1,1,0,0,-1,1,1,0,0,0,0,1,1,1,1,1,1,0,0,-1,-1,-1,1,1,1,1,0,0,0,-1,-1,-1,-1,1,1,1,1,1,0,0,0,0,0,0,-1,-1,-1,-1"));

            String lot = (lotComboBox.getText().toString());
            String floor = (floorComboBox.getText().toString());
            int floorWidth = 0;
            String map = null;

            for (ParkingLot pLots : parkingLots) {
                if (pLots.getName().equals(lot)) {
                    map = pLots.getMap();
                    floorWidth = pLots.getFloorWidth();
                    break;
                }
            }
            String[] elements = map.split(",");
            int[] arr = Arrays.stream(elements).mapToInt(Integer::parseInt).toArray();

            int target = (3 * floorWidth) * (Integer.parseInt(floor));
            int start = target - (3 * floorWidth);

            Object Cindex = null;
            for (int i = start; i < (start+24); i++) {
                int index = i - start;
                String fieldName = "C" + index;
                try {
                    Field field = PCEmployeeMainParkingTrackingController.class.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Cindex = field.get(this);
                    if ((Cindex instanceof VBox)) {
                        if(i < target){
                            ((VBox) Cindex).setVisible(true);
                        } else{
                            ((VBox) Cindex).setVisible(false);
                        }
                        List<Node> vboxObj = ((VBox) Cindex).getChildren();
                        HBox hboxObj = (HBox) vboxObj.get(0);
                        List<Node> textObj = hboxObj.getChildren();
                        Text text = (Text) textObj.get(0);
                        text.setText(String.valueOf(i));
                    }
                    if ((Cindex instanceof VBox)) {
                        List<Node> vboxObj = ((VBox) Cindex).getChildren();
                        HBox hboxObj = (HBox) vboxObj.get(1);
                        List<Node> svgObj = hboxObj.getChildren();
                        SVGPath svg = (SVGPath) svgObj.get(0);
                        if (arr[i] == 1) {
                            svg.setVisible(true);
                            svg.setContent("M42.3 110.94c2.22 24.11 2.48 51.07 1.93 79.75-13.76.05-24.14 1.44-32.95 6.69-4.96 2.96-8.38 6.28-10.42 12.15-1.37 4.3-.36 7.41 2.31 8.48 4.52 1.83 22.63-.27 28.42-1.54 2.47-.54 4.53-1.28 5.44-2.33.55-.63 1-1.4 1.35-2.31 1.49-3.93.23-8.44 3.22-12.08.73-.88 1.55-1.37 2.47-1.61-1.46 62.21-6.21 131.9-2.88 197.88 0 43.41 1 71.27 43.48 97.95 41.46 26.04 117.93 25.22 155.25-8.41 32.44-29.23 30.38-50.72 30.38-89.54 5.44-70.36 1.21-134.54-.79-197.69.69.28 1.32.73 1.89 1.42 2.99 3.64 1.73 8.15 3.22 12.08.35.91.8 1.68 1.35 2.31.91 1.05 2.97 1.79 5.44 2.33 5.79 1.27 23.9 3.37 28.42 1.54 2.67-1.07 3.68-4.18 2.31-8.48-2.04-5.87-5.46-9.19-10.42-12.15-8.7-5.18-18.93-6.6-32.44-6.69-.75-25.99-1.02-51.83-.01-77.89C275.52-48.32 29.74-25.45 42.3 110.94zm69.63-90.88C83.52 30.68 62.75 48.67 54.36 77.59c21.05-15.81 47.13-39.73 57.57-57.53zm89.14-4.18c28.41 10.62 49.19 28.61 57.57 57.53-21.05-15.81-47.13-39.73-57.57-57.53zM71.29 388.22l8.44-24.14c53.79 8.36 109.74 7.72 154.36-.15l7.61 22.8c-60.18 28.95-107.37 32.1-170.41 1.49zm185.26-34.13c5.86-34.1 4.8-86.58-1.99-120.61-12.64 47.63-9.76 74.51 1.99 120.61zM70.18 238.83l-10.34-47.2c45.37-57.48 148.38-53.51 193.32 0l-12.93 47.2c-57.58-14.37-114.19-13.21-170.05 0zM56.45 354.09c-5.86-34.1-4.8-86.58 1.99-120.61 12.63 47.63 9.76 74.51-1.99 120.61z");
                            svg.setScaleX(0.05);
                            svg.setScaleY(0.05);
                        } else if (arr[i] == -1) {
                            svg.setVisible(true);
                            svg.setContent("m 12.45 37.65 l -2.1 -2.1 L 21.9 24 L 10.35 12.45 l 2.1 -2.1 L 24 21.9 l 11.55 -11.55 l 2.1 2.1 L 26.1 24 l 11.55 11.55 l -2.1 2.1 L 24 26.1 Z");
                            svg.setScaleX(1.0);
                            svg.setScaleY(1.0);
                        } else if (arr[i] == 0) {
                            svg.setVisible(false);
                        }
                    }

                } catch (NoSuchFieldException | IllegalAccessException e) {
                    // exception
                }
            }

        });
    }
}
