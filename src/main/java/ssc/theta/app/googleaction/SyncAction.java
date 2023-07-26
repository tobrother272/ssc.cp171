/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.theta.app.googleaction;

import java.util.HashMap;
import java.util.Map;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventDispatchChain;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.json.simple.JSONObject;
import ssc.base.global.TC;
import ssc.base.run.ActionWithAccountBase;
import ssc.base.ultil.ApiHelper;
import ssc.theta.app.model.GoogleAccount;
import ssc.theta.app.model.GoogleAccount.ACCOUNT_LOGIN_STATUS;

/**
 * @author simpl
 */
public class SyncAction extends ActionWithAccountBase {

    @Override
    public String objectHistory() {
        return "";
    }

    public SyncAction(int stt, GoogleAccount accountModel) {
        super(stt, accountModel);
    }

    private SimpleStringProperty accountUsername;

    public SimpleStringProperty getAccountUsername() {
        return accountUsername;
    }

    public void setAccountUsername(SimpleStringProperty accountUsername) {
        this.accountUsername = accountUsername;
    }
    private Boolean syncSussess = false;

    public Boolean getSyncSussess() {
        return syncSussess;
    }

    public void setSyncSussess(Boolean syncSussess) {
        this.syncSussess = syncSussess;
    }

    @Override
    public String initFunction() {
        return "";
    }

    @Override
    public void afterFail() {

    }

    @Override
    public void afterSuccess() {

    }
    private SimpleIntegerProperty serverPort;

    public SimpleIntegerProperty serverPortProperty() {
        if (serverPort == null) {
            serverPort = new SimpleIntegerProperty(-1);
        }
        return serverPort;
    }

    public int getServerPort() {
        return serverPort.get();
    }

    public void setServerPort(int _serverPort) {
        if (serverPort == null) {
            serverPort = new SimpleIntegerProperty(-1);
        }
        this.serverPort.set(_serverPort);
    }

    @Override
    public boolean automationAction() {
        try {

            if (getAccount().getCookies().length() == 0) {
                updateMessage("Tài khoản này không có cookies ");
                return false;
            }
            if (getAccount().getLogin_status().equals(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue())) {
                updateMessage("Tài khoản này chưa login");
                return false;
            }
            if (getAccount().getPremium_expired() == 0) {
                updateMessage("Tài khoản này chưa có premium");
                return false;
            }

            JSONObject fingrPrintOBJ = ApiHelper.getDataWithUrl("http://sscapi-env.eba-6ecwx6hg.ap-southeast-1.elasticbeanstalk.com/api/getFinger");
               
            if (fingrPrintOBJ == null || fingrPrintOBJ.get("finger") == null) {
                updateMessage("Không lấy được finger print ");
                return false;
            }
            String finger = fingrPrintOBJ.get("finger").toString();
            
            if(TC.getInts().account_api.contains("http://accpremiumpostget-env.ap-southeast-1.elasticbeanstalk.com")){
                Map<String, String> datas = new HashMap<String, String>();
                datas.put("username", getAccount().getUsername());
                datas.put("password", getAccount().getPassword());
                datas.put("recover", getAccount().getGmail_recover());
                datas.put("cookie", getAccount().getCookies());
                datas.put("encodefinger", finger);
                datas.put("live", "1");
                datas.put("premium", "1");
                datas.put("endtrial", "" + getAccount().getPremium_expired());
                JSONObject fingrPrintOBJReusl = ApiHelper.postDataWitAccessTokenDataUrl(TC.getInts().account_api + "/gmails/create", TC.getInts().account_token, datas);
                //System.out.println(fingrPrintOBJReusl.toJSONString());
                if (fingrPrintOBJReusl.toJSONString().contains("Duplicate entry")) {
                    updateMessage("Tài khoản này đã được đồng bộ trước đó");
                    syncSussess = false;
                    return false;
                }
                if (fingrPrintOBJReusl != null && fingrPrintOBJReusl.get("status") != null) {
                    if (fingrPrintOBJReusl.get("status").toString().contains("true")) {
                        updateMessage("Đồng bộ thành công ");
                        syncSussess = true;
                    }
                }
            }else{
                //{{apiURL}}
            //"username",[[EMAIL]],"passwd",[[PASSWORD]],"recover",[[RECOVERY_EMAIL]],"computer","","cookies",[[SAVED_COOKIES]],"platform","PC","finger_print",[[ENCODE_FINGERPRINT]],"live","1","premium","1","premium_expired",[[EXPIRED_DATETIME]]
            Map<String, String> datas = new HashMap<String, String>();
            datas.put("username", getAccount().getUsername());
            datas.put("passwd", getAccount().getPassword());
            datas.put("recover", getAccount().getGmail_recover());
            datas.put("computer", "");
            datas.put("cookies", getAccount().getCookies());
            datas.put("platform", "PC");
            datas.put("finger_print", finger);
            datas.put("live", "1");
            datas.put("premium", "1");
            datas.put("premium_expired", "" + getAccount().getPremium_expired());

            JSONObject fingrPrintOBJReusl = ApiHelper.postDataWitAccessTokenDataUrl(TC.getInts().account_api + "/account/insert", TC.getInts().account_token, datas);
            if (fingrPrintOBJReusl.toJSONString().contains("Duplicate entry")) {
                updateMessage("Tài khoản này đã được đồng bộ trước đó");
                syncSussess = false;
                return false;
            }

            if (fingrPrintOBJReusl != null && fingrPrintOBJReusl.get("status") != null) {
                if (fingrPrintOBJReusl.get("status").toString().contains("success")) {
                    updateMessage("Đồng bộ thành công ");
                    syncSussess = true;
                }
            }
            }
            
            

        } catch (Exception e) {
        }
        return true;
    }

    public static void initTableLoginAction(TableView tv, ObservableList<SyncAction> data) {

        TableColumn<SyncAction, String> sttCol = new TableColumn("STT");
        sttCol.setCellValueFactory(new PropertyValueFactory<>("stt"));
        sttCol.setPrefWidth(50);
        sttCol.setResizable(false);

        TableColumn<SyncAction, SyncAction> infoCol = new TableColumn("Tài khoản");
        infoCol.setResizable(false);
        infoCol.setPrefWidth(150);
        infoCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        infoCol.setCellFactory(param -> new TableCell<SyncAction, SyncAction>() {
            private Label lbTitle;
            private Label lbMessage;

            @Override
            public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
                return super.buildEventDispatchChain(tail); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            protected void updateItem(SyncAction videoData, boolean empty) {
                super.updateItem(videoData, empty);
                if (videoData == null) {
                    setGraphic(null);
                    return;
                }
                try {
                    AnchorPane apRoot = new AnchorPane();
                    lbTitle = new Label(videoData.getAccount().getUsername());
                    lbTitle.setPrefSize(apRoot.getPrefWidth(), 30);
                    lbTitle.setLayoutX(10);
                    lbTitle.setLayoutY(5);
                    lbTitle.getStyleClass().setAll("labelTableMain");
                    apRoot.getChildren().add(lbTitle);
                    lbMessage = new Label();
                    lbMessage.setPrefSize(apRoot.getPrefWidth(), 30);
                    lbMessage.setLayoutX(10);
                    lbMessage.setLayoutY(25);
                    apRoot.getChildren().add(lbMessage);
                    lbMessage.getStyleClass().setAll("labelTableSecondary");
                    lbMessage.textProperty().bind(videoData.ipProperty());
                    setGraphic(apRoot);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        TableColumn<SyncAction, String> serverPortCol = new TableColumn("Port");
        serverPortCol.setCellValueFactory(new PropertyValueFactory<>("serverPort"));
        serverPortCol.setPrefWidth(100);
        serverPortCol.setResizable(false);

        TableColumn<SyncAction, SyncAction> processCol = new TableColumn("Tiền trình");
        processCol.setResizable(false);
        processCol.setPrefWidth(tv.getPrefWidth() - 330);
        processCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        processCol.setCellFactory(param -> new TableCell<SyncAction, SyncAction>() {
            private Label lbTitle;
            private Label lbMessage;

            @Override
            public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
                return super.buildEventDispatchChain(tail); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            protected void updateItem(SyncAction videoData, boolean empty) {
                super.updateItem(videoData, empty);
                if (videoData == null) {
                    setGraphic(null);
                    return;
                }
                try {
                    AnchorPane apRoot = new AnchorPane();
                    lbTitle = new Label();
                    lbTitle.setPrefSize(apRoot.getPrefWidth(), 30);
                    lbTitle.setLayoutX(10);
                    lbTitle.setLayoutY(5);
                    lbTitle.getStyleClass().setAll("labelTableMain");
                    apRoot.getChildren().add(lbTitle);
                    lbMessage = new Label();
                    lbMessage.setPrefSize(apRoot.getPrefWidth(), 30);
                    lbMessage.setLayoutX(10);
                    lbMessage.setLayoutY(25);
                    lbMessage.getStyleClass().setAll("labelTableSecondary");
                    apRoot.getChildren().add(lbMessage);
                    lbTitle.textProperty().bind(videoData.titleProperty());
                    lbMessage.textProperty().bind(videoData.messageProperty());
                    setGraphic(apRoot);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        tv.getColumns().addAll(sttCol, serverPortCol, infoCol, processCol);

        tv.setItems(data);
    }

 

}
