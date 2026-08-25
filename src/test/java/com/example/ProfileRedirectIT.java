package com.example;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit5.container.annotation.ArquillianTest;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.net.URL;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@ArquillianTest
public class ProfileRedirectIT {

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.createFromZipFile(WebArchive.class,
                new File("target/jakartaee-security-example.war"));
    }

    @ArquillianResource
    private URL deploymentUrl;

    @Drone
    private WebDriver browser;

    @Test
    public void testProfileRedirectsToLogin() {
        // Guard the browser navigation so Graphene initializes the runtime
        Graphene.guardHttp(browser).get(deploymentUrl.toExternalForm() + "index.xhtml");

        // The runtime is now active; waitGui will no longer throw an exception
        Graphene.waitGui();

        // hinting profile directly will raise an error:
        //java.lang.IllegalStateException: Cannot create a session after the response has been committed

        Graphene.guardHttp(browser).get(deploymentUrl.toExternalForm() + "profile.xhtml");
        Graphene.waitGui();

        await().atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> assertThat(browser.getCurrentUrl()).contains("login.xhtml"));
    }
}
