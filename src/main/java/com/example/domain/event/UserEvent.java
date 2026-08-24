package com.example.domain.event;

public sealed interface UserEvent extends DomainEvent permits UserRegisteredEvent {

    String username();
}
