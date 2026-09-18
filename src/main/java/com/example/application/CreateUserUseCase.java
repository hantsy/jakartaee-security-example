/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.example.application;

import com.example.domain.event.UserCreatedEvent;
import com.example.domain.model.RoleType;
import com.example.domain.model.UserAccount;
import com.example.domain.repository.UserAccountRepository;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Set;

@UseCase
public class CreateUserUseCase {

    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Inject
    private UserAccountRepository repository;

    @Inject
    private Pbkdf2PasswordHash passwordHash;

    @Inject
    private Event<UserCreatedEvent> userCreatedEvent;

    public String create(String username, String email, Set<RoleType> roles) {
        String password = generatePassword();
        repository.save(new UserAccount(username, email, passwordHash.generate(password.toCharArray()), roles));
        userCreatedEvent.fire(new UserCreatedEvent(username, Instant.now()));
        return password;
    }

    private String generatePassword() {
        StringBuilder sb = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            sb.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}
