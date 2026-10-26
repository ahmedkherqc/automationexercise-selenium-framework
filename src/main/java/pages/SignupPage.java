package pages;

import base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.User;

/**
 * "Enter Account Information" form shown after the initial signup step.
 */
public class SignupPage extends BasePage {

    private final By accountInfoHeader = By.xpath("//h2[b[normalize-space()='Enter Account Information']]");
    private final By titleMr = By.id("id_gender1");
    private final By titleMrs = By.id("id_gender2");
    private final By password = By.cssSelector("[data-qa='password']");
    private final By days = By.cssSelector("[data-qa='days']");
    private final By months = By.cssSelector("[data-qa='months']");
    private final By years = By.cssSelector("[data-qa='years']");
    private final By newsletter = By.id("newsletter");
    private final By optin = By.id("optin");
    private final By firstName = By.cssSelector("[data-qa='first_name']");
    private final By lastName = By.cssSelector("[data-qa='last_name']");
    private final By company = By.cssSelector("[data-qa='company']");
    private final By address = By.cssSelector("[data-qa='address']");
    private final By address2 = By.cssSelector("[data-qa='address2']");
    private final By country = By.cssSelector("[data-qa='country']");
    private final By state = By.cssSelector("[data-qa='state']");
    private final By city = By.cssSelector("[data-qa='city']");
    private final By zipcode = By.cssSelector("[data-qa='zipcode']");
    private final By mobile = By.cssSelector("[data-qa='mobile_number']");
    private final By createAccountButton = By.cssSelector("[data-qa='create-account']");

    public SignupPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(accountInfoHeader);
    }

    @Step("Fill account information and create account")
    public AccountCreatedPage fillAndCreateAccount(User user) {
        handleAds();
        click("Mrs".equalsIgnoreCase(user.title) ? titleMrs : titleMr);
        type(password, user.password);
        selectByValue(days, user.day);
        selectByVisibleText(months, user.month);
        selectByValue(years, user.year);
        click(newsletter);
        click(optin);
        type(firstName, user.firstName);
        type(lastName, user.lastName);
        type(company, user.company);
        type(address, user.address);
        type(address2, user.address2);
        selectByVisibleText(country, user.country);
        type(state, user.state);
        type(city, user.city);
        type(zipcode, user.zipcode);
        type(mobile, user.mobile);
        click(createAccountButton);
        return new AccountCreatedPage(driver);
    }
}
