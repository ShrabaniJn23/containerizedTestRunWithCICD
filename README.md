# Selenium + Cucumber + TestNG Maven Starter

Minimal starter for Selenium automation using Cucumber (BDD) and TestNG.

## Quick Start

```powershell
cd testFramework
mvn test
```

## Notes
- Requires Java 11+ and Maven installed on your machine
- Tests use WebDriverManager to download browser drivers automatically
- Feature files are in `src/test/resources/features/`
- Step implementations are in `src/test/java/com/example/steps/`
- Hooks (setup/teardown) are in `src/test/java/com/example/hooks/`
