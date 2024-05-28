package com.example.consumer1.models.Entity;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferInfoRepository extends CrudRepository<TransferInfo,String> {
}
