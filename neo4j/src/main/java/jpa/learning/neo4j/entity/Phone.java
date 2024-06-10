package jpa.learning.neo4j.entity;

import jpa.learning.neo4j.utils.CustomUUID;
import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Phone")
@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Phone {
    @Id
    @GeneratedValue(generatorClass = CustomUUID.class)
    String id;
    String name;
    String system;
    int memory;
    int price;

    public Phone(PhoneType phoneType, int memory, int price) {
        setName(phoneType.name());
        setSystem(phoneType.getSystem().name());
        this.memory = memory;
        this.price = price;
    }
}
