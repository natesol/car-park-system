package net.cps.server.utils;

import net.cps.common.entities.DailyStatistics;
import net.cps.common.entities.ParkingLot;
import net.cps.common.entities.Reservation;
import net.cps.common.utils.Entities;
import net.cps.server.Database;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class ScheduledTaskService {
    private static final int MILLIS_IN_DAY = 86400000; // 86400000ms = 24 hours
    private static final int MILLIS_IN_HOUR = 3600000; // 3600000ms = 1 hour
    private static final int MILLIS_IN_MINUTE = 60000; // 60000ms = 1 minute
    
    private static final int DAILY_TASK_DEFAULT_TIME_HOUR = 23;
    private static final int DAILY_TASK_DEFAULT_TIME_MINUTE = 59;
    private static final int HOURLY_TASK_DEFAULT_TIME_MINUTE = 59;
    private static final int MINUTELY_TASK_DEFAULT_TIME_SECOND = 59;
    
    
    
    /* ----- Constructors ------------------------------------------- */
    
    public ScheduledTaskService () {
        try {
            this.dailyTasksRunner();
            this.scheduleOneTimeTask("31/12/2025 23:59");
        }
        catch (Throwable e) {
            Logger.print("Error: ScheduledTaskService: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
    /* ----- Utility Methods (Timed Tasks) -------------------------- */
    
    /**
     * Runs the daily tasks at the default time (23:59), every day.
     **/
    private void dailyTasksRunner () {
        Calendar start = Calendar.getInstance();
        start.set(Calendar.HOUR_OF_DAY, ScheduledTaskService.DAILY_TASK_DEFAULT_TIME_HOUR);
        start.set(Calendar.MINUTE, ScheduledTaskService.DAILY_TASK_DEFAULT_TIME_MINUTE);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        start.setTimeZone(TimeZone.getTimeZone("Israel"));
        long delay = start.getTimeInMillis() - System.currentTimeMillis();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        
        scheduler.scheduleAtFixedRate(new DailyStatisticsTask(), delay, MILLIS_IN_DAY, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Runs the hourly tasks at the default time (59 minutes), every hour.
     **/
    private void hourlyTasksRunner () {
        Calendar start = Calendar.getInstance();
        start.set(Calendar.MINUTE, ScheduledTaskService.HOURLY_TASK_DEFAULT_TIME_MINUTE);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        start.setTimeZone(TimeZone.getTimeZone("Israel"));
        long delay = start.getTimeInMillis() - System.currentTimeMillis();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        
        scheduler.scheduleAtFixedRate(new exampleTask(), delay, MILLIS_IN_HOUR, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Runs the minutely tasks at the default time (59 seconds), every minute.
     **/
    private void minutelyTasksRunner () {
        Calendar start = Calendar.getInstance();
        start.set(Calendar.SECOND, ScheduledTaskService.MINUTELY_TASK_DEFAULT_TIME_SECOND);
        start.set(Calendar.MILLISECOND, 0);
        start.setTimeZone(TimeZone.getTimeZone("Israel"));
        long delay = start.getTimeInMillis() - System.currentTimeMillis();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        
        scheduler.scheduleAtFixedRate(new exampleTask(), delay, MILLIS_IN_MINUTE, TimeUnit.MILLISECONDS);
    }
    
    /**
     * Schedules a one-time task to run at the specified time.
     *
     * @param dateString The time to run the task at, in the format "dd/MM/yyyy HH:mm".
     **/
    private void scheduleOneTimeTask (String dateString) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar runTime = Calendar.getInstance();
        runTime.setTime(format.parse(dateString));
        runTime.setTimeZone(TimeZone.getTimeZone("Israel"));
        Date result = new Date(runTime.getTimeInMillis());
        long delay = runTime.getTimeInMillis() - System.currentTimeMillis();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        
        scheduler.schedule(new exampleTask(), delay, TimeUnit.MILLISECONDS);
    }
    
    
    
    /* ----- Tasks (Runnable Classes) ------------------------------- */
    
    private static class exampleTask implements Runnable {
        @Override
        public void run () {
            
            System.out.println("Running task...");
        }
    }
    
    private static class DailyStatisticsTask implements Runnable {
        @Override
        public void run () {
            ArrayList<ParkingLot> parkingLots = Database.getAllEntities(Database.getSessionFactory(), ParkingLot.class);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String cal = LocalDate.now().format(formatter);
            for (ParkingLot parkingLot : parkingLots) {
                ArrayList<Reservation> reservations = Database.getCustomQuery(Database.getSessionFactory(), Reservation.class, "SELECT * FROM " + Entities.RESERVATION.getTableName() + " WHERE DATE(arrival_time) = '" + cal + "' AND parking_lot_id='" + parkingLot.getId() + "'");
                if (reservations == null || reservations.size() == 0) return;
                DailyStatistics dailyStatistics = new DailyStatistics(parkingLot, reservations);
            }
        }
    }
}