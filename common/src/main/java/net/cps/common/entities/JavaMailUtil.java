package net.cps.common.entities;
import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
public class JavaMailUtil {

    private static final String FROM_EMAIL = "cityparkcps@gmail.com";
    private static final String PASSWORD = "bgdgjdohnzeerejv";
    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "587";

    public static void sendMail(String toAccountEmail, String userName, String emailType,String parkingLot,int refundAmount, Date time, String responseFromCustomerService) throws Exception {

        /*
        <dependency>
            <groupId>com.sun.mail</groupId>
            <artifactId>javax.mail</artifactId>
            <version>1.6.2</version>
        </dependency>
        import java.util.Date;
        String toAccountEmail = "example@gmail.com";
            String userName = "John Smith";
            String emailType = "parkingCancellation";
            String parkingLotName = "Parking lot #1";
            int refundAmount = 10;
            Date time = new Date();
            String info = "text";
            try {
                JavaMailUtil mailUtil = new JavaMailUtil();
                JavaMailUtil.sendMail(toAccountEmail,userName, emailType, parkingLotName,refundAmount, time, info);
            } catch (Exception e) {
                e.printStackTrace();
            }
        */

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", HOST);
        properties.put("mail.smtp.port", PORT);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, PASSWORD);
            }
        });

        String subject = "";
        String template = "";
        String messageInfo = "";
        switch (emailType) {
            case "subscriptionExpiration":
                subject = "Your subscription is expiring soon";
                template = "Your subscription is expiring soon. Please renew it before it expires.";
                break;
            case "parkingConfirmation":
                subject = "Your parking reservation has been confirmed";
                template = "Your parking reservation at our parking lot located in "
                        + parkingLot
                        + " has been confirmed.Please arrive at the parking lot before "
                        + time;
                break;
            case "parkingCancellation":
                subject = "Your parking reservation has been canceled";
                template = "Your parking reservation at our parking lot located in "
                        + parkingLot
                        + " has been canceled.\n"
                        + "A refund of "
                        + refundAmount
                        + " has been credited to your account.";
                break;
            case "parkingPateReminder":
                subject = "Late for parking reservation";
                template = "We noticed that you are running late for your parking reservation at our parking lot located in "
                        + parkingLot
                        + ".\n"
                        + "We kindly remind you that the parking reservation time is "
                        + time;
                break;
            case "subscriptionRenewal":
                subject = "Your subscription has been renewed";
                template = "Your subscription has been renewed and is valid until "
                        + "time";
                break;
            case "welcome":
                subject = "Welcome to CPS parking system";
                template = "Thank you for signing up for CPS parking system. We hope you enjoy our services. Please let us know if you have any questions or concerns.";
                break;
            case "passwordReset":
                subject = "Password reset instruction";
                template = "We received a request to reset your password. Your new temporary password is : "
                        + "[temporaryPassword]"
                        + "\n"
                        + "Please login with this temporary password and change it to a new one of your choice as soon as possible.\n"
                        + "If you didn't request a password reset, please contact us immediately.";
                break;
            case "responseFromCustomerService":
                subject = "Response from customer service";
                template = "We have received your request and here is the response from our customer service team :"
                        + responseFromCustomerService;
                break;
            case "Complaint":
                subject = "response From CustomerService";
                template = "We have received your complaint and we are sorry to hear that you have encountered a problem with our service. Our customer service team will be in touch with you shortly";
                break;
            default:
                break;
        }
        messageInfo = "Dear " + userName + ",\n" + template + "\nIf you have any questions or concerns, please don't hesitate to contact us.\nThank you for choosing CPS parking system.\nBest regards,\nCPS parking system team.";

        Message message = prepareMessage(session, FROM_EMAIL, toAccountEmail, subject, messageInfo);
        Transport.send(message);
        System.out.println("Message Sent successfully");
    }

    private static Message prepareMessage(Session session, String from, String to, String subject, String messageInfo) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(subject);
        message.setText(messageInfo);
        return message;
    }
}
