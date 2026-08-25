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
