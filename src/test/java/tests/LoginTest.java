package tests;

import base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.SignupLoginPage;
import utils.JsonReader;
import utils.User;

import java.util.Map;

@Feature("Login")
public class LoginTest extends BaseTest {

    @DataProvider(name = "invalidLogins", parallel = true)
    public Object[][] invalidLogins() {
        return JsonReader.toDataProvider("invalidLogins.json");
    }

    @Test(groups = {"smoke", "regression"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Register a user, log out, then log back in with valid credentials. The account is deleted at the end.")
    public void loginWithValidCredentials() {
        User user = registerAndLogout();

        HomePage home = new SignupLoginPage(driver()).login(user.email, user.password);
        Assert.assertTrue(home.isLoggedIn(), "User should be logged in");
        Assert.assertEquals(home.loggedInUsername(), user.name);

        Assert.assertEquals(home.deleteAccount().headerText(), "ACCOUNT DELETED!");
    }

    @Test(groups = {"regression"}, dataProvider = "invalidLogins")
    @Severity(SeverityLevel.NORMAL)
    @Description("Login with invalid credentials (data-driven from invalidLogins.json) shows an error message.")
    public void loginWithInvalidCredentials(Map<String, Object> data) {
        String email = (String) data.get("email");
        String password = (String) data.get("password");
        Allure.parameter("email", email);

        SignupLoginPage page = home().goToSignupLogin();
        Assert.assertEquals(page.loginHeaderText(), "Login to your account");

        page.loginExpectingError(email, password);
        Assert.assertEquals(page.loginErrorText(), "Your email or password is incorrect!");
    }

    /** Test setup: creates a throwaway account through the UI and logs out, leaving the browser on the login page. */
    private User registerAndLogout() {
        User user = User.newUniqueUser();
        Allure.step("Setup: register user " + user.email, () -> {
            HomePage home = home().goToSignupLogin()
                    .signup(user.name, user.email)
                    .fillAndCreateAccount(user)
                    .clickContinue();
            Assert.assertTrue(home.isLoggedIn(), "Setup user should be logged in after registration");
            home.logout();
        });
        return user;
    }
}
