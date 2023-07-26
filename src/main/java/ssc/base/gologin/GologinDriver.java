/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.gologin;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.ObservableList;
import ssc.base.global.TC;
import ssc.theta.app.model.TaskLog;
import ssc.base.ultil.MyFileUtils;
import ssc.base.ultil.CMDUtils;
/**
 *
 * @author PC
 */
public class GologinDriver {

    private String profileName;
    private String canvas;
    private String profile_id;
    private String profileFolder;
    private ObservableList<TaskLog> arrRemoteLog;

    
    public GologinDriver(String profileName,String profile_id, String canvas, String profileFolder, ObservableList<TaskLog> arrRemoteLog) {
        this.profile_id=profile_id;
        this.profileName = profileName;
        this.canvas = canvas;
        this.profileFolder = profileFolder;
        this.arrRemoteLog = arrRemoteLog;
    }
    
    public boolean openBrowser(String proxy, String size) {
        /**
         * write json fake data 
         */
        MyFileUtils.createFolder("temps_sh");
        String shFile = "temps_sh" + File.separator + profileName + ".sh";
        MyFileUtils.deleteFile(shFile);
        List<String> query = new ArrayList<>(Arrays.asList("node",
                System.getProperty("user.dir") + File.separator + "tool" + File.separator + "gologin" + File.separator + "o.js",
                "-t " + TC.getInts().gologin_token,
                "-p " + profile_id,
                "-s " + size,
                "-c " + canvas,
                "-b " + TC.getInts().browser_path,
                "-n " + profileName,
                "-f "+TC.getInts().gologin_folder,
                (proxy.length() == 0 ? "" : "-x " + proxy)
        ));
        if (TC.isWin()) {
            return CMDUtils.cmdStartWithNewLine(query, arrRemoteLog);
        }
        String writer = "";
        for (String string : query) {
            writer = writer + " " + string;
        }
        MyFileUtils.writeStringToFileUTF8(writer, shFile);
        if (!new File(shFile).exists()) {
            return false;
        } else {
            List<String> queryCall = new ArrayList<>();
            queryCall.addAll(Arrays.asList(
                    "bash",
                    shFile
            ));
            return CMDUtils.cmdStartWithNewLine(queryCall, arrRemoteLog);
        }
    }
    
    public static String BROWSER_PATH="C:"+File.separator+"Users"+File.separator+System.getProperty("user.name")+File.separator+".theta"+File.separator+"browser"+File.separator+"orbita-browser"+File.separator+"chrome.exe";
    
    public boolean startRemote(String proxy,String viewPort,int x, int y, String size,boolean headless) {
       
        MyFileUtils.createFolder("temps");
        String shFile = "temps_sh" + File.separator + profileName + ".sh";
        MyFileUtils.deleteFile(shFile);
        List<String> query = new ArrayList<>(Arrays.asList("node",
                System.getProperty("user.dir") + File.separator + "tool" + File.separator + "gologin" + File.separator + "e.js",
                "-t " + TC.getInts().gologin_token,
                "-p " + profile_id.trim(),
                "-i " + "127.0.0.1",
                "-s " + size,
                "-n " + profileName,
                "-h " + headless,
                "-v " + viewPort, 
                "-c " + canvas,
                "-f " + TC.getInts().gologin_folder ,
                "-b " + TC.getInts().browser_path,
                "-l " + (x + "," + y) + ""
                +(proxy.length() == 0 ? "" : "-x http:"+proxy)
        ));
        
        for (String string : query) {
           //System.out.print(string+" ");
        }
        
        
        if (TC.isWin()) {
            return CMDUtils.cmdStartWithNewLine(query, arrRemoteLog);
        }
        String writer = "";
        for (String string : query) {
            writer = writer + " " + string;
        }
        MyFileUtils.writeStringToFileUTF8(writer, shFile);
        if (!new File(shFile).exists()) {
            return false;
        } else {
            List<String> queryCall = new ArrayList<>();
            queryCall.addAll(Arrays.asList(
                    "bash",
                    shFile
            ));
            return CMDUtils.cmdStartWithNewLine(queryCall, arrRemoteLog);
        }
    }
}
