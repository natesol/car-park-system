package net.cps.client.events;

import jdk.jfr.Event;
import net.cps.common.entities.Customer;

public class CustomerLoginEvent extends Event {
    private final Customer customer;

    public CustomerLoginEvent (Customer customer) {
        this.customer = customer;
    }
    
    public Customer getCustomer () {
        return customer;
    }
}
