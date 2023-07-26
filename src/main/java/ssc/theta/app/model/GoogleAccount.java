/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.theta.app.model;

import java.io.File;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import org.json.simple.JSONObject;
import ssc.base.annotation.SSCDatabaseField;
import ssc.base.annotation.SSCDatabaseTable;
import ssc.base.ui.components.ButtonTooltip;
import ssc.base.modal.SSCConfirmTaskModal;
import ssc.base.view.SSCLogView;
import ssc.base.database.BaseModel;
import ssc.base.database.BaseModel.ColType;
import ssc.base.database.BaseModel.ViewType;
import ssc.base.database.SQLIteHelper;
import static ssc.base.database.SQLIteHelper.getIntegerFromRS;
import ssc.base.global.TC;
import ssc.base.gologin.OpenGologinBrowserTask;
import ssc.base.ultil.Graphics;
import ssc.base.ultil.StringUtils;
import ssc.base.ui.components.SSCTableColum;
import ssc.base.ultil.MyFileUtils;
import ssc.base.view.SSCMessage;
import ssc.theta.app.api.MetaApi;

/**
 *
 * @author PC
 */
public class GoogleAccount extends BaseModel {

    public static enum ACCOUNT_STATUS {
        LIVE("Sống"), EMAIL_DEAD("Chết Email"), CHANNEL_DEAD("Chết Kênh"), WRONG_INFO("Sai Info"), VERIFY("Tài khoản verify"), EXCEPTION("Lỗi ngoại lệ");
        private String value;

        private ACCOUNT_STATUS(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public String getProfileFolder() {
        return TC.getInts().gologin_folder + File.separator + (username.contains("@") ? username.toLowerCase().split("@")[0] : username.toLowerCase()).trim().replaceAll("[^\\x20-\\x7e]", "");
    }

    public String getConnectionName() {
        return (username.contains("@") ? username.toLowerCase().split("@")[0] : username.toLowerCase()).trim().replaceAll("[^\\x20-\\x7e]", "");
    }

    public void removeCache() {
        try {
            String path = getProfileFolder();

            MyFileUtils.deleteFolder(new File(path + File.separator + "Default" + File.separator + "Cache"));

            //System.out.println("cache " + path + File.separator + "Default" + File.separator + "Cache");

            MyFileUtils.deleteFolder(new File(path + File.separator + "Default" + File.separator + "Code Cache"));
        } catch (Exception e) {
        }
    }

    public static enum ACCOUNT_LOGIN_STATUS {
        LOGINED("Đã login"), LOGOUT("Chưa login");
        private String value;

        private ACCOUNT_LOGIN_STATUS(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }
    @SSCDatabaseField(sql_col_name = "id", sql_col_type = ColType.BIGINT, sql_col_key = true, sql_col_tyle_length = 80, view_type = ViewType.NOVIEW)
    private long id;
    @SSCDatabaseField(not_null = true, txt_index = 0, sql_col_name = "username", txt_format = true, sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 70, view_name = "Tên đăng nhập", view_type = ViewType.TextField)
    private String username;
    @SSCDatabaseField(not_null = true, txt_index = 1, sql_col_name = "password", txt_format = true, sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 15, view_name = "Mật khẩu", view_type = ViewType.TextField)
    private String password;
    @SSCDatabaseField(not_null = true, txt_index = 2, sql_col_name = "gmail_recover", txt_format = true, sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 100, view_name = "Email recover", view_type = ViewType.TextField)
    private String gmail_recover;
    @SSCDatabaseField(not_null = true, txt_index = 3, sql_col_name = "phone_recover", txt_format = true, sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 100, view_name = "Phone recover", view_type = ViewType.TextField)
    private String phone_recover;
    @SSCDatabaseField(not_null = true, sql_col_name = "gmail_recover_passwd", txt_format = true, sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 100, view_name = "Email recover", view_type = ViewType.TextField)
    private String gmail_recover_passwd;
    @SSCDatabaseField(not_null = true, sql_col_name = "profile_name", txt_format = true, sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 70, view_name = "Tên đăng nhập", view_type = ViewType.TextField)
    private String profile_name;
    @SSCDatabaseField(not_null = true, sql_col_name = "status", txt_format = true, sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 20, view_name = "Trạng thái", view_type = ViewType.ComboBox, defaultValue = {"Sống", "Chết Email", "Chết Kênh", "Sai Info"}, start_value = "Sống")
    private String status;
    @SSCDatabaseField(not_null = true, sql_col_name = "login_status", txt_format = true, sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 20, view_name = "Trạng thái Login", view_type = ViewType.ComboBox, defaultValue = {"Chưa login", "Đã login"}, start_value = "Chưa login")
    private String login_status;
    @SSCDatabaseField(not_null = false, sql_col_name = "profile_id", txt_format = true, sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 100, view_name = "Profile Id", view_type = ViewType.TextField)
    private String profile_id;
    @SSCDatabaseField(sql_col_name = "last_time", sql_col_type = ColType.BIGINT, sql_col_tyle_length = 80, view_type = ViewType.NOVIEW)
    private long last_time;
    @SSCDatabaseField(not_null = false, sql_col_name = "proxy", txt_format = true, sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 20, view_name = "Proxy", view_type = ViewType.TextField)
    private String proxy;
    @SSCDatabaseField(sql_col_name = "note", sql_col_type = ColType.INTEGER, sql_col_tyle_length = 80, view_type = ViewType.NOVIEW)
    private int note;
    @SSCDatabaseField(sql_col_name = "full_name", sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 140, view_type = ViewType.NOVIEW)
    private String full_name;
    @SSCDatabaseField(sql_col_name = "twofa", sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 140, view_name = "2FA Code", view_type = ViewType.TextField)
    private String twofa;
    @SSCDatabaseField(sql_col_name = "premium_expired", sql_col_type = ColType.BIGINT, sql_col_tyle_length = 80, view_type = ViewType.NOVIEW)
    private long premium_expired;
    @SSCDatabaseField(sql_col_name = "out_card", sql_col_type = ColType.BIGINT, sql_col_tyle_length = 80, view_type = ViewType.NOVIEW)
    private long out_card;
    @SSCDatabaseField(sql_col_name = "card_error", sql_col_type = ColType.INTEGER, sql_col_tyle_length = 80, view_type = ViewType.NOVIEW)
    private int card_error;

    public String getPhone_recover() {
        return phone_recover;
    }

    public void setPhone_recover(String phone_recover) {
        this.phone_recover = phone_recover;
    }

    private SimpleStringProperty dateExString;

    public SimpleStringProperty dateExStringProperty() {
        if (dateExString == null) {
            dateExString = new SimpleStringProperty(premium_expired == 0 ? "n/a" : StringUtils.convertLongToDataTime("dd/MM/YYYY", premium_expired));
        }
        return dateExString;
    }

    public String getDateExString() {

        return dateExString.get();
    }

    public void setDateExString(String _dateExString) {
        if (dateExString == null) {
            dateExString = new SimpleStringProperty("");
        }
        this.dateExString.set(_dateExString);
    }

    public int getCard_error() {
        return card_error;
    }

    public void setCard_error(int card_error) {
        this.card_error = card_error;
    }

    @SSCDatabaseField(sql_col_name = "cookies", sql_col_type = ColType.VARCHAR, sql_col_tyle_length = 9000, view_type = ViewType.NOVIEW)
    private String cookies;

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public long getOut_card() {
        return out_card;
    }

    public void setOut_card(long out_card) {
        this.out_card = out_card;
    }

    public SimpleBooleanProperty selection;

    public SimpleBooleanProperty getSelection() {
        if (selection == null) {
            selection = new SimpleBooleanProperty(note == 0 ? false : true);
        }
        return selection;
    }

    public SimpleBooleanProperty selectionProperty() {
        if (selection == null) {
            selection = new SimpleBooleanProperty(note == 0 ? false : true);
        }
        return selection;
    }

    public void setSelection(SimpleBooleanProperty selection) {
        this.selection = selection;
    }

    public void setSelection(boolean selection) {
        this.selection.set(selection);
    }

    public long getPremium_expired() {
        return premium_expired;
    }

    public void setPremium_expired(long premium_expired) {
        this.premium_expired = premium_expired;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setDateExString(premium_expired == 0 ? "n/a" : StringUtils.convertLongToDataTime("dd/MM/YYYY", premium_expired));
            }
        });
    }

    public String getTwofa() {
        return twofa;
    }

    public void setTwofa(String twofa) {
        this.twofa = twofa;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getGmail_recover_passwd() {
        return gmail_recover_passwd;
    }

    public void setGmail_recover_passwd(String gmail_recover_passwd) {
        this.gmail_recover_passwd = gmail_recover_passwd;
    }

    public String getProfile_name() {
        return profile_name;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }

    public int getNote() {
        return note;
    }

    public void setNote(int note) {
        this.note = note;
        setSelection(note == 1 ? true : false);
    }

    private SimpleStringProperty last_time_string;
    private SimpleStringProperty status_string;
    private SimpleStringProperty login_status_string;

    private String gothe_string;

    public String getGothe_string() {
        gothe_string = out_card == 0 ? "n/a" : StringUtils.convertLongToDataTime("dd/MM hh:mm", out_card);
        return gothe_string;
    }

    public void setGothe_string(String gothe_string) {
        this.gothe_string = gothe_string;
    }

    private ProfileG profile;
    private int countVideo = 0;

    public int getCountVideo() {
        return countVideo;
    }

    public void setCountVideo(int countVideo) {
        this.countVideo = countVideo;
    }

    public String getUsername() {
        return username.replaceAll("[^\\x20-\\x7e]", "");
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public SimpleStringProperty last_time_stringProperty() {
        if (last_time_string == null) {
            last_time_string = new SimpleStringProperty(StringUtils.convertLongToDataTime("dd/MM hh:mm", last_time));
        }
        return last_time_string;
    }

    public String getLast_time_string() {
        return last_time_string.get();
    }

    public void setLast_time_string(String last_time_string) {
        this.last_time_string.set(last_time_string);
    }

    public SimpleStringProperty status_stringProperty() {
        if (status_string == null) {
            status_string = new SimpleStringProperty(status);
        }
        return status_string;
    }

    public String getStatus_string() {
        return status_string.get();
    }

    public void setStatus_string(String status_string) {
        if (this.status_string == null) {
            this.status_string = new SimpleStringProperty(status_string);
        }
        this.status_string.set(status_string);
    }

    public SimpleStringProperty login_status_stringProperty() {
        if (login_status_string == null) {
            login_status_string = new SimpleStringProperty(login_status);
        }
        return login_status_string;
    }

    public String getLogin_status_string() {
        return login_status_string.get();
    }

    public void setLogin_status_string(String login_status_string) {
        if (this.login_status_string == null) {
            this.login_status_string = new SimpleStringProperty(login_status_string);
        }
        this.login_status_string.set(login_status_string);
    }

    @SSCDatabaseTable(tableName = "gmail_account")
    public GoogleAccount(int stt, String line) {
        super(stt, line);
        setId(System.currentTimeMillis());
        setStatus("Sống");
        setLogin_status("Chưa login");
        setLast_time(System.currentTimeMillis());
    }

    @SSCDatabaseTable(tableName = "gmail_account")
    public GoogleAccount(int stt) {
        super(stt);
        setId(System.currentTimeMillis());
        setStatus("Sống");
        setLogin_status("Chưa login");
        setLast_time(System.currentTimeMillis());

        //if (!SQLIteHelper.checkColumExist("Twitter", "last_tweet")) {
        SQLIteHelper.alterTable(new SQLIteHelper.Column("out_card", "BIGINT", "0", "gmail_account"));
        SQLIteHelper.alterTable(new SQLIteHelper.Column("cookies", "VARCHAR(9000)", "", "gmail_account"));
        SQLIteHelper.alterTable(new SQLIteHelper.Column("card_error", "INT", "0", "gmail_account"));
        SQLIteHelper.alterTable(new SQLIteHelper.Column("phone_recover", "VARCHAR(100)", "0", "gmail_account"));

        //}
    }

    @SSCDatabaseTable(tableName = "gmail_account")
    public GoogleAccount(int stt, ResultSet rs) {
        super(stt);
        initValueFromResultSet(rs);
        try {
            this.countVideo = getIntegerFromRS("countVideo", rs);
        } catch (Exception e) {
        }

    }

    @SSCDatabaseTable(tableName = "gmail_account")
    public GoogleAccount(JSONObject object, int stt) {
        super(object, stt);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGoogleAccountname() {
        return username;
    }

    public void setGoogleAccountname(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGmail_recover() {
        return gmail_recover.replaceAll("[^\\x20-\\x7e]", "");
    }

    public void setGmail_recover(String gmail_recover) {
        this.gmail_recover = gmail_recover;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setStatus_string(status);
            }
        });
    }

    public String getLogin_status() {
        return login_status;
    }

    public void setLogin_status(String login_status) {
        this.login_status = login_status;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setLogin_status_string(login_status);
            }
        });
    }

    public String getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(String profile_id) {
        this.profile_id = profile_id;
    }

    public long getLast_time() {
        return last_time;
    }

    public void setLast_time(long last_time) {
        this.last_time = last_time;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public ProfileG getProfile() {
        return profile;
    }

    public void setProfile(ProfileG profile) {
        this.profile = profile;
    }

    public static List<TableColumn> getArrCol(ObservableList data) {
        List<TableColumn> arrCol = new ArrayList<>();
        try {
            TableColumn<GoogleAccount, GoogleAccount> noteCol = new TableColumn("---");
            noteCol.setResizable(false);
            noteCol.setPrefWidth(80);
            noteCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
            noteCol.setCellFactory(param -> new TableCell<GoogleAccount, GoogleAccount>() {

                private CheckBox cbSelect;

                @Override
                public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
                    return super.buildEventDispatchChain(tail); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                protected void updateItem(GoogleAccount account, boolean empty) {
                    super.updateItem(account, empty);
                    if (account == null) {
                        setGraphic(null);
                        return;
                    }
                    try {

                        cbSelect = new CheckBox();

                        cbSelect.setDisable(true);

                        cbSelect.setSelected(account.getNote() == 1 ? true : false);

                        account.selectionProperty().addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                                cbSelect.setSelected(t1);
                            }
                        });

                        setGraphic(cbSelect);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            TableColumn<GoogleAccount, GoogleAccount> actionCol = new TableColumn("Hành Động");
            actionCol.setResizable(false);
            actionCol.setPrefWidth(150);
            actionCol.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
            actionCol.setCellFactory(param -> new TableCell<GoogleAccount, GoogleAccount>() {

                private HBox rootPane;

                @Override
                public EventDispatchChain buildEventDispatchChain(EventDispatchChain tail) {
                    return super.buildEventDispatchChain(tail); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                protected void updateItem(GoogleAccount profile, boolean empty) {
                    super.updateItem(profile, empty);
                    if (profile == null) {
                        setGraphic(null);
                        return;
                    }
                    try {

                        ButtonTooltip btnEdit = new ButtonTooltip("Sửa thông tin");
                        Graphics.setIconLeft(btnEdit, "EDIT", 1.0);
                        btnEdit.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                profile.showEditViewFromDB(data, "Sửa thông tin");
                            }
                        });
                        ButtonTooltip btnDelete = new ButtonTooltip("Xóa profile");
                        //btnDelete.getStylesheets().addAll("btn-secondary");
                        Graphics.setIconLeft(btnDelete, "BITBUCKET_SQUARE", 1.0);

                        ButtonTooltip btnOpenBrowser = new ButtonTooltip("Mở trình duyệt");
                        //btnOpenBrowser.getStylesheets().addAll("btn-secondary");
                        Graphics.setIconLeft(btnOpenBrowser, "APPLE", 1.0);

                        btnOpenBrowser.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                OpenGologinBrowserTask ogbt = new OpenGologinBrowserTask(profile);
                                SSCLogView SSLogView = new SSCLogView(ogbt.getArrLog(), ogbt.getArrLog());
                                ogbt.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                                    @Override
                                    public void handle(WorkerStateEvent t) {

                                    }
                                });
                                btnOpenBrowser.disableProperty().bind(ogbt.runningProperty());
                                SSLogView.getLbMessage().textProperty().bind(ogbt.messageProperty());

                                SSLogView.show();
                                ogbt.start();
                            }
                        });

                        btnDelete.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {
                                SSCConfirmTaskModal deleteConfirm = new SSCConfirmTaskModal(3, 150, "Bạn có chắc chắn muốn xóa profile này ?") {
                                    @Override
                                    public boolean afterConfirmAction() {
                                        String folderFolder = TC.getInts().gologin_folder + File.separator + "gologin_profile_" + profile.getProfile_id();
                                        String profileZip = TC.getInts().gologin_folder + File.separator + profile.getProfile_id() + ".zip";
                                        if (MetaApi.deleteProfile(profile.getProfile_id())) {
                                            if (profile.deleteData().length() == 0) {
                                                MyFileUtils.deleteFolder(new File(folderFolder));
                                                MyFileUtils.deleteFile(profileZip);
                                                data.remove(profile);
                                                SSCMessage.showSuccessInThread("Đã xóa account " + profile.getUsername());
                                                return true;
                                            } else {
                                                SSCMessage.showErrorInThread("Xóa account " + profile.getUsername() + " lỗi");
                                                return false;
                                            }
                                        } else {
                                            SSCMessage.showErrorInThread("Xóa account " + profile.getUsername() + " lỗi");
                                            return false;
                                        }

                                    }
                                };
                                deleteConfirm.show();
                            }
                        });

                        rootPane = new HBox();
                        rootPane.getStyleClass().addAll("actionCol");
                        rootPane.setPrefSize(200, 280);
                        rootPane.getChildren().addAll(btnEdit, btnDelete, btnOpenBrowser);

                        setGraphic(rootPane);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            arrCol.add(noteCol);
            arrCol.add(SSCTableColum.getTableColInteger("STT", "stt", 50, false));
            arrCol.add(SSCTableColum.getTableColString("Gỡ thẻ", "gothe_string", 80, false));
            arrCol.add(SSCTableColum.getTableColInteger("Thẻ lỗi", "card_error", 80, false));
            arrCol.add(SSCTableColum.getTableColString("Lần Cuối", "last_time_string", 100, false));
            arrCol.add(SSCTableColum.getTableColString("Proxy", "proxy", 160, false));
            arrCol.add(SSCTableColum.getTableColString("Username", "username", 1270 - 1100, false));
            arrCol.add(SSCTableColum.getTableColString("Premium ", "dateExString", 100, false));
            arrCol.add(SSCTableColum.getTableColString("Trạng thái", "status_string", 150, false));
            arrCol.add(SSCTableColum.getTableColString("TT Login", "login_status_string", 100, false));
            arrCol.add(actionCol);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrCol;
    }

}
