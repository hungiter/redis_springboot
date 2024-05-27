package com.example.consumer1.models.Data.Transfer;


import com.example.consumer1.models.EnumUtil;
import com.example.consumer1.models.IdGenerator;
import org.springframework.stereotype.Repository;

@Repository
public class TransferRepository {
    public TransferInfo generateTransfer(){
        To transferTo = EnumUtil.getRandomEnumValue(To.class);
        IdGenerator idGenerator= new IdGenerator();
        return new TransferInfo(From.C1,transferTo, idGenerator.generateId());
    }
}
