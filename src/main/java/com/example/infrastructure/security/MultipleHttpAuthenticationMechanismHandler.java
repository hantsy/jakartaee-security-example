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

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Alternative;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationException;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import jakarta.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanismHandler;
import jakarta.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.glassfish.api.security.jwt.MicroProfileJwtAuthenticationMechanism;

import java.util.logging.Level;
import java.util.logging.Logger;

import static jakarta.interceptor.Interceptor.Priority.APPLICATION;

// A single HttpAuthenticationMechanismHandler that dispatches between the
// web (form) and REST (JWT) authentication mechanisms based on the request path.
@Alternative
@Priority(APPLICATION)
@ApplicationScoped
public class MultipleHttpAuthenticationMechanismHandler implements HttpAuthenticationMechanismHandler {
    private static final Logger LOGGER = Logger.getLogger(MultipleHttpAuthenticationMechanismHandler.class.getName());

    @Inject
    @MicroProfileJwtAuthenticationMechanism
    //@Named("jwtAuthenticationMechanism")
    private HttpAuthenticationMechanism restAuthenticationMechanism;

    @Inject
    @WebAuthenticationMechanism
    private HttpAuthenticationMechanism webAuthenticationMechanism;

    @Override
    public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response, HttpMessageContext httpMessageContext) throws AuthenticationException {
        String path = request.getRequestURI().substring(request.getContextPath().length());
        LOGGER.log(Level.INFO, "The request path(without context path): {0}", path);

        if (path.startsWith("/api")) {
            LOGGER.log(Level.INFO, "Handling authentication using MicroProfileJwtAuthenticationMechanism...");
            return restAuthenticationMechanism.validateRequest(request, response, httpMessageContext);
        }

        LOGGER.log(Level.INFO, "Handling authentication using WebAuthenticationQualifier HttpAuthenticationMechanism...");
        return webAuthenticationMechanism.validateRequest(request, response, httpMessageContext);
    }
}
