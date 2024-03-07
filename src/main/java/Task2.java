import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class Task2 {
    public static void main(String[] args){

        final int MIN_PRICE = 99;
        final Duration DURATION = Duration.ofSeconds(10);

        WebDriverManager.chromedriver().setup();

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, DURATION);

        driver.get("https://demowebshop.tricentis.com");

        driver.findElement(By.linkText("Gift Cards")).click();


        for (WebElement element : driver.findElements(By.xpath("//div[contains(@class, 'product-item')]"))) {
            WebElement priceElement = element.findElement(By.cssSelector(".price"));
            double price = Double.parseDouble(priceElement.getText().replace("$", ""));
            if (price > MIN_PRICE) {
                element.click();
                break;
            }
        }

        driver.findElement(By.cssSelector(".recipient-name")).sendKeys("Audrone");

        driver.findElement(By.cssSelector(".sender-name")).sendKeys("Audronyte");

        WebElement qty = driver.findElement(By.cssSelector(".qty-input"));
        qty.clear();
        qty.sendKeys("5000");

        driver.findElement(By.cssSelector("[value='Add to cart']")).click();

        ensureAddSuccess(wait);

        driver.findElement(By.cssSelector("[value='Add to wishlist']")).click();

        ensureAddSuccess(wait);

        wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Jewelry"))).click();
        driver.findElement(By.linkText("Create Your Own Jewelry")).click();

        new Select(driver.findElement(By.id("product_attribute_71_9_15"))).selectByValue("47");

        driver.findElement(By.cssSelector(".textbox")).sendKeys("80");

        driver.findElement(By.cssSelector("input[type='radio'][value='50']")).click();

        WebElement customQty = driver.findElement(By.cssSelector(".qty-input"));
        customQty.clear();
        customQty.sendKeys("26");

        driver.findElement(By.cssSelector("[value='Add to cart']")).click();

        ensureAddSuccess(wait);

        driver.findElement(By.cssSelector("[value='Add to wishlist']")).click();

        ensureAddSuccess(wait);

        driver.findElement(By.linkText("Wishlist")).click();

        for(WebElement checkbox : driver.findElements(By.cssSelector("input[type='checkbox'][name='addtocart']"))){
            checkbox.click();
        }

        driver.findElement(By.cssSelector("[value='Add to cart']")).click();

        WebElement subtotalValue = driver.findElement(By.cssSelector(".cart-total-right .product-price"));
        try {
            assert subtotalValue.getText().equals("1002600.00") : "Subtotal value does not match expected value";
            System.out.println("Subtotal value matches expected value.");
        } catch (AssertionError e) {
            System.out.println("Assertion failed: " + e.getMessage());
        }

        driver.quit();
    }

    static public void ensureAddSuccess(WebDriverWait wait){
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".bar-notification.success")));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".bar-notification.success")));

    }
}
