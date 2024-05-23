package com.example.stream_review.models.Game;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class Player {
    String username;

    public Player(String name) {
        this.username = name;
    }

    public String getUsername() {
        return username;
    }
}
