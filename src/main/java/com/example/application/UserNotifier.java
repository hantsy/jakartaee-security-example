package com.example.application;

import com.example.domain.event.UserEvent;

public interface UserNotifier {

    void notify(UserEvent event);
}
