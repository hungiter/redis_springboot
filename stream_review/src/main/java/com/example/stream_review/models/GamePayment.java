package com.example.stream_review.models;

import com.example.stream_review.models.Card.Card;
import com.example.stream_review.models.Game.Game;
import com.example.stream_review.models.Game.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GamePayment {
    Player player;
    Game game;
    Card card;

    // Self-defined
    public int cardPrice() {
        return card.getPrice();
    }

    public String cardSupplier() {
        return card.getSupplier();
    }

    public String playerName() {
        return player.getUsername();
    }

    public String gameTitle() {
        return game.name();
    }
}
