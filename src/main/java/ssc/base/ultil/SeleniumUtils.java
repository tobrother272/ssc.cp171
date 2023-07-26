/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ultil;

import java.io.File;
import java.util.Random;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import ssc.base.global.TC;
import static ssc.base.ultil.ThreadUtils.Sleep;

/**
 *
 * @author PC
 */
public class SeleniumUtils {

    public static String GECKO = System.getProperty("user.dir") + File.separator + "tool" + File.separator + "geckodriver.exe";

    public static boolean loadPage(WebDriver driver, String url, int timeOut) {
        try {
//            driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
//            driver.manage().timeouts().setScriptTimeout(timeOut, TimeUnit.SECONDS);
//            driver.manage().timeouts().pageLoadTimeout(timeOut, TimeUnit.SECONDS);
            driver.get(url);
            waitForPageLoad(driver, timeOut);
            if ((driver.getTitle().contains("Lỗi")) || (driver.getTitle().contains("Error")) || (driver.getTitle().contains("Problem loading page"))) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean clickElementByAction(WebDriver driver, WebElement element) {
        try {
          
            new Actions(driver).moveToElement(element).perform();
            element.click();
            return true;
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
    }
    public static boolean sendKey(WebDriver driver, String xpath, String text) {
        try {

            for (char ch : text.toCharArray()) {
                driver.findElement(By.xpath(xpath)).sendKeys(Character.toString(ch));
                Sleep((new Random().nextInt(3) + 2) * 50);
            }

            return true;
        } catch (Exception e) {
            //e.printStackTrace();

        }
        return true;
    }

    public static WebDriver getWebdriverByRemotePort(int port) {
        try {
            System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + File.separator + "tool" + File.separator + "chromedriver.exe");
            ChromeOptions opt = new ChromeOptions();
            opt.setBinary(TC.getInts().browser_path);
            opt.setExperimentalOption("debuggerAddress", "localhost:" + port);
            WebDriver driver = new ChromeDriver(opt);
            return driver;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    public static WebDriver getPlaywright(int port) {
        try {
            System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + File.separator + "tool" + File.separator + "chromedriver.exe");
            ChromeOptions opt = new ChromeOptions();
            opt.setBinary(TC.getInts().browser_path);
            opt.setExperimentalOption("debuggerAddress", "localhost:" + port);
            WebDriver driver = new ChromeDriver(opt);
            return driver;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    public static WebDriver getWebDriver() {
        try {
            System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + File.separator + "tool" + File.separator + "chromedriver.exe");
            ChromeOptions opt = new ChromeOptions();
            opt.setBinary(TC.getInts().browser_path);
            opt.setHeadless(true);
            WebDriver driver = new ChromeDriver(opt);
            return driver;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    

    public static int getLengthJS(String xpath, WebDriver driver) {
        int result = 0;
        try {
            String cmd = "return document.evaluate(\"" + xpath + "\", document, null, 7, null).snapshotLength";
            //System.out.println("getLengthJS " + cmd);
            result = Integer.parseInt(String.valueOf(((JavascriptExecutor) driver).executeScript(cmd, new Object[0])));
        } catch (Exception e) {
            //e.printStackTrace();
            return 0;
        }
        return result;
    }

    public static void waitForPageLoad(WebDriver d, int timeout) {
        String s = "";
        try {
            int countTime = 0;
            while (!s.equals("complete") && countTime < timeout) {
                s = (String) ((JavascriptExecutor) d).executeScript("return document.readyState");
                Sleep(1000);
                countTime++;
            }
        } catch (Exception e) {

        }

    }

    public static String getContentValue(String xpath, WebDriver driver) {
        String result = "";
        try {
            String query = "return document.evaluate(\"" + xpath + "\", document, null, 7, null).snapshotItem(0).value";
            //System.out.println(query);
            result = String.valueOf(((JavascriptExecutor) driver).executeScript(query, new Object[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return result;
    }
    
    
    public static String getJS(String query, WebDriver driver) {
        String result = "";
        try {
            result = String.valueOf(((JavascriptExecutor) driver).executeScript(query, new Object[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return result;
    }
}
