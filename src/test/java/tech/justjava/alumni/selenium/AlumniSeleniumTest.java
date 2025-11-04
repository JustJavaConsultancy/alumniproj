package tech.justjava.alumni.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AlumniSeleniumTest {

    private WebDriver driver;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/alumni/start");
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testStartProcess() {
        WebElement documentType = driver.findElement(By.id("documentType"));
        Select documentTypeSelect = new Select(documentType);
        documentTypeSelect.selectByValue("Transcript");

        WebElement paymentGateway = driver.findElement(By.id("paymentGateway"));
        Select paymentGatewaySelect = new Select(paymentGateway);
        paymentGatewaySelect.selectByValue("Remita");

        WebElement alumniId = driver.findElement(By.id("alumniId"));
        alumniId.sendKeys("12345");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        assertEquals("http://localhost:8080/alumni/tasks", driver.getCurrentUrl());
    }

    @Test
    public void testCompleteSubmitRequestTask() {
        driver.get("http://localhost:8080/alumni/tasks");

        WebElement taskLink = driver.findElement(By.linkText("Submit Document Request"));
        taskLink.click();

        WebElement documentType = driver.findElement(By.id("documentType"));
        Select documentTypeSelect = new Select(documentType);
        documentTypeSelect.selectByValue("Transcript");

        WebElement alumniId = driver.findElement(By.id("alumniId"));
        alumniId.sendKeys("12345");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        assertEquals("http://localhost:8080/alumni/tasks", driver.getCurrentUrl());
    }

    @Test
    public void testCompletePaymentTask() {
        driver.get("http://localhost:8080/alumni/tasks");

        WebElement taskLink = driver.findElement(By.linkText("Pay for Transcript"));
        taskLink.click();

        WebElement paymentGateway = driver.findElement(By.id("paymentGateway"));
        Select paymentGatewaySelect = new Select(paymentGateway);
        paymentGatewaySelect.selectByValue("Remita");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        assertEquals("http://localhost:8080/alumni/tasks", driver.getCurrentUrl());
    }

    @Test
    public void testCompletePaymentGatewayTask() {
        driver.get("http://localhost:8080/alumni/tasks");

        WebElement taskLink = driver.findElement(By.linkText("Pay via Remita"));
        taskLink.click();

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        assertEquals("http://localhost:8080/alumni/tasks", driver.getCurrentUrl());
    }

    @Test
    public void testCompleteApprovalTask() {
        driver.get("http://localhost:8080/alumni/tasks");

        WebElement taskLink = driver.findElement(By.linkText("Approve Transcript Request"));
        taskLink.click();

        WebElement approvalStatus = driver.findElement(By.id("approvalStatus"));
        Select approvalStatusSelect = new Select(approvalStatus);
        approvalStatusSelect.selectByValue("Approved");

        WebElement submitButton = driver.findElement(By.cssSelector("button[type='submit']"));
        submitButton.click();

        assertEquals("http://localhost:8080/alumni/tasks", driver.getCurrentUrl());
    }
}
