# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Selenium UI test framework for https://www.automationexercise.com/.
Stack: Java 17, Maven, Selenium 4 (Selenium Manager), TestNG, Allure 2 (+ AspectJ agent), Jackson.
Pattern: Page Object Model; data-driven tests read JSON from `src/test/resources/testdata` via `utils.JsonReader`.

## Working rules

- By default the user writes Pages and Tests. Claude's role is infrastructure, code review, debugging and explaining. Don't write page objects or test classes unless explicitly asked. (On 2026-10-26 the user asked Claude to build the initial framework including pages and tests. That was a one-off override.)

## Environment (this machine)

- The `java` on PATH is JDK 8, which is too old. Run Maven with `JAVA_HOME` pointing to JDK 17+, e.g. `~/.jdks/openjdk-25.0.1`. The code compiles with `release 17`.
- No standalone `mvn` on PATH. IntelliJ's bundled one is at `C:\Program Files\JetBrains\IntelliJ IDEA 2025.2.4\plugins\maven\lib\maven3\bin\mvn`.

## Commands

- Full suite: `mvn clean test` (headed), `mvn clean test -Dheadless=true` (what CI runs)
- Single class/method: `mvn test -Dtest=LoginTest` / `mvn test -Dtest=LoginTest#loginWithInvalidCredentials`
- Any `config.properties` key can be overridden with `-D`, e.g. `-Dbrowser=firefox`.
- Report: `mvn allure:serve` or `mvn allure:report`. Results go to `target/allure-results`.

## Architecture

- **Surefire must stay on 3.5.x.** Surefire 3.6+ dropped TestNG suite XML support (`suiteXmlFiles`). The suite is defined in `src/test/resources/testng.xml` (parallel methods, 2 threads) and registers the listeners `TestListener` and `RetryTransformer`. New test classes in package `tests` are picked up automatically.
- The AspectJ `-javaagent` in Surefire's `argLine` is required for Allure `@Step` annotations on page methods to show up.
- `utils.DriverFactory` keeps one driver per thread in a `ThreadLocal`. Always get the driver through it, never store it in static fields. `BaseTest` creates the driver in `@BeforeMethod` and quits it in `@AfterMethod(alwaysRun = true)`.
- `ConfigReader`: a system property overrides the `config.properties` value. `implicitWait` is 0 on purpose, so use the explicit-wait helpers in `BasePage`.
- Ads and vignettes: Chrome blocks ad hosts through CDP (`DriverFactory.blockAdHosts`). `BasePage.handleAds()` removes ad iframes and strips `#google_vignette`, and it runs around every `click()`. It skips when a JS alert is open, because touching the page would auto-dismiss the alert. Page code should use `BasePage.click/type`, not raw `WebElement.click()`.
- `RetryTransformer` applies `RetryAnalyzer` (1 retry) to every test. `TestListener` attaches a screenshot and the URL to Allure on failure.
- Tests that need an account create a throwaway user with `User.newUniqueUser()` (timestamped email) and delete the account at the end.
- CI: `.github/workflows/ci.yml` publishes the Allure report with history to the `gh-pages` branch.
