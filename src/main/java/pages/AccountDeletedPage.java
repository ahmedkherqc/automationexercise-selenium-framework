package pages;

import base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountDeletedPage extends BasePage {

    private final By header = By.cssSelector("[data-qa='account-deleted']");
    private final By continueButton = By.cssSelector("[data-qa='continue-button']");

    public AccountDeletedPage(WebDriver driver) {
        super(driver);
    }

    public String headerText() {
        return getText(header);
    }

    @Step("Continue after account deletion")
    public HomePage clickContinue() {
        click(continueButton);
        return new HomePage(driver);
    }
}
