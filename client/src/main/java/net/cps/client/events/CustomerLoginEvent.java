package net.cps.client.events;

import jdk.jfr.Event;
import net.cps.common.entities.Customer;
import net.cps.common.utils.AbstractMessage;
import org.jetbrains.annotations.NotNull;

public class CustomerLoginEvent extends Event {
    private final Customer customer;

    public CustomerLoginEvent (@NotNull Customer customer) {
        this.customer = customer;
    }
    
    public Customer getCustomer () {
        return customer;
    }
}
