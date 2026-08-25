package com.example.interfaces.faces;

import jakarta.faces.FacesException;
import jakarta.faces.application.NavigationHandler;
import jakarta.faces.application.ViewExpiredException;
import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerWrapper;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.event.ExceptionQueuedEvent;
import jakarta.faces.event.ExceptionQueuedEventContext;
import jakarta.security.enterprise.AuthenticationException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FacesExceptionHandler extends ExceptionHandlerWrapper {

    private static final Logger LOG = Logger.getLogger(FacesExceptionHandler.class.getName());

    public FacesExceptionHandler(ExceptionHandler wrapped) {
        super(wrapped);
    }

    @Override
    public void handle() throws FacesException {
        LOG.log(Level.INFO, "invoking custom ExceptionHandler...");
        Iterator<ExceptionQueuedEvent> events = getUnhandledExceptionQueuedEvents().iterator();
        while (events.hasNext()) {
            ExceptionQueuedEvent event = events.next();
            ExceptionQueuedEventContext context = event.getContext();
            Throwable t = context.getException();
            try {
                ViewExpiredException vee = findCause(t, ViewExpiredException.class);
                AuthenticationException auth = findCause(t, AuthenticationException.class);
                if (vee != null) {
                    handleViewExpiredException(vee);
                } else if (auth != null) {
                    handleAuthenticationException(auth);
                } else {
                    handleGenericException(findRootCause(t));
                }
            } finally {
                events.remove();
            }
        }
        getWrapped().handle();
    }

    private static <T extends Throwable> T findCause(Throwable t, Class<T> type) {
        Set<Throwable> seen = new HashSet<>();
        for (Throwable cause = t; cause != null && seen.add(cause); cause = cause.getCause()) {
            if (type.isInstance(cause)) {
                return type.cast(cause);
            }
        }
        return null;
    }

    private static Throwable findRootCause(Throwable t) {
        Set<Throwable> seen = new HashSet<>();
        Throwable root = t;
        for (Throwable cause = t; cause != null && seen.add(cause); cause = cause.getCause()) {
            root = cause;
        }
        return root;
    }

    private void handleViewExpiredException(ViewExpiredException vee) {
        LOG.log(Level.INFO, "Handling ViewExpiredException: {0}", vee.getMessage());
        FacesContext context = FacesContext.getCurrentInstance();

        String viewId = vee.getViewId();
        NavigationHandler nav = context.getApplication().getNavigationHandler();
        nav.handleNavigation(context, null, viewId);

        context.responseComplete();
    }

    private void handleAuthenticationException(AuthenticationException e) {
        LOG.log(Level.INFO, "Handling AuthenticationException: {0}", e.getMessage());
        FacesContext context = FacesContext.getCurrentInstance();

        // URL encode the message safely
        String encodedMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
        String loginViewId = "/login.xhtml?faces-redirect=true&error=" + encodedMessage;

        NavigationHandler nav = context.getApplication().getNavigationHandler();
        nav.handleNavigation(context, null, loginViewId);

        context.responseComplete();
    }

    private void handleGenericException(Throwable e) {
        LOG.log(Level.INFO, "Handling generic exception: {0}", e.getMessage());
        FacesContext facesContext = FacesContext.getCurrentInstance();

        Flash flash = facesContext.getExternalContext().getFlash();
        flash.put("message", e.getMessage());
        flash.put("type", e.getClass().getName());

        NavigationHandler nav = facesContext.getApplication().getNavigationHandler();
        nav.handleNavigation(facesContext, null, "/error.xhtml?faces-redirect=true");
        facesContext.responseComplete();
    }
}
