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

import java.util.Iterator;
import java.util.Objects;
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
            Throwable cause = context.getException();
            while (cause != null) {
                LOG.log(Level.INFO, "Caught exception:", cause.getClass().getName());
                if (cause instanceof ViewExpiredException vee) {
                    try {
                        handleViewExpiredException(vee);
                        break;
                    } finally {
                        events.remove();
                    }
                } else if (cause instanceof AuthenticationException ae) {
                    try {
                        handleAuthenticationException(ae);
                        break;
                    } finally {
                        events.remove();
                    }
                }
                cause = cause.getCause();
            }
        }
        getWrapped().handle();
    }


    private void handleViewExpiredException(ViewExpiredException vee) {
        FacesContext context = FacesContext.getCurrentInstance();
        String viewId = vee.getViewId();
        NavigationHandler nav = context.getApplication().getNavigationHandler();
        nav.handleNavigation(context, null, viewId);
        context.renderResponse();
    }

    private void handleAuthenticationException(AuthenticationException e) {
        FacesContext context = FacesContext.getCurrentInstance();
        String loginViewId = "/login.xhtml?faces-redirect=true";
        NavigationHandler nav = context.getApplication().getNavigationHandler();
        nav.handleNavigation(context, null, loginViewId);
        context.getViewRoot().getViewMap(true).put("errors", e.getMessage());
        context.renderResponse();
    }

}
