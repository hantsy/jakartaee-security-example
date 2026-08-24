package com.example.application;

import com.example.domain.event.UserEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class DomainEventListener {

    @Inject
    Logger logger;

    public void onUserEvent(@Observes UserEvent event) {
        logger.log(Level.INFO, "UserEvent: " + event);
    }
}
