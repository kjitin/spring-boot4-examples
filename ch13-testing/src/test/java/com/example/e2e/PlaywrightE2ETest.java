package com.example.e2e;

import com.example.testing.Ch13Application;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

// Playwright downloads its own browsers on first run. Run with: mvn verify -Pe2e
@Tag("e2e")
@SpringBootTest(classes = Ch13Application.class, webEnvironment = RANDOM_PORT)
public class PlaywrightE2ETest {

    @LocalServerPort
    private int port;

    private Playwright playwright;
    private Browser browser;
    private Page page;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        page = browser.newPage();
        baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    void tearDown() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @Test
    void shouldDisplayGreetingOnHomePage() {
        page.navigate(baseUrl + "/");
        assertThat(page.locator("h1")).hasText("Welcome to Spring Boot App");
    }

    @Test
    void shouldNavigateToAboutPage() {
        page.navigate(baseUrl + "/");
        page.locator("text=About").click();
        assertThat(page.locator("h1")).hasText("About Us");
    }
}
