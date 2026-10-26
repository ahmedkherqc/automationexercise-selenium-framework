package pages;

import base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SignupLoginPage extends BasePage {

    private final By loginHeader = By.xpath("//div[@class='login-form']/h2");
    private final By signupHeader = By.xpath("//div[@class='signup-form']/h2");
    private final By loginEmail = By.cssSelector("[data-qa='login-email']");
    private final By loginPassword = By.cssSelector("[data-qa='login-password']");
    private final By loginButton = By.cssSelector("[data-qa='login-button']");
    private final By signupName = By.cssSelector("[data-qa='signup-name']");
    private final By signupEmail = By.cssSelector("[data-qa='signup-email']");
    private final By signupButton = By.cssSelector("[data-qa='signup-button']");
    private final By loginError = By.xpath("//form[@action='/login']/p");

    public SignupLoginPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open Signup / Login page")
    public SignupLoginPage open() {
        open("/login");
        return this;
    }

    public String loginHeaderText() {
        return getText(loginHeader);
    }

    public String signupHeaderText() {
        return getText(signupHeader);
    }

    @Step("Start signup with name '{name}' and email '{email}'")
    public SignupPage signup(String name, String email) {
        type(signupName, name);
        type(signupEmail, email);
        click(signupButton);
        return new SignupPage(driver);
    }

    @Step("Log in with email '{email}'")
    public HomePage login(String email, String password) {
        submitLogin(email, password);
        return new HomePage(driver);
    }

    @Step("Attempt invalid login with email '{email}'")
    public SignupLoginPage loginExpectingError(String email, String password) {
        submitLogin(email, password);
        return this;
    }

    public String loginErrorText() {
        return getText(loginError);
    }

    private void submitLogin(String email, String password) {
        type(loginEmail, email);
        type(loginPassword, password);
        click(loginButton);
    }
}
