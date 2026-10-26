package base;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

import java.time.Duration;
import java.util.List;

/**
 * Common page behaviour: explicit-wait helpers and Google ad / vignette handling.
 */
public abstract class BasePage {

    private static final String VIGNETTE_FRAGMENT = "#google_vignette";

    /** Removes ad iframes, AdSense slots and the vignette overlay; returns how many nodes were removed. */
    private static final String REMOVE_ADS_JS = """
            let removed = 0;
            const selectors = [
              'iframe[id^="aswift"]', 'iframe[src*="googleads"]', 'iframe[src*="doubleclick"]',
              'iframe[name^="google_ads"]', 'ins.adsbygoogle', '#ad_position_box', '#google_vignette',
              'div[id^="google_ads"]', 'div[class*="google-auto-placed"]', '.fc-consent-root'
            ];
            selectors.forEach(s => document.querySelectorAll(s).forEach(e => { e.remove(); removed++; }));
            document.body && (document.body.style.overflow = 'auto');
            return removed;
            """;

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.explicitWait()));
    }

    // ---------- navigation ----------

    protected void open(String path) {
        driver.get(ConfigReader.baseUrl().replaceAll("/$", "") + path);
        handleAds();
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }

    public String title() {
        return driver.getTitle();
    }

    // ---------- ad handling ----------

    /**
     * Dismisses the Google vignette (URL ends with #google_vignette) by reloading the clean URL,
     * then strips any remaining ad iframes from the DOM.
     */
    public void handleAds() {
        if (isAlertPresent()) {
            // Touching the page now would auto-dismiss a JS alert the test is about to handle.
            return;
        }
        String url = driver.getCurrentUrl();
        if (url != null && url.contains(VIGNETTE_FRAGMENT)) {
            driver.navigate().to(url.replace(VIGNETTE_FRAGMENT, ""));
        }
        try {
            js().executeScript(REMOVE_ADS_JS);
        } catch (RuntimeException ignored) {
            // Page may be mid-navigation; ads are cleaned on the next interaction.
        }
    }

    // ---------- waits ----------

    protected WebElement waitVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected List<WebElement> waitAllVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    protected void waitUrlContains(String fragment) {
        wait.until(ExpectedConditions.urlContains(fragment));
    }

    protected boolean isDisplayed(By locator) {
        try {
            return waitVisible(locator).isDisplayed();
        } catch (RuntimeException e) {
            return false;
        }
    }

    // ---------- actions ----------

    protected void click(By locator) {
        handleAds();
        WebElement element = waitClickable(locator);
        scrollIntoView(element);
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            // An ad overlay slipped in between; clean up and fall back to a JS click.
            handleAds();
            js().executeScript("arguments[0].click();", element);
        }
        handleAds();
    }

    protected void type(By locator, String text) {
        WebElement element = waitVisible(locator);
        scrollIntoView(element);
        element.clear();
        element.sendKeys(text);
    }

    protected String getText(By locator) {
        return waitVisible(locator).getText().trim();
    }

    protected void selectByVisibleText(By locator, String text) {
        new Select(waitVisible(locator)).selectByVisibleText(text);
    }

    protected void selectByValue(By locator, String value) {
        new Select(waitVisible(locator)).selectByValue(value);
    }

    protected void scrollIntoView(WebElement element) {
        js().executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    protected void acceptAlert() {
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }

    protected JavascriptExecutor js() {
        return (JavascriptExecutor) driver;
    }
}
