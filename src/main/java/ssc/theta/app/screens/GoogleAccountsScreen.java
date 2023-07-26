/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.theta.app.screens;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import ssc.base.ui.components.ListSelectItem;
import ssc.base.view.Navigator;
import ssc.base.view.ScreenBase;
import ssc.base.database.BaseModel;
import ssc.base.database.BaseTable;
import ssc.base.database.SQLIteHelper;
import ssc.base.global.BrowserMapData;
import ssc.base.global.TC;
import ssc.base.global.ViewGlobal;
import ssc.base.modal.InputModal;
import ssc.base.modal.SSCTaskModal;
import ssc.base.modal.TaskModal;
import ssc.base.proxy.tmproxy.ListTMKeyInstance;
import ssc.base.proxy.tmproxy.TMKeyAndProxy;
import ssc.base.run.connection.ToolSocket;
import ssc.base.task.BaseTask;
import ssc.base.ui.components.ChildMenuButton;
import ssc.base.ui.components.SSCTextField;
import ssc.base.ultil.ApiHelper;
import static ssc.base.ultil.Constains.DESKTOP_PATH;
import ssc.base.ultil.MyFileUtils;

import ssc.base.view.SSCMessage;
import ssc.task.FinishEventTask;
import ssc.theta.app.api.MetaApi;
import ssc.theta.app.googleaction.LoginAction;
import ssc.theta.app.googleaction.SyncAction;
import ssc.theta.app.model.GoogleAccount;
import ssc.theta.app.model.GoogleAccount.ACCOUNT_LOGIN_STATUS;
import ssc.theta.app.model.GoogleAccount.ACCOUNT_STATUS;
import ssc.theta.app.model.sqlQuery.GoogleAccountQuery;

/**
 *
 * @author PC
 */
public class GoogleAccountsScreen extends ScreenBase {

    public GoogleAccountsScreen(String title, int tabIndex, String menuIcon, Navigator navigator) {
        super(title, tabIndex, menuIcon, navigator);
    }
    ObservableList<GoogleAccount> arrData;
    ObservableList<GoogleAccount> arrDataSelect;
    ObservableList<LoginAction> arrLogin;
    ObservableList<SyncAction> arrSync;
    private BaseTable profilesTable;

    private SSCTextField txtApiFromUrl;
    private SSCTextField txtApiFromToken;
    //
    private SSCTextField txtApiToUrl;
    private SSCTextField txtApiToToken;

    @Override
    public void initView() {

        arrData = FXCollections.observableArrayList();
        arrLogin = FXCollections.observableArrayList();
        arrSync = FXCollections.observableArrayList();
        arrDataSelect = FXCollections.observableArrayList();
        List<ListSelectItem> selectItems = new ArrayList<>();
        selectItems.add(new ListSelectItem("Thêm tài khoản", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                GoogleAccount ga = new GoogleAccount(arrData.size() + 1);
                ga.showAddViewFromDB(arrData, "Điền thông tin tài khoản");
            }
        }));
        selectItems.add(new ListSelectItem("Thêm nhiều tài khoản", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                BaseModel.AddDataFromFileTxt(arrData);
            }
        }));
        profilesTable = new BaseTable("profilesContainer", arrData, "Import", selectItems) {
            @Override
            public ObservableList initGetData(String query) {
                return GoogleAccountQuery.getListGoogleAccount(query);
            }

            @Override
            public List<TableColumn> initArrCol() {
                return GoogleAccount.getArrCol(arrData);
            }

            @Override
            public void removeEvt(ObservableList listRemove) {
                ObservableList<GoogleAccount> arrDataS = FXCollections.observableArrayList();
                arrDataS.addAll(listRemove);
                for (GoogleAccount ga : arrDataS) {
                    //System.out.println("a "+ga.getGoogleAccountname());
                    if (ga.deleteData().length() == 0) {
                        arrData.remove(ga);
                    }
                }
            }
        };

        profilesTable.getTvData().setRowFactory(tv -> {
            TableRow<GoogleAccount> row = new TableRow<>();
            row.setOnMouseClicked((MouseEvent event) -> {
                if (event.getClickCount() == 2) {
                    GoogleAccount account = (GoogleAccount) row.getItem();
                    if (account == null) {
                        return;
                    }
                    String path = TC.getInts().gologin_folder + File.separator + "gologin_profile_" + account.getProfile_id();
                    if (new File(path).exists()) {
                        StringSelection stringSelection = new StringSelection(path);
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(stringSelection, null);
                        SSCMessage.showSuccess("Đã copy đường dẫn " + path);
                    } else {
                        SSCMessage.showWarning("Chưa có profile trên máy");
                    }
                }
            });
            return row;
        });
        arrListAccount = new ArrayList<>();
    }

    @Override
    public void reloadView() {
        profilesTable.reloadData("");
    }

    private List<String> arrListAccount;

    @Override
    public void initArrBtn(List<ChildMenuButton> arrChildMenuBtn) {
        arrChildMenuBtn.add(new ChildMenuButton("Import DB", "Import danh sách account từ BAS ", "DATABASE", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Chọn File database cũ từ BAS ");
                fileChooser.setInitialDirectory(new File(DESKTOP_PATH));
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter[]{new FileChooser.ExtensionFilter("Database", "*.db")});
                File selectedDirectory = fileChooser.showOpenDialog(new Stage());
                if (selectedDirectory != null) {
                    BaseTask baseTask = new BaseTask() {
                        @Override
                        public boolean mainFunction() {
                            try {
                                Connection connection = SQLIteHelper.getConnectionFromDBFile(selectedDirectory.getAbsolutePath());
                                if (connection == null) {
                                    return false;
                                }
                                try {
                                    String query = " Select * from Account";
                                    Statement stmt = null;
                                    ResultSet rs = null;
                                    stmt = connection.createStatement();
                                    rs = stmt.executeQuery(query);
                                    while (rs.next()) {
                                        try {
                                            String username = SQLIteHelper.getStringFromRS("Email", rs);
                                            String passwd = SQLIteHelper.getStringFromRS("Password", rs);
                                            String recover = SQLIteHelper.getStringFromRS("RecoveryEmail", rs);
                                            String proxy = SQLIteHelper.getStringFromRS("Proxy", rs);
                                            GoogleAccount ga = new GoogleAccount(arrData.size() + 1);
                                            ga.setId(System.currentTimeMillis());
                                            ga.setUsername(username);
                                            ga.setPassword(passwd);
                                            ga.setGmail_recover(recover);
                                            ga.setProxy(proxy.replaceAll("http://", "").replaceAll("https://", "").replaceAll("sock5://", ""));
                                            ga.setStatus(ACCOUNT_STATUS.LIVE.getValue());
                                            ga.setLogin_status(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                                            if (ga.insertData().length() == 0) {
                                                arrData.add(ga);
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                            }
                            return true;
                        }
                    };
                    baseTask.start();

                }

            }
        }));
        arrChildMenuBtn.add(new ChildMenuButton("Proxy", "Cập nhật proxy cho danh sách tài khoản", "WIFI", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                ObservableList<GoogleAccount> arrSelected = FXCollections.observableArrayList();
                for (Object selectedItem : profilesTable.getTvData().getSelectionModel().getSelectedItems()) {
                    arrSelected.add((GoogleAccount) selectedItem);
                }
                if (arrSelected.size() == 0) {
                    SSCMessage.showWarning("Chọn danh sách tài khoản !");
                    return;
                }

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Chọn file txt chứa proxy");
                fileChooser.setInitialDirectory(new File(DESKTOP_PATH));
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter[]{new FileChooser.ExtensionFilter("TXT", "*.txt")});
                File selectedDirectory = fileChooser.showOpenDialog(new Stage());
                if (selectedDirectory != null) {
                    BaseTask baseTask = new BaseTask() {
                        @Override
                        public boolean mainFunction() {
                            try {
                                List<String> arrProxy = new ArrayList<>();
                                arrProxy.addAll(MyFileUtils.getListStringFromFile(selectedDirectory.getAbsolutePath()));
                                for (GoogleAccount googleAccount : arrSelected) {
                                    String proxy = arrProxy.get(new Random().nextInt(arrProxy.size()));
                                    arrProxy.remove(proxy);
                                    if (arrProxy.size() == 0) {
                                        arrProxy.addAll(MyFileUtils.getListStringFromFile(selectedDirectory.getAbsolutePath()));
                                    }
                                    googleAccount.setProxy(proxy);
                                    googleAccount.updateData();
                                }
                            } catch (Exception e) {
                            }
                            return true;
                        }
                    };
                    baseTask.start();

                }

            }
        }));
        arrChildMenuBtn.add(new ChildMenuButton("Xuất", "Xuất Account Ra file", "EXPAND", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                ObservableList<GoogleAccount> arrSelected = FXCollections.observableArrayList();
                for (Object selectedItem : profilesTable.getTvData().getSelectionModel().getSelectedItems()) {
                    arrSelected.add((GoogleAccount) selectedItem);
                }
                if (arrSelected.size() == 0) {
                    SSCMessage.showWarning("Chọn danh sách tài khoản cần xuất");
                    return;
                }
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Chọn file txt lưu account");
                fileChooser.setInitialDirectory(new File(DESKTOP_PATH));
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter[]{new FileChooser.ExtensionFilter("TXT", "*.txt"), new FileChooser.ExtensionFilter("Excel", "*.xlsx")});
                File selectedDirectory = fileChooser.showOpenDialog(new Stage());
                if (selectedDirectory != null) {
                    if (selectedDirectory.getAbsolutePath().contains("txt")) {
                        BaseTask baseTask = new BaseTask() {
                            @Override
                            public boolean mainFunction() {
                                try {
                                    for (GoogleAccount googleAccount : arrSelected) {
                                        String line = googleAccount.getUsername() + "|" + googleAccount.getPassword() + "|" + googleAccount.getGmail_recover() + "|" + googleAccount.getGmail_recover_passwd() + "|" + googleAccount.getProfile_id() + "|" + googleAccount.getTwofa();
                                        MyFileUtils.writeStringToFile(line, selectedDirectory.getAbsolutePath());
                                    }
                                } catch (Exception e) {
                                }
                                return true;
                            }
                        };
                        baseTask.start();
                    } else {
                        BaseTask baseTask = new BaseTask() {
                            @Override
                            public boolean mainFunction() {
                                try {
                                    Workbook workbook = new XSSFWorkbook();
                                    Sheet sheet = workbook.createSheet("Accounts"); // Create sheet with sheet name
                                    int rowIndex = 0;
                                    for (GoogleAccount googleAccount : arrSelected) {
                                        Row row = sheet.createRow(rowIndex);
                                        // Write data on row
                                        Cell cell = row.createCell(0);
                                        cell.setCellValue(googleAccount.getUsername());

                                        cell = row.createCell(1);
                                        cell.setCellValue(googleAccount.getPassword());

                                        cell = row.createCell(2);
                                        cell.setCellValue(googleAccount.getGmail_recover());

                                        cell = row.createCell(3);
                                        cell.setCellValue(String.valueOf(googleAccount.getPremium_expired()));

                                        cell = row.createCell(4);
                                        cell.setCellValue(googleAccount.getCookies());

                                        rowIndex++;

                                    }
                                    try (OutputStream os = new FileOutputStream(selectedDirectory.getAbsolutePath())) {
                                        workbook.write(os);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return true;
                            }
                        };
                        baseTask.start();
                    }
                }
            }
        }));

        arrChildMenuBtn.add(new ChildMenuButton("Lưu", "Lưu Chạy", "CHECK_SQUARE", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                ObservableList<GoogleAccount> arrSelected = FXCollections.observableArrayList();
                for (Object selectedItem : profilesTable.getTvData().getSelectionModel().getSelectedItems()) {
                    arrSelected.add((GoogleAccount) selectedItem);
                }
                if (arrSelected.size() == 0) {
                    SSCMessage.showWarning("Chọn danh sách tài khoản cần lưu chạy!");
                    return;
                }
                BaseTask baseTask = new BaseTask() {
                    @Override
                    public boolean mainFunction() {
                        try {
                            for (GoogleAccount googleAccount : arrSelected) {
                                googleAccount.setNote(1);
                                googleAccount.updateData();
                            }
                        } catch (Exception e) {
                        }
                        return true;
                    }
                };
                baseTask.start();
            }
        }));
        arrChildMenuBtn.add(new ChildMenuButton("Hủy", "Lưu Chạy", "TIMES", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                ObservableList<GoogleAccount> arrSelected = FXCollections.observableArrayList();
                for (Object selectedItem : profilesTable.getTvData().getSelectionModel().getSelectedItems()) {
                    arrSelected.add((GoogleAccount) selectedItem);
                }
                if (arrSelected.size() == 0) {
                    SSCMessage.showWarning("Chọn danh sách tài khoản cần lưu chạy!");
                    return;
                }
                BaseTask baseTask = new BaseTask() {
                    @Override
                    public boolean mainFunction() {
                        try {
                            for (GoogleAccount googleAccount : arrSelected) {
                                googleAccount.setNote(0);
                                googleAccount.updateData();
                            }
                        } catch (Exception e) {
                        }
                        return true;
                    }
                };
                baseTask.start();
            }
        }));

        arrChildMenuBtn.add(new ChildMenuButton("Login", "Login tài khoản tự động", "PLUS_CIRCLE", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                arrDataSelect.clear();
                for (Object googleAccount : profilesTable.getTvData().getSelectionModel().getSelectedItems()) {
                    arrDataSelect.add((GoogleAccount) googleAccount);
                }

                arrLogin.clear();
                SSCTaskModal taskModal = new SSCTaskModal(1, (int) ViewGlobal.getInst().getMainContainer().getPrefHeight(), "Tự động login " + arrDataSelect.size() + " tài khoản") {
                    @Override
                    public void initTable(TableView tv) {
                        LoginAction.initTableLoginAction(tv, arrLogin);
                    }

                    @Override
                    public EventHandler<ActionEvent> event() {
                        return new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {

                            }
                        };
                    }

                    @Override
                    public void modalTimerTaskBody() {
                        int maxThreadOpen = 16;

                        if (getUploadTaskView().getCurrentRunningPosition() < arrLogin.size() && getUploadTaskView().getCountRunning() < getUploadTaskView().getThread() && TC.getInts().arrOpens.size() < maxThreadOpen) {
                            LoginAction loginAction = arrLogin.get(getUploadTaskView().getCurrentRunningPosition());
                            loginAction.setArrAccountFollow(arrListAccount);
                            if (getUploadTaskView().getResetMode() == 5) {
                                TMKeyAndProxy tpAndProxy = ListTMKeyInstance.getInstance().getTMKeyAvailable();
                                String proxyInfo = "";
                                if (tpAndProxy == null) {
                                    SSCMessage.showWarningInThread("TM proxy key không khả dụng");
                                    return;
                                }
                                tpAndProxy.setAccount(loginAction.getAccount().getUsername().toLowerCase());
                                proxyInfo = tpAndProxy.getProxyInfo();
                                if (proxyInfo.length() == 0) {
                                    SSCMessage.showWarningInThread("Không thể lấy proxy");
                                    tpAndProxy.setAccount("");
                                    return;
                                }
                                SSCMessage.showSuccessInThread("Proxy đã lấy " + proxyInfo);
                                tpAndProxy.reloadTime(proxyInfo);
                                loginAction.setIp(proxyInfo.split(":")[0]);
                                loginAction.setProxyInfo(proxyInfo);
                                loginAction.settMKeyAndProxy(tpAndProxy);
                            }
                            if (getUploadTaskView().getResetMode() == 4) {
                                System.out.println("proxy tai khoan : " + loginAction.getAccount().getProxy());
                                String proxyInfo = "";
                                try {
                                    proxyInfo = loginAction.getAccount().getProxy();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (proxyInfo == null) {
                                    SSCMessage.showWarningInThread("Format proxy lỗi");
                                    return;
                                }
                                loginAction.setIp(proxyInfo.split(":")[0]);
                                loginAction.setProxyInfo(proxyInfo);
                            }
                            TC.getInts().arrOpens.add(loginAction.getAccount().getUsername());
                            getUploadTaskView().increaseRunning();
                            getUploadTaskView().increaseRunningPosition();
                            getUploadTaskView().updateProcess();
                            getUploadTaskView().getListRunning().add(loginAction);
                            loginAction.setBrowserLocation(BrowserMapData.getInstance().getPosition());
                            loginAction.eventListener(new FinishEventTask() {
                                @Override
                                public void main() {
                                    TC.getInts().arrOpens.remove(loginAction.getAccount().getUsername());
                                    getUploadTaskView().getListRunning().remove(loginAction);
                                    getUploadTaskView().decreaseRunning();
                                    loginAction.clearBrowserLocation();
                                    if (loginAction.getAccount().getLogin_status().equals(ACCOUNT_LOGIN_STATUS.LOGINED.getValue())) {
                                        getUploadTaskView().getListSuccess().add(loginAction);
                                    } else {
                                        getUploadTaskView().getListError().add(loginAction);
                                    }
                                    getUploadTaskView().updateProcess();
                                    if (getUploadTaskView().getResetMode() == 5) {
                                        loginAction.gettMKeyAndProxy().setAccount("");
                                    }
                                }
                            });
                            loginAction.start();
                        }
                        if (getUploadTaskView().getCurrentRunningPosition() >= arrLogin.size() && getUploadTaskView().getCountRunning() == 0) {
                            getUploadTaskView().stopTask();
                        }
                    }

                    @Override
                    public void modalRunEventBody() {

                        for (GoogleAccount googleAccount : arrDataSelect) {
                            //System.out.println(googleAccount.getProxy());
                            LoginAction la = new LoginAction(googleAccount, arrLogin.size() + 1, "");
                            arrLogin.add(la);
                        }

                        if (getUploadTaskView().getResetMode() == 5) {
                            if (TC.getInts().tm_keys.length() == 0) {
                                SSCMessage.showError("chưa cài danh sách tmproxy");
                                return;
                            }
                            List<String> arrKey = new ArrayList<>();
                            arrKey.addAll(Arrays.asList(TC.getInts().tm_keys.split("\n")));
                            if (arrKey.size() < getUploadTaskView().getThread()) {
                                SSCMessage.showError("Số key nhỏ hơn số luồng");
                                return;
                            }
                            ListTMKeyInstance.getInstance().initTMList();
                        }

                        ToolSocket.getInstance().setScene(ViewGlobal.getInst().scene);
                        ToolSocket.getInstance().start();
                        SSCMessage.showSuccess("Khởi tạo socket");

                        getUploadTaskView().startTask();

                    }
                };
                //taskModal.getDataRun().setAll(arrData);
                taskModal.show();

            }
        }));

        arrChildMenuBtn.add(new ChildMenuButton("Cookies", "Lấy cookies tài khoản", "EXPAND", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                arrDataSelect.clear();
                for (Object googleAccount : profilesTable.getTvData().getSelectionModel().getSelectedItems()) {
                    arrDataSelect.add((GoogleAccount) googleAccount);
                }

                arrLogin.clear();
                SSCTaskModal taskModal = new SSCTaskModal(1, (int) ViewGlobal.getInst().getMainContainer().getPrefHeight(), "Load cookies tài khoản") {
                    @Override
                    public void initTable(TableView tv) {
                        LoginAction.initTableLoginAction(tv, arrLogin);
                    }

                    @Override
                    public EventHandler<ActionEvent> event() {
                        return new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {

                            }
                        };
                    }

                    @Override
                    public void modalTimerTaskBody() {
                        int maxThreadOpen = 5;
                        if (getUploadTaskView().getCurrentRunningPosition() < arrLogin.size() && getUploadTaskView().getCountRunning() < getUploadTaskView().getThread() && TC.getInts().arrOpens.size() < maxThreadOpen) {
                            LoginAction loginAction = arrLogin.get(getUploadTaskView().getCurrentRunningPosition());
                            loginAction.setArrAccountFollow(arrListAccount);
                            loginAction.setGetCookies(true);
                            if (getUploadTaskView().getResetMode() == 5) {
                                TMKeyAndProxy tpAndProxy = ListTMKeyInstance.getInstance().getTMKeyAvailable();
                                String proxyInfo = "";
                                if (tpAndProxy == null) {
                                    SSCMessage.showWarningInThread("TM proxy key không khả dụng");
                                    return;
                                }
                                tpAndProxy.setAccount(loginAction.getAccount().getUsername().toLowerCase());
                                proxyInfo = tpAndProxy.getProxyInfo();
                                if (proxyInfo.length() == 0) {
                                    SSCMessage.showWarningInThread("Không thể lấy proxy");
                                    tpAndProxy.setAccount("");
                                    return;
                                }
                                SSCMessage.showSuccessInThread("Proxy đã lấy " + proxyInfo);
                                tpAndProxy.reloadTime(proxyInfo);
                                loginAction.setIp(proxyInfo.split(":")[0]);
                                loginAction.setProxyInfo(proxyInfo);
                                loginAction.settMKeyAndProxy(tpAndProxy);
                            }
                            if (getUploadTaskView().getResetMode() == 4) {
                                String proxyInfo = "";
                                try {
                                    proxyInfo = loginAction.getAccount().getProxy();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (proxyInfo == null) {
                                    SSCMessage.showWarningInThread("Format proxy lỗi");
                                    return;
                                }
                                loginAction.setIp(proxyInfo.split(":")[0]);
                                loginAction.setProxyInfo(proxyInfo);
                            }
                            TC.getInts().arrOpens.add(loginAction.getAccount().getUsername());
                            getUploadTaskView().increaseRunning();
                            getUploadTaskView().increaseRunningPosition();
                            getUploadTaskView().updateProcess();
                            getUploadTaskView().getListRunning().add(loginAction);
                            loginAction.setBrowserLocation(BrowserMapData.getInstance().getPosition());
                            loginAction.eventListener(new FinishEventTask() {
                                @Override
                                public void main() {
                                    TC.getInts().arrOpens.remove(loginAction.getAccount().getUsername());
                                    getUploadTaskView().getListRunning().remove(loginAction);
                                    getUploadTaskView().decreaseRunning();
                                    loginAction.clearBrowserLocation();
                                    if (loginAction.getAccount().getLogin_status().equals(ACCOUNT_LOGIN_STATUS.LOGINED.getValue())) {
                                        getUploadTaskView().getListSuccess().add(loginAction);

                                    } else {
                                        getUploadTaskView().getListError().add(loginAction);

                                    }
                                    getUploadTaskView().updateProcess();
                                    if (getUploadTaskView().getResetMode() == 5) {
                                        loginAction.gettMKeyAndProxy().setAccount("");
                                    }
                                }
                            });
                            loginAction.start();
                        }
                        if (getUploadTaskView().getCurrentRunningPosition() >= arrLogin.size() && getUploadTaskView().getCountRunning() == 0) {
                            getUploadTaskView().stopTask();
                        }
                    }

                    @Override
                    public void modalRunEventBody() {
                        arrLogin.clear();
                        for (GoogleAccount googleAccount : arrDataSelect) {
                            //System.out.println(googleAccount.getProxy());
                            LoginAction la = new LoginAction(googleAccount, arrLogin.size() + 1, "");
                            arrLogin.add(la);
                        }

                        if (getUploadTaskView().getResetMode() == 5) {
                            if (TC.getInts().tm_keys.length() == 0) {
                                SSCMessage.showError("chưa cài danh sách tmproxy");
                                return;
                            }
                            List<String> arrKey = new ArrayList<>();
                            arrKey.addAll(Arrays.asList(TC.getInts().tm_keys.split("\n")));
                            if (arrKey.size() < getUploadTaskView().getThread()) {
                                SSCMessage.showError("Số key nhỏ hơn số luồng");
                                return;
                            }
                            ListTMKeyInstance.getInstance().initTMList();
                        }

                        ToolSocket.getInstance().setScene(ViewGlobal.getInst().scene);
                        ToolSocket.getInstance().start();
                        SSCMessage.showSuccess("Khởi tạo socket");

                        getUploadTaskView().startTask();

                    }
                };
                //taskModal.getDataRun().setAll(arrData);
                taskModal.show();

            }
        }));

        arrChildMenuBtn.add(new ChildMenuButton("Đồng bộ", "Đồng bộ tài khoản lên server", "UPLOAD", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                arrDataSelect.clear();
                for (Object googleAccount : profilesTable.getTvData().getSelectionModel().getSelectedItems()) {
                    arrDataSelect.add((GoogleAccount) googleAccount);
                }
                arrSync.clear();
                SSCTaskModal taskModal = new SSCTaskModal(1, (int) ViewGlobal.getInst().getMainContainer().getPrefHeight(), "Đồng bộ " + arrDataSelect.size() + " tài khoản lên server") {
                    @Override
                    public void initTable(TableView tv) {
                        SyncAction.initTableLoginAction(tv, arrSync);
                    }

                    @Override
                    public EventHandler<ActionEvent> event() {
                        return new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent t) {

                            }
                        };
                    }

                    @Override
                    public void modalTimerTaskBody() {
                        int maxThreadOpen = 5;

                        System.out.println(getUploadTaskView().getCurrentRunningPosition() + " / " + arrSync.size());

                        if (getUploadTaskView().getCurrentRunningPosition() < arrSync.size() && getUploadTaskView().getCountRunning() < getUploadTaskView().getThread() && TC.getInts().arrOpens.size() < maxThreadOpen) {
                            SyncAction loginAction = arrSync.get(getUploadTaskView().getCurrentRunningPosition());
                            getUploadTaskView().increaseRunning();
                            getUploadTaskView().increaseRunningPosition();
                            getUploadTaskView().updateProcess();
                            getUploadTaskView().getListRunning().add(loginAction);
                            loginAction.setBrowserLocation(BrowserMapData.getInstance().getPosition());
                            loginAction.eventListener(new FinishEventTask() {
                                @Override
                                public void main() {
                                    getUploadTaskView().getListRunning().remove(loginAction);
                                    getUploadTaskView().decreaseRunning();
                                    loginAction.clearBrowserLocation();
                                    getUploadTaskView().updateProcess();
                                    if (loginAction.getSyncSussess()) {
                                        getUploadTaskView().getListSuccess().add(loginAction);
                                    } else {
                                        getUploadTaskView().getListError().add(loginAction);
                                    }
                                }
                            });
                            loginAction.start();
                        }
                        if (getUploadTaskView().getCurrentRunningPosition() >= arrSync.size() && getUploadTaskView().getCountRunning() == 0) {
                            getUploadTaskView().stopTask();
                        }
                    }

                    @Override
                    public void modalRunEventBody() {
                        if (TC.getInts().account_api.length() == 0 || TC.getInts().account_token.length() == 0) {
                            SSCMessage.showError("Cài đặt url account và token trước khi sử dụng ! ");
                            return;
                        }
                        arrSync.clear();
                        for (GoogleAccount googleAccount : arrDataSelect) {
                            SyncAction la = new SyncAction(arrSync.size() + 1, googleAccount);
                            arrSync.add(la);
                        }
                        getUploadTaskView().startTask();
                    }
                };
                taskModal.show();

            }
        }));

        arrChildMenuBtn.add(new ChildMenuButton("Xóa Cache", "Làm sạch cache trình duyệt", "RECYCLE", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                TaskModal taskM = new TaskModal(3, 100, "Xóa Cache trình duyệt", new BaseTask() {
                    @Override
                    public boolean mainFunction() {
                        try {
                            List<String> arrFolderOfProfile = new ArrayList<>();
                            for (GoogleAccount googleAccount : arrData) {
                                updateMessage("[" + (arrData.indexOf(googleAccount) + 1) + "/" + arrData.size() + "]Đang xóa cache " + googleAccount.getUsername());
                                googleAccount.removeCache();
                                arrFolderOfProfile.add(TC.getInts().gologin_folder + File.separator + "gologin_profile_" + googleAccount.getProfile_id());
                                //System.out.println(TC.getInts().gologin_folder + File.separator + "gologin_profile_" + googleAccount.getProfile_id());
                            }
                            File arrFolder[] = new File(TC.getInts().gologin_folder).listFiles();
                            if (arrFolder != null) {
                                for (File object : arrFolder) {
                                    if (object.isDirectory()) {
                                        if (!arrFolderOfProfile.contains(object.getAbsolutePath())) {
                                            updateMessage("Đang xóa " + object.getAbsolutePath());
                                            MyFileUtils.deleteFolder(object);
                                        }
                                    }
                                }
                            }
                            updateMessage("Hoàn thành : Bấm nút X để tắt");
                        } catch (Exception e) {
                        }
                        return true;
                    }
                }) {
                };
                taskM.show();
            }
        }));
        
        arrChildMenuBtn.add(new ChildMenuButton("Chuyển DB", "Chuyển database giữa 2 server", "SERVER", new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {

                InputModal inputModal = new InputModal(2, 450, "Nhập thông tin 2 server chuyển đổi", new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        TaskModal taskM = new TaskModal(3, 100, "Xóa Cache trình duyệt", new BaseTask() {
                            @Override
                            public boolean mainFunction() {
                                try {
                                    List<String> arr=MetaApi.getListUserName(txtApiFromUrl.getValue());
                                    for (String string : arr) {
                                        System.out.println(string);
                                        JSONObject jSONObject=ApiHelper.getDataWithAccessTokenAndURl2(txtApiFromUrl.getValue()+"/account/get/move?username="+string+"&endpoint="+txtApiToUrl.getValue(), txtApiFromToken.getValue(),txtApiToToken.getValue());
                                        System.out.println(jSONObject.toJSONString());
                                        updateMessage("Đang chuyển "+(arr.indexOf(string)+1)+"/"+arr.size());
                                    }
                                } catch (Exception e) {
                                }
                                return true;

                            }
                        }) {
                        };
                        taskM.show();
                    }
                });

                txtApiFromUrl = new SSCTextField(inputModal.getObjectForm(), "txtApiFromUrl", "Api link lấy", "", "Nhập link api cần lấy dữ liệu", Arrays.asList("required"));
                txtApiFromToken = new SSCTextField(inputModal.getObjectForm(), "txtApiFromToken", "Api token lấy", "", "Nhập token api cần lấy dữ liệu", Arrays.asList("required"));
                //
                txtApiToUrl = new SSCTextField(inputModal.getObjectForm(), "txtApiToUrl", "Api link nhận", "", "Nhập link api cần nhận dữ liệu", Arrays.asList("required"));
                txtApiToToken = new SSCTextField(inputModal.getObjectForm(), "txtApiToToken", "Api token nhận", "", "Nhập token api cần nhận dữ liệu", Arrays.asList("required"));

                inputModal.show();

            }
        }
        ));
        
    }


}
