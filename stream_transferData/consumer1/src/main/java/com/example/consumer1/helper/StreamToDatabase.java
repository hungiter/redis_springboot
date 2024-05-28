package com.example.consumer1.helper;

import com.example.consumer1.models.Data.Transfer.TransferInfoDTO;
import com.example.consumer1.models.Entity.TransferInfo;
import com.example.consumer1.models.Entity.TransferInfoRepository;

import java.util.List;

public class StreamToDatabase {
    TransferInfoRepository transferInfoRepository;
    List<TransferInfo> transferInfos;

    public StreamToDatabase(TransferInfoRepository transferInfoRepository, List<TransferInfo> transferInfos) {
        this.transferInfoRepository = transferInfoRepository;
        this.transferInfos = transferInfos;
    }


    public void transferInfo_update(TransferInfoDTO transferInfoDTO) {
        TransferInfo transferInfo = new TransferInfo(transferInfoDTO.getFrom(), transferInfoDTO.getTo(), transferInfoDTO.getTransferId());
        transferInfoRepository.save(transferInfo);
        transferInfos.add(transferInfo);
    }
}
