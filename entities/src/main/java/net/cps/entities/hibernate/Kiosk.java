package net.cps.entities.hibernate;
import javax.persistence.*;

@Entity
@Table(name = "Kiosk")
public class Kiosk {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
