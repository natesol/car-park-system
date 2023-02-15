package net.cps.server.utils;

import net.cps.common.entities.*;
import net.cps.common.utils.ComplaintStatus;
import net.cps.common.utils.EmployeeRole;
import net.cps.common.utils.Entities;
import net.cps.common.utils.ReservationStatus;
import net.cps.server.Database;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class ScheduledTaskService {
    private static final int MILLIS_IN_DAY = 86400000; // 86400000ms = 24 hours
    private static final int MILLIS_IN_MINUTE = 60000; // 60000ms = 1 minute
    
    private static final int DAILY_TASK_DEFAULT_TIME_HOUR = 23;
    private static final int DAILY_TASK_DEFAULT_TIME_MINUTE = 59;
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public ScheduledTaskService () {
        this.minutelyTasksRunner();
        this.dailyTasksRunner();
    }
    
    
    /* ----- Utility Methods (Timed Tasks) -------------------------- */
    
    /**
     * Runs the daily tasks at the default time (23:59), every day.
     **/
    private void dailyTasksRunner () {
        Calendar with = Calendar.getInstance();
        with.set(Calendar.HOUR_OF_DAY, ScheduledTaskService.DAILY_TASK_DEFAULT_TIME_HOUR);
        with.set(Calendar.MINUTE, ScheduledTaskService.DAILY_TASK_DEFAULT_TIME_MINUTE);
        with.set(Calendar.SECOND, 0);
        with.set(Calendar.MILLISECOND, 0);
        with.setTimeZone(TimeZone.getTimeZone("Israel"));
        long delay = with.getTimeInMillis() - System.currentTimeMillis();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        
        scheduler.scheduleAtFixedRate(new Daily(), delay, MILLIS_IN_DAY, TimeUnit.MILLISECONDS);
    }
    
    private void minutelyTasksRunner () {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new Minute(), 0, MILLIS_IN_MINUTE, TimeUnit.MILLISECONDS);
    }
    
    /* ----- Tasks (Runnable Classes) ------------------------------- */
    
    public class Daily implements Runnable {
        @Override
        public void run() {
            
            ArrayList<ParkingLot> parkingLots = Database.getAllEntities(Database.getSessionFactory(), ParkingLot.class);
            ArrayList<Reservation> reservations = Database.getAllEntities(Database.getSessionFactory(), Reservation.class);
            
            for (ParkingLot parkingLot:parkingLots) {
                ArrayList<Reservation> parkingLotReservations = new ArrayList<>();
                for (Reservation reservation:reservations){
                    if (reservation.getParkingLot().getName().equals(parkingLot.getName())){
                        parkingLotReservations.add(reservation);
                    }
                }
                
                ArrayList<Reservation> relevantReservations = new ArrayList<>();
                
                Calendar yesterday = Calendar.getInstance();
                yesterday.add(Calendar.HOUR_OF_DAY,-24);
                Calendar tomorrow = Calendar.getInstance();
                tomorrow.add(Calendar.MINUTE,1);
                for (Reservation reservation:parkingLotReservations){
                    if (reservation.getArrivalTime().before(tomorrow) && reservation.getArrivalTime().after(yesterday)){
                        relevantReservations.add(reservation);
                    }
                }
                if (relevantReservations == null || relevantReservations.size()==0) {}else {
                    DailyStatistics dailyStatistics = new DailyStatistics(parkingLot, relevantReservations);
                    //update DB
                    SessionFactory sessionFactory = Database.getSessionFactory();
                    Session session = sessionFactory.openSession();
                    Transaction transaction = session.beginTransaction();
                    session.save(dailyStatistics);
                    session.flush();
                    
                    transaction.commit();
                    session.clear();
                    session.close();
                }
                
            }
            
            //Maximum parking duration is 14 days
            
            Calendar calendar14 = Calendar.getInstance();
            calendar14.add(Calendar.DAY_OF_MONTH, -14);
            
            for (Reservation reservation:reservations) {
                if (reservation.getStatus()==ReservationStatus.CHECKED_IN && reservation.getEntryTime()!=null &&
                        reservation.getEntryTime().before(calendar14)) {
                    
                    //MAIL
                    Date date = new Date();
                    try {
                        MailSender.sendMail("", reservation.getCustomer().getEmail(), reservation.getCustomer().getFullName(), "Long Parking", reservation.getParkingLotName(), 0, date, "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            
            
            
            //A reminder to renew a subscription a week before the date
            ArrayList<Subscription> subscriptionList = Database.getAllEntities(Database.getSessionFactory(), Subscription.class);
            Calendar calendar = Calendar.getInstance();
            Calendar calendarExpires;
            calendar.add(Calendar.WEEK_OF_MONTH,1);
            
            for (Subscription subscription: subscriptionList){
                calendarExpires = subscription.getExpiresAt();
                if(calendarExpires.before(calendar)){
                    //MAIL
                    Date date = new Date();
                    try {
                        String name = subscription.getParkingLotName();
                        MailSender.sendMail("",subscription.getCustomer().getEmail(),subscription.getCustomer().getFullName(),"subscriptionRenewal",subscription.getParkingLotName(),0,date,"");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            
        }
    }
    
    public class Minute implements Runnable {
        
        @Override
        public void run() {
            
            ArrayList<Reservation> reservations = Database.getAllEntities(Database.getSessionFactory(), Reservation.class);
            
            //Being late for parking, sending an email to the client and checking for an answer after 30 minutes.
            //The answer is random because we were asked to implement a one-sided interface.
            
            for (Reservation reservation : reservations) {
                if (reservation.getStatus() == ReservationStatus.PENDING
                        && reservation.getArrivalTime().before(Calendar.getInstance())) {
                    //late occasional customer
                    //MAIL
                    Date date = new Date();
                    
                    try {
                        MailSender.sendMail("", reservation.getCustomer().getEmail(), reservation.getCustomer().getFullName(), "parkingLateReminder", reservation.getParkingLot().getName(), 0, date, "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    //check answer and cancel or save reservation
                    Random rand = new Random();
                    boolean answer = rand.nextBoolean();
                    Calendar halfHour = reservation.getArrivalTime();
                    halfHour.add(Calendar.MINUTE, 30);
                    if (!answer && halfHour.before(Calendar.getInstance())) {
                        reservation.setStatus(ReservationStatus.CANCELLED);
                        
                        //MAIL
                        date = new Date();
                        try {
                            MailSender.sendMail("", reservation.getCustomer().getEmail(), reservation.getCustomer().getFullName(), "parkingCancellation", reservation.getParkingLot().getName(), 0, date, "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        
                        //update DB
                        SessionFactory sessionFactory = Database.getSessionFactory();
                        Session session = sessionFactory.openSession();
                        Transaction transaction = session.beginTransaction();
                        session.update(reservation);
                        session.flush();
                        
                        transaction.commit();
                        session.clear();
                        session.close();
                        
                        
                    }
                    
                }
            }
            
            
            //Check response to complaints after 24 hours.
            
            ArrayList<Complaint> complaints = Database.getAllEntities(Database.getSessionFactory(), Complaint.class);
            ArrayList<Employee> employees = Database.getAllEntities(Database.getSessionFactory(), Employee.class);
            Employee serviceEmployee = new Employee();
            for (Employee employee : employees) {
                if (employee.getRole() == EmployeeRole.CUSTOMER_SERVICE_EMPLOYEE) {
                    serviceEmployee = employee;
                    break;
                }
                
            }
            Calendar hour = Calendar.getInstance();
            hour.add(Calendar.HOUR_OF_DAY, -24); //-24 hours
            for (Complaint complaint : complaints) {
                if (complaint.getStatus() == ComplaintStatus.ACTIVE &&
                        complaint.getSubmissionTime().before(hour)) {
                    //mail to customer and service employees
                    //MAIL
                    Date date = new Date();
                    try {
                        MailSender.sendMail("", serviceEmployee.getEmail(), serviceEmployee.getFirstName(), "Complaint handling", new ParkingLot().getName(), 0, date, "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                }
            }
        }
    }
    
}