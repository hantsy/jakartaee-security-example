/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.example.infrastructure.security;

import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import jakarta.security.enterprise.SecurityContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

@Interceptor
@Authorized
public class AuthorizedInterceptor {
    private static final Logger LOGGER = Logger.getLogger(AuthorizedInterceptor.class.getName());

    @Inject
    SecurityContext securityContext;

    @AroundInvoke
    public Object checkAuthorized(InvocationContext ctx) throws Exception {
        LOGGER.log(Level.INFO, "check Authorization....");

        Method method = ctx.getMethod();
        Class<?> declaringClass = method.getDeclaringClass();

        var methodAnnotation = method.getAnnotation(Authorized.class);
        var authorizedAnnotation = methodAnnotation != null ? methodAnnotation : declaringClass.getAnnotation(Authorized.class);

        if (authorizedAnnotation != null) {
            if (securityContext.getCallerPrincipal() == null) {
                LOGGER.log(Level.INFO, "Principal is unauthenticated!!!");
                throw new UnauthorizedException();
            }

            if (Arrays.stream(authorizedAnnotation.roles()).anyMatch(role -> securityContext.isCallerInRole(role))) {
                return ctx.proceed();
            }

            LOGGER.log(Level.INFO, "Authorization failed!!!");
            throw new ForbiddenException();
        }
        return ctx.proceed();
    }
}
