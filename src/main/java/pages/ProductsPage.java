package pages;

import base.BasePage;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ProductsPage extends BasePage {

    private final By pageTitle = By.cssSelector(".features_items h2.title");
    private final By productNames = By.cssSelector(".features_items .productinfo p");
    private final By searchInput = By.id("search_product");
    private final By searchButton = By.id("submit_search");
    private final By continueShoppingButton = By.cssSelector("#cartModal button.close-modal");
    private final By viewCartInModal = By.cssSelector("#cartModal a[href='/view_cart']");

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    @Step("Open All Products page")
    public ProductsPage open() {
        open("/products");
        return this;
    }

    public String titleText() {
        return getText(pageTitle);
    }

    public List<String> productNames() {
        return waitAllVisible(productNames).stream().map(WebElement::getText).map(String::trim).toList();
    }

    @Step("Search for product '{term}'")
    public ProductsPage search(String term) {
        type(searchInput, term);
        click(searchButton);
        waitUrlContains("search=");
        return this;
    }

    @Step("View details of product #{productId}")
    public ProductDetailsPage viewProduct(int productId) {
        click(By.cssSelector("a[href='/product_details/" + productId + "']"));
        return new ProductDetailsPage(driver);
    }

    @Step("Add product #{productId} to cart and continue shopping")
    public ProductsPage addToCartAndContinue(int productId) {
        click(addToCartButton(productId));
        click(continueShoppingButton);
        wait.until(d -> !d.findElement(By.id("cartModal")).isDisplayed());
        return this;
    }

    @Step("Add product #{productId} to cart and open cart")
    public CartPage addToCartAndViewCart(int productId) {
        click(addToCartButton(productId));
        click(viewCartInModal);
        return new CartPage(driver);
    }

    private By addToCartButton(int productId) {
        return By.cssSelector(".productinfo a.add-to-cart[data-product-id='" + productId + "']");
    }
}
