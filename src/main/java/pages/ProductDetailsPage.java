package pages;

import base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductDetailsPage extends BasePage {

    private final By name = By.cssSelector(".product-information h2");
    private final By category = By.xpath("//div[@class='product-information']/p[contains(., 'Category')]");
    private final By price = By.cssSelector(".product-information > span > span");
    private final By availability = By.xpath("//div[@class='product-information']/p[b[contains(., 'Availability')]]");
    private final By condition = By.xpath("//div[@class='product-information']/p[b[contains(., 'Condition')]]");
    private final By brand = By.xpath("//div[@class='product-information']/p[b[contains(., 'Brand')]]");
    private final By quantity = By.id("quantity");
    private final By addToCartButton = By.cssSelector(".product-information button.cart");
    private final By viewCartInModal = By.cssSelector("#cartModal a[href='/view_cart']");

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    public String name() {
        return getText(name);
    }

    public String category() {
        return getText(category);
    }

    public String price() {
        return getText(price);
    }

    public String availability() {
        return valueAfterLabel(getText(availability));
    }

    public String condition() {
        return valueAfterLabel(getText(condition));
    }

    public String brand() {
        return valueAfterLabel(getText(brand));
    }

    @Step("Set quantity to {qty} and add to cart")
    public CartPage addToCartWithQuantity(int qty) {
        type(quantity, String.valueOf(qty));
        click(addToCartButton);
        click(viewCartInModal);
        return new CartPage(driver);
    }

    private static String valueAfterLabel(String text) {
        int idx = text.indexOf(':');
        return idx >= 0 ? text.substring(idx + 1).trim() : text;
    }
}
