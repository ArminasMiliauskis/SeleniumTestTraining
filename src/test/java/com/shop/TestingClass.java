package com.shop;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class TestingClass {

    private WebDriverWait wait;
    private EdgeDriver driver;

    /**
     * Pradiniu driver duomenu inicializavimas
     */
    @Before
    public void setup() {
        System.setProperty("webdriver.edge.driver","msedgedriver.exe");
        this.driver = new EdgeDriver();
        int timeWait = 20; //10 seconds
        wait = new WebDriverWait(driver, timeWait);
    }

    /**
     * driver'io uzdarymas pabaigus testus
     */
    @After
    public void close() {
        driver.quit();
    }

    /**
     * Svetaines testas
     */
    @Test
    public void checkTrueSiteTest() {
        driver.get("http://automationpractice.com/");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("header")));
        WebElement header = driver.findElement(By.id("header"));
        header.findElement(By.className("login")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("create-account_form")));
        WebElement element = driver.findElement(By.xpath("//*[@id=\"SubmitLogin\"]"));
        String title = element.getAttribute("name");
        Assert.assertEquals(title,"SubmitLogin");
    }

    /**
     * Registracijos testas
     */
    @Test
    public void registrationTest()  {
        checkTrueSiteTest();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("header")));
        WebElement header = driver.findElement(By.id("header"));

        header.findElement(By.className("login")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("create-account_form")));
        Random random = new Random();
        int i = random.nextInt(1000) + 1;
        driver.findElement(By.id("email_create")).sendKeys(i + "ArminasMiliauskis" + "@gmail.com");
        driver.findElement(By.id("SubmitCreate")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("account-creation_form")));

        driver.findElement(By.id("id_gender1")).click();
        driver.findElement(By.id("customer_firstname")).sendKeys("Arminas");
        driver.findElement(By.id("customer_lastname")).sendKeys("Miliauskis");
        driver.findElement(By.id("passwd")).sendKeys("password");
        WebElement select = driver.findElement(By.id("days"));
        Select selected = new Select(select);
        selected.selectByIndex(2);

        select = driver.findElement(By.id("months"));
        selected = new Select(select);
        selected.selectByIndex(2);

        select = driver.findElement(By.id("years"));
        selected = new Select(select);
        selected.selectByIndex(15);

        driver.findElement(By.id("address1")).sendKeys("Didlaukio g.");
        driver.findElement(By.id("city")).sendKeys("Vilnius");
        select = driver.findElement(By.id("id_state"));
        selected = new Select(select);
        selected.selectByIndex(5);

        driver.findElement(By.id("postcode")).sendKeys("51236");
        driver.findElement(By.id("phone_mobile")).sendKeys("867723236");
        driver.findElement(By.id("submitAccount")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("lnk_wishlist")));
        String titleAcc = driver.getTitle();
        Assert.assertEquals("My account - My Store", titleAcc);
    }

    /**
     * Prisijungimo testas (su validžiais duomenimis)
     */
    @Test
    public void checkLogIn(){
        checkTrueSiteTest();
        driver.findElement(By.id("email")).sendKeys("ArminasMiliauskis@gmail.com");
        driver.findElement(By.id("passwd")).sendKeys("password");
        driver.findElement(By.id("SubmitLogin")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"center_column\"]")));
        String actual = driver.findElement(By.xpath("//*[@id=\"center_column\"]/h1")).getText();
        Assert.assertEquals(actual,"MY ACCOUNT");   // "MY ACCOUNT";
    }

    /**
     * Prisijungimo testas (su nevalidžiu elektroniniu paštu)
     */
    @Test
    public void CheckInvalidEmailRegistration(){
        checkTrueSiteTest();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("header")));
        WebElement header = driver.findElement(By.id("header"));
        header.findElement(By.className("login")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("create-account_form")));
        driver.findElement(By.id("email_create")).sendKeys("email@email");
        driver.findElement(By.xpath("//button[@id='SubmitCreate']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='create_account_error']")));
        WebElement element = driver.findElement(By.xpath("//div[@id='create_account_error']"));
        Assert.assertEquals("Invalid email address.",element.getText());
    }

    /**
     * Prisijungimo testas (su nevalidžiais duomenimis)
     */
    @Test
    public void CheckInvalidLogin(){
        checkTrueSiteTest();
        driver.findElement(By.id("email")).sendKeys("armin@eif.viko");
        driver.findElement(By.id("passwd")).sendKeys("lala8");
        driver.findElement(By.id("SubmitLogin")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"center_column\"]/div[1]")));
        WebElement element = driver.findElement(By.xpath("//*[@id=\"center_column\"]/div[1]"));
        String invalidText = "There is 1 error\n" +
                "Authentication failed.";
        Assert.assertEquals(invalidText,element.getText());

    }

    /**
     * Krepšelio testas (neprisijungus)
     */
    @Test
    public void checkAddCardNotLogin() {
        driver.get("http://automationpractice.com/");
        Actions action = new Actions(driver);
        WebElement we = driver.findElement(By.xpath("//*[@id=\"homefeatured\"]/li[1]/div"));
        action.moveToElement(we).moveToElement(driver.findElement(By.xpath("//*[@id=\"homefeatured\"]/li[1]/div/div[2]/div[2]/a[1]"))).click().build().perform();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"layer_cart\"]")));
        driver.findElement(By.xpath("//*[@id=\"layer_cart\"]/div[1]/div[2]/div[4]/span")).click();
        we = driver.findElement(By.xpath("//*[@id=\"homefeatured\"]/li[7]/div"));
        action.moveToElement(we).moveToElement(driver.findElement(By.xpath("//*[@id=\"homefeatured\"]/li[7]/div/div[2]/div[2]/a[1]"))).click().build().perform();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"layer_cart\"]")));
        driver.findElement(By.xpath("//*[@id=\"layer_cart\"]/div[1]/div[2]/div[4]/a")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"center_column\"]")));
        String expected = "Your shopping cart contains: 2 Products";
        String actual = driver.findElement(By.xpath("//*[@id=\"cart_title\"]/span")).getText();
        Assert.assertEquals(expected,actual);
    }

    /**
     * Krepšelio testas (prisijungus)
     */
    @Test
    public void checkAddCardLogin() {
        checkLogIn();
        driver.findElement(By.xpath("//*[@id=\"header_logo\"]/a/img")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"page\"]/div[2]")));
        Actions action = new Actions(driver);
        WebElement we = driver.findElement(By.xpath("//*[@id=\"homefeatured\"]/li[1]/div"));
        action.moveToElement(we).moveToElement(driver.findElement(By.xpath("//*[@id=\"homefeatured\"]/li[1]/div/div[2]/div[2]/a[1]"))).click().build().perform();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"layer_cart\"]")));
        driver.findElement(By.xpath("//*[@id=\"layer_cart\"]/div[1]/div[2]/div[4]/span")).click();
        we = driver.findElement(By.xpath("//*[@id=\"homefeatured\"]/li[7]/div"));
        action.moveToElement(we).moveToElement(driver.findElement(By.xpath("//*[@id=\"homefeatured\"]/li[7]/div/div[2]/div[2]/a[1]"))).click().build().perform();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"layer_cart\"]")));
        driver.findElement(By.xpath("//*[@id=\"layer_cart\"]/div[1]/div[2]/div[4]/a")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"center_column\"]")));
        String expected = "Your shopping cart contains: 2 Products";
        String actual = driver.findElement(By.xpath("//*[@id=\"cart_title\"]/span")).getText();
        Assert.assertEquals(expected,actual);
    }

    /**
     * Contact Us funkcijos testas
     */
    @Test
    public void checkContactUsTest(){
        driver.get("http://automationpractice.com/");
        driver.findElement(By.xpath("//*[@id=\"block_various_links_footer\"]/ul/li[5]/a")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"columns\"]")));
        WebElement select = driver.findElement(By.id("id_contact"));
        Select selected = new Select(select);
        selected.selectByIndex(1);
        driver.findElement(By.xpath("//*[@id=\"email\"]")).sendKeys("Arminas@Miliauskis.com");
        driver.findElement(By.xpath("//*[@id=\"id_order\"]")).sendKeys("12");
        driver.findElement(By.xpath("//*[@id=\"message\"]")).sendKeys("Sending a message");
        driver.findElement(By.xpath("//*[@id=\"submitMessage\"]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"center_column\"]")));
        String expected = "Your message has been successfully sent to our team.";
        String actual = driver.findElement(By.xpath("//*[@id=\"center_column\"]/p")).getText();
        Assert.assertEquals(expected,actual);

    }

    /**
     * Filtravimo funkcijos testas
     */
    @Test
    public void filterTest() {
        driver.get("http://automationpractice.com/");
        driver.findElement(By.xpath("//*[@id=\"block_top_menu\"]/ul/li[1]/a")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"layered_form\"]/div")));
        driver.findElement(By.xpath("//*[@id=\"layered_id_attribute_group_1\"]")).click();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        driver.findElement(By.xpath("//*[@id=\"layered_id_attribute_group_13\"]")).click();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);

        WebElement slider = driver.findElement(By.xpath("//*[@id=\"layered_price_slider\"]"));
        int width=slider.getSize().getWidth();
        Actions move = new Actions(driver);
        org.openqa.selenium.interactions.Action action  = move.dragAndDropBy(slider, ((width*25)/100), 0).build();
        action.perform();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        if(validationMessageCheck("//*[@id=\"center_column\"]/ul/p/img")){
            Assert.assertTrue(false);
        }
    }

    /**
     * Noru Saraso funkcijos testavimas
     * @throws InterruptedException thrown when a thread is interrupted while waiting/sleeping
     */
    @Test
    public void wishListTest() throws InterruptedException {
        checkLogIn();
        driver.findElement(By.xpath("//*[@id=\"center_column\"]/div/div[2]/ul/li/a")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"mywishlist\"]")));
        if(validationMessageCheck("//*[@id=\"block-history\"]")){
            driver.findElement(By.xpath("//*[@id=\"name\"]")).sendKeys("My new wishlist");
            driver.findElement(By.xpath("//*[@id=\"submitWishlist\"]")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"block-history\"]")));
            driver.findElement(By.xpath("/html/body/div/div[2]/div/div[3]/div[2]/div/div[1]/table/tbody/tr/td[6]/a")).click();
            driver.switchTo().alert().accept();
            Thread.sleep(10000);
            boolean isDisplay = validationMessageCheck("//*[@id=\"block-history\"]");
            Assert.assertTrue(isDisplay);
        }else{
            driver.findElement(By.xpath("/html/body/div/div[2]/div/div[3]/div[2]/div/div[1]/table/tbody/tr/td[6]/a")).click();
            driver.switchTo().alert().accept();
            Thread.sleep(10000);
            boolean isDisplay = validationMessageCheck("//*[@id=\"block-history\"]");
            Assert.assertTrue(isDisplay);
        }

    }

    /**
     * Adreso ištrynimo testas
     */
    @Test
    public void AddressDeletionTest() {
                                                            // if fails, replase the last [3] on line 281 with a [4]
        checkLogIn();
        driver.findElement(By.xpath("/html/body/div/div[3]/footer/div/section[5]/div/ul/li[3]/a")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"columns\"]")));
        if(validationMessageCheck("//*[@id=\"center_column\"]/div[1]/div/div/ul/li[9]/a[2]")){
            driver.findElement(By.xpath("//*[@id=\"center_column\"]/div/a")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"center_column\"]/div")));
            driver.findElement(By.xpath("//*[@id=\"address1\"]")).sendKeys("Address123");
            driver.findElement(By.xpath("//*[@id=\"postcode\"]")).sendKeys("33018");
            driver.findElement(By.xpath("//*[@id=\"city\"]")).sendKeys("Vilnius");
            WebElement select = driver.findElement(By.id("id_state"));
            Select selected = new Select(select);
            selected.selectByIndex(10);
            driver.findElement(By.xpath("//*[@id=\"phone\"]")).sendKeys("28128412");
            Random random = new Random();
            int i = random.nextInt(10000) + 1;
            driver.findElement(By.xpath("//*[@id=\"alias\"]")).sendKeys("MyAddress" + i);
            driver.findElement(By.xpath("//*[@id=\"submitAddress\"]")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"center_column\"]")));
            String actual = driver.findElement(By.xpath("//*[@id=\"center_column\"]/div[1]/p[1]/strong")).getText();
            String expected = "Your addresses are listed below.";
            Assert.assertEquals(expected,actual);
            /*
            if(!validationMessageCheck("//*[@id=\"center_column\"]/div[1]/div/div/ul/li[9]/a[2]")){
            driver.findElement(By.xpath("//*[@id=\"center_column\"]/div[1]/div/div/ul/li[9]/a[2]")).click();
            driver.switchTo().alert().accept();
            }
             */
            driver.findElement(By.xpath("/html/body/div/div[2]/div/div[3]/div/div[1]/div/div/ul/li[9]/a[2]")).click();
            driver.switchTo().alert().accept();
            String actual1 = driver.findElement(By.xpath("//*[@id=\"center_column\"]/p[2]")).getText();

            String expected1 = "No addresses are available. Add a new address";
            Assert.assertEquals(expected1,actual1);
        }else {
            driver.findElement(By.xpath("/html/body/div/div[2]/div/div[3]/div/div[1]/div/div/ul/li[9]/a[2]")).click();
            driver.switchTo().alert().accept();
            String actual = driver.findElement(By.xpath("//*[@id=\"center_column\"]/p[2]")).getText();
            String expected = "No addresses are available. Add a new address";
            Assert.assertEquals(expected,actual);
        }
    }

    /**
     * Elemento validacia. Patikrina ar elementas egzistuoja
     * @param xpa XPath adresas
     * @return false - neegzistuoja, true - jei egzistuoja
     */
    public boolean validationMessageCheck(String xpa) {
        try {
            driver.findElement(By.xpath(xpa));
            return false;
        } catch (NoSuchElementException e) {
            return true;
        }
    }

    /**
     * Pasirinktos prekes pirkimo funkcijos testas
     */
    @Test
    public void purchaseTest() {
        checkLogIn();
        ///html/body/div/div[2]/div/div[3]/div/div/div[1]/ul/li[4]/a

        ///html/body/div/div[2]/div/div[3]/div/div/div[1]/ul/li[3]/a


        driver.findElement(By.xpath("/html/body/div/div[3]/footer/div/section[5]/div/ul/li[3]/a")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"columns\"]")));
        //if(validationMessageCheck("//*[@id=\"center_column\"]/div[1]/div/div/ul/li[9]/a[2]")){
        driver.findElement(By.xpath("//*[@id=\"center_column\"]/div/a")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"center_column\"]/div")));
        driver.findElement(By.xpath("//*[@id=\"address1\"]")).sendKeys("Address123");
        driver.findElement(By.xpath("//*[@id=\"postcode\"]")).sendKeys("33018");
        driver.findElement(By.xpath("//*[@id=\"city\"]")).sendKeys("Vilnius");
        WebElement select = driver.findElement(By.id("id_state"));
        Select selected = new Select(select);
        selected.selectByIndex(10);
        driver.findElement(By.xpath("//*[@id=\"phone\"]")).sendKeys("28128412");
        Random random = new Random();
        int i = random.nextInt(10000) + 1;
        driver.findElement(By.xpath("//*[@id=\"alias\"]")).sendKeys("MyAddress" + i);
        driver.findElement(By.xpath("//*[@id=\"submitAddress\"]")).click();


        driver.findElement(By.xpath("//*[@id=\"header_logo\"]/a/img")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"page\"]/div[2]")));
        Actions action = new Actions(driver);
        WebElement we = driver.findElement(By.xpath("//*[@id=\"homefeatured\"]/li[1]/div"));
        action.moveToElement(we).moveToElement(driver.findElement(By.xpath("//*[@id=\"homefeatured\"]/li[1]/div/div[2]/div[2]/a[1]"))).click().build().perform();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"layer_cart\"]")));
        driver.findElement(By.xpath("//*[@id=\"layer_cart\"]/div[1]/div[2]/div[4]/a")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"columns\"]")));
        driver.findElement(By.xpath("//*[@id=\"center_column\"]/p[2]/a[1]")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"order_step\"]/li[4]")));
        driver.findElement(By.xpath("//*[@id=\"center_column\"]/form/p/button")).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"order_step\"]/li[4]")));
        driver.findElement(By.xpath("//*[@id=\"cgv\"]")).click();
        driver.findElement(By.xpath("//*[@id=\"form\"]/p/button")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"center_column\"]")));
        driver.findElement(By.xpath("//*[@id=\"HOOK_PAYMENT\"]/div[2]/div/p")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"center_column\"]")));
        driver.findElement(By.xpath("//*[@id=\"cart_navigation\"]/button")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"center_column\"]/p[1]")));
        String actual = driver.findElement(By.xpath("//*[@id=\"center_column\"]/p[1]")).getText();
        String expected = "Your order on My Store is complete.";
        Assert.assertEquals(expected,actual);

        driver.findElement(By.xpath("/html/body/div/div[3]/footer/div/section[5]/div/ul/li[3]/a")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"columns\"]")));
        driver.findElement(By.xpath("/html/body/div/div[2]/div/div[3]/div/div[1]/div/div/ul/li[9]/a[2]")).click();
        driver.switchTo().alert().accept();

    }
}


