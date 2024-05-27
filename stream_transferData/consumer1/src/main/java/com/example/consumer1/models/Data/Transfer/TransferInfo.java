package com.example.consumer1.models.Data.Transfer;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TransferInfo {
    private final From from;
    private final To to;
    private final String transferId;

    public TransferInfo(From from, To to, String transferId) {
        this.to = to;
        this.transferId = transferId;
        this.from = from;
    }

    public String getFrom() {
        return from.name();
    }

    public String getTo() {
        return to.name();
    }

    public String getTransferId() {
        return transferId;
    }
}
