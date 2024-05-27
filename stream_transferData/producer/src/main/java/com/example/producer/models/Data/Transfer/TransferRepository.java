package com.example.producer.models.Data.Transfer;


import com.example.producer.models.EnumUtil;
import com.example.producer.models.IdGenerator;
import org.springframework.stereotype.Repository;

@Repository
public class TransferRepository {
    public TransferInfo generateTransfer() {
        To transferTo = EnumUtil.getRandomEnumValue(To.class);
        IdGenerator idGenerator = new IdGenerator();
        return new TransferInfo(From.ROOT, transferTo, idGenerator.generateId());
    }
}
