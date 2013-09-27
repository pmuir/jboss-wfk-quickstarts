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
package org.jboss.as.quickstarts.kitchensink.test;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.net.URL;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.waitGui;

@RunAsClient
@RunWith(Arquillian.class)
public class KitchensinkSpringControllerAdviceTest {

    @Drone
    WebDriver browser;

    /**
     * Injects URL on which application is running.
     */
    @ArquillianResource
    URL contextPath;

    @Deployment(testable = false)
    public static WebArchive deployment() {
        return Deployments.kitchensink();
    }

    @FindBy(id = "name")
    WebElement nameField;

    @FindBy(id = "email")
    WebElement emailField;

    @FindBy(id = "phoneNumber")
    WebElement phoneField;

    @FindBy(id = "datepicker")
    WebElement datePicker;

    @FindByJQuery("input.register")
    WebElement registerButton;

    @FindByJQuery("table.simpletablestyle:first tbody tr")
    List<WebElement> tableMembersRows;

    @FindByJQuery("table.simpletablestyle:first tbody tr:first td")
    List<WebElement> tableMembersRowColumns;

    @FindBy(id = "name.errors")
    WebElement nameErrorMessage;

    @FindBy(id = "email.errors")
    WebElement emailErrorMessage;

    @FindBy(id = "phoneNumber.errors")
    WebElement phoneErrorMessage;

    @FindBy(id = "date.errors")
    WebElement dateErrorMessage;

    @FindByJQuery("a.ui-state-highlight")
    WebElement today;

    /**
     * Name of the member to register in the right format.
     */
    private static final String NAME_FORMAT_OK = "John Doe";

    /**
     * Name of the member to register in the bad format.
     */
    private static final String NAME_FORMAT_BAD = "John1";

    /**
     * Name of the member to register which is too long (1-25)
     */
    private static final String NAME_FORMAT_TOO_LONG = "John Doe John Doe John Doe";

    /**
     * E-mail of the member to register in the right format.
     */
    private static final String EMAIL_FORMAT_OK = "john@doe.com";

    /**
     * E-mail of the member to register in the bad format - #1.
     */
    private static final String EMAIL_FORMAT_BAD_1 = "joe";

    /**
     * E-mail of the member to register in the bad format - #2.
     */
    private static final String EMAIL_FORMAT_BAD_2 = "john@doe.com ";

    /**
     * Phone number of the member to register in the right format.
     */
    private static final String PHONE_FORMAT_OK = "0123456789";

    /**
     * Phone number of the member to register in the bad format - illegal
     * characters.
     */
    private static final String PHONE_FORMAT_BAD_ILLEGAL_CHARS = "as/df.123@";

    /**
     * Phone number of the member to register in the bad format - too long.
     */
    private static final String PHONE_FORMAT_BAD_TOO_LONG = "12345678901234567890";

    /**
     * Phone nuber of the member to register in the bad format - too short
     */
    private static final String PHONE_FORMAT_BAD_TOO_SHORT = "123456789";


    /**
     * This method tests there is no new member in the registration table when
     * all three input fields are empty.
     */
    @Test
    @InSequence(1)
    public void testEmptyRegistration() {
        browser.get(contextPath.toString());
        guardHttp(registerButton).click();
        assertTrue("Name validation message should be present", nameErrorMessage.isDisplayed());
        assertTrue("Email validation message should be present", emailErrorMessage.isDisplayed());
        assertTrue("PhoneNumber validation message should be present", phoneErrorMessage.isDisplayed());
        assertTrue("Date validation message should be present", dateErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());
    }

    /**
     * This method tests registration of the new member with the name of bad
     * formats.
     */
    @Test
    @InSequence(2)
    public void testRegistrationWithBadNameFormat() {
        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_BAD, EMAIL_FORMAT_OK, PHONE_FORMAT_OK, true);
        guardHttp(registerButton).click();
        assertTrue("Name validation message should be present", nameErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());

        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_TOO_LONG, EMAIL_FORMAT_OK, PHONE_FORMAT_OK, true);
        guardHttp(registerButton).click();
        assertTrue("Name validation message should be present", nameErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());
    }

    /**
     * This method tests registration of the new member with the email of bad
     * format.
     */
    @Test
    @InSequence(3)
    public void testRegistrationWithBadEmailFormat() {
        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_BAD_1, PHONE_FORMAT_OK, true);
        guardHttp(registerButton).click();
        assertTrue(emailErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());

        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_BAD_2, PHONE_FORMAT_OK, true);
        guardHttp(registerButton).click();
        assertTrue(emailErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());
    }

    /**
     * This method tests registration of the new member with the phone of bad
     * format
     */
    @Test
    @InSequence(4)
    public void testRegistrationWithBadPhoneFormat() {
        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_BAD_ILLEGAL_CHARS, true);
        guardHttp(registerButton).click();
        assertTrue(phoneErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());

        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_BAD_TOO_SHORT, true);
        guardHttp(registerButton).click();
        assertTrue(phoneErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());

        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_BAD_TOO_LONG, true);
        guardHttp(registerButton).click();
        assertTrue(phoneErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());
    }


    /**
     * Tests registration of the new member without registration date
     */
    @Test
    @InSequence(5)
    public void testRegistrationWithoutDate() {
        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_OK, false);
        guardHttp(registerButton).click();
        assertTrue(dateErrorMessage.isDisplayed());
        assertEquals("Member should not be registered", 1, tableMembersRows.size());
    }

    /**
     * This method tests regular registration process
     */
    @Test
    @InSequence(6)
    public void testRegularRegistration() {
        browser.get(contextPath.toString());
        setInputFields(NAME_FORMAT_OK, EMAIL_FORMAT_OK, PHONE_FORMAT_OK, true);
        guardHttp(registerButton).click();

        assertEquals(2, tableMembersRows.size());
        assertEquals(6, tableMembersRowColumns.size());

        assertTrue((tableMembersRowColumns.get(1)).getText().equals(NAME_FORMAT_OK));
        assertTrue((tableMembersRowColumns.get(2)).getText().equals(EMAIL_FORMAT_OK));
        assertTrue((tableMembersRowColumns.get(3)).getText().equals(PHONE_FORMAT_OK));
    }

    /**
     * This helper method sets values into the according input fields.
     * @param name name to set into the name input field
     * @param email email to set into the email input field
     * @param phone phone to set into the phone input field
     * @param fillDate specifies whether the registration date will be set
     */
    private void setInputFields(String name, String email, String phone, boolean fillDate) {
        nameField.clear();
        nameField.sendKeys(name);
        emailField.clear();
        emailField.sendKeys(email);
        phoneField.clear();
        phoneField.sendKeys(phone);
        if (fillDate) {
            datePicker.click();
            waitGui().until().element(today).is().visible();
            today.click();
            waitGui().until().element(today).is().not().visible();
        }
    }

}
