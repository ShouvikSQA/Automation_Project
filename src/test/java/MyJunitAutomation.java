import com.github.javafaker.Faker;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.time.Duration;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MyJunitAutomation {
    public WebDriver driver;
   @BeforeAll
    public void setUp(){
        driver = new ChromeDriver();
        driver.get("https://parabank.parasoft.com/parabank");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test
    public void testRun() throws InterruptedException {
       registerAccount();
       newAccount();
       fundTransfer();
       extractTrnxId();
       findTransaction();
       checkDate();
    }

    @Test
    public void registerAccount(){
       driver.findElement(By.xpath("//a[text()='Register']")).click();
       List<WebElement> txtBox = driver.findElements(By.className("input"));
       Faker faker=new Faker();
       txtBox.get(2).sendKeys(faker.name().firstName());
       txtBox.get(3).sendKeys(faker.name().lastName());
        txtBox.get(4).sendKeys("Gulshan");
        txtBox.get(5).sendKeys("Dhaka");
        txtBox.get(6).sendKeys("Dhaka");
        txtBox.get(7).sendKeys("1212");
        txtBox.get(8).sendKeys("01504477888");
        txtBox.get(9).sendKeys("123456");
        txtBox.get(10).sendKeys(faker.name().username());
        txtBox.get(11).sendKeys("1234");
        txtBox.get(12).sendKeys("1234");

        driver.findElement(By.xpath("//input[@value='Register']")).click();


    }

    @Test
    public void newAccount() throws InterruptedException {

       driver.findElement(By.xpath("//a[text()='Open New Account']")).click();
        Thread.sleep(2000);
        Select select = new Select(driver.findElement(By.id("type")));
        select.selectByValue("1");
        driver.findElement(By.xpath("//input[@type=\"submit\"]")).click();
        Thread.sleep(3000);
        String accountId = driver.findElement(By.id("newAccountId")).getText();
        System.out.println(accountId);
        driver.findElement(By.id("newAccountId")).click();

    }

    public void fundTransfer(){
        driver.findElement(By.partialLinkText("Funds Transfer Received")).click();

    }
    String trnxId;
   @Test
    public void extractTrnxId(){
        String html= driver.getPageSource();
        int startIndex= html.indexOf("Transaction ID");
        String str= html.substring(startIndex,startIndex+50);
        trnxId= str.replaceAll("[^0-9]","");
        System.out.println(trnxId);
    }

    @Test
    public void findTransaction() throws InterruptedException {

        driver.findElement(By.partialLinkText("Find Transactions")).click();

        driver.findElement(By.xpath("//input[@ng-model='criteria.transactionId']")).sendKeys(trnxId);
        driver.findElements(By.xpath("//button[@type='submit']")).get(0).click();
   }

   public void checkDate(){
       String date = driver.findElement(By.xpath("//td[@class=\"ng-binding\"]")).getText();
       Assertions.assertTrue(date.equals("04-27-2024"));

   }


    @AfterAll
    public void CloseWindow(){
      // driver.close();
    }

}
