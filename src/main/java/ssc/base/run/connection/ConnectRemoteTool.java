/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.run.connection;

import java.util.Random;
import org.json.simple.JSONObject;
import ssc.base.run.ActionWithAccountBase;
import static ssc.base.ultil.ThreadUtils.Sleep;


/**
 * @author PC
 */
public class ConnectRemoteTool {

    private ActionWithAccountBase task;
    private TaskClientConnection connection = null;

    public TaskClientConnection getConnection() {
        return connection;
    }

    public void setConnection(TaskClientConnection connection) {
        this.connection = connection;
    }

    public ConnectRemoteTool(ActionWithAccountBase task) {
        this.task = task;
    }

    public void disconnect() {
        try {
            ToolSocket.getInstance().getArrClients().remove(connection);
            try {
                if (connection != null) {
                    connection.shutdown();
                }
                //client.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
        }
    }

    public Boolean connectClient(int timeout) { 
        int startTime = 1;
        while (startTime < timeout) {
            if (connection != null) {
                break;
            }
            for (TaskClientConnection client : ToolSocket.getInstance().getArrClients()) {
                
                //System.out.println("client.getName() "+client.getName()+"["+client.getName().length()+"] / "+task.getTaskOwner().toLowerCase()+"["+task.getTaskOwner().toLowerCase().length()+"]");

                System.out.println(client.getName().toLowerCase().trim()+"-"+(client.getName().toLowerCase().trim().length())+" / "+task.getTaskOwner().toLowerCase().trim()+"-"+task.getTaskOwner().toLowerCase().trim().length());
                String a="﻿huber4mc39";
                System.out.println("--"+a.charAt(0));
                
                
                if (client.getName().toLowerCase().trim().equals(task.getTaskOwner().toLowerCase().trim())){
                    System.out.println("connectingggg");
                    connection = client;
                    connection.setTask(task);
                    connection.getTask().setConnectStatus("Đã connect");
                    break;
                }
            }
            task.updateMyMessage("Chờ kết nối trình điều khiển " + startTime + "/" + timeout);
            Sleep(1000);
            startTime++;
        }
        startTime = 1;
        connection.getTask().setConnectStatus("Chờ browser");
        while (startTime < timeout) {
            
            if (connection.currentMessage().getMessage().equals("ready")) {
                connection.getTask().setConnectStatus("Browser mở");
                return true;
            }
            if (connection.currentMessage().getMessage().equals("INITFAIL")) {
                connection.getTask().setConnectStatus("Browser khởi tạo fail");
                return false;
            }
            
            task.updateMyMessage("Chờ khởi tạo trình duyệt " + startTime + "/" + timeout);
            Sleep(1000);
            startTime++;
        }

        return false;
    }

    public MessageParser getCurrentMessage() {
        return connection.currentMessage();
    }

    private long lastID;

    public long sendMessage(JSONObject object) {
        lastID = System.currentTimeMillis();
        object.put("id", lastID);
        //System.err.println(object.toJSONString());
        connection.sendMessage(object.toJSONString());
        return lastID;
    }

    public boolean loadUrl(String url, int timeOut) {
        JSONObject obj = new JSONObject();
        obj.put("url", url);
        obj.put("timeout", timeOut * 1000);
        obj.put("command", "LOAD");
        return execQuery(obj, timeOut, "Load " + url);
    }
    public boolean loadUrlWithOutTimeout(String url, int delay) {
        JSONObject obj = new JSONObject();
        obj.put("url", url);
        obj.put("timeout", 0);
        obj.put("command", "LOAD");
        return execQuery(obj, delay, "Load " + url);
    }
    public boolean clearCache() {
        JSONObject obj = new JSONObject();
        obj.put("timeout", 30* 1000);
        obj.put("command", "CLEAR_CACHE");
        return execQuery(obj, 30, "clearCache");
    }
     public boolean minimize(int timeOut) {
        JSONObject obj = new JSONObject();
        obj.put("timeout", timeOut * 1000);
        obj.put("command", "HIDE");
        return execQuery(obj, timeOut, "minimize");
    }
    
    

    public boolean loadUrlNewTab(String url, int timeOut) {
        JSONObject obj = new JSONObject();
        obj.put("url", url);
        obj.put("timeout", timeOut * 1000);
        obj.put("command", "LOAD_NEWTAB");
        return execQuery(obj, timeOut, "Load " + url);
    }
    public boolean changeTab(int index, int timeOut) {
        JSONObject obj = new JSONObject();
        obj.put("timeout", timeOut * 1000);
        obj.put("command", "CHANGE_TAB");
        obj.put("index", index);
        return execQuery(obj, timeOut, "Đổi tab "+index);
    }    
    public boolean closeTab(int index, int timeOut) {
        JSONObject obj = new JSONObject();
        obj.put("timeout", timeOut * 1000);
        obj.put("command", "CLOSE_TAB");
        obj.put("index", index);
        return execQuery(obj, timeOut, "Đóng tab "+index);
    }    
    public boolean captureImage(String path, int timeOut) {
        JSONObject obj = new JSONObject();
        obj.put("path", path);
        obj.put("timeout", timeOut * 1000);
        obj.put("command", "CAPTURE");
        return execQuery(obj, timeOut, "Capture image " + path);
    }

    public void waitForLoad(int timeOut) {
        Sleep(4000);
        JSONObject obj = new JSONObject();
        obj.put("timeout", timeOut * 1000);
        obj.put("command", "WAIT");
        execQuery(obj, timeOut, "Đang đợi trang load ");
    }

    public boolean waitElementVisible(String xpath, int timeOut, String message) {
        //Sleep(4000);
        JSONObject obj = new JSONObject();
        obj.put("timeout", timeOut * 1000);
        obj.put("command", "WAITELEMENTVISIBLE");
        obj.put("xpath", xpath);
        return execQuery(obj, timeOut, message);
    }

    public boolean waitElementVisibleWithoutClose(String xpath, int timeOut, String message) {
        //Sleep(4000);
        JSONObject obj = new JSONObject();
        obj.put("timeout", timeOut * 1000);
        obj.put("command", "WAITELEMENTVISIBLE");
        obj.put("xpath", xpath);
        return execQueryWithoutTimeout(obj, timeOut, message);
    }

    public boolean waitElementInvisibale(String xpath, int timeOut, String message) {
        //Sleep(4000);
        JSONObject obj = new JSONObject();
        obj.put("timeout", timeOut * 1000);
        obj.put("command", "WAITELEMENTDISAPPEAR");
        obj.put("xpath", xpath);
        return execQuery(obj, timeOut, message);
    }

    public boolean quitBrowser(int timeOut) {
        Sleep(3000);
        JSONObject obj = new JSONObject();
        obj.put("command", "STOP");
        return execQuery(obj, timeOut, "");
    }

    public boolean scroll(int x, int y) {
        return runCustomJs("window.scrollBy(" + x + "," + y + ")", "Scroll");
    }

    public boolean setAttribute(String xpath, int position, String key, String value) {
        return runCustomJs("document.evaluate(\"" + xpath + "\", document, null, 7, null).snapshotItem(" + position + ").setAttribute('" + key + "','" + value + "')", "Set " + key + " value " + value);
    }

    public boolean removeClass(String xpath, int position, String _class) {
        return runCustomJs("document.evaluate(\"" + xpath + "\", document, null, 7, null).snapshotItem(" + position + ").classList.remove(\"" + _class + "\")", "Xóa class " + _class);
    }

    public boolean runCustomJs(String query, String message) {
        JSONObject obj = new JSONObject();
        obj.put("command", "RUNJS");
        obj.put("JS", query);
        //System.out.println("query "+query);
        return execQuery(obj, 3, "Thực thi " + message);
    }

    public boolean runCustomJsWithWaitElement(String query, String message, String xpath, int timeout) {
        if (!runCustomJs(query, message)) {
            return false;
        }
        if (!waitElementVisible(xpath, timeout, "Chờ phần tử xuất hiện")) {
            return false;
        }
        return true;
    }

    public boolean checkFocus() {
        try {
            String checkfocus = getJs("return document.hasFocus()", 10, "");
            if (checkfocus.contains("false")) {
                return false;
            }
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public boolean runCustomJsWithWait(String query, String message) {
        JSONObject obj = new JSONObject();
        obj.put("command", "RUNJS");
        obj.put("JS", query);
        obj.put("timeout", AFTER_CLICK_TIMEOOUT * 1000);
        obj.put("waitUntil", "domcontentloaded");
        return execQuery(obj, AFTER_CLICK_TIMEOOUT, "Thực thi " + message);
    }

    public boolean upload(String xpath, String file, int timeOut) {
        JSONObject obj = new JSONObject();
        obj.put("xpath", xpath);
        obj.put("command", "SENDFILE");
        obj.put("file", file);
        obj.put("timeout", timeOut * 1000);
        obj.put("waitUntil", "domcontentloaded");
        return execQuery(obj, timeOut, "Upload file " + xpath);
    }
    public boolean exportCookies(String cookiePath, int timeOut) {
        JSONObject obj = new JSONObject();
        obj.put("command", "SAVECOOKIE");
        obj.put("cookiePath", cookiePath);
        obj.put("timeout", timeOut * 1000);
        return execQuery(obj, timeOut, "Export cookies file " + cookiePath);
    }
    public String GETURL(int timeOut) {
        JSONObject obj = new JSONObject();
        obj.put("command", "GETURL");
        return execGetQuery(obj, timeOut, "Lấy url hiện tại ");
    }

    public String getAttibute(String xpath, String key, String message, int position, int timeout) {
        String query = "return document.evaluate(\"" + xpath + "\", document, null, 7, null).snapshotItem(" + position + ").getAttribute('" + key + "')";
        return getJs(query, timeout, message);
    }

    public String getJs(String JS, int timeOut, String message) {
        JSONObject obj = new JSONObject();
        obj.put("command", "GETJS");
        obj.put("JS", JS);
        //System.out.println(JS);
        return execGetJSQuery(obj, timeOut, message);
    }

    public String getJsWithFrame(String frame,String JS, int timeOut, String message) {
        JSONObject obj = new JSONObject();
        obj.put("command", "GETJS");
        obj.put("frame", frame);
        obj.put("JS", JS);
        //System.out.println(JS);
        return execGetJSQuery(obj, timeOut, message);
    }
     
    public int GETLENGHT(String xpath, int timeOut, String message) {
        JSONObject obj = new JSONObject();
        obj.put("command", "COUNT");
        obj.put("xpath", xpath);
        return execGetLenght(obj, timeOut, message);
    }
    
    public int GETLENGHTFRAME(String frame,String xpath, int timeOut, String message) {
        JSONObject obj = new JSONObject();
        obj.put("command", "COUNT");
        obj.put("xpath", xpath);
        obj.put("frame", frame);
        return execGetLenght(obj, timeOut, message);
    }
    
    public int COUNT_PAGES(int timeOut) {
        JSONObject obj = new JSONObject();
        obj.put("command", "COUNT_PAGES");
        return execGetLenght(obj, timeOut, "Lấy số pages");
    }
    public static int AFTER_CLICK_TIMEOOUT = 240;

    public boolean clickWithoutWait(String xpath, int position) {
        return clickWithWait(xpath, position, -1);
    }
    public boolean clickWithoutWaitWithFrame(String frame,String xpath, int position) {
        return clickWithWaitFrame(frame,xpath, position, -1);
    }
    
    public boolean clickWithoutWaitWithFrameAndPosition(String frame,String xpath, int position) {
        return clickWithWaitFrame(frame,xpath, position, -1);
    }
    
    public boolean clickWithWaitTimeout(String xpath, int position, int wait) {
        return clickWithWait(xpath, position, wait);
    }

    public boolean clickWithWaitElement(String xpath, int position, String waitXpath, int timeout) {
        if (!clickWithoutWait(xpath, position)) {
            return false;
        }
        if (!waitElementVisible(waitXpath, timeout, "")) {
            return false;
        }
        return true;
    }
    
    public void popPage(int index,int wait) {
        JSONObject obj = new JSONObject();
        obj.put("command", "POPPAGE");
        if (wait != -1) {
            obj.put("timeout", wait * 1000);
            //obj.put("waitUntil", "domcontentloaded");
        }
        obj.put("index", index);
        execQuery(obj, wait == -1 ? 10 : wait, "Đổi page [" + 1 + "]");
    }
    public void backMainPage(int wait) {
        JSONObject obj = new JSONObject();
        obj.put("command", "BACKMAIN");
        if (wait != -1) {
            obj.put("timeout", wait * 1000);
            //obj.put("waitUntil", "domcontentloaded");
        }
        execQuery(obj, wait == -1 ? 10 : wait, "Đổi page [" + 1 + "]");
    }
    public void resetPage(int wait) {
        JSONObject obj = new JSONObject();
        obj.put("command", "RESETPAGE");
        if (wait != -1) {
            obj.put("timeout", wait * 1000);
            //obj.put("waitUntil", "domcontentloaded");
        }
        execQuery(obj, wait == -1 ? 10 : wait, "Đổi page [" + 1 + "]");
    }
    private boolean clickWithWait(String xpath, int position, int wait) {
        JSONObject obj = new JSONObject();
        if (xpath.startsWith("//")) {
            obj.put("xpath", xpath);
        } else {
            obj.put("selector", xpath);
        }
        obj.put("command", "CLICK");
        if (wait != -1) {
            obj.put("timeout", wait * 1000);
            obj.put("waitUntil", "domcontentloaded");
        }
        obj.put("position", position);
        return execQuery(obj, wait == -1 ? 10 : wait, "Click " + xpath + "[" + position + "]");
    }
    private boolean clickWithWaitFrame(String frame,String xpath, int position, int wait) {
        JSONObject obj = new JSONObject();
        if (xpath.startsWith("//")) {
            obj.put("xpath", xpath);
        } else {
            obj.put("selector", xpath);
        }
        obj.put("frame",frame);
        obj.put("command", "CLICK");
        if (wait != -1) {
            obj.put("timeout", wait * 1000);
            obj.put("waitUntil", "domcontentloaded");
        }
        obj.put("position", position);
        return execQuery(obj, wait == -1 ? 10 : wait, "Click " + xpath + "[" + position + "]");
    }
    public boolean clickWithPosition(String xpath, int position) {
        JSONObject obj = new JSONObject();
        if (xpath.startsWith("//")) {
            obj.put("xpath", xpath);
        } else {
            obj.put("selector", xpath);
        }
        obj.put("command", "CLICK");
        obj.put("position", position);
        return execQuery(obj, AFTER_CLICK_TIMEOOUT, "Click " + xpath + "[" + position + "]");
    }

    public boolean hoverWithPosition(String xpath, int position) {
        JSONObject obj = new JSONObject();
        if (xpath.startsWith("//")) {
            obj.put("xpath", xpath);
        } else {
            obj.put("selector", xpath);
        }
        obj.put("command", "HOVER");
        obj.put("position", position);
        return execQuery(obj, 10, "hover " + xpath + "[" + position + "]");
    }

    public boolean click(String xpath) {
        JSONObject obj = new JSONObject();
        obj.put("xpath", xpath);
        obj.put("command", "CLICK");
        obj.put("timeout", AFTER_CLICK_TIMEOOUT * 1000);
        obj.put("waitUntil", "domcontentloaded");
        return execQuery(obj, AFTER_CLICK_TIMEOOUT, "Click " + xpath);
    }

    public boolean clickWithoutTimeout(String xpath) {
        JSONObject obj = new JSONObject();
        obj.put("xpath", xpath);
        obj.put("command", "CLICK");
        obj.put("waitUntil", "domcontentloaded");
        return execQuery(obj, AFTER_CLICK_TIMEOOUT, "Click " + xpath);
    }

    public boolean sendKey(String xpath, int position, String data, int timeOut) {
        JSONObject obj = new JSONObject();
        obj.put("xpath", xpath);
        obj.put("command", "TYPE");
        obj.put("delay", (new Random().nextInt(3) + 1) * 100);
        obj.put("data", data);
        obj.put("position", position);
        return execQuery(obj, timeOut, "Send Text " + xpath + "[" + position + "]");
    }
     public boolean sendKeyWithFrame(String frame, String xpath, int position, String data, int timeOut) {
        JSONObject obj = new JSONObject();
        obj.put("xpath", xpath);
        obj.put("command", "TYPE");
        obj.put("frame",frame);
        obj.put("delay", ((new Random().nextInt(3) + 5) * 100));
        obj.put("data", data);
        obj.put("position", position);
        return execQuery(obj, timeOut, "Send Text " + xpath + "[" + position + "]");
    }        
    public boolean select(String xpath, int position, String data, int timeOut) {
        JSONObject obj = new JSONObject();
        obj.put("xpath", xpath);
        obj.put("command", "SELECT");
        obj.put("data", data);
        obj.put("position", position);
        return execQuery(obj, timeOut, "Select Value " + xpath + "[" + position + "]");
    }
        
        
        
    public int extraTimeout=0;
    public boolean execQueryWithoutTimeout(JSONObject obj, int time, String message) {
        long id = sendMessage(obj);
        long startTime = System.currentTimeMillis();
        long currentTime = 0;
        MessageParser currentMessage = connection.currentMessage();
        while (currentMessage == null || currentMessage.getCode() != id) {
            currentTime = (System.currentTimeMillis() - startTime) / 1000;
            Sleep(1000);
            if (message.length() != 0) {
                task.updateMyMessage(" Đang " + message + " " + currentTime + "/" + (time + extraTimeout));
            }
            currentMessage = connection.currentMessage();
            if (currentTime >= time + extraTimeout) {
                task.insertErrorLog("[" + currentTime + "] chờ " + message, currentMessage.getError());
                return false;
            }
        }
        if (currentMessage.getCode() == id && currentMessage.getMessage().contains("success")) {
            task.insertSuccessLog("[" + currentTime + "] Hoàn thành " + message);
            return true;
        }
        task.insertErrorLog("[" + currentTime + "] lỗi " + message, currentMessage.getError());
        return false;
    }
    
    public boolean execQuery(JSONObject obj, int time, String message) {
        long id = sendMessage(obj);
        long startTime = System.currentTimeMillis();
        long currentTime = 0;
        MessageParser currentMessage = connection.currentMessage();
        while (currentMessage == null || currentMessage.getCode() != id) {
            currentTime = (System.currentTimeMillis() - startTime) / 1000;
            Sleep(1000);
            if (message.length() != 0) {
                task.updateMyMessage(" Đang " + message + " " + currentTime + "/" + (time + extraTimeout));
            }
            currentMessage = connection.currentMessage();
            if (currentTime >= time + extraTimeout) {
                task.setTimeout(true);
                task.insertSuccessLog(message + " time out");
                return false;
            }
        }
        //System.out.println("getCode "+currentMessage.getCode());
        //System.out.println("message "+currentMessage.getMessage());
        if (currentMessage.getCode() == id && currentMessage.getMessage().contains("success")) {
            task.insertSuccessLog("[" + currentTime + "]Hoàn thành " + message);
            return true;
        }
        task.insertErrorLog("[" + currentTime + "] Lỗi "+ message, currentMessage.getError());
        return false;
    }

    public int execGetLenght(JSONObject obj, int time, String message) {
        long id = sendMessage(obj);
        long startTime = System.currentTimeMillis();
        long currentTime = 0;
        MessageParser currentMessage = connection.currentMessage();
        while (currentMessage == null || currentMessage.getCode() != id) {
            currentTime = (System.currentTimeMillis() - startTime) / 1000;
            Sleep(1000);
            if (message.length() != 0) {
                task.updateMyMessage(" Đang " + message + " " + currentTime + "/" + (time + extraTimeout));
            }
            currentMessage = connection.currentMessage();
            if (currentTime >= time + extraTimeout) {
                task.setTimeout(true);
                //task.insertSuccessLog(message + " time out");
                return 0;
            }
        }
        if (currentMessage.getCode() == id && currentMessage.getMessage().contains("success")) {
            if (message.length() != 0) {
                //task.insertSuccessLog("[" + currentTime + "]" + (TS.getInstance().getSmode() ? "Count: " : "Số phần tử ") + currentMessage.getCount());
            }
            return currentMessage.getCount();
        }
        task.insertErrorLog("[" + currentTime + "] Lỗi " + message, currentMessage.getError());
        return 0;

    }

    private String execGetJSQuery(JSONObject obj, int time, String message) {
        long id = sendMessage(obj);
        long startTime = System.currentTimeMillis();
        long currentTime = 0;
        MessageParser currentMessage = connection.currentMessage();
        while (currentMessage == null || currentMessage.getCode() != id) {
            currentTime = (System.currentTimeMillis() - startTime) / 1000;
            Sleep(1000);
            if (message.length() != 0) {
                task.updateMyMessage(" Đang " + message + " " + currentTime + "/" + (time + extraTimeout));
            }
            currentMessage = connection.currentMessage();
            if (currentTime >= time + extraTimeout) {
                task.setTimeout(true);
                //task.insertSuccessLog(message + " time out");
                return "";
            }
        }
        if (currentMessage.getCode() == id && currentMessage.getMessage().contains("success")) {
            if (message.length() != 0) {
                //task.insertSuccessLog(message + " " + currentMessage.getValue());
                //task.insertSuccessLog("[" + currentTime + "]" + (TS.getInstance().getSmode() ? "Result: " : "Kết quả ") + currentMessage.getValue());
            }
            return currentMessage.getValue();
        }
        task.insertErrorLog("[" + currentTime + "] Lỗi " + message, currentMessage.getError());
        return "";

    }

    public String execGetQuery(JSONObject obj, int time, String message) {
        long id = sendMessage(obj);
        long startTime = System.currentTimeMillis();
        long currentTime = 0;
        MessageParser currentMessage = connection.currentMessage();
        while (currentMessage == null || currentMessage.getCode() != id) {
            currentTime = (System.currentTimeMillis() - startTime) / 1000;
            Sleep(1000);
            task.updateMyMessage(" Đang " + message + " " + currentTime + "/" + (time + extraTimeout));
            currentMessage = connection.currentMessage();
            if (currentTime >= time + extraTimeout) {
                task.setTimeout(true);
                task.insertSuccessLog(message + " time out");
                return "";
            }
        }
        if (currentMessage.getCode() == id) {
            //task.insertSuccessLog("[" + currentTime + "]" + (TS.getInstance().getSmode() ? "Result: " : "Kết quả ") + currentMessage.getValue());
            return currentMessage.getMessage();
        }
        task.insertErrorLog("[" + currentTime + "] Lỗi " + message, currentMessage.getError());
        return "";

    }
  
}
