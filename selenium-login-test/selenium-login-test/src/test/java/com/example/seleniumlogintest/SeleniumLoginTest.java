package com.example.seleniumlogintest;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URL;

public class SeleniumLoginTest {
    private WebDriver driver;

    @BeforeMethod
    public void setUp() throws Exception {
        System.out.println("Bắt đầu thiết lập trình duyệt...");
        System.setProperty("webdriver.chrome.driver", "C:\\drivers\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        System.out.println("Trình duyệt Chrome đã mở và phóng to.");

        // Truy cập file index.html từ resources
        URL resource = getClass().getClassLoader().getResource("index.html");
        if (resource == null) {
            throw new RuntimeException("index.html not found in resources");
        }
        driver.get(resource.toString());
        Thread.sleep(2000);
    }

    @Test
    public void testValidLogin() throws InterruptedException {
        System.out.println("Bắt đầu test đăng nhập hợp lệ...");
        System.out.println("Tìm trường email...");
        WebElement emailField = driver.findElement(By.id("email"));
        System.out.println("Tìm trường số điện thoại...");
        WebElement phoneField = driver.findElement(By.id("phone"));
        System.out.println("Tìm trường mật khẩu...");
        WebElement passwordField = driver.findElement(By.id("password"));
        System.out.println("Tìm nút đăng nhập...");
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        Thread.sleep(1000);

        System.out.println("Nhập email: test@example.com");
        emailField.sendKeys("test@example.com");
        Thread.sleep(1000);
        System.out.println("Nhập số điện thoại: 0123456789");
        phoneField.sendKeys("0123456789");
        Thread.sleep(1000);
        System.out.println("Nhập mật khẩu: Password123");
        passwordField.sendKeys("Password123");
        Thread.sleep(1000);
        System.out.println("Nhấn nút đăng nhập...");
        loginButton.click();
        Thread.sleep(2000);

        System.out.println("Kiểm tra thông báo thành công...");
        WebElement successMessage = driver.findElement(By.id("successMessage"));
        Assert.assertTrue(successMessage.isDisplayed(), "Login failed: Success message not displayed");
        System.out.println("Đăng nhập hợp lệ thành công!");
    }

    @Test
    public void testInvalidLogin() throws InterruptedException {
        System.out.println("Bắt đầu test đăng nhập không hợp lệ...");
        System.out.println("Tìm trường email...");
        WebElement emailField = driver.findElement(By.id("email"));
        System.out.println("Tìm trường số điện thoại...");
        WebElement phoneField = driver.findElement(By.id("phone"));
        System.out.println("Tìm trường mật khẩu...");
        WebElement passwordField = driver.findElement(By.id("password"));
        System.out.println("Tìm nút đăng nhập...");
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        Thread.sleep(1000);

        System.out.println("Nhập email: invalid@example.com");
        emailField.sendKeys("invalid@example.com");
        Thread.sleep(1000);
        System.out.println("Nhập số điện thoại: 9876543210");
        phoneField.sendKeys("9876543210");
        Thread.sleep(1000);
        System.out.println("Nhập mật khẩu: WrongPass");
        passwordField.sendKeys("WrongPass");
        Thread.sleep(1000);
        System.out.println("Nhấn nút đăng nhập...");
        loginButton.click();
        Thread.sleep(2000);

        System.out.println("Kiểm tra thông báo lỗi...");
        WebElement errorMessage = driver.findElement(By.id("errorMessage"));
        Assert.assertTrue(errorMessage.isDisplayed(), "Error message not displayed for invalid login");
        System.out.println("Test đăng nhập không hợp lệ thành công!");
    }

    @Test
    public void testEmptyEmail() throws InterruptedException {
        System.out.println("Bắt đầu test đăng nhập với email trống...");
        WebElement emailField = driver.findElement(By.id("email"));
        WebElement phoneField = driver.findElement(By.id("phone"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        Thread.sleep(1000);

        System.out.println("Nhập số điện thoại: 0123456789");
        phoneField.sendKeys("0123456789");
        Thread.sleep(1000);
        System.out.println("Nhập mật khẩu: Password123");
        passwordField.sendKeys("Password123");
        Thread.sleep(1000);
        System.out.println("Nhấn nút đăng nhập...");
        loginButton.click();
        Thread.sleep(2000);

        System.out.println("Kiểm tra thông báo lỗi...");
        WebElement errorMessage = driver.findElement(By.id("errorMessage"));
        Assert.assertTrue(errorMessage.isDisplayed(), "Error message not displayed for empty email");
        System.out.println("Test đăng nhập với email trống thành công!");
    }

    @Test
    public void testEmptyPhone() throws InterruptedException {
        System.out.println("Bắt đầu test đăng nhập với số điện thoại trống...");
        WebElement emailField = driver.findElement(By.id("email"));
        WebElement phoneField = driver.findElement(By.id("phone"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        Thread.sleep(1000);

        System.out.println("Nhập email: test@example.com");
        emailField.sendKeys("test@example.com");
        Thread.sleep(1000);
        System.out.println("Nhập mật khẩu: Password123");
        passwordField.sendKeys("Password123");
        Thread.sleep(1000);
        System.out.println("Nhấn nút đăng nhập...");
        loginButton.click();
        Thread.sleep(2000);

        System.out.println("Kiểm tra thông báo lỗi...");
        WebElement errorMessage = driver.findElement(By.id("errorMessage"));
        Assert.assertTrue(errorMessage.isDisplayed(), "Error message not displayed for empty phone");
        System.out.println("Test đăng nhập với số điện thoại trống thành công!");
    }

    @Test
    public void testEmptyPassword() throws InterruptedException {
        System.out.println("Bắt đầu test đăng nhập với mật khẩu trống...");
        WebElement emailField = driver.findElement(By.id("email"));
        WebElement phoneField = driver.findElement(By.id("phone"));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));
        Thread.sleep(1000);

        System.out.println("Nhập email: test@example.com");
        emailField.sendKeys("test@example.com");
        Thread.sleep(1000);
        System.out.println("Nhập số điện thoại: 0123456789");
        phoneField.sendKeys("0123456789");
        Thread.sleep(1000);
        System.out.println("Nhấn nút đăng nhập...");
        loginButton.click();
        Thread.sleep(2000);

        System.out.println("Kiểm tra thông báo lỗi...");
        WebElement errorMessage = driver.findElement(By.id("errorMessage"));
        Assert.assertTrue(errorMessage.isDisplayed(), "Error message not displayed for empty password");
        System.out.println("Test đăng nhập với mật khẩu trống thành công!");
    }

    @Test
    public void testEmailPlaceholder() throws InterruptedException {
        System.out.println("Bắt đầu test placeholder trường email...");
        WebElement emailField = driver.findElement(By.id("email"));
        String placeholder = emailField.getAttribute("placeholder");
        Assert.assertEquals(placeholder, "Nhập email", "Email placeholder incorrect");
        System.out.println("Test placeholder trường email thành công!");
        Thread.sleep(1000);
    }

    @Test
    public void testPageLoadTime() throws InterruptedException {
        System.out.println("Bắt đầu test thời gian tải trang...");
        long startTime = System.currentTimeMillis();
        driver.get(getClass().getClassLoader().getResource("index.html").toString());
        long loadTime = System.currentTimeMillis() - startTime;
        Assert.assertTrue(loadTime < 2000, "Page load time exceeds 2 seconds: " + loadTime + "ms");
        System.out.println("Thời gian tải trang: " + loadTime + "ms");
        Thread.sleep(1000);
    }

    @AfterMethod
    public void tearDown() {
        System.out.println("Đóng trình duyệt...");
        if (driver != null) {
            driver.quit();
        }
        System.out.println("Trình duyệt đã đóng.");
    }
}