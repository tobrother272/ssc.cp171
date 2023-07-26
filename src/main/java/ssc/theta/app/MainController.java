package ssc.theta.app;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import ssc.theta.app.api.MetaApi;
import ssc.base.view.Navigator;
import ssc.base.view.WindowBarView;
import ssc.base.global.ViewGlobal;
import static ssc.base.gologin.GologinDriver.BROWSER_PATH;
import ssc.theta.app.screens.GoogleAccountsScreen;
import ssc.base.task.BaseTask;
import ssc.base.task.InitViewTask;
import ssc.base.ultil.MyFileUtils;
import ssc.theta.app.screens.ViewVideoScreen;

public class MainController implements Initializable {

    @FXML
    TabPane contentContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        InitViewTask task = new InitViewTask();
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                BaseTask dbt = new BaseTask() {
                    @Override
                    public boolean mainFunction() {
                        try {
                            String browserZip = "C:\\Users\\" + System.getProperty("user.name") + File.separator + "theta-browser.zip";
                            String browserFolder = "C:\\Users\\" + System.getProperty("user.name");
                            File browserZipFile = new File(browserZip);
                            //(browserZipFile.length() / 1024 / 1024) < 300 ||
                            if (!browserZipFile.exists() ||  !MyFileUtils.checkZip(browserZip)) {
                                MyFileUtils.deleteFile(browserZip);
                                MyFileUtils.deleteFolder(new File(browserFolder+File.separator+".theta"));
                                updateMessage("Đang tải trình duyệt ...");
                                MetaApi.downloadFile("https://theta-browser-profiles.s3.ap-southeast-1.amazonaws.com/.theta.zip",
                                        browserZip,this,300,browserZip);
                            }
                            browserZipFile = new File(browserZip);
                            if (!browserZipFile.exists() || (browserZipFile.length() / 1024 / 1024) < 150) {
                                return false;
                            }
                            if (!new File(BROWSER_PATH).exists()) {
                                updateMessage("Đang giải nén trình duyệt ...");
                                MyFileUtils.extractZip(browserZip, browserFolder+File.separator+".theta");
                            }  
                        } catch (Exception e) {
                        }
                        return true;
                    }
                };
                dbt.start();
                new WindowBarView();
                ViewGlobal.getInst().initToolTipView();
                ViewGlobal.getInst().getLbToolTipMessage().textProperty().bind(dbt.messageProperty());
                ViewGlobal.getInst().getTooltipContainer().visibleProperty().bind(dbt.runningProperty());
                dbt.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent event) {
                        ViewGlobal.getInst().getLbToolTipMessage().textProperty().unbind();
                        ViewGlobal.getInst().getTooltipContainer().visibleProperty().unbind();
                    }
                });
                
                Navigator navigator = new Navigator();
                navigator.addScreen(new GoogleAccountsScreen("Tạo Tài Khoản", 0, "GROUP", navigator));
                navigator.addScreen(new ViewVideoScreen("Chạy View", 1, "EYE", navigator));
                navigator.setTabIndex(0);
            }
        });
        task.start();

    }

}
