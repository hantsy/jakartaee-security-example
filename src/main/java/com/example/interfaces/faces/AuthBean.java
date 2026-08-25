package com.example.interfaces.faces;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.security.enterprise.SecurityContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@RequestScoped
public class AuthBean {

    private static final Logger LOG = Logger.getLogger(AuthBean.class.getName());

    @Inject
    private SecurityContext securityContext;

    public boolean isAuthenticated() {
        return securityContext.getCallerPrincipal() != null;
    }

    public boolean isAdmin() {
        return isAuthenticated() && securityContext.isCallerInRole("admin");
    }

    public String logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.invalidateSession();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        try {
            request.logout();
        } catch (ServletException e) {
            LOG.log(Level.WARNING, "Container logout failed: {0}", e.getMessage());
        }
        return "/login.xhtml?faces-redirect=true";
    }
}
