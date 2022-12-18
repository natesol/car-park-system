package net.cps.server;

import net.cps.entities.hibernate.ParkingLot;
import net.cps.server.utils.HibernateUtils;
import org.hibernate.Session;

import java.io.IOException;

public class Main {
    private static final int DEFAULT_PORT = 3000;
    private static CPSServer server;
    private static Session dbSession;
    
    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        
        server = new CPSServer(port);
        server.listen();
        System.out.println("[SERVER] server is listening on port: " + port + ".");
    
        dbSession = HibernateUtils.getSessionFactory().openSession();
        dbSession.beginTransaction();
        System.out.println("[SERVER] server connected to database session successfully.");
        
        
        //    // Save a parking lot
        //    ParkingLot parkingLot = new ParkingLot();
        //    parkingLot.setName("Parking Lot 1");
        //    parkingLot.setAddress("123 Main Street");
        //    session.save(parkingLot);
        //
        //    // Commit the transaction
        //    session.getTransaction().commit();
        //
        //    // Close the session
        //    session.close();
        //
        //    // Shutdown the session factory
        //    HibernateUtils.shutdown();
    }
}
