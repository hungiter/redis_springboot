package com.example.consumer1.models.Entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="transfer_info")
@Data
@Getter
@Setter
@NoArgsConstructor
public class TransferInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "_from")
    String from;

    @Column(name = "_to")
    String to;

    @Column(name = "_id")
    String transferId;

    public TransferInfo(String from, String to, String transferId) {
        this.from = from;
        this.to = to;
        this.transferId = transferId;
    }
}
