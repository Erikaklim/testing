import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.util.Arrays;

public class Task4 {

    private final Duration DURATION = Duration.ofSeconds(10);
    private WebDriver userDriver, testDriver;
    private WebDriverWait wait;

    private User user;


    @Before
    public void setUp(){
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-save-card");
        options.setExperimentalOption("prefs", "{\"profile.default_content_settings.popups\": 1}");
        userDriver = new ChromeDriver();
        testDriver = new ChromeDriver();
        wait = new WebDriverWait(testDriver, DURATION);
        createUser();
    }

    @Test
    public void testScenario1(){
        testDriver.get("https://demowebshop.tricentis.com/");
        login();
        testDriver.findElement(By.linkText("Digital downloads")).click();
        addItemToCart("data1.txt");
        testDriver.findElement(By.className("cart-label")).click();
        checkout();
    }

    @Test
    public void testScenario2(){
        testDriver.get("https://demowebshop.tricentis.com/");
        login();
        testDriver.findElement(By.linkText("Digital downloads")).click();
        addItemToCart("data2.txt");
        testDriver.findElement(By.className("cart-label")).click();
        checkout();
    }

//    @After
//    public void tearDown(){
//        if(testDriver != null){
//            testDriver.quit();
//        }
//        if(userDriver != null){
//            userDriver.quit();
//    }

    private void createUser(){
        user = new User();
        userDriver.get("https://demowebshop.tricentis.com/");
        userDriver.findElement(By.linkText("Log in")).click();
        userDriver.findElement(By.className("register-button")).click();
        userDriver.findElement(By.id("gender-male")).click();
        userDriver.findElement(By.id("FirstName")).sendKeys(user.getFirstName());
        userDriver.findElement(By.id("LastName")).sendKeys(user.getLastName());
        userDriver.findElement(By.id("Email")).sendKeys(user.getEmail());
        userDriver.findElement(By.id("Password")).sendKeys(user.getPassword());
        userDriver.findElement(By.id("ConfirmPassword")).sendKeys(user.getPassword());
        userDriver.findElement(By.id("register-button")).click();
        userDriver.findElement(By.cssSelector(".register-continue-button")).click();
        userDriver.findElement(By.linkText("Log out")).click();

    }

    private void login(){
        testDriver.findElement(By.linkText("Log in")).click();
        testDriver.findElement(By.id("Email")).sendKeys(user.getEmail());
        testDriver.findElement(By.id("Password")).sendKeys(user.getPassword());
        testDriver.findElement(By.cssSelector("input[value='Log in']")).click();
    }
    private String[] readFromFile(String fileName){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/"+fileName));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(",");
            }
            reader.close();
            return stringBuilder.toString().split(",");
        }catch (Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
            return null;
        }
    }

    public void addItemToCart(String filename) {
        String[] items = readFromFile(filename);
        if(items == null){
            System.out.println("items null");
            return;
        }
        for (String item : items) {
            testDriver.findElement(By.linkText(item)).click();
            testDriver.findElement(By.className("add-to-cart-button")).click();
            testDriver.navigate().back();
        }
        testDriver.findElement(By.partialLinkText("Shopping cart")).click();
    }

    private void checkout(){
        testDriver.findElement(By.id("termsofservice")).click();
        testDriver.findElement(By.id("checkout")).click();
        testDriver.findElement(By.id("BillingNewAddress_CountryId")).click();
        testDriver.findElement(By.xpath("//option[contains(text(),'Fiji')]")).click();
        testDriver.findElement(By.id("BillingNewAddress_City")).sendKeys("Kaunas");
        testDriver.findElement(By.id("BillingNewAddress_Address1")).sendKeys("123 Street");
        testDriver.findElement(By.id("BillingNewAddress_ZipPostalCode")).sendKeys("10001");
        testDriver.findElement(By.id("BillingNewAddress_PhoneNumber")).sendKeys("1234567890");
        testDriver.findElement(By.cssSelector("input[value='Continue']")).click();
        WebElement paymentMethod = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("payment-method-next-step-button")));
        paymentMethod.click();
        WebElement paymentInfo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("payment-info-next-step-button")));
        paymentInfo.click();
        WebElement confirm = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("confirm-order-next-step-button")));
        confirm.click();
        WebElement continueButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[value='Continue']")));
        continueButton.click();

    }

}
