package com.example;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ArquillianExtension.class)
public class SecurityIT {

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        final String WEBAPP_SRC = "src/main/webapp";
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true,
                        "com.example.domain",
                        "com.example.application",
                        "com.example.infrastructure",
                        "com.example.interfaces")
                .addAsWebInfResource("test-beans.xml", "beans.xml")
                .addAsWebInfResource("test-web.xml", "web.xml")
                .addAsWebInfResource("test-faces-config.xml", "faces-config.xml")
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsResource("META-INF/microprofile-config.properties", "META-INF/microprofile-config.properties")
                .addAsResource("publickey.pem", "publickey.pem")
                .addAsResource("privatekey.pem", "privatekey.pem")
                .addAsWebResource(new File(WEBAPP_SRC, "login.xhtml"))
                .addAsWebResource(new File(WEBAPP_SRC, "profile.xhtml"));
    }

    @ArquillianResource
    private URL baseUrl;

    @Test
    @RunAsClient
    public void testTokenEndpoint() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl.toExternalForm() + "api/token"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"username\":\"restuser\",\"password\":\"password\"}"))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode(), "token endpoint should issue a JWT for valid credentials");
    }
}
