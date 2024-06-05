package com.example.consumer1.models.Data.Transfer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@Data
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TransferInfoDTO {
    String from;
    String to;
    String transferId;

    // Convert Object to Json
    public String toJson(){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    // Convert JSON to Object
    public static TransferInfoDTO fromJson(String jsonString) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, TransferInfoDTO.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
