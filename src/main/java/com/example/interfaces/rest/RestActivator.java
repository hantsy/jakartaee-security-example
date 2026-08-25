package com.example.interfaces.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import org.eclipse.microprofile.auth.LoginConfig;

@ApplicationPath("/api")
@ApplicationScoped
@LoginConfig(authMethod = "MP-JWT", realmName = "MP JWT Realm")
public class RestActivator extends Application {
}
