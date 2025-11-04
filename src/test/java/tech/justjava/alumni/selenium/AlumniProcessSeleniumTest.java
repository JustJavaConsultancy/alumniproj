package tech.justjava.alumni.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlumniProcessSeleniumTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    @BeforeEach
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testSubmitDocumentRequest() {
        driver.get("http://localhost:" + port + "/alumni/requestForm");

        WebElement documentType = driver.findElement(By.id("documentType"));
        documentType.sendKeys("Transcript");

        WebElement paymentGateway = driver.findElement(By.id("paymentGateway"));
        paymentGateway.sendKeys("Remita");

        WebElement alumniId = driver.findElement(By.id("alumniId"));
        alumniId.sendKeys("12345");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        assertTrue(driver.getPageSource().contains("Process started successfully"));
    }

    @Test
    public void testCompleteTask() {
        driver.get("http://localhost:" + port + "/api/alumni/tasks/12345");

        WebElement taskLink = driver.findElement(By.linkText("Complete Task"));
        taskLink.click();

        assertTrue(driver.getPageSource().contains("Task completed successfully"));
    }
}
