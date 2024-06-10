package jpa.learning.neo4j.entity;

import lombok.*;

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Query {
    int minPrice;
    int maxPrice;
    int viewPage;
    int phoneType;
    int phoneSystem;
    int phoneMemory;

    public Query(int minPrice, int maxPrice, int viewPage) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.viewPage = viewPage;
    }
}
