package com.example.application;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class NotificationCollector {

    private final List<String> notifications = new ArrayList<>();

    public void add(String notification) {
        notifications.add(notification);
    }

    public List<String> getNotifications() {
        return notifications;
    }
}
