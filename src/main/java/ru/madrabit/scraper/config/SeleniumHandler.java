package ru.madrabit.scraper.config;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
public class SeleniumHandler {
    private WebDriver driver;
    private Wait<WebDriver> wait;
    public static final int WAIT_TIME_MAX = 6;

    public boolean start() {
        try {
            driver = getChromeDriver();
            wait = new WebDriverWait(driver, WAIT_TIME_MAX);
        } catch (Exception e) {
            log.error("Driver was not initialized: {}", e.getMessage());
            return false;
        }
        return true;
    }

    public boolean openPage(String url) {
        try {
            driver.get(url);
        } catch (Exception e) {
            log.warn("Error opening page: {}", url);
            return false;
        }
        return true;
    }

    public void stop() {
        if (driver != null) {
            driver.quit();
        }
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    private WebDriver getChromeDriver() {
        ChromeConfig.setDriverPath();
        return new ChromeDriver(ChromeConfig.getChromeOptions());
    }

    public void click(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", element);
    }

    public WebElement getElement(String path) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(path)));
    }

    public void jumpToResult() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        js.executeScript("document.querySelectorAll(\".watu-question\").forEach(e => e.parentNode.removeChild(e));" +
                "window.confirm = () => true; document.querySelector(\'.watupro-submit-button:not([style*=\"display: none\"]\').click();");

    }
}
