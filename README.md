# AutomationExercise – Selenium UI Test Framework

[![UI Tests](https://github.com/ahmedkherqc/automationexercise-selenium-framework/actions/workflows/ci.yml/badge.svg)](https://github.com/ahmedkherqc/automationexercise-selenium-framework/actions/workflows/ci.yml)
[![Allure Report](https://img.shields.io/badge/Allure-Report-orange)](https://ahmedkherqc.github.io/automationexercise-selenium-framework/)
![Java](https://img.shields.io/badge/Java-17-blue)
![Selenium](https://img.shields.io/badge/Selenium-4-43B02A)
![TestNG](https://img.shields.io/badge/TestNG-7-red)

End-to-end UI test automation for [automationexercise.com](https://www.automationexercise.com/), built with the Page Object Model, data-driven tests from JSON, parallel execution, and Allure reporting published to GitHub Pages on every run.

## Tech stack

| Area | Tool |
|---|---|
| Language / build | Java 17, Maven |
| Browser automation | Selenium 4 (Selenium Manager resolves drivers automatically) |
| Test runner | TestNG 7 (parallel methods, groups, DataProviders, retry) |
| Reporting | Allure 2 + AspectJ (steps, screenshots on failure) |
| Test data | Jackson (JSON) |
| CI/CD | GitHub Actions → Allure report on GitHub Pages |

## Project structure

```
src
├── main/java
│   ├── base/BasePage.java          # explicit-wait helpers, Google ad / vignette handling
│   ├── pages/                      # Page Objects (Home, SignupLogin, Signup, AccountCreated/Deleted,
│   │                               #   Products, ProductDetails, Cart, ContactUs)
│   └── utils/                      # ConfigReader, DriverFactory (ThreadLocal), JsonReader, User
└── test
    ├── java
    │   ├── base/BaseTest.java      # fresh browser per test, always quit
    │   ├── listeners/              # TestListener (screenshot → Allure), RetryAnalyzer + RetryTransformer
    │   └── tests/                  # test classes
    └── resources
        ├── config.properties       # baseUrl, browser, headless, timeouts
        ├── testng.xml              # suite: parallel="methods", thread-count="2"
        └── testdata/*.json         # data-driven inputs
```

## Running the tests

Prerequisites: JDK 17+, Maven 3.9+, and Chrome or Firefox installed.

```bash
# full suite (headed)
mvn clean test

# headless, the way CI runs it
mvn clean test -Dheadless=true

# another browser
mvn clean test -Dbrowser=firefox -Dheadless=true
```

Any key in `config.properties` can be overridden with `-D<key>=<value>`.

## Allure report

- **Online:** the latest CI report, with trend history, is at **https://ahmedkherqc.github.io/automationexercise-selenium-framework/**
- **Locally:**
  ```bash
  mvn allure:serve     # build and open the report in a browser
  mvn allure:report    # static report in target/site/allure-maven-plugin
  ```

Failed tests attach a screenshot and the page URL. Each failed test is retried once.

## Handling ads

The demo site serves Google ads and full-page vignettes (`#google_vignette`) that can intercept clicks. The framework handles them in layers:
1. Chrome blocks the ad networks at the network level (CDP `Network.setBlockedURLs`).
2. `BasePage.handleAds()` removes ad iframes and AdSense slots and reloads the URL without `#google_vignette`. It runs after navigation and around every click.
3. If a click is still intercepted, it falls back to a JavaScript click.

## Test coverage

| Feature | Test | Groups |
|---|---|---|
| Registration | Register a new user (unique timestamped email), verify logged in, delete account | smoke, regression |
| Login | Login with valid credentials (user registered in setup, deleted afterwards) | smoke, regression |
| Login | Login with invalid credentials, data-driven from `invalidLogins.json` | regression |
| Products | All Products page lists products | smoke, regression |
| Products | Product details: name, category, price, availability, condition, brand | regression |
| Products | Search returns only matching products | smoke, regression |
| Cart | Add two products, verify names, prices, quantities, totals | smoke, regression |
| Cart | Add product with quantity 4 from the details page | regression |
| Contact Us | Submit form, accept alert, verify success message | regression |

## CI

`.github/workflows/ci.yml` runs on push, on pull requests, on manual dispatch, and daily at 03:00 UTC. It runs the suite headless on Java 17 and uploads `allure-results` as an artifact. For non-PR runs, it generates the Allure report with history and deploys it to the `gh-pages` branch.

> One-time setup: in **Settings → Pages**, set the source to the `gh-pages` branch.
