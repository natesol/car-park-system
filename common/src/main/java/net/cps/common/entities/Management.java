package net.cps.common.entities;

public class Management extends AbstractOrganization {
    private Long id;

    public Management(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
