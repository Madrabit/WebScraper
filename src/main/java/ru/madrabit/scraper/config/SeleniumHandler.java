package ru.madrabit.scraper.config;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

@Slf4j
public class SeleniumHandler {
    private WebDriver driver;

    public boolean start() {
        try {
            driver = getChromeDriver();
        } catch (Exception e) {
            log.error("Driver was not initialized: {}", e.getMessage());
            return false;
        }
        return true;
    }

    public boolean openPage(String url) {
        try {
            driver.get(url);
            Thread.sleep(5000);  // Let the user actually see something!
            driver.quit();
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

    private WebDriver getChromeDriver() {
        ChromeConfig.setDriverPath();
        return new ChromeDriver(ChromeConfig.getChromeOptions());
    }
}
