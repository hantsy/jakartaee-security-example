package com.example.infrastructure;

import com.example.application.NotificationCollector;
import com.example.application.UserNotifier;
import com.example.domain.event.UserEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@ApplicationScoped
public class LoggingUserNotifier implements UserNotifier {

    private static final Logger LOG = Logger.getLogger(LoggingUserNotifier.class.getName());

    @Inject
    private NotificationCollector collector;

    @Override
    public void notify(UserEvent event) {
        LOG.info("User event: " + event.username());
        collector.add("logging: " + event.username());
    }

    public void onUserEvent(@Observes UserEvent event) {
        notify(event);
    }
}
