package com.example.entityTransfer.entities.models_database;

import jakarta.persistence.*;


@Entity
@Table(name = "Tribes")
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

    public H2Tribes() {
    }

    public H2Tribes(String creatureType, int male, int female) {
        this.creatureType = creatureType;
        this.male = male;
        this.female = female;
        this.quantity = male + female;
    }

    public String getCreatureType() {
        return creatureType;
    }

    public void setCreatureType(String creatureType) {
        this.creatureType = creatureType;
    }

    public int getMale() {
        return male;
    }

    public void setMale(int male) {
        this.male = male;
        setQuantity();
    }

    public int getFemale() {
        return female;
    }

    public void setFemale(int female) {
        this.female = female;
        setQuantity();
    }

    public int getQuantity() {
        return quantity;
    }

    private void setQuantity() {
        this.quantity = this.male + this.female;
    }

    public int getTp() {
        return tp;
    }

    public void setTp(int tp) {
        this.tp = tp;
    }

    public Long getId() {
        return id;
    }
}
