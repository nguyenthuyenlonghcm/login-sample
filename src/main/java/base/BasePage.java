package base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

public class BasePage {
    public WebDriverWait getWait(WebDriver driver, long timeOut) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeOut));
    }

    public WebElement waitForVisibilityOfElementLocated(WebDriver driver, By locator, long timeOut) {
        WebDriverWait wait = getWait(driver, timeOut);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForElementToBeClickable(WebDriver driver, By locator, long timeOut) {
        WebDriverWait wait = getWait(driver, timeOut);
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void sendKeys(WebDriver driver, By locator, long timeOut, String text) {
        WebElement element = waitForVisibilityOfElementLocated(driver, locator, timeOut);
        element.sendKeys(text);
    }

    public void click(WebDriver driver, By locator, long timeOut) {
        WebElement element = waitForElementToBeClickable(driver, locator, timeOut);
        element.click();
    }

    public String getText(WebDriver driver, By locator, long timeOut) {
        WebElement element = waitForVisibilityOfElementLocated(driver, locator, timeOut);
        return element.getText();
    }

    public boolean isElementVisibility(WebDriver driver, By locator, long timeOut) {
        WebDriverWait wait = getWait(driver, timeOut);
        try {
            // Wait for the element to be visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isFirstLogin(WebDriver driver, By wrongPassToastLocator, long timeOut) {
        return !isElementVisibility(driver, wrongPassToastLocator, timeOut);
    }

    public boolean isTrustedDevice(WebDriver driver, By logoutBtnLocator, long timeOut) {
        return isElementVisibility(driver, logoutBtnLocator, timeOut);
    }
}