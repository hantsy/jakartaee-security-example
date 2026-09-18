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

package com.example;

import com.example.interfaces.rest.*;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientProperties;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ArquillianExtension.class)
public class SecurityIT {

    private static final String PASSWORD = "password";

    private static Client client;

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File("target/jakartaee-security-example.war"));
    }

    @ArquillianResource
    private URL baseUrl;

    @BeforeAll
    public static void initClient() {
        client = ClientBuilder.newClient().property(ClientProperties.FOLLOW_REDIRECTS, false);
    }

    @AfterAll
    public static void closeClient() {
        if (client != null) {
            client.close();
        }
    }

    private String target(String path) {
        return baseUrl.toExternalForm() + path;
    }

    private String obtainToken(String username, String password) {
        try (Response response = client.target(target("api/token"))
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(new TokenRequest(username, password)))) {
            assertThat(response.getStatus()).isEqualTo(200);
            return response.readEntity(TokenResponse.class).token();
        }
    }

    @Test
    @RunAsClient
    public void testTokenEndpoint() {
        String token = obtainToken("user", PASSWORD);
        assertThat(token).isNotBlank();
    }

    @Test
    @RunAsClient
    public void testTokenEndpointRejectsInvalidCredentials() {
        try (Response response = client.target(target("api/token"))
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(new TokenRequest("user", "wrong-password")))) {
            assertThat(response.getStatus()).isEqualTo(400);
        }
    }

    @Test
    @RunAsClient
    public void testRegisterEndpoint() {
        String username = "registered-" + System.currentTimeMillis();
        RegisterRequest request = new RegisterRequest(username, username + "@example.com", "pass123", "pass123");
        try (Response response = client.target(target("api/register"))
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(request))) {
            assertThat(response.getStatus()).isEqualTo(201);
            RegisterResponse registerResponse = response.readEntity(RegisterResponse.class);
            assertThat(registerResponse.username()).isEqualTo(username);
            assertThat(registerResponse.roles()).containsExactly("user");
        }
    }

    @Test
    @RunAsClient
    public void testMeEndpoint() {
        String token = obtainToken("user", PASSWORD);
        try (Response response = client.target(target("api/me"))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get()) {
            assertThat(response.getStatus()).isEqualTo(200);
            MeResponse me = response.readEntity(MeResponse.class);
            assertThat(me.username()).isEqualTo("user");
            assertThat(me.roles()).contains("user");
        }
    }

    @Test
    @RunAsClient
    public void testMeEndpointWithoutToken() {
        try (Response response = client.target(target("api/me"))
                .request(MediaType.APPLICATION_JSON)
                .get()) {
            assertThat(response.getStatus()).isEqualTo(401);
        }
    }

    @Test
    @RunAsClient
    public void testIndexPage() {
        try (Response response = client.target(target("index.xhtml")).request().get()) {
            assertThat(response.getStatus()).isEqualTo(200);
            // assertThat(response.getHeaderString("Location")).contains("login");
        }
    }

    /*
    @Test
    @RunAsClient
    public void testProfileRedirectsToLogin() {
        try (Response response = client.target(target("profile.xhtml")).request().get()) {
            assertThat(response.getStatusInfo().getFamily()).isEqualTo(Response.Status.Family.REDIRECTION);
            // assertThat(response.getHeaderString("Location")).contains("login");
        }
    }
    */
}
