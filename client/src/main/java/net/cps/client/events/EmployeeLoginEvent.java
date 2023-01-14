package net.cps.client.events;

import jdk.jfr.Event;
import net.cps.common.entities.Employee;

public class EmployeeLoginEvent extends Event {
    private final Employee employee;

    public EmployeeLoginEvent (Employee employee) {
        this.employee = employee;
    }
    
    public Employee getEmployee () {
        return employee;
    }
}
