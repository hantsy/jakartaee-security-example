package com.example.domain.event;

import java.time.Instant;

public record UserRegisteredEvent(String username, Instant registeredAt) implements UserEvent {
}
