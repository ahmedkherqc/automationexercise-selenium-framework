package tests;

import base.BaseTest;
import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.ProductsPage;
import utils.JsonReader;

@Feature("Cart")
public class CartTest extends BaseTest {

    private final JsonNode data = JsonReader.readTree("products.json");

    @Test(groups = {"smoke", "regression"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Add two products to the cart and check names, prices, quantities and totals.")
    public void addProductsToCart() {
        JsonNode first = data.get("cartProducts").get(0);
        JsonNode second = data.get("cartProducts").get(1);

        ProductsPage products = home().goToProducts();
        CartPage cart = products
                .addToCartAndContinue(first.get("id").asInt())
                .addToCartAndViewCart(second.get("id").asInt());

        Assert.assertEquals(cart.itemCount(), 2);
        for (JsonNode product : data.get("cartProducts")) {
            int id = product.get("id").asInt();
            Assert.assertTrue(cart.productNames().contains(product.get("name").asText()),
                    "Cart should contain " + product.get("name").asText());
            Assert.assertEquals(cart.price(id), product.get("price").asText());
            Assert.assertEquals(cart.quantity(id), 1);
            Assert.assertEquals(cart.total(id), product.get("price").asText());
        }
    }

    @Test(groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Add a product with quantity 4 from its details page and check the quantity in the cart.")
    public void verifyProductQuantityInCart() {
        CartPage cart = home().goToProducts().viewProduct(1).addToCartWithQuantity(4);

        Assert.assertEquals(cart.quantity(1), 4);
        Assert.assertEquals(cart.total(1), "Rs. 2000");
    }
}
