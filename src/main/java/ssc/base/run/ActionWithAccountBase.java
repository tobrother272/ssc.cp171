/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.run;

import java.awt.Toolkit;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ssc.base.global.BrowserMapData;
import ssc.base.global.TC;
import ssc.base.gologin.GologinDriver;
import ssc.base.proxy.tmproxy.TMKeyAndProxy;
import ssc.base.run.connection.ConnectRemoteTool;
import ssc.theta.app.googleaction.SyncAction;
import ssc.theta.app.model.GoogleAccount;
import ssc.theta.app.model.TaskLog;

/**
 *
 * @author PC
 */
public abstract class ActionWithAccountBase extends ActionBase {

    private ConnectRemoteTool connection;
    private GoogleAccount account;
    private int driverPort;
    private SimpleStringProperty errorImage;
    private GologinDriver driver;

    private ObservableList<TaskLog> arrRemoteLog;
    private boolean timeout = false;
    private SimpleStringProperty connectStatus;
    private String proxyInfo = "";
    private boolean headless = false;

    public boolean isHeadless() {
        return headless;
    }

    public void setHeadless(boolean headless) {
        this.headless = headless;
    }

    public String getProxyInfo() {
        return proxyInfo;
    }

    public void setProxyInfo(String proxyInfo) {
        this.proxyInfo = proxyInfo;
    }

    private TMKeyAndProxy tMKeyAndProxy;

    public TMKeyAndProxy gettMKeyAndProxy() {
        return tMKeyAndProxy;
    }

    public void settMKeyAndProxy(TMKeyAndProxy tMKeyAndProxy) {
        this.tMKeyAndProxy = tMKeyAndProxy;
    }

    @Override
    public boolean initAutomationAction() {
        if (this instanceof SyncAction) {

        } else {
            waitChildTask(240, new OpenAndConnectBrowserTask(this, headless));
            if (getConnection().getConnection() == null) {
                return false;
            }
        }

        return true;
    }

    public SimpleStringProperty connectStatusProperty() {
        return connectStatus;
    }

    public String getConnectStatus() {
        return connectStatus.get();
    }

    public void setConnectStatus(String connectStatus) {
        this.connectStatus.set(connectStatus);
    }

    public boolean isTimeout() {
        return timeout;
    }

    public void setTimeout(boolean timeout) {
        this.timeout = timeout;
    }
    private int browserX = 0;
    private int browserY = 0;

    public int getBrowserX() {
        return browserX;
    }

    public void setBrowserX(int browserX) {
        this.browserX = browserX;
    }

    public int getBrowserY() {
        return browserY;
    }

    public void setBrowserY(int browserY) {
        this.browserY = browserY;
    }

    private SimpleStringProperty browserLocation;

    public SimpleStringProperty browserLocationProperty() {
        return browserLocation;
    }

    public String getBrowserLocation() {
        return browserLocation.get();
    }

    public int browserW;
    public int browserH;

    public void initTab() {
        int windowW = 0;
        int windowH = 0;
        windowW = (int) (Toolkit.getDefaultToolkit().getScreenSize().getWidth() > 1920 ? 1920 : Toolkit.getDefaultToolkit().getScreenSize().getWidth());
        windowH = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() > 1080 ? 1080 : Toolkit.getDefaultToolkit().getScreenSize().getHeight());

        if (this.numberTabsBrowserOpen <= 4) {
            if (this.numberTabsBrowserOpen == 1) {
                browserW = (int) windowW;
                browserH = (int) windowH;
            } else if (this.numberTabsBrowserOpen == 2) {
                browserW = (int) windowW / 2;
                browserH = (int) windowH;
            } else {
                browserW = (int) windowW / 2;
                browserH = (int) windowH / 2;
            }
        } else {
            browserW = (int) windowW / 4;
            browserH = (int) (windowH / (numberTabsBrowserOpen / 4));
        }

        BrowserMapData.getInstance().initData(numberTabsBrowserOpen);
    }
    public int numberTabsBrowserOpen = 20;

    public int[] getBrowserLocation(int position) {
        int data[] = new int[2];

        if (this.numberTabsBrowserOpen <= 4) {
            if (this.numberTabsBrowserOpen == 1) {
                data[0] = 0;
                data[1] = 0;
            } else if (this.numberTabsBrowserOpen == 2) {
                data[0] = browserW * position;
                data[1] = 0;
            } else {
                int currentRow = position / 2;
                data[0] = (position % 2) * browserW;
                data[1] = currentRow * browserH;
            }
        } else {
            int currentRow = position / 4;
            data[0] = (position % 4) * browserW;
            data[1] = currentRow * browserH;
        }

        return data;
    }

    public void setBrowserLocation(String browserLocation) {
        try {
            this.browserLocation.set(browserLocation);
            int point[] = getBrowserLocation(getBrowserLocationInt());
            browserX = point[0];
            browserY = point[1];
        } catch (Exception e) {
        }

    }

    public void getScreenLocation() {
        String x = getConnection().getJs("return window.screenX", 5, "");
        String y = getConnection().getJs("return window.screenY", 5, "");
        insertSuccessLog("Vị trí trình duyệt " + x + "," + y);
    }

    public int getBrowserLocationInt() {
        return Integer.parseInt(browserLocation.get());
    }

    public void clearBrowserLocation() {
        BrowserMapData.getInstance().addPosition(browserLocation.get());
    }

    public ObservableList<TaskLog> getArrRemoteLog() {
        return arrRemoteLog;
    }

    public void setArrRemoteLog(ObservableList<TaskLog> arrRemoteLog) {
        this.arrRemoteLog = arrRemoteLog;
    }

    public void insertRemoteLog(TaskLog log) {
        arrRemoteLog.add(log);
    }

    public GologinDriver getDriver() {
        return driver;
    }

    public void setDriver(GologinDriver driver) {
        this.driver = driver;
    }

    public SimpleStringProperty errorImageProperty() {
        if (errorImage == null) {
            errorImage = new SimpleStringProperty("");
        }
        return errorImage;
    }

    public String getErrorImage() {
        return errorImage.get();
    }

    public void setErrorImage(String _errorImage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                errorImage.set(_errorImage);
            }
        });

    }

    public int getDockerPort() {
        return driverPort;
    }

    
    public void setDockerPort(int driverPort) {
        this.driverPort = driverPort;
    }

    public GoogleAccount getAccount() {
        return account;
    }
    

    public void setAccount(GoogleAccount account) {
        this.account = account;
    }

    public ConnectRemoteTool getConnection() {
        return connection;
    }

    public void setConnection(ConnectRemoteTool connection) {
        this.connection = connection;
    }

    public ActionWithAccountBase(int stt, GoogleAccount account) {
        super(stt);
        arrRemoteLog = FXCollections.observableArrayList();
        this.account = account;
        setTaskOwner(account.getConnectionName());
        errorImage = new SimpleStringProperty("");
        connectStatus = new SimpleStringProperty("Chờ");
        browserLocation = new SimpleStringProperty("Chờ");
        connection = new ConnectRemoteTool(this);
        if (account.getProfile() != null) {
            driver = new GologinDriver(account.getConnectionName(), account.getProfile().getId(), account.getProfile().getCanvas(), TC.getInts().gologin_folder, getArrRemoteLog());
        }
    }

    @Override
    public void afterFail() {

    }

    @Override
    public void afterSuccess() {

    }

    @Override
    public String initFunction() {
        return "";
    }

    @Override
    public String objectHistory() {
        return "";
    }

    @Override
    public void checkAndStopTask() {
        if (connection.getConnection() != null && connection.getConnection().isShudown()) {
            stop();
        }
    }

    @Override
    public void updateAccountInfo() {
        account.updateData();
        getConnection().disconnect();
    }

}
