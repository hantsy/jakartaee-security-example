package com.example.infrastructure;

import com.example.application.NotificationCollector;
import com.example.application.UserNotifier;
import com.example.domain.event.UserEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Alternative
@ApplicationScoped
public class EmailUserNotifier implements UserNotifier {

    private static final Logger LOG = Logger.getLogger(EmailUserNotifier.class.getName());

    @Inject
    private NotificationCollector collector;

    @Override
    public void notify(UserEvent event) {
        LOG.info("Sending email for user event: " + event.username());
        collector.add("email: " + event.username());
    }

    public void onUserEvent(@Observes UserEvent event) {
        notify(event);
    }
}
