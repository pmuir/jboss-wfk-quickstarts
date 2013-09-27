/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.quickstarts.erraihelloworld.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.File;
import java.net.URL;

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.junit.Assert.*;

/**
 * Tests Errai Hello World quickstart
 * 
 * @author Oliver Kiss
 */
@RunAsClient
@RunWith(Arquillian.class)
public class HelloworldErraiTest {

    /**
     * Locator for name input field
     */
    @FindBy(tagName = "input")
    WebElement input;

    /**
     * Locator for submit button
     */
    @FindBy(tagName = "button")
    WebElement button;

    /**
     * Locator for hello message
     */
    @FindByJQuery("form div.gwt-Label")
    WebElement message;

    /**
     * Name to be entered
     */
    private static final String NAME = "John Doe";

    private static final String DEPLOYMENT_WAR = "../target/jboss-helloworld-errai.war";

    @Drone
    WebDriver browser;

    /**
     * Injects URL on which application is running.
     */
    @ArquillianResource
    URL contextPath;

    @Deployment(testable = false)
    public static WebArchive helloWorld() {
        return ShrinkWrap.createFromZipFile(WebArchive.class, new File(DEPLOYMENT_WAR));
    }

    @Test
    public void sayHelloTest() {
        browser.get(contextPath.toString());
        input.clear();
        input.sendKeys(NAME);

        guardAjax(button).click();

        assertEquals("Entered name does not match.", "Hello " + NAME + "!", message.getText());
    }

}
