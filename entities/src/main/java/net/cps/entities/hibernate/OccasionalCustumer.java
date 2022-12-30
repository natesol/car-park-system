package net.cps.entities.hibernate;

import java.util.Date;

public class OccasionalCustumer extends AbstractCostumer {
    private Reservation reservation;
    private Date timeOfDeparture;
    private String email;
}
