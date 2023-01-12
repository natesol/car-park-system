package org.example;
import java.time.Period;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TimerProject {
    public void timer(List<ParkingLot> parkingLots){

        Timer timerDay = new Timer();
        Timer timerMinute = new Timer();

        timerMinute.schedule(new TimerTask() {
            @Override
            public void run() {
                //TASKS
                //אם משתמשת )שאינה לקוחה( הזמינה חניה ולא הגיעה בזמן המוזמן , המערכת שולחת לה
                //הודעת תזכורת באימייל. במידה והמזמינה לא החזירה תשובה תוך חצי שעה שהיא עדיין
                //מעוניינת בחניה, ההזמנה שלה מבוטלת אוטומטית.
             //   System.out.println("RunningMinute: " + new java.util.Date());
                for (ParkingLot parkingLot: parkingLots){
                    List<Reservation> reservations = parkingLot.getReservationsList();
                    for (Reservation reservation: reservations){
                        if (!reservation.getSubscribedCustomer()&&reservation.getStatusReservation()&&!reservation.getReportSend()&&!reservation.getEnterParkingLot()
                        &&reservation.getArrivalTime().before(Calendar.getInstance())){
                            //late occasional customer
                            reservation.setReportSend(true);
                            System.out.println("please send mail about late to reservation\n");
                        }
                    }
                }
            }
            }, 0,1000L*60);
        timerDay.schedule(new TimerTask() {
            @Override
            public void run() {
                // תזכורת לחידוש
                ////למנוייה שבוע לפני מועד הפקיעה.  יומי
                //
                List<Subscription> subscriptionList;
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.WEEK_OF_MONTH,1);
                for (ParkingLot parkingLot: parkingLots){
                    subscriptionList = parkingLot.getSubscriptionList();
                    for (Subscription subscription: subscriptionList){
                        if(subscription.getEndDate().before(calendar)&&!subscription.getSendReminder()){
                            System.out.println("please send mail to subscription\n");
                            subscription.setSendReminder(true);
                        }
                    }
                }

                ////משך החניה
                ////המרבי ברציפות באמצעות מנוי מלא הוא 14 יממות. יומי

                Calendar calendar14 = Calendar.getInstance();
                calendar14.add(Calendar.DAY_OF_MONTH, -14);
                for (ParkingLot parkingLot : parkingLots){
                    ParkingSpace[][][] parkingSpaces = parkingLot.getRobot().getParkingSpaceSmartArray().getArray();
                    for (int i=0; i<parkingLot.getRobot().getParkingSpaceSmartArray().getRows();i++){
                        for (int j=0; j<parkingLot.getRobot().getParkingSpaceSmartArray().getColumns();j++){
                            for (int k=0; k<parkingLot.getRobot().getParkingSpaceSmartArray().getFloors();k++){
                                if (parkingSpaces[i][j][k].getArrivalTime()!=null &&
                                parkingSpaces[i][j][k].getArrivalTime().before(calendar14)){
                                    System.out.println("Get out of my parking lot!\n");
                                }

                            }
                        }
                    }
                }

                ////ה לקוחה צריכה לקבל תשובה על פנייתה
                ////תוך 24 שעות.
                ////need timer?

                ////המערכת מחשבת ושומרת מידע סטטיסטי יומי.
                ////חישובים אלה מתבצעים אוטומטית מידי יום : ממוצע יומי, חציון והתפלגות ערכים על פי
                ////העשירונים השונים )100-0
                Report report = new Report();
                report.StatisticData();

                //System.out.println("Running2: " + new java.util.Date());
            }
            }, 0,1000L*60*60*24);
    }
}