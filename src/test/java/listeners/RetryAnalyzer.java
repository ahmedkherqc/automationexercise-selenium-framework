package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retries a failed test once to absorb flakiness from the public demo site (ads, slow responses).
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static final int MAX_RETRIES = 1;
    private int attempts = 0;

    @Override
    public boolean retry(ITestResult result) {
        if (attempts < MAX_RETRIES) {
            attempts++;
            return true;
        }
        return false;
    }
}
