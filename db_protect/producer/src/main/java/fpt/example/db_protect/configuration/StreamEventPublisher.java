package fpt.example.db_protect.configuration;

import fpt.example.db_protect.models.Sessions;

public interface StreamEventPublisher {
    void transferData();
    Sessions transferSession();
}
