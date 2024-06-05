package com.example.entityTransfer.entities.models_database;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "Tribes")
@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
public class H2Tribes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "creature's type", nullable = true)
    private String creatureType;

    @Column(name = "♂", nullable = false)
    private int male;

    @Column(name = "♀", nullable = false)
    private int female;

    @Column(name = "tribe's population", nullable = false)
    private int quantity;

    @Column(name = "tribe's tp", nullable = false)
    private int tp;


    public H2Tribes(String creatureType, int male, int female) {
        this.creatureType = creatureType;
        this.male = male;
        this.female = female;
        this.quantity = male + female;
    }

    // Self-defined
    public void setMale(int male) {
        this.male = male;
        setQuantity();
    }

    public void setFemale(int female) {
        this.female = female;
        setQuantity();
    }

    private void setQuantity() {
        this.quantity = this.male + this.female;
    }

}
