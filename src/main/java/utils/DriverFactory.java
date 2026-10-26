package utils;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * Creates one WebDriver per thread so TestNG can run methods in parallel.
 * Driver binaries are resolved by Selenium Manager.
 */
public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();
    private static final int WIDTH = 1920;
    private static final int HEIGHT = 1080;

    private DriverFactory() {
    }

    public static WebDriver initDriver() {
        String browser = ConfigReader.browser().toLowerCase();
        boolean headless = ConfigReader.headless();

        WebDriver driver = switch (browser) {
            case "chrome" -> {
                ChromeDriver chrome = new ChromeDriver(chromeOptions(headless));
                blockAdHosts(chrome);
                yield chrome;
            }
            case "firefox" -> new FirefoxDriver(firefoxOptions(headless));
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };

        driver.manage().window().setSize(new Dimension(WIDTH, HEIGHT));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(ConfigReader.implicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.pageLoadTimeout()));
        DRIVER.set(driver);
        return driver;
    }

    public static WebDriver getDriver() {
        WebDriver driver = DRIVER.get();
        if (driver == null) {
            throw new IllegalStateException("Driver not initialised for thread " + Thread.currentThread().getName());
        }
        return driver;
    }

    public static boolean hasDriver() {
        return DRIVER.get() != null;
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                DRIVER.remove();
            }
        }
    }

    /**
     * First line of defence against Google ads/vignettes: block ad networks at the network layer via CDP.
     * BasePage still removes leftover ad iframes and #google_vignette as a fallback (and for Firefox).
     */
    private static void blockAdHosts(ChromeDriver driver) {
        driver.executeCdpCommand("Network.enable", Map.of());
        driver.executeCdpCommand("Network.setBlockedURLs", Map.of("urls", List.of(
                "*googlesyndication.com*",
                "*doubleclick.net*",
                "*googleadservices.com*",
                "*adservice.google.*",
                "*fundingchoicesmessages.google.com*",
                "*google-analytics.com*",
                "*googletagmanager.com*")));
    }

    private static ChromeOptions chromeOptions(boolean headless) {
        ChromeOptions options = new ChromeOptions();
        // EAGER: do not block on slow third-party ad scripts.
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments(
                "--window-size=" + WIDTH + "," + HEIGHT,
                "--disable-notifications",
                "--disable-popup-blocking",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--disable-search-engine-choice-screen");
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false));
        return options;
    }

    private static FirefoxOptions firefoxOptions(boolean headless) {
        FirefoxOptions options = new FirefoxOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);
        if (headless) {
            options.addArguments("-headless");
        }
        options.addArguments("--width=" + WIDTH, "--height=" + HEIGHT);
        return options;
    }
}
