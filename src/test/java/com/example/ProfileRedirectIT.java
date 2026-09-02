package com.example;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit5.container.annotation.ArquillianTest;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@ArquillianTest
public class ProfileRedirectIT {
    private static final Logger LOGGER = Logger.getLogger(ProfileRedirectIT.class.getName());

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
        LOGGER.log(Level.FINEST, "deployment Url: {0}", new Object[]{deploymentUrl});

        String profileUrl = deploymentUrl.toExternalForm() + "profile.xhtml";
        LOGGER.log(Level.FINEST, "profile Url: {0}", profileUrl);
        browser.get(profileUrl);

        // it should redirect to login page
        assertThat(browser.getCurrentUrl()).contains("login.xhtml");
    }
}
