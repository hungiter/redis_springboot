package com.example.producer.models.Data.Transfer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransferInfoDTO {
    String from;
    String to;
    String transferId;

    // Convert object to JSON string
    public String toJson(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    // Convert JSON string to object
    public static TransferInfoDTO fromJson(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, TransferInfoDTO.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
