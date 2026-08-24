package com.example.interfaces.faces;

import com.example.application.CreateUserUseCase;
import com.example.domain.model.RoleType;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class CreateUserBean {

    @Inject
    private CreateUserUseCase createUserUseCase;

    private String username;
    private String email;
    private RoleType role = RoleType.REST;
    private String generatedPassword;

    public String create() {
        this.generatedPassword = createUserUseCase.create(username, email, role);
        return null;
    }

    public RoleType[] getRoles() {
        return RoleType.values();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public String getGeneratedPassword() {
        return generatedPassword;
    }
}
