package fpt.example.db_protect.models;

import fpt.example.db_protect.models.Sessions;

import java.util.List;

public class SessionRepository {
    private List<Sessions> sessionsList = List.of(
            new Sessions("beadd857-fe35-11ee-a061-110f2c7", "sgfdn0401", "COMPLETED"),
            new Sessions("a3a10809-fe37-11ee-a061-4f9d656", "sgfdn0401", "OPEN"),
            new Sessions("6d96829b-fe3b-11ee-a061-6fb6a0c", "sgfdn0401", "OPEN"),
            new Sessions("1bf1851d-fe3c-11ee-a061-17f5eaf", "sgfdn0401", "OPEN"),
            new Sessions("cdfdcfcf-fe3c-11ee-a061-c3bc7f5", "sgfdn0401", "COMPLETED"),
            new Sessions("a4268d52-03ad-11ef-a07f-2d9239f", "sgfdn0401", "COMPLETED"),
            new Sessions("e80e95d4-03ad-11ef-a07f-21c3234", "sgfdn0401", "OPEN"),
            new Sessions("cb08e648-03b0-11ef-a07f-b96730a", "sgfdn0401", "COMPLETED"),
            new Sessions("b700ceed-03b2-11ef-a07f-2bff6c7", "sgfdn0401", "OPEN"),
            new Sessions("40f85e5f-03b4-11ef-a07f-61ba14d", "sgfdn0401", "COMPLETED"),
            new Sessions("f091fe91-03b8-11ef-a07f-513acba", "sgfdn0401", "OPEN")
    );

    public Sessions getSession(int id) {
        return sessionsList.get((id - 1) % 10);
    }
}
