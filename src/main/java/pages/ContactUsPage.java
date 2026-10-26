package pages;

import base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ContactUsPage extends BasePage {

    private final By getInTouchHeader = By.xpath("//div[@class='contact-form']/h2");
    private final By name = By.cssSelector("[data-qa='name']");
    private final By email = By.cssSelector("[data-qa='email']");
    private final By subject = By.cssSelector("[data-qa='subject']");
    private final By message = By.cssSelector("[data-qa='message']");
    private final By submitButton = By.cssSelector("[data-qa='submit-button']");
    private final By successMessage = By.cssSelector("#contact-page .status.alert-success");
    private final By homeButton = By.cssSelector("#form-section a.btn-success");

    public ContactUsPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open Contact Us page")
    public ContactUsPage open() {
        open("/contact_us");
        return this;
    }

    public String headerText() {
        return getText(getInTouchHeader);
    }

    @Step("Submit contact form as '{nameValue}'")
    public ContactUsPage submitForm(String nameValue, String emailValue, String subjectValue, String messageValue) {
        type(name, nameValue);
        type(email, emailValue);
        type(subject, subjectValue);
        type(message, messageValue);
        click(submitButton);
        acceptAlert();
        return this;
    }

    public String successMessageText() {
        return getText(successMessage);
    }

    @Step("Return to home page")
    public HomePage backToHome() {
        click(homeButton);
        return new HomePage(driver);
    }
}
