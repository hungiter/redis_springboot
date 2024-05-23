package com.example.stream_review.models;

import com.example.stream_review.models.Card.Card;
import com.example.stream_review.models.Card.CardSupplier;
import com.example.stream_review.models.Card.CardValue;
import com.example.stream_review.models.Game.Game;
import com.example.stream_review.models.Game.Player;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Random;

@Repository
public class GamePaymentRepository {
    private final List<Player> playerList = List.of(
            new Player("kirito"), new Player("asuna"),
            new Player("klein"), new Player("agil"),
            new Player("silica"), new Player("lisbeth"),
            new Player("kayaba"), new Player("sinon"),
            new Player("shino"), new Player("alice"),
            new Player("cardinal"), new Player("yuuki")
    );

    public GamePayment generateRandomPayment() {
        Player randomPlayer = playerList.get(new Random().nextInt(playerList.size()));
        Game randomGame = EnumUtil.getRandomEnumValue(Game.class);
        Card randomCard = new Card(EnumUtil.getRandomEnumValue(CardValue.class), EnumUtil.getRandomEnumValue(CardSupplier.class));
        return new GamePayment(randomPlayer, randomGame, randomCard);
    }
}
