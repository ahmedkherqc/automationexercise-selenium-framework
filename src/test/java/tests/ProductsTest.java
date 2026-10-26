package tests;

import base.BaseTest;
import com.fasterxml.jackson.databind.JsonNode;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ProductDetailsPage;
import pages.ProductsPage;
import utils.JsonReader;

import java.util.List;

@Feature("Products")
public class ProductsTest extends BaseTest {

    private final JsonNode data = JsonReader.readTree("products.json");

    @Test(groups = {"smoke", "regression"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("The All Products page lists products.")
    public void verifyAllProductsPage() {
        ProductsPage products = home().goToProducts();

        Assert.assertEquals(products.titleText(), "ALL PRODUCTS");
        Assert.assertTrue(products.productNames().size() > 0, "Product list should not be empty");
    }

    @Test(groups = {"regression"})
    @Severity(SeverityLevel.NORMAL)
    @Description("The product details page shows name, category, price, availability, condition and brand.")
    public void viewProductDetails() {
        JsonNode expected = data.get("productDetails");

        ProductDetailsPage details = home().goToProducts().viewProduct(1);

        Assert.assertTrue(details.currentUrl().contains("/product_details/1"), "Should be on product 1 details");
        Assert.assertEquals(details.name(), expected.get("name").asText());
        Assert.assertEquals(details.category(), expected.get("category").asText());
        Assert.assertEquals(details.price(), expected.get("price").asText());
        Assert.assertEquals(details.availability(), expected.get("availability").asText());
        Assert.assertEquals(details.condition(), expected.get("condition").asText());
        Assert.assertEquals(details.brand(), expected.get("brand").asText());
    }

    @Test(groups = {"smoke", "regression"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Searching for a product shows only matching results.")
    public void searchProduct() {
        String term = data.get("searchTerm").asText();

        ProductsPage products = home().goToProducts().search(term);

        Assert.assertEquals(products.titleText(), "SEARCHED PRODUCTS");
        List<String> names = products.productNames();
        Assert.assertFalse(names.isEmpty(), "Search should return results");
        names.forEach(name -> Assert.assertTrue(name.toLowerCase().contains(term.toLowerCase()),
                "Result '" + name + "' should contain '" + term + "'"));
    }
}
