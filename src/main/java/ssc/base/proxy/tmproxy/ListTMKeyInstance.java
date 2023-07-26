/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.proxy.tmproxy;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ssc.base.global.TC;
import ssc.base.ultil.StringUtils;


/**
 *
 * @author PC
 */
public class ListTMKeyInstance {
    private ObservableList<TMKeyAndProxy> dataKeyAndProxyTM;
    public static ListTMKeyInstance instance;
    public ListTMKeyInstance() {
        this.dataKeyAndProxyTM = FXCollections.observableArrayList();
    }
    public ObservableList<TMKeyAndProxy> getDataKeyAndProxyTM() {
        return dataKeyAndProxyTM;
    }
    public void setDataKeyAndProxyTM(ObservableList<TMKeyAndProxy> dataKeyAndProxyTM) {
        this.dataKeyAndProxyTM = dataKeyAndProxyTM;
    }
    public static ListTMKeyInstance getInstance() {
        if (instance == null) {
            instance = new ListTMKeyInstance();
        }
        return instance;
    }
    public void initTMList() {
        dataKeyAndProxyTM.clear();
        List<String> arr = StringUtils.getListStringBySplit(TC.getInts().tm_keys, "\n");
        for (String string : arr) {
            dataKeyAndProxyTM.add(new TMKeyAndProxy(string));
        }
    }

    public TMKeyAndProxy getTMKeyAvailable() {
        for (TMKeyAndProxy tMKeyAndProxy : dataKeyAndProxyTM) {
            if (tMKeyAndProxy.getAccount().length() == 0 && tMKeyAndProxy.getTimeNext() < System.currentTimeMillis()) {
                return tMKeyAndProxy;
            }
        }
        return null;
    }

}
