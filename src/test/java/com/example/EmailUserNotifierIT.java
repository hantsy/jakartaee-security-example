package com.example;

import com.example.application.NotificationCollector;
import com.example.domain.event.DomainEvent;
import com.example.domain.event.UserEvent;
import com.example.domain.event.UserRegisteredEvent;
import com.example.infrastructure.EmailUserNotifier;
import com.example.infrastructure.LoggingUserNotifier;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ArquillianExtension.class)
public class EmailUserNotifierIT {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(DomainEvent.class, UserEvent.class, UserRegisteredEvent.class, NotificationCollector.class, LoggingUserNotifier.class, EmailUserNotifier.class)
                .addAsWebInfResource("test-beans-email.xml", "beans.xml");
    }

    @Inject
    private Event<UserRegisteredEvent> userRegisteredEvent;

    @Inject
    private NotificationCollector collector;

    @Test
    public void testEmailNotifierActivated() {
        userRegisteredEvent.fire(new UserRegisteredEvent("testuser", Instant.now()));

        assertTrue(collector.getNotifications().contains("email: testuser"),
                "email notifier should be activated");
    }
}
