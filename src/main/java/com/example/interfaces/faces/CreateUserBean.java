package com.example.interfaces.faces;

import com.example.application.CreateUserUseCase;
import com.example.domain.model.RoleType;
import com.example.infrastructure.security.Authorized;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.HashSet;
import java.util.Set;

@Named
@RequestScoped
@Authorized(roles = "admin")
public class CreateUserBean {

    @Inject
    private CreateUserUseCase createUserUseCase;

    private String username;
    private String email;
    private Set<RoleType> roles = new HashSet<>();
    private String generatedPassword;

    public String create() {
        this.generatedPassword = createUserUseCase.create(username, email, roles);
        return null;
    }

    public RoleType[] getAllRoles() {
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

    public Set<RoleType> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleType> roles) {
        this.roles = roles;
    }

    public String getGeneratedPassword() {
        return generatedPassword;
    }
}
