package com.example.consumer1.models.Data.Transfer;

import lombok.*;

@Data
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransferInfo {
    From from;
    To to;
    String transferId;

	// Self-defined
    public TransferInfoDTO getTransferInfoDTO() {
        return new TransferInfoDTO(getFrom().name(), getTo().name(), getTransferId());
    }
}
