# 📱 Wikipedia Mobile Test Automation

A mobile test automation framework for the **Wikipedia Android app** built with Java, Appium, and TestNG — following the Page Object Model (POM) design pattern.

---

## 🛠️ Tools & Technologies

| Tool | Version | Purpose |
|------|---------|---------|
| Java | JDK 11 | Programming language |
| Appium | 8.6.0 (Java Client) | Mobile automation framework |
| TestNG | 7.8.0 | Test execution and reporting |
| Maven | 3.9.x | Build and dependency management |
| Android Studio | Latest | Emulator and Android SDK |
| UIAutomator2 | Latest | Android automation driver |
| GitHub Actions | - | CI/CD pipeline |

---

## 📋 Prerequisites

Before running this project, make sure the following are installed:

- **Java JDK 11** — [Download](https://adoptium.net)
- **Maven** — [Download](https://maven.apache.org/download.cgi)
- **IntelliJ IDEA** — [Download](https://www.jetbrains.com/idea/download)
- **Node.js** — [Download](https://nodejs.org)
- **Appium** — Install via npm:
  ```bash
  npm install -g appium
  appium driver install uiautomator2
  ```
- **Android Studio** — [Download](https://developer.android.com/studio)
  - Android SDK (API 30)
  - Android Emulator (Pixel 6, API 30)

---

## 📁 Project Structure

```
mobile-testing/
├── src/
│   ├── main/java/
│   │   └── com/wikipedia/
│   │       ├── pages/
│   │       │   ├── OnboardingPage.java
│   │       │   ├── SearchPage.java
│   │       │   └── ArticlePage.java
│   │       └── utils/
│   │           └── BaseTest.java
│   └── test/java/
│       └── com/wikipedia/
│           └── tests/
│               └── WikipediaTests.java
├── .github/
│   └── workflows/
│       └── ci.yml
├── pom.xml
├── testng.xml
└── README.md
```

---

## ⚙️ Setup Instructions

### 1. Clone the Repository

```bash
git clone https://github.com/YourUsername/wikipedia-appium-tests.git
cd wikipedia-appium-tests
```

### 2. Install Maven Dependencies

```bash
mvn clean install
```

### 3. Set Up Android Environment Variables

Add the following to your system environment variables:

```
ANDROID_HOME = C:\Users\<YourName>\AppData\Local\Android\Sdk
PATH += %ANDROID_HOME%\platform-tools
PATH += %ANDROID_HOME%\emulator
```

### 4. Create Android Emulator

1. Open **Android Studio** → Virtual Device Manager
2. Create a **Pixel 6** device with **API 30 (Android 11)**
3. Start the emulator and wait for it to fully boot

### 5. Install Wikipedia APK

Download the Wikipedia alpha APK and install it:

```bash
adb install path/to/app-alpha-universal-release.apk
```

Verify installation:

```bash
adb shell pm list packages | findstr wikipedia
# Expected: package:org.wikipedia.alpha
```

### 6. Disable Stylus Dialog (Important!)

Run this command to prevent a stylus popup from interfering with tests:

```bash
adb shell settings put secure stylus_handwriting_enabled 0
```

---

## ▶️ How to Run Tests Locally

### Step 1: Start the Android Emulator

Open Android Studio → Virtual Device Manager → Start the Pixel 6 API 30 emulator.

### Step 2: Start Appium Server

```bash
appium
```

You should see: `Appium REST http interface listener started on 0.0.0.0:4723`

### Step 3: Run Tests

**Via Maven:**
```bash
mvn test
```

**Via IntelliJ:**
Right-click on `testng.xml` → Run

**Via TestNG XML directly:**
```bash
mvn test -DsuiteXmlFile=testng.xml
```

---

## 🧪 Test Cases

| # | Test Name | Description |
|---|-----------|-------------|
| 1 | testSkipOnboarding | Verifies the onboarding screen can be skipped |
| 2 | testSearchBarVisible | Verifies search bar is visible and tappable |
| 3 | testSearchReturnsResults | Verifies search returns results for a query |
| 4 | testSearchResultTitleNotEmpty | Verifies first search result has a non-empty title |
| 5 | testOpenArticleFromSearch | Verifies tapping a result opens an article |
| 6 | testArticleTitleMatchesSearch | Verifies article title matches the search term |
| 7 | testSearchForCity | Verifies search works for a city name |
| 8 | testSearchForTechnology | Verifies search works for a technology topic |
| 9 | testSearchForScientist | Verifies search works for a scientist name |
| 10 | testSaveArticle | Verifies an article can be saved |

---

## 🏗️ Framework Design

### Page Object Model (POM)

Each screen of the app has its own Page class that encapsulates:
- Element locators (`@AndroidFindBy`)
- Actions that can be performed on that screen

This avoids code duplication and makes tests easier to maintain.

### BaseTest

All test classes extend `BaseTest`, which handles:
- Appium `AndroidDriver` initialization (`@BeforeClass`)
- Driver teardown (`@AfterClass`)
- Shared capabilities and configuration

### App Capabilities

```java
AppPackage:  org.wikipedia.alpha
AppActivity: org.wikipedia.main.MainActivity
Platform:    Android
Automation:  UIAutomator2
Device:      emulator-5554
```

---

## 🔄 CI/CD Pipeline

The project uses **GitHub Actions** for continuous integration.

### Trigger Events

- Every **push** to `main` branch
- Every **Pull Request** targeting `main`

### Pipeline Steps

1. Checkout source code
2. Set up Java 11 (Temurin)
3. Cache Maven dependencies
4. Compile the project (`mvn clean compile`)
5. Build verification (`mvn test`)

### Pipeline File

Located at: `.github/workflows/ci.yml`

> **Note:** Full Appium UI tests require a running Android emulator, which is not available in the standard GitHub Actions environment. The CI pipeline performs build verification and compilation checks. For full test execution, run locally with a connected emulator.

---

## 🌿 Git Workflow

### Branching Strategy

```
main          ← protected, production-ready
develop       ← integration branch
feature/*     ← individual feature branches
```

### Rules

- ❌ No direct commits to `main`
- ✅ All changes go through Pull Requests
- ✅ Each feature developed in its own branch
- ✅ Meaningful commit messages required
- ✅ At least one reviewer required before merge

### Example Workflow

```bash
# Create a feature branch
git checkout -b feature/search-tests

# Make changes, then commit
git add .
git commit -m "add search page tests for city and scientist queries"

# Push and open Pull Request
git push origin feature/search-tests
```

### Commit Message Format

```
<type>: <short description>

Examples:
feat: add article save test
fix: update search input locator for alpha APK
docs: update README with setup instructions
test: add onboarding skip test case
```

---

## 🐛 Common Issues & Fixes

| Issue | Fix |
|-------|-----|
| `Activity does not exist` | Change `appPackage` to `org.wikipedia.alpha` |
| `Instrumentation process not running` | Use API 30 emulator, not API 36 |
| Stylus dialog blocking search | Run `adb shell settings put secure stylus_handwriting_enabled 0` |
| `NoSuchElementException` | Verify element IDs using Appium Inspector |
| Appium server not connecting | Ensure Appium is running on port 4723 |

---

## 📸 Screenshots

