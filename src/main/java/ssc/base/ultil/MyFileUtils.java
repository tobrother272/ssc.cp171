/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ultil;

import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import org.json.simple.JSONObject;

/**
 *
 * @author PC
 */
public class MyFileUtils {

    public static void printLogToDesktop() {
        try {
            if (new File("log.txt").exists()) {
                PrintStream printStream = new PrintStream(new FileOutputStream("log.txt"));
                System.setOut(printStream);
                System.setErr(printStream);
            }
        } catch (Exception e) {
        }
    }

    public static BufferedImage cropImage(File filePath, int x, int y, int w, int h) {

        try {
            BufferedImage originalImgage = ImageIO.read(filePath);

            BufferedImage subImgage = originalImgage.getSubimage(x, y, w, h);
            return subImgage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveImage(String imageUrl, String destName) throws IOException {
        URL url = new URL(imageUrl);
        String tempFolder = System.getProperty("user.dir") + File.separator + "temp";
        MyFileUtils.createFolder(tempFolder);
        String tempFile = tempFolder + File.separator + System.currentTimeMillis() + ".jpg";
        InputStream is = url.openStream();
        OutputStream os = new FileOutputStream(tempFile);

        byte[] b = new byte[2048];
        int length;

        while ((length = is.read(b)) != -1) {
            os.write(b, 0, length);
        }

        is.close();
        os.close();

        File fileToWrite = new File(tempFile);

        BufferedImage resizeImg = resize(ImageIO.read(fileToWrite), 360, 202);

        BufferedImage bufferedImage = resizeImg.getSubimage(10, 10, 340, 182);

        ImageIO.write(bufferedImage, "jpg", new File(destName));
        MyFileUtils.deleteFile(tempFile);

    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }

    public static boolean copyFile(File source, File target) {
        try (
                 InputStream in = new FileInputStream(source);  OutputStream out = new FileOutputStream(target)) {
            byte[] buf = new byte[1024];
            int length;
            while ((length = in.read(buf)) > 0) {
                out.write(buf, 0, length);
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static String getFileExtension(String file) {
        try {
            return file.substring(file.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }

    public static void copy(File sourceLocation, File targetLocation) throws IOException {
        //&&!sourceLocation.getName().contains("firefox") khong bit de lam gi
        if (sourceLocation.isDirectory() && !sourceLocation.getName().contains("cache2")) {
            copyDirectory(sourceLocation, targetLocation);
        } else {
            copyFile(sourceLocation, targetLocation);
        }
    }

    public static void copyDirectory(File source, File target) {
        try {
            if (!target.exists()) {
                //System.out.println("tạo "+target.getAbsolutePath());
                target.mkdir();
            }
            for (String f : source.list()) {
                copy(new File(source, f), new File(target, f));
            }
        } catch (Exception e) {
        }
    }

    public static Boolean extractZip(String source, String destination) {
        try {
            ZipFile zipFile = new ZipFile(source);
            zipFile.extractAll(destination);
            return true;
        } catch (ZipException e) {
            //e.printStackTrace();
            return false;
        }
    }

    public static Boolean checkZip(String source) {
        try {
            ZipFile zipFile = new ZipFile(source);
            for (FileHeader hd : zipFile.getFileHeaders()) {
                // System.out.println(hd.getFileName());
            }
            return true;
        } catch (ZipException e) {
            //e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<String> getListStringFromFile(String filePath) {
        ArrayList<String> result = new ArrayList<>();
        try {
            //BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "Cp1252"));      
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
            String line;
            while ((line = br.readLine()) != null) {
                result.add(line);
            }
            br.close();

        } catch (IOException e) {
            //e.printStackTrace();
        }
        return result;
    }

    public static void openUrl(String url) {
        if (System.getProperty("os.name").contains("Linux")) {
            try {
                Runtime rt = Runtime.getRuntime();
                String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
                    "netscape", "opera", "links", "lynx"};

                StringBuffer cmd = new StringBuffer();
                for (int i = 0; i < browsers.length; i++) {
                    if (i == 0) {
                        cmd.append(String.format("%s \"%s\"", browsers[i], url));
                    } else {
                        cmd.append(String.format(" || %s \"%s\"", browsers[i], url));
                    }
                }
                // If the first didn't work, try the next browser and so on

                rt.exec(new String[]{"sh", "-c", cmd.toString()});
            } catch (Exception e) {
            }
        } else {
            try {
                Desktop desktop = java.awt.Desktop.getDesktop();
                URI oURL = new URI(url);
                desktop.browse(oURL);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean deleteFolder(File file) {
        try {
            File[] contents = file.listFiles();
            if (contents != null) {
                for (File f : contents) {
                    deleteFolder(f);
                }
            }
            file.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void openFile(String url) {
        if (url.length() == 0) {
            return;
        }
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop desktop = Desktop.getDesktop();
                File myFile = new File(url);
                desktop.open(myFile);
            } catch (Exception ex) {

            }
        }
    }

    public static JSONObject getFileInfo(String path) {
        try {
            JSONObject object = new JSONObject();
            File file = new File(path);
            if (!file.exists()) {
                return null;
            }

            object.put("dateTime", file.lastModified());
            object.put("dateTimeString", StringUtils.convertLongToDataTime("dd/MM HH:mm", file.lastModified()));
            object.put("size", file.length() / 1024 / 1024);

            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createFolder(String uri) {
        File theDir = new File(uri);
        // if the directory does not exist, create it
        if (!theDir.exists()) {
            boolean result = false;
            try {
                theDir.mkdir();
                result = true;
            } catch (SecurityException se) {
                se.printStackTrace();
            }
            if (result) {
                //System.out.println("DIR created");  
            }
        }
    }

    public static boolean writeStringToFileUTF8(String content, String path) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(path), "UTF-8"));
            writer.write(content);
            writer.newLine();
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean writeStringToFile(String content, String path) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path, true));
            writer.write(content);
            writer.newLine();
            writer.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static boolean deleteFile(String fileurl) {
        try {
            File file = new File(fileurl);
            if (!file.exists()) {
                return true;
            }
            if (file.delete()) {
                return true;
                //System.out.println(file.getName() + " is deleted!");
            } else {
                return false;
                //System.out.println("Delete operation is failed.");
            }
        } catch (Exception e) {
            System.err.println("delete fail :" + e.getMessage());
            return false;
        }
    }

    public static boolean writeJsonToFile(String jsonString, String pathFile) {
        FileWriter file = null;
        try {
            //System.out.println("---------------------");
            //System.out.println(jsonString);

            file = new FileWriter(pathFile);
            file.write(jsonString);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                file.flush();
                file.close();
            } catch (Exception e) {
            }

        }
        return false;
    }

    public static boolean zipFolderToFile(File fileToBeCreated, File directory) {
        try {
            ZipFile zipFile = new ZipFile(fileToBeCreated);
            ZipParameters params = new ZipParameters();
            params.setIncludeRootFolder(false);
            zipFile.addFolder(directory, params);
            return true;
        } catch (ZipException e) {
        }
        return false;
    }
}
