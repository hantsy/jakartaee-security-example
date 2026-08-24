package com.example.interfaces.faces;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters.withParams;

@Named
@RequestScoped
public class LoginBean {

    @NotNull
    @Size(min = 3, max = 15, message = "Username must be between 3 and 15 characters")
    private String username;

    @NotNull
    @Size(min = 5, max = 50, message = "Password must be between 5 and 50 characters")
    private String password;

    @Inject
    Logger LOG;

    @Inject
    private SecurityContext securityContext;

    @Inject
    FacesContext facesContext;

    @Inject
    ExternalContext externalContext;

    public void login() {
        Credential credential = new UsernamePasswordCredential(username, new Password(password));

        AuthenticationStatus status = securityContext.authenticate(
                (HttpServletRequest) externalContext.getRequest(),
                (HttpServletResponse) externalContext.getResponse(),
                withParams().credential(credential).newAuthentication(true)
        );

        LOG.log(Level.INFO, "Login bean authentication status: {0}", status);
        switch (status) {
            case NOT_DONE -> LOG.log(Level.INFO, "do noting: {0}", status);
            case SEND_CONTINUE -> facesContext.responseComplete();
            case SEND_FAILURE -> addError(facesContext, "Authentication failed");
            case SUCCESS -> {
                try {
                    externalContext.redirect("profile.xhtml");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static void addError(FacesContext context, String message) {
        context.addMessage(
                null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null)
        );
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
