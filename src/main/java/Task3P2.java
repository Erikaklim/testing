import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Task3P2 {

    public static <List> void main(String[] args) {
        final Duration DURATION = Duration.ofSeconds(10);
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, DURATION);

        driver.get("https://demoqa.com");

        driver.findElement(By.className("fc-cta-consent")).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fc-dialog-overlay")));

        WebElement elementsElement = driver.findElement(By.xpath("//div[@class='card mt-4 top-card']//h5[text()='Elements']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", elementsElement);
        elementsElement.click();

        driver.findElement(By.xpath("//span[text()='Web Tables']")).click();

        new Select(driver.findElement(By.xpath("//select[@aria-label='rows per page']"))).selectByValue("5");

        for(int i = 0; i < 3; i++){
            driver.findElement(By.id("addNewRecordButton")).click();
            driver.findElement(By.id("firstName")).sendKeys(RandomStringUtils.randomAlphabetic(5));
            driver.findElement(By.id("lastName")).sendKeys(RandomStringUtils.randomAlphabetic(5));
            driver.findElement(By.id("userEmail")).sendKeys("user" + UUID.randomUUID()
                    .toString().replaceAll("-", "") + "@example.com");
            driver.findElement(By.id("age")).sendKeys(String.valueOf(new Random().nextInt(10)));
            driver.findElement(By.id("salary")).sendKeys(String.valueOf(new Random().nextInt(10)));
            driver.findElement(By.id("department")).sendKeys(RandomStringUtils.randomAlphabetic(5));
            WebElement adDiv = driver.findElement(By.id("adplus-anchor"));
            ((JavascriptExecutor)driver).executeScript("arguments[0].style.display='none'", adDiv);
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("adplus-anchor")));
            driver.findElement(By.id("submit")).click();
        }

        WebElement nextElement = driver.findElement(By.xpath("//button[@class='-btn' and text()='Next']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", nextElement);

        java.util.List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
        for (WebElement iframe : iframes) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].style.display='none'", iframe);
        }

        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", nextElement);

        // Delete all elements on page 2
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("rt-tbody")));

        java.util.List<WebElement> deleteButtons = driver.findElements(By.xpath("//span[@data-toggle='tooltip' and contains(@title, 'Delete')]"));
        System.out.println("Number of delete buttons: " + deleteButtons.size());

        for (int i = 0; i < deleteButtons.size(); i++) {
            deleteButtons = driver.findElements(By.xpath("//span[@data-toggle='tooltip' and contains(@title, 'Delete')]"));
            System.out.println("Clicking delete button");
            deleteButtons.get(i).click();
        }

        WebElement page = driver.findElement(By.xpath("//input[@aria-label='jump to page']"));
        page.clear();
        page.sendKeys("1");
    }
}
