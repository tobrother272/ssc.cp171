/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ultil;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.io.File;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import static org.sikuli.script.KeyModifier.KEY_CTRL;
import org.sikuli.script.Location;

import org.sikuli.script.Screen;
import ssc.base.run.ActionWithAccountBase;

/**
 *
 * @author ASUS
 */
public class SikulixUtils {

    public static boolean sendText(Screen s, String image, String value) {
        try {

            if (!clickByImage(s, image)) {
                return false;
            }
            StringSelection selection = new StringSelection(value);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);

            ThreadUtils.Sleep(2000);
            s.type("v", KEY_CTRL);
            ThreadUtils.Sleep(2000);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    /*
    public static boolean sendTextByXpath(Screen s, String xpath, String value, ConnectRemoteTool crt) {
        try {
            StringSelection selection = new StringSelection(value);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);

            if (!clickByXpath(s, xpath, crt)) {
                return false;
            }

            ThreadUtils.Sleep(2000);

            s.type("v", KEY_CTRL);

            ThreadUtils.Sleep(2000);
            return true;
        } catch (Exception e) {
        }
        return false;
    }
     */
    

    public static boolean clickByImage(Screen s, String image) {
        try {
            if (s.exists(image) == null) {
                System.out.println("Không thấy image " + image);
                return false;
            }

            s.mouseMove(image);
            ThreadUtils.Sleep(2000);
            Robot bot = new Robot();
            bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            ThreadUtils.Sleep(2000);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean clickByImageOnUrl(Screen s, String url) {
        String file = System.getProperty("user.dir") + File.separator + "temps" + File.separator + System.currentTimeMillis() + ".jpg";
        try {

            MyFileUtils.createFolder(System.getProperty("user.dir") + File.separator + "temps");

            MyFileUtils.saveImage(url, file);

            if (s.exists(file) == null) {
                System.out.println("Không thấy image " + file);
                return false;
            }

            s.mouseMove(file);
            ThreadUtils.Sleep(2000);
            Robot bot = new Robot();
            bot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            bot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);

            ThreadUtils.Sleep(2000);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                MyFileUtils.deleteFile(file);
            } catch (Exception e) {
            }
        }
        return false;
    }

}
