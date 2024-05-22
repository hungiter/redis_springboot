package com.example.pub.models;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRespository extends CrudRepository<Product, String> {
}