package net.cps.client.controllers.pc.employee.networkManager;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.utils.others.FunctionalStringConverter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import net.cps.client.CPSClient;
import net.cps.client.utils.AbstractPCEmployeePageController;
import net.cps.common.entities.ParkingLot;
import net.cps.common.entities.ParkingSpace;
import net.cps.common.messages.RequestMessage;
import net.cps.common.messages.ResponseMessage;
import net.cps.common.utils.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PCEmployeeNMParkingLotsController extends AbstractPCEmployeePageController implements Initializable {
    private ArrayList<ParkingLot> allParkingLots;
    private ArrayList<ParkingSpace> allParkingSpaces;
    private ParkingLot selectedParkingLot = null;
    private Integer selectedFloor = 1;
    
    
    @FXML
    public MFXComboBox<ParkingLot> parkingLotsCombo;
    @FXML
    public MFXComboBox<Integer> floorCombo;
    @FXML
    public MFXButton showDataBtn;
    
    @FXML
    public Text parkingLotNameField;
    @FXML
    public Text totalCapacityField;
    
    @FXML
    public GridPane spacesGrid;
    
    @FXML
    public MFXButton downloadPDFBtn;
    
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        
        Platform.runLater(() -> {
            ObservableList<Integer> floorsList = FXCollections.observableArrayList(1, 2, 3);
            floorCombo.setItems(floorsList);
            floorCombo.selectIndex(0);
            initMap();
        });
        
        CPSClient.sendRequestToServer(RequestType.GET, Entities.PARKING_LOT.getTableName(), this::onGetParkingLots);
        CPSClient.sendRequestToServer(RequestType.GET, Entities.PARKING_SPACE.getTableName(), this::onGetParkingSpaces);
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void showDataBtnClickHandler (ActionEvent event) {
        selectedParkingLot = parkingLotsCombo.getValue();
        selectedFloor = floorCombo.getValue();
        
        initMap();
    }
    
    @FXML
    public void downloadPDFBtnClickHandler (ActionEvent event) {
        Platform.runLater(() -> {
            WritableImage image = spacesGrid.snapshot(new SnapshotParameters(), null);
            File file = new File("chart.png");
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Image");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File pdffile = fileChooser.showSaveDialog(spacesGrid.getScene().getWindow());
            PDDocument doc = new PDDocument();
            PDRectangle myPageSize = new PDRectangle(470, 200);
            PDPage page = new PDPage(myPageSize);
            PDImageXObject pdImage;
            PDPageContentStream content;
            try {
                pdImage = PDImageXObject.createFromFile("chart.png", doc);
                content = new PDPageContentStream(doc, page);
                content.drawImage(pdImage, 0, 0);
                content.close();
                doc.addPage(page);
                doc.save(pdffile.getAbsolutePath());
                doc.close();
                file.delete();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    
    
    /* ----- Event Bus Listeners ------------------------------------ */
    
    // ...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    @RequestCallback.Method
    private void onGetParkingLots (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.SUCCESS) {
            allParkingLots = (ArrayList<ParkingLot>) response.getData();
            ObservableList<ParkingLot> parkingLots = FXCollections.observableArrayList((List<ParkingLot>) response.getData());
            
            Platform.runLater(() -> {
                StringConverter<ParkingLot> converter = FunctionalStringConverter.to(parkingLot -> (parkingLot == null) ? "null" : parkingLot.getName());
                parkingLotsCombo.setConverter(converter);
                parkingLotsCombo.setItems(parkingLots);
                parkingLotsCombo.setItems(parkingLots);
                parkingLotsCombo.setValue(parkingLots.get(0));
                parkingLotsCombo.setValue(parkingLots.get(0));
            });
        }
        else {
            System.out.println("failed to get all parking lots from the server.");
        }
    }
    
    @RequestCallback.Method
    private void onGetParkingSpaces (RequestMessage request, ResponseMessage response) {
        if (response.getStatus() == ResponseStatus.SUCCESS) {
            allParkingSpaces = (ArrayList<ParkingSpace>) response.getData();
            
            Platform.runLater(this::initMap);
        }
        else {
            System.out.println("failed to get all parking lots from the server.");
        }
    }
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    private void initMap () {
        Platform.runLater(() -> {
            selectedParkingLot = parkingLotsCombo.getValue();
            
            parkingLotNameField.setText(selectedParkingLot.getName());
            totalCapacityField.setText(Integer.toString(selectedParkingLot.getNumOfCols() * selectedParkingLot.getNumOfRows() * selectedParkingLot.getNumOfFloors()));
            
            ArrayList<ParkingSpace> parkingSpaces = allParkingSpaces.stream().filter(parkingSpace -> Objects.equals(parkingSpace.getParkingLot().getId(), selectedParkingLot.getId())).collect(Collectors.toCollection(ArrayList::new));
            for (ParkingSpace parkingSpace : parkingSpaces) {
                if (parkingSpace.getFloor() != selectedFloor - 1) continue;
                
                String cellName = "C" + parkingSpace.getRow() + parkingSpace.getCol();
                VBox cell = (VBox) spacesGrid.lookup("#" + cellName);
                cell.setManaged(true);
                cell.setVisible(true);
                cell.setDisable(false);
                cell.setMaxWidth(50);
                
                Text text = (Text) ((HBox) cell.getChildren().get(0)).getChildren().get(0);
                text.setText(selectedFloor + "-" + parkingSpace.getRow() + "-" + parkingSpace.getCol());
                
                SVGPath svg = (SVGPath) ((HBox) cell.getChildren().get(1)).getChildren().get(0);
                svg.setManaged(true);
                svg.setVisible(true);
                svg.setDisable(false);
                if (parkingSpace.getState() == ParkingSpaceState.AVAILABLE) {
                    svg.setContent("");
                    svg.setScaleX(1.0);
                    svg.setScaleY(1.0);
                }
                if (parkingSpace.getState() == ParkingSpaceState.OCCUPIED) {
                    svg.setContent("M42.3 110.94c2.22 24.11 2.48 51.07 1.93 79.75-13.76.05-24.14 1.44-32.95 6.69-4.96 2.96-8.38 6.28-10.42 12.15-1.37 4.3-.36 7.41 2.31 8.48 4.52 1.83 22.63-.27 28.42-1.54 2.47-.54 4.53-1.28 5.44-2.33.55-.63 1-1.4 1.35-2.31 1.49-3.93.23-8.44 3.22-12.08.73-.88 1.55-1.37 2.47-1.61-1.46 62.21-6.21 131.9-2.88 197.88 0 43.41 1 71.27 43.48 97.95 41.46 26.04 117.93 25.22 155.25-8.41 32.44-29.23 30.38-50.72 30.38-89.54 5.44-70.36 1.21-134.54-.79-197.69.69.28 1.32.73 1.89 1.42 2.99 3.64 1.73 8.15 3.22 12.08.35.91.8 1.68 1.35 2.31.91 1.05 2.97 1.79 5.44 2.33 5.79 1.27 23.9 3.37 28.42 1.54 2.67-1.07 3.68-4.18 2.31-8.48-2.04-5.87-5.46-9.19-10.42-12.15-8.7-5.18-18.93-6.6-32.44-6.69-.75-25.99-1.02-51.83-.01-77.89C275.52-48.32 29.74-25.45 42.3 110.94zm69.63-90.88C83.52 30.68 62.75 48.67 54.36 77.59c21.05-15.81 47.13-39.73 57.57-57.53zm89.14-4.18c28.41 10.62 49.19 28.61 57.57 57.53-21.05-15.81-47.13-39.73-57.57-57.53zM71.29 388.22l8.44-24.14c53.79 8.36 109.74 7.72 154.36-.15l7.61 22.8c-60.18 28.95-107.37 32.1-170.41 1.49zm185.26-34.13c5.86-34.1 4.8-86.58-1.99-120.61-12.64 47.63-9.76 74.51 1.99 120.61zM70.18 238.83l-10.34-47.2c45.37-57.48 148.38-53.51 193.32 0l-12.93 47.2c-57.58-14.37-114.19-13.21-170.05 0zM56.45 354.09c-5.86-34.1-4.8-86.58 1.99-120.61 12.63 47.63 9.76 74.51-1.99 120.61z");
                    svg.setScaleX(0.05);
                    svg.setScaleY(0.05);
                }
                if (parkingSpace.getState() == ParkingSpaceState.RESERVED) {
                    svg.setContent("M 12.45 37.65 L 10.35 35.55 L 21.9 24 L 10.35 12.45 L 12.45 10.35 L 24 21.9 L 35.55 10.35 L 37.65 12.45 L 26.1 24 L 37.65 35.55 L 35.55 37.65 L 24 26.1 Z");
                    svg.setScaleX(1.0);
                    svg.setScaleY(1.0);
                }
                if (parkingSpace.getState() == ParkingSpaceState.DISABLED) {
                    svg.setContent("M 12.45 37.65 L 10.35 35.55 L 21.9 24 L 10.35 12.45 L 12.45 10.35 L 24 21.9 L 35.55 10.35 L 37.65 12.45 L 26.1 24 L 37.65 35.55 L 35.55 37.65 L 24 26.1 Z");
                    svg.setScaleX(1.0);
                    svg.setScaleY(1.0);
                }
                if (parkingSpace.getState() == ParkingSpaceState.OUT_OF_ORDER) {
                    svg.setContent("M 12.45 37.65 L 10.35 35.55 L 21.9 24 L 10.35 12.45 L 12.45 10.35 L 24 21.9 L 35.55 10.35 L 37.65 12.45 L 26.1 24 L 37.65 35.55 L 35.55 37.65 L 24 26.1 Z");
                    svg.setScaleX(1.0);
                    svg.setScaleY(1.0);
                }
            }
            for (int i = 0 ; i < selectedParkingLot.getNumOfRows() ; i++) {
                for (int j = selectedParkingLot.getNumOfCols() ; j < ParkingLot.MAX_COLS ; j++) {
                    String cellName = "C" + i + j;
                    VBox cell = (VBox) spacesGrid.lookup("#" + cellName);
                    if (cell == null) continue;
                    cell.setManaged(false);
                    cell.setVisible(false);
                    cell.setDisable(true);
                    cell.setMaxWidth(0);
                }
            }
        });
    }
}
