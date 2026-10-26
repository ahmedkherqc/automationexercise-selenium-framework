package pages;

import base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage extends BasePage {

    private final By sliderCarousel = By.id("slider-carousel");
    private final By signupLoginLink = By.cssSelector("a[href='/login']");
    private final By productsLink = By.cssSelector("a[href='/products']");
    private final By cartLink = By.cssSelector("#header a[href='/view_cart']");
    private final By contactUsLink = By.cssSelector("a[href='/contact_us']");
    private final By logoutLink = By.cssSelector("a[href='/logout']");
    private final By deleteAccountLink = By.cssSelector("a[href='/delete_account']");
    private final By loggedInAs = By.xpath("//a[contains(., 'Logged in as')]");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Step("Open home page")
    public HomePage open() {
        open("/");
        return this;
    }

    public boolean isLoaded() {
        return isDisplayed(sliderCarousel);
    }

    @Step("Go to Signup / Login")
    public SignupLoginPage goToSignupLogin() {
        click(signupLoginLink);
        return new SignupLoginPage(driver);
    }

    @Step("Go to Products")
    public ProductsPage goToProducts() {
        click(productsLink);
        return new ProductsPage(driver);
    }

    @Step("Go to Cart")
    public CartPage goToCart() {
        click(cartLink);
        return new CartPage(driver);
    }

    @Step("Go to Contact Us")
    public ContactUsPage goToContactUs() {
        click(contactUsLink);
        return new ContactUsPage(driver);
    }

    public boolean isLoggedIn() {
        return isDisplayed(loggedInAs);
    }

    public String loggedInUsername() {
        return getText(loggedInAs).replace("Logged in as", "").trim();
    }

    @Step("Log out")
    public SignupLoginPage logout() {
        click(logoutLink);
        return new SignupLoginPage(driver);
    }

    @Step("Delete account")
    public AccountDeletedPage deleteAccount() {
        click(deleteAccountLink);
        return new AccountDeletedPage(driver);
    }
}
