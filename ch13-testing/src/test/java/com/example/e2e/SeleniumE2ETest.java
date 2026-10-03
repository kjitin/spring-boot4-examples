package com.example.e2e;

import com.example.testing.Ch13Application;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

// Requires a local Chrome installation. Run with: mvn verify -Pe2e
@Tag("e2e")
@SpringBootTest(classes = Ch13Application.class, webEnvironment = RANDOM_PORT)
public class SeleniumE2ETest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private String baseUrl;

    @BeforeAll
    static void setupClass() {
        WebDriverManager.chromedriver().setup(); // Automatically downloads and sets up ChromeDriver
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run Chrome in headless mode
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    void shouldDisplayGreetingOnHomePage() {
        driver.get(baseUrl + "/");
        assertThat(driver.findElement(By.tagName("h1")).getText()).isEqualTo("Welcome to Spring Boot App");
    }

    @Test
    void shouldNavigateToAboutPage() {
        driver.get(baseUrl + "/");
        driver.findElement(By.linkText("About")).click();
        assertThat(driver.findElement(By.tagName("h1")).getText()).isEqualTo("About Us");
    }
}
