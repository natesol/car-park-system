package net.cps.client.controllers.kiosk;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import net.cps.client.utils.AbstractKioskPageController;
import net.cps.common.entities.Customer;
import net.cps.common.entities.Reservation;

import java.net.URL;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;


public class KioskBookNowController extends AbstractKioskPageController implements Initializable {
    Customer customer;
    
    @FXML
    public MFXTextField email;
    @FXML
    public Text emailErrorText;
    @FXML
    public MFXTextField vehicleNumber;
    @FXML
    public Text vehicleErrorText;
    @FXML
    public MFXDatePicker departureDate;
    @FXML
    public MFXTextField departureHour;
    @FXML
    public MFXTextField departureMinute;
    @FXML
    public Text departureErrorText;
    @FXML
    public MFXButton enterParkingLotBtn;
    
    
    /* ----- Scene Controller Initialization ------------------------ */
    
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
    }
    
    
    /* ----- GUI Events Handlers ------------------------------------ */
    
    @FXML
    public void enterParkingLotBtnClickHandler (ActionEvent event) {
        String emailText = email.getText();
        emailErrorText.setText("");
        if (emailText.isEmpty()) {
            emailErrorText.setText("Please enter your email.");
            return;
        }
        if (!(emailText.contains("@") && emailText.contains("."))) {
            emailErrorText.setText("Please enter a valid email.");
            return;
        }
        String vehicleNumberText = vehicleNumber.getText();
        vehicleErrorText.setText("");
        if (vehicleNumberText.isEmpty()) {
            vehicleErrorText.setText("Please enter your vehicle number.");
            return;
        }
        if (!vehicleNumberText.matches("\\d+")) {
            vehicleErrorText.setText("Vehicle number can only contain numbers.");
            return;
        }
        if (vehicleNumberText.length() < 8) {
            vehicleErrorText.setText("Vehicle number must be 8 digits long.");
            return;
        }
        LocalDate departureDateValue = departureDate.getValue();
        departureErrorText.setText("");
        if (departureDateValue == null) {
            departureErrorText.setText("Please enter estimated departure date.");
            return;
        }
        if (departureDateValue.isBefore(LocalDate.now())) {
            departureErrorText.setText("Departure time cannot be in the past.");
            return;
        }
        if (departureDateValue.isAfter(LocalDate.now().plusDays(14))) {
            departureErrorText.setText("Departure time cannot be more than 14 days from now.");
            return;
        }
        String departureHourValue = departureHour.getText();
        if (departureHourValue.isEmpty()) {
            departureErrorText.setText("Please enter an estimated departure hour.");
            return;
        }
        if (!departureHourValue.matches("\\d+")) {
            departureErrorText.setText("Departure hour can only contain numbers.");
            return;
        }
        int departureHourInt = Integer.parseInt(departureHourValue);
        if (departureHourInt < 0 || departureHourInt > 23) {
            departureErrorText.setText("Departure hour must be between 0 and 23.");
            return;
        }
        String departureMinuteValue = departureMinute.getText();
        if (departureMinuteValue.isEmpty()) {
            departureErrorText.setText("Please enter an estimated departure minute.");
            return;
        }
        if (!departureMinuteValue.matches("\\d+")) {
            departureErrorText.setText("Departure minute can only contain numbers.");
            return;
        }
        int departureMinuteInt = Integer.parseInt(departureMinuteValue);
        if (departureMinuteInt < 0 || departureMinuteInt > 59) {
            departureErrorText.setText("Departure minute must be between 0 and 59.");
            return;
        }
        Calendar departureCalendar = Calendar.getInstance();
        departureCalendar.set(departureDateValue.getYear(), departureDateValue.getMonthValue() - 1, departureDateValue.getDayOfMonth(), departureHourInt, departureMinuteInt);
        
        
        dialog.setTitleText("Enter Parking Lot");
        dialog.setBodyText("You have successfully entered the parking lot.", "Your email is: " + emailText, "Your vehicle number is: " + vehicleNumberText, "Your estimated departure time is: " + departureCalendar.getTime());
        dialog.open();
    }
    
    /* ----- Event Bus Listeners ------------------------------------ */
    
    // ...
    
    
    /* ----- Requests Callbacks (on server response) ---------------- */
    
    // ...
    
    
    /* ----- Utility Methods ---------------------------------------- */
    
    private Reservation createReservation (String email, String vehicleNumber, Calendar departureTime) {
        Reservation reservation = new Reservation();
        //reservation.setEmail(email);
        //reservation.setVehicleNumber(vehicleNumber);
        //reservation.setDepartureTime(departureTime);
        return reservation;
    }
    
}
