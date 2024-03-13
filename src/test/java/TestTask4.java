import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.util.Arrays;

public class TestTask4 {

    private final Duration DURATION = Duration.ofSeconds(10);
    private static WebDriver driver;
    private WebDriverWait wait;
    private static User user;


    @BeforeClass
    public static void initialSetUp(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        createUser();
    }

    @Before
    public void setUp(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, DURATION);
    }

    public void testcase(String filename){
        driver.get("https://demowebshop.tricentis.com/");
        login();
        driver.findElement(By.linkText("Digital downloads")).click();
        addItemToCart(filename);
        driver.findElement(By.className("cart-label")).click();
        checkout();
    }
    @Test
    public void testScenario1(){
        testcase("data1.txt");
    }

    @Test
    public void testScenario2(){
        testcase("data2.txt");
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @AfterClass
    public static void finalTearDown(){
        driver.quit();
    }

    private static void createUser(){
        user = new User();
        driver.get("https://demowebshop.tricentis.com/");
        driver.findElement(By.linkText("Log in")).click();
        driver.findElement(By.className("register-button")).click();
        driver.findElement(By.id("gender-male")).click();
        driver.findElement(By.id("FirstName")).sendKeys(user.getFirstName());
        driver.findElement(By.id("LastName")).sendKeys(user.getLastName());
        driver.findElement(By.id("Email")).sendKeys(user.getEmail());
        driver.findElement(By.id("Password")).sendKeys(user.getPassword());
        driver.findElement(By.id("ConfirmPassword")).sendKeys(user.getPassword());
        driver.findElement(By.id("register-button")).click();
        driver.findElement(By.cssSelector(".register-continue-button")).click();
        driver.findElement(By.linkText("Log out")).click();

    }

    private void login(){
        driver.findElement(By.linkText("Log in")).click();
        driver.findElement(By.id("Email")).sendKeys(user.getEmail());
        driver.findElement(By.id("Password")).sendKeys(user.getPassword());
        driver.findElement(By.cssSelector("input[value='Log in']")).click();
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
            driver.findElement(By.linkText(item)).click();
            driver.findElement(By.className("add-to-cart-button")).click();
            driver.navigate().back();
        }
        driver.findElement(By.partialLinkText("Shopping cart")).click();
    }

    private void checkout(){
        driver.findElement(By.id("termsofservice")).click();
        driver.findElement(By.id("checkout")).click();
        try{
            driver.findElement(By.id("BillingNewAddress_CountryId")).click();
            driver.findElement(By.xpath("//option[contains(text(),'Fiji')]")).click();
            driver.findElement(By.id("BillingNewAddress_City")).sendKeys("Kaunas");
            driver.findElement(By.id("BillingNewAddress_Address1")).sendKeys("123 Street");
            driver.findElement(By.id("BillingNewAddress_ZipPostalCode")).sendKeys("10001");
            driver.findElement(By.id("BillingNewAddress_PhoneNumber")).sendKeys("1234567890");
        }catch (ElementNotInteractableException ignored){

        }

        driver.findElement(By.cssSelector("input[value='Continue']")).click();
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

