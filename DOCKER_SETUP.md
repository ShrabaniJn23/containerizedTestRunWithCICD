# WebDriver Setup - Local and Docker Compatible

This project supports both **Local WebDriver** and **Docker/Selenium Grid** execution modes.

## Configuration

The driver mode is configured via `src/test/resources/config.properties`:

```properties
driver.mode=local    # or 'remote' for Docker
docker.host=http://localhost:4444
docker.browser=chrome
headless=false
```

---

## 1. Local Execution (Default)

### Setup
1. Ensure Chrome browser is installed
2. WebDriverManager will automatically download the matching ChromeDriver

### Run Tests
```bash
mvn clean test
```

### Run Headless
Edit `src/test/resources/config.properties`:
```properties
driver.mode=local
headless=true
```

Then run:
```bash
mvn clean test
```

---

## 2. Docker/Selenium Grid Execution

### Start Selenium Grid
```bash
docker-compose up
```

This starts:
- **Selenium Hub**: http://localhost:4444
- **Chrome Node**: Browser accessible via VNC on port 7900
- **Firefox Node**: Browser accessible via VNC on port 7901

### Update Configuration
Edit `src/test/resources/config.properties`:
```properties
driver.mode=remote
docker.host=http://localhost:4444
docker.browser=chrome
```

### Run Tests Against Grid
```bash
mvn clean test
```

### View Browser in VNC
Use any VNC viewer and connect to:
- Chrome: `localhost:7900`
- Firefox: `localhost:7901`

Default VNC password: `secret`

### Stop Selenium Grid
```bash
docker-compose down
```

---

## 3. Docker Test Execution (Full Container)

Run tests entirely within Docker:

```bash
docker-compose up --build
```

This will:
1. Build the test image
2. Start Selenium Grid
3. Run tests in a container connected to the Grid

---

## Project Structure

```
testFramework/
├── src/test/java/com/example/
│   ├── hooks/
│   │   └── TestHooks.java         # Sets up local or remote WebDriver
│   ├── steps/
│   │   └── SampleSteps.java       # Step definitions
│   ├── utils/
│   │   └── DriverManager.java     # WebDriver management
│   ├── config/
│   │   └── ConfigManager.java     # Reads properties
│   └── runner/
│       └── TestRunner.java        # Cucumber runner
├── src/test/resources/
│   ├── config.properties          # Configuration
│   ├── testng.xml
│   └── features/
│       └── sample.feature
├── docker-compose.yml             # Selenium Grid setup
├── Dockerfile                      # Test container image
└── pom.xml
```

---

## Switching Between Modes

### Local Mode (Default)
```bash
# Edit config.properties
driver.mode=local
mvn clean test
```

### Remote Mode (Docker)
```bash
# Start Selenium Grid
docker-compose up &

# Edit config.properties
driver.mode=remote
docker.host=http://localhost:4444

# Run tests
mvn clean test
```

---

## Troubleshooting

### Issue: "Could not connect to Docker host"
- Ensure Docker Desktop is running
- Verify docker-compose is installed: `docker-compose --version`

### Issue: "Connection refused on localhost:4444"
- Start Selenium Grid: `docker-compose up`
- Wait 30 seconds for services to be ready

### Issue: "Chrome version mismatch"
- Local mode: WebDriverManager will handle it automatically
- Remote mode: Ensure Selenium image matches your needs

### View Logs
```bash
# Hub logs
docker logs selenium-hub

# Chrome node logs
docker logs selenium-chrome-node
```

---

## Advanced Configuration

Edit `src/test/resources/config.properties` for more options:

```properties
# Driver mode
driver.mode=local

# Local settings
browser=chrome
headless=false

# Remote settings
docker.host=http://localhost:4444
docker.browser=chrome
docker.platform=WINDOWS
```

---

## Running on CI/CD

For CI/CD pipelines (GitHub Actions, Jenkins, GitLab CI):

```yaml
# Example: GitHub Actions
- name: Start Selenium Grid
  run: docker-compose up -d

- name: Run Tests
  run: mvn clean test

- name: Stop Selenium Grid
  run: docker-compose down
```

---

**Created:** February 18, 2026  
**Supported Browsers:** Chrome, Firefox  
**Selenium Version:** 4.15.0
