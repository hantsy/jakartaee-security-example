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
        LOGGER.log(Level.FINEST, "deployment URL: {0}", deploymentUrl);
        // Guard the browser navigation so Graphene initializes the runtime
        //Graphene.guardHttp(browser).get(deploymentUrl.toExternalForm() + "profile.xhtml");

        // The runtime is now active; waitGui will no longer throw an exception
        //Graphene.waitGui();

        String profileUrl = deploymentUrl.toExternalForm() + "/profile.xhtml";
        LOGGER.log(Level.FINEST, "profile URL: {0}", profileUrl);

        browser.get(profileUrl);

        assertThat(browser.getCurrentUrl()).contains("/login.xhtml");
    }
}
