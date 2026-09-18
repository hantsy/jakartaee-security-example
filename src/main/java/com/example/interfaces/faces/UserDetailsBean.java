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

package com.example.interfaces.faces;

import com.example.application.DeleteUserUseCase;
import com.example.application.GetUserUseCase;
import com.example.application.ToggleUserEnabledUseCase;
import com.example.application.UserNotFoundException;
import com.example.domain.model.UserAccount;
import com.example.infrastructure.security.Authorized;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
@Authorized(roles = "admin")
public class UserDetailsBean {

    private Long id;

    @Inject
    private GetUserUseCase getUserUseCase;

    @Inject
    private ToggleUserEnabledUseCase toggleUserEnabledUseCase;

    @Inject
    private DeleteUserUseCase deleteUserUseCase;

    private UserAccount user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void load() {
        this.user = getUserUseCase.getById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public UserAccount getUser() {
        return user;
    }

    public String toggleEnabled() {
        toggleUserEnabledUseCase.toggle(id);
        return "/users/details.xhtml?id=" + id + "&faces-redirect=true";
    }

    public String delete() {
        deleteUserUseCase.delete(id);
        return "/users/list.xhtml?faces-redirect=true";
    }
}
