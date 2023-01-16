package net.cps.server.ocsf;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;


public class ScheduledTaskService {
    /*
    public ScheduledTaskService(){
        this.dailyschedulertaskTask(23, 59);
        this.scheduleOneTimeTask("31/12/2025 23:59");
    }

    */
    private void dailyschedulertaskTask(int hours, int minutes) {
        Calendar with = Calendar.getInstance();
        with.set(Calendar.HOUR_OF_DAY, hours);
        with.set(Calendar.MINUTE, minutes);
        with.set(Calendar.SECOND, 0);
        with.set(Calendar.MILLISECOND, 0);
        with.setTimeZone(TimeZone.getTimeZone("Israel"));
        long delay = with.getTimeInMillis() - System.currentTimeMillis();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new exampleTask(), delay,
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

    public class exampleTask implements Runnable {

        @Override
        public void run() {
            System.out.println("Running task...");
        }
    }
}

