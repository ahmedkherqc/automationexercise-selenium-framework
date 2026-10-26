package listeners;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.DriverFactory;

import java.io.ByteArrayInputStream;

/**
 * Attaches a screenshot and the current URL to the Allure report when a test fails.
 * Runs before @AfterMethod, so the driver is still alive.
 */
public class TestListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        attachFailureContext(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        // Retried attempts are reported as skipped; keep their evidence too.
        if (result.wasRetried()) {
            attachFailureContext(result);
        }
    }

    private void attachFailureContext(ITestResult result) {
        if (!DriverFactory.hasDriver()) {
            return;
        }
        try {
            byte[] png = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(result.getName() + " - screenshot", "image/png", new ByteArrayInputStream(png), ".png");
            Allure.addAttachment("URL", "text/plain", DriverFactory.getDriver().getCurrentUrl());
        } catch (RuntimeException e) {
            Allure.addAttachment("Screenshot error", "text/plain", String.valueOf(e.getMessage()));
        }
    }
}
