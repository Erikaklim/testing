import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Task3P1 {
    public static void main(String[] args){
        final Duration DURATION = Duration.ofSeconds(10);

        WebDriverManager.chromedriver().setup();

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, DURATION);

        driver.get("https://demoqa.com/");

        driver.findElement(By.className("fc-cta-consent")).click();

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("fc-dialog-overlay")));

        WebElement widgetsElement = driver.findElement(By.xpath("//div[@class='card mt-4 top-card']//h5[text()='Widgets']"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", widgetsElement);
        widgetsElement.click();

        driver.findElement(By.xpath("//span[text()='Progress Bar']")).click();
        driver.findElement(By.id("startStopButton")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("resetButton"))).click();

//        wait.until((ExpectedCondition<Boolean>) driver1 -> {
//            String progressBarValue = driver1.findElement(By.xpath("//div[@role='progressbar']")).getAttribute("aria-valuenow");
//            return Integer.parseInt(progressBarValue) == 100;
//        });
//
//        driver.findElement(By.id("resetButton")).click();

        String progressBarValue = driver.findElement(By.xpath("//div[@role='progressbar']")).getAttribute("aria-valuenow");
        System.out.println("Progress bar value after reset: " + progressBarValue);

        driver.quit();

    }
}
