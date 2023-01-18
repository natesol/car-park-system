package net.cps.server.utils;

import net.cps.common.entities.*;
import net.cps.common.utils.ComplaintStatus;
import net.cps.common.utils.Entities;
import net.cps.common.utils.ReservationStatus;
import net.cps.server.Database;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;
import java.text.ParseException;


public class ScheduledTaskService {

    public ScheduledTaskService(){
        this.dailyschedulertaskTask(23, 59);
        this.scheduleOneTimeTask("31/12/2025 23:59");
    }

    private void dailyschedulertaskTask(int hours, int minutes) {
        Calendar with = Calendar.getInstance();
        with.set(Calendar.HOUR_OF_DAY, hours);
        with.set(Calendar.MINUTE, minutes);
        with.set(Calendar.SECOND, 0);
        with.set(Calendar.MILLISECOND, 0);
        with.setTimeZone(TimeZone.getTimeZone("Israel"));
        long delay = with.getTimeInMillis() - System.currentTimeMillis();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new Daily(), delay,
                86400000, TimeUnit.MILLISECONDS); // 86400000ms = 24 hours

    }

    private void scheduleOneTimeTask(String dateString) {
        //SimpleDateFormat simple = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        //System.out.println(simple.format(result));
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar with = Calendar.getInstance();
        try {
            with.setTime(format.parse(dateString));
        } catch (ParseException e) {
            System.out.println("Error");
        }
        with.setTimeZone(TimeZone.getTimeZone("Israel"));
        Date result = new Date(with.getTimeInMillis());
        long delay = with.getTimeInMillis() - System.currentTimeMillis();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        TimeUnit unit = TimeUnit.MILLISECONDS;
        scheduler.schedule(new exampleTask(), delay, unit);
    }

    public class Minute implements Runnable {

        @Override
        public void run() {
            //boolean is send reminder?
           // ArrayList<Reservation> reservationsNew = new ArrayList<>();
            ArrayList<ParkingLot> parkingLots = Database.getAllEntities(Database.getSessionFactory(), ParkingLot.class);
            //אם משתמשת )שאינה לקוחה( הזמינה חניה ולא הגיעה בזמן המוזמן , המערכת שולחת לה
            //הודעת תזכורת באימייל. במידה והמזמינה לא החזירה תשובה תוך חצי שעה שהיא עדיין
            //מעוניינת בחניה, ההזמנה שלה מבוטלת אוטומטית.
            //   System.out.println("RunningMinute: " + new java.util.Date());
            for (ParkingLot parkingLot: parkingLots){
                List<Reservation> reservations = parkingLot.getReservations();
                for (Reservation reservation: reservations){
                    if (reservation.getStatus() == ReservationStatus.PENDING
                            && reservation.getArrivalTime().before(Calendar.getInstance())){
                        //late occasional customer
                        System.out.println("please send mail about late to reservation\n");
                        //check answer and cancel or save reservation
                        Random rand = new Random();
                        boolean answer = rand.nextBoolean();
                        Calendar halfHour = reservation.getArrivalTime();
                        halfHour.add(Calendar.MINUTE,30);
                        if (!answer && halfHour.before(reservation.getArrivalTime())){
                            reservation.setStatus(ReservationStatus.CANCELLED);
                        }
                    }
                }
            }

            ////ה לקוחה צריכה לקבל תשובה על פנייתה
            ////תוך 24 שעות.

            ArrayList<Complaint> complaints = Database.getAllEntities(Database.getSessionFactory(), Complaint.class);
            Calendar hour = Calendar.getInstance();
            hour.add(Calendar.DAY_OF_MONTH,-1); //-24 hours
            for (Complaint complaint:complaints) {
                if (complaint.getStatus()== ComplaintStatus.ACTIVE &&
                        complaint.getSubmissionTime().before(hour)){
                    //mail to customer and service employees
                }
            }

            //UPDATE DB

        }

    }

    public class Daily implements Runnable {

        @Override
        public void run() {

            ArrayList<ParkingLot> parkingLots = Database.getAllEntities(Database.getSessionFactory(), ParkingLot.class);

            //statistics
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String cal = LocalDate.now().format(formatter);
            for (ParkingLot parkingLot:parkingLots) {
                ArrayList<Reservation> reservations = Database.getCustomQuery(Database.getSessionFactory(),Reservation.class, "SELECT * FROM "+ Entities.RESERVATION.getTableName() +" WHERE DATE(arrival_time) = '"+cal+"' AND parking_lot_id='"+parkingLot.getId()+"'");
                if (reservations == null || reservations.size()==0) return;
                DailyStatistics dailyStatistics = new DailyStatistics(parkingLot, reservations);
            }

            ////משך החניה
            ////המרבי ברציפות באמצעות מנוי מלא הוא 14 יממות

            Calendar calendar14 = Calendar.getInstance();
            calendar14.add(Calendar.DAY_OF_MONTH, -14);
            for (ParkingLot parkingLot : parkingLots){
                Robot robot = new Robot(parkingLot);
                ParkingSpace[][][] parkingSpaces = robot.getMatrix3D();
                for (int i=0; i<robot.getRows();i++){
                    for (int j=0; j<robot.getCols();j++){
                        for (int k=0; k<robot.getFloors();k++){
                            if (parkingSpaces[i][j][k].getReservation().getEntryTime()!=null &&
                                    parkingSpaces[i][j][k].getReservation().getEntryTime().before(calendar14)){
                                System.out.println("Get out of my parking lot!\n");
                                //remove
                                robot.remove(parkingSpaces[i][j][k].getVehicle());
                                //mail
                            }

                        }
                    }
                }
            }

            // תזכורת לחידוש
            ////למנוייה שבוע לפני מועד הפקיעה.  יומי
            //
            List<Subscription> subscriptionList;
            Calendar calendar = Calendar.getInstance();
            Calendar calendarExpires;
            calendar.add(Calendar.WEEK_OF_MONTH,1);
            for (ParkingLot parkingLot: parkingLots){
                subscriptionList = parkingLot.getSubscriptions();
                for (Subscription subscription: subscriptionList){
                    calendarExpires = subscription.getExpiresAt();
                    if(calendarExpires.before(calendar)){
                        System.out.println("please send mail to subscription\n");
                    }
                }
            }


            //UPDATE DB

        }
    }
}