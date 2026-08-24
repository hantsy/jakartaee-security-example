package com.example.interfaces.faces;

import com.example.application.DeleteUserUseCase;
import com.example.application.GetUserUseCase;
import com.example.application.ToggleUserEnabledUseCase;
import com.example.application.UserNotFoundException;
import com.example.domain.model.UserAccount;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.omnifaces.cdi.Param;

@Named
@RequestScoped
public class UserDetailsBean {

    @Inject
    @Param(pathName = "id")
    private Long id;

    @Inject
    private GetUserUseCase getUserUseCase;

    @Inject
    private ToggleUserEnabledUseCase toggleUserEnabledUseCase;

    @Inject
    private DeleteUserUseCase deleteUserUseCase;

    private UserAccount user;

    @PostConstruct
    public void init() {
        this.user = getUserUseCase.getById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    public UserAccount getUser() {
        return user;
    }

    public String toggleEnabled() {
        toggleUserEnabledUseCase.toggle(id);
        return "/users/" + id + "?faces-redirect=true";
    }

    public String delete() {
        deleteUserUseCase.delete(id);
        return "/users?faces-redirect=true";
    }
}
