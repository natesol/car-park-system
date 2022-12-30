package net.cps.entities.hibernate;
import javax.persistence.*;

@Entity
@Table(name = "Robot")
public class Robot {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
