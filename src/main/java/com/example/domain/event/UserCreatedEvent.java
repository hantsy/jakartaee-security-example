package com.example.domain.event;

import java.time.Instant;

public record UserCreatedEvent(String username, Instant createdAt) implements UserEvent {
}
