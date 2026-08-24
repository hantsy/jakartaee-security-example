package com.example.infrastructure.security;

import com.example.domain.repository.UserAccountRepository;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.security.enterprise.SecurityContext;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

@Interceptor
@Authenticated
public class AuthenticatedInterceptor {
    private static final Logger LOGGER = Logger.getLogger(AuthenticatedInterceptor.class.getName());

    @Inject
    SecurityContext securityContext;

    @Inject
    UserAccountRepository repository;

    @AroundInvoke
    public Object checkAuthenticated(InvocationContext ctx) throws Exception {
        LOGGER.log(Level.INFO, "Enter AuthenticatedInterceptor....");

        Method method = ctx.getMethod();
        Class<?> declaringClass = method.getDeclaringClass();

        var methodAnnotation = method.getAnnotation(Authenticated.class);
        var authenticatedAnnotation = methodAnnotation != null ? methodAnnotation : declaringClass.getAnnotation(Authenticated.class);

        if (authenticatedAnnotation != null) {
            if (securityContext.getCallerPrincipal() == null) {
                LOGGER.log(Level.INFO, "Principal is unauthenticated!!!");
                throw new UnauthorizedException();
            }

            var user = repository.findByUsername(securityContext.getCallerPrincipal().getName())
                    .orElseThrow(UnauthorizedException::new);
            if (!user.isEnabled()) {
                LOGGER.log(Level.INFO, "Account is disabled!!!");
                throw new UnauthorizedException("Account is disabled");
            }
        }

        return ctx.proceed();
    }
}
