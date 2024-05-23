package com.example.stream_review.models.Card;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class Card {
    CardValue value;
    CardSupplier supplier;
    int price;

    public Card(CardValue value, CardSupplier supplier) {
        this.value = value;
        this.supplier = supplier;
        this.price = Integer.parseInt(value.toString().replace("C", "").trim());
    }

    public int getPrice() {
        return price*1000;
    }

    public String getSupplier() {
        return supplier.name();
    }
}
