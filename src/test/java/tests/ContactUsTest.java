package tests;

import base.BaseTest;
import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ContactUsPage;
import pages.HomePage;
import utils.JsonReader;

@Feature("Contact Us")
public class ContactUsTest extends BaseTest {

    @Test(groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Submit the Contact Us form, accept the confirmation alert and see the success message.")
    public void submitContactUsForm() {
        JsonNode data = JsonReader.readTree("contact.json");

        ContactUsPage contact = home().goToContactUs();
        Assert.assertEquals(contact.headerText(), "GET IN TOUCH");

        contact.submitForm(
                data.get("name").asText(),
                data.get("email").asText(),
                data.get("subject").asText(),
                data.get("message").asText());
        Assert.assertEquals(contact.successMessageText(), "Success! Your details have been submitted successfully.");

        HomePage home = contact.backToHome();
        Assert.assertTrue(home.isLoaded(), "Should be back on the home page");
    }
}
