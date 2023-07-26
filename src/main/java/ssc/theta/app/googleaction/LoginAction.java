/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.theta.app.googleaction;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventDispatchChain;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import ssc.base.global.TC;
import ssc.base.gologin.GologinDriver;
import ssc.base.run.ActionWithAccountBase;
import ssc.theta.app.api.MetaApi;
import ssc.theta.app.model.GoogleAccount;
import ssc.theta.app.model.GoogleAccount.ACCOUNT_LOGIN_STATUS;
import ssc.theta.app.model.ProfileG;

/**
 * @author simpl
 */
public class LoginAction extends ActionWithAccountBase {

    @Override
    public String objectHistory() {
        return "";
    }
    private List<String> arrAccountFollow;

    public List<String> getArrAccountFollow() {
        return arrAccountFollow;
    }

    public void setArrAccountFollow(List<String> arrAccountFollow) {
        this.arrAccountFollow = arrAccountFollow;
    }

    public LoginAction(int stt, GoogleAccount accountModel) {
        super(stt, accountModel);
        this.arrAccountFollow = new ArrayList<>();
        this.login = false;
    }
    private boolean clearMode = false;
    private boolean login = true;
    private String verifyPage = "";
    private String bio;
    private boolean getCookies = false;

    public boolean isGetCookies() {
        return getCookies;
    }

    public void setGetCookies(boolean getCookies) {
        this.getCookies = getCookies;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getVerifyPage() {
        return verifyPage;
    }

    public void setVerifyPage(String verifyPage) {
        this.verifyPage = verifyPage;
    }

    private SimpleStringProperty accountUsername;

    public SimpleStringProperty getAccountUsername() {
        return accountUsername;
    }

    public void setAccountUsername(SimpleStringProperty accountUsername) {
        this.accountUsername = accountUsername;
    }

    public boolean isClearMode() {
        return clearMode;
    }

    public void setClearMode(boolean clearMode) {
        this.clearMode = clearMode;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    /**
     * @param accountModel
     * @param stt
     * @param makeChannel
     * @param checkDie
     */
    private String image;

    public LoginAction(GoogleAccount accountModel, int stt, String image) {
        super(stt, accountModel);
        updateTitle("Login tài khoản vào VPS");
        this.accountUsername = new SimpleStringProperty(accountModel.getGoogleAccountname());
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String initFunction() {
        updateMyMessage("Khởi tạo profile");

        if (getCookies == true) {
            insertSuccessLog("Profile cũ " + getAccount().getUsername().toLowerCase());
            try {
                ProfileG profile;
                if (getAccount().getUsername().toLowerCase().length() == 0) {
                    insertErrorLog("Tài khoản này chưa có profile", "");
                    return "Tài khoản này chưa có profile";
                } else {
                    updateMyMessage("Đang load profile cũ " + getAccount().getUsername().toLowerCase());
                    profile = MetaApi.getProfileInfo(getAccount().getUsername().toLowerCase());
                    if (profile == null) {
                        insertErrorLog("Tài khoản này chưa có profile", "");
                        return "Tài khoản này chưa có profile";

                    } else {
                        getAccount().setProfile(profile);
                        insertSuccessLog("Lấy được profile cũ");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (getAccount().getProfile() == null) {
                insertErrorLog("Không tạo được profile", "");
                return "Không khởi tạo được profile";
            }
        } else {
            try {
                ProfileG profile = new ProfileG(0, getAccount());
                updateMyMessage("Đang tạo profile gologin ");
                ProfileG profileInsert = MetaApi.insertProfile(profile);
                if (profileInsert == null) {
                    insertErrorLog("Không tạo được profile", "");
                    return "Không thể tạo profile gologin";
                }
                String profileId = profileInsert.getId();
                if (profileId.length() == 0) {
                    insertErrorLog("Không tạo được profile", "");
                    return "Không thể tạo profile gologin";
                }
                insertSuccessLog("Đã tạo profile gologin " + profileId);
                getAccount().setProfile_id(profileId);
                getAccount().updateData();
                getAccount().setProfile(profileInsert);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (getAccount().getProfile() == null) {
                insertErrorLog("Không tạo được profile", "");
                return "Không khởi tạo được profile";
            }
            insertSuccessLog("Đang khởi tạo gologin driver");
            getAccount().setLogin_status(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
            getAccount().updateData();
        }

        //System.out.println(TC.getInts().gologin_folder + File.separator + "gologin_profile_" + getAccount().getProfile_id());
        //ThreadUtils.Sleep(30000000);
        setDriver(new GologinDriver(getAccount().getConnectionName(), getAccount().getProfile().getId(), getAccount().getProfile().getCanvas(), TC.getInts().gologin_folder, getArrRemoteLog()));
        return "";
    }

    @Override
    public void afterFail() {

    }

    @Override
    public void afterSuccess() {

    }

    @Override
    public boolean automationAction() {
        try {

            waitChildTask(760, new LoginTask(this));

        } catch (Exception e) {
        }
        return true;
    }

    public static void initTableLoginAction(TableView tv, ObservableList<LoginAction> data) {

        TableColumn<LoginAction, String> sttCol = new TableColumn("STT");
        sttCol.setCellValueFactory(new PropertyValueFactory<>("stt"));
        sttCol.setPrefWidth(50);
        sttCol.setResizable(false);

        TableColumn<LoginAction, LoginAction> infoCol = new TableColumn("Tài khoản");
        infoCol.setResizable(false);
        infoCol.setPrefWidth(150);
        infoCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        infoCol.setCellFactory(param -> new TableCell<LoginAction, LoginAction>() {
            private Label lbTitle;
            private Label lbMessage;

            @Override
            public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
                return super.buildEventDispatchChain(tail); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            protected void updateItem(LoginAction videoData, boolean empty) {
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

        TableColumn<LoginAction, String> serverPortCol = new TableColumn("Port");
        serverPortCol.setCellValueFactory(new PropertyValueFactory<>("port"));
        serverPortCol.setPrefWidth(100);
        serverPortCol.setResizable(false);

        TableColumn<LoginAction, LoginAction> processCol = new TableColumn("Tiền trình");
        processCol.setResizable(false);
        processCol.setPrefWidth(tv.getPrefWidth() - 330);
        processCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        processCol.setCellFactory(param -> new TableCell<LoginAction, LoginAction>() {
            private Label lbTitle;
            private Label lbMessage;

            @Override
            public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
                return super.buildEventDispatchChain(tail); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            protected void updateItem(LoginAction videoData, boolean empty) {
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
