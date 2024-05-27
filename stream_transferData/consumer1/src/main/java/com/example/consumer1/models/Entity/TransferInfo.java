package com.example.consumer1.models.Entity;

import jakarta.persistence.*;

@Entity
public class TransferInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "_from")
    private String from;

    @Column(name = "_to")
    private String to;

    @Column(name = "_id")
    private String transferId;


    public TransferInfo(String from, String to, String transferId) {
        this.from = from;
        this.to = to;
        this.transferId = transferId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }
}
