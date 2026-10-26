package pages;

import base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class CartPage extends BasePage {

    private final By cartRows = By.cssSelector("#cart_info_table tbody tr[id^='product-']");
    private final By emptyCart = By.id("empty_cart");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open cart")
    public CartPage open() {
        open("/view_cart");
        return this;
    }

    public int itemCount() {
        return waitAllVisible(cartRows).size();
    }

    public List<String> productNames() {
        return waitAllVisible(By.cssSelector("#cart_info_table td.cart_description h4 a"))
                .stream().map(e -> e.getText().trim()).toList();
    }

    public String price(int productId) {
        return getText(cell(productId, "cart_price"));
    }

    public int quantity(int productId) {
        return Integer.parseInt(getText(cell(productId, "cart_quantity")));
    }

    public String total(int productId) {
        return getText(cell(productId, "cart_total"));
    }

    public boolean isEmpty() {
        return isDisplayed(emptyCart);
    }

    private By cell(int productId, String cssClass) {
        return By.cssSelector("tr#product-" + productId + " td." + cssClass + " > *");
    }
}
