package net.cps.entities.hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

public class Vehicle {
    private Long num;
    private Long ownerId;

    public Vehicle(Long num, Long ownerId) {
        this.num = num;
        this.ownerId = ownerId;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }


}
