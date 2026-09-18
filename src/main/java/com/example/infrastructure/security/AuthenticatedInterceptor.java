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
import java.util.logging.Level;
import java.util.logging.Logger;

@Interceptor
@Authenticated
public class AuthenticatedInterceptor {
    private static final Logger LOGGER = Logger.getLogger(AuthenticatedInterceptor.class.getName());

    @Inject
    SecurityContext securityContext;

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
        }

        return ctx.proceed();
    }
}
