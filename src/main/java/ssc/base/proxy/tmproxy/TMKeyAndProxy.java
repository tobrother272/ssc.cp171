



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.proxy.tmproxy;


import javafx.beans.property.SimpleStringProperty;
import ssc.base.ultil.StringUtils;

/**
 * @author PC
 */
public class TMKeyAndProxy {
    private String key;
    private SimpleStringProperty ip;
    private SimpleStringProperty account;
    private SimpleStringProperty time;
    public SimpleStringProperty ipProperty() {
        return ip;
    }
     public String getIp() {
        return ip.get();
    }
    public void setIp(String ip) {
        this.ip.set(ip);
    }
    public String getProxyInfo() {
        return GetTMProxy.GetTMProxy(key);
    }
    public SimpleStringProperty accountProperty() {
        return account;
    }
    public String getAccount() {
        return account.get();
    }
    public void setAccount(String account) {
        this.account.set(account);
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }
    public String getTime() {
        return time.get();
    }
    public void setTime(SimpleStringProperty time) {
        this.time = time;
    }
    
    
    private long timeNext;
    
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public long getTimeNext() {
        return timeNext;
    }

    public void setTimeNext(long timeNext) {
        this.timeNext = timeNext;
    }
    
    public void reloadTime(String ip){
        timeNext=System.currentTimeMillis()+(GetTMProxy.getTMProxyLastTime(key)*1000);
        time.set(StringUtils.convertLongToDataTime("HH:mm", timeNext));
        setIp(ip);
    }
    
    public TMKeyAndProxy(String key) {
        this.key = key;
        this.time=new SimpleStringProperty("n/a");
        this.account=new SimpleStringProperty("");
        this.ip=new SimpleStringProperty("n/a");
        reloadTime("");
    }
    
    
}
