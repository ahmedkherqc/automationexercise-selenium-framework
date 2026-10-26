package tests;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AccountCreatedPage;
import pages.AccountDeletedPage;
import pages.HomePage;
import pages.SignupLoginPage;
import pages.SignupPage;
import utils.User;

@Feature("User Registration")
public class RegisterTest extends BaseTest {

    @Test(groups = {"smoke", "regression"})
    @Severity(SeverityLevel.BLOCKER)
    @Description("Register a new user with a unique email, verify they are logged in, then delete the account.")
    public void registerNewUserAndDeleteAccount() {
        User user = User.newUniqueUser();

        Assert.assertTrue(home().isLoaded(), "Home page should be visible");

        SignupLoginPage signupLogin = home().goToSignupLogin();
        Assert.assertEquals(signupLogin.signupHeaderText(), "New User Signup!");

        SignupPage signup = signupLogin.signup(user.name, user.email);
        Assert.assertTrue(signup.isLoaded(), "'Enter Account Information' should be visible");

        AccountCreatedPage created = signup.fillAndCreateAccount(user);
        Assert.assertEquals(created.headerText(), "ACCOUNT CREATED!");

        HomePage home = created.clickContinue();
        Assert.assertEquals(home.loggedInUsername(), user.name);

        AccountDeletedPage deleted = home.deleteAccount();
        Assert.assertEquals(deleted.headerText(), "ACCOUNT DELETED!");
    }
}
