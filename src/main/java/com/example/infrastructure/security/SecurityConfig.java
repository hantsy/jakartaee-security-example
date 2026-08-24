package com.example.infrastructure.security;

import jakarta.annotation.security.DeclareRoles;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import jakarta.security.enterprise.authentication.mechanism.http.LoginToContinue;

@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/login.faces",
                errorPage = "/login.faces?error",
                useForwardToLogin = false // use redirect
        ),
        qualifiers = {WebAuthenticationQualifier.class}
)
@DeclareRoles({"web", "rest"})
@ApplicationScoped
public class SecurityConfig {
}
