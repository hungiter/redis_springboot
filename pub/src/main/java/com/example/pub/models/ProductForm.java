package com.example.pub.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class ProductForm {
    @Min(1)
    private int quantity;

    @Min(1000)
    private int price;

    @Size(min = 2, max = 30)
    private String name;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Product{ price=" + price +
                ", quantity=" + quantity +
                ", name='" + name + '\'' +
                '}';
    }
}
