package com.example.interfaces.faces;

import com.example.application.ListUsersUseCase;
import com.example.domain.model.UserAccount;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.List;

@Named
@RequestScoped
public class UsersBean {

    @Inject
    private ListUsersUseCase listUsersUseCase;

    public List<UserAccount> getUsers() {
        return listUsersUseCase.list();
    }
}
