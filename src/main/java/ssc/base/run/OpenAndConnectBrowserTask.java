/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.run;

import java.io.File;
import java.util.Random;
import ssc.base.global.BrowserMapData;
import ssc.base.global.TC;
import ssc.base.ultil.MyFileUtils;
import ssc.base.ultil.ThreadUtils;
import ssc.theta.app.api.MetaApi;
import ssc.theta.app.model.ProfileG;

/**
 * @author simpl
 */
public class OpenAndConnectBrowserTask extends GoogleInteractiveChildTask {

    Boolean headless;

    public OpenAndConnectBrowserTask(ActionWithAccountBase task, boolean headless) {
        super(task);
        this.headless = headless;
    }

    @Override
    protected Boolean call() {
        try {

            String defaultProfileZip = System.getProperty("user.dir") + File.separator + "tool" + File.separator + "default_profile.zip";
            String profileFolder = getParentTask().getAccount().getProfileFolder();

            ProfileG profile = getParentTask().getAccount().getProfile();

            String fakeData = profileFolder + File.separator + "dataFake.json";
            getParentTask().updateMyMessage("Đang check phiên bản profile");

            if (!new File(defaultProfileZip).exists()) {
                getParentTask().updateMyMessage("[1]Đang download default profile từ cloud ...");
                MetaApi.downloadProfile("gologin_default", defaultProfileZip, getParentTask().getArrLog());
                getParentTask().insertSuccessLog("Đã download default profile từ cloud");
            }

            if (!new File(profileFolder).exists()) {
                File arrProfile[] = new File(TC.getInts().old_profile).listFiles();
                if (TC.getInts().old_profile.length() != 0 && arrProfile != null && arrProfile.length != 0) {
                    getParentTask().updateMyMessage("Đang copy profile cũ ...");
                    File oldFolder = arrProfile[new Random().nextInt(arrProfile.length)];
                    MyFileUtils.copyDirectory(oldFolder, new File(profileFolder));
                    MyFileUtils.deleteFile(profileFolder + File.separator + "Default" + File.separator + "Web Data");
                    getParentTask().insertSuccessLog("Xóa Web Data");
                    MyFileUtils.deleteFile(profileFolder + File.separator + "Default" + File.separator + "Web Data-journal");
                    getParentTask().insertSuccessLog("Xóa Web Data-journal");
                } else {
                    getParentTask().updateMyMessage("Đang giải nén profile ...");
                    MyFileUtils.extractZip(defaultProfileZip, profileFolder);
                }

                if (!new File(profileFolder).exists()) {
                    getParentTask().updateMyMessage("Không thể giải nén profile");
                    getParentTask().insertErrorLog("Không thể giải nén profile", "");
                    return false;
                }
            }

            if (!new File(fakeData).exists()) {
                getParentTask().updateMyMessage("Tạo dataFake.json");
                MyFileUtils.deleteFile(fakeData);
                MyFileUtils.writeJsonToFile(profile.getFake_data(), fakeData);
            } else {
                getParentTask().insertSuccessLog("Đã có file dataFake.json");
            }

            getParentTask().updateMyMessage("Đang mở trình duyệt");
            ThreadUtils.Sleep(500);
            getParentTask().updateMyMessage("Đang khởi tạo trình duyệt");

            int locations[] = BrowserMapData.getInstance().getBrowserLocation(getParentTask().getBrowserLocationInt(), false);
            getParentTask().getDriver().startRemote(getParentTask().getProxyInfo(), "1280x720", locations[0], locations[1], BrowserMapData.getInstance().browserW + "x" + BrowserMapData.getInstance().browserH, false);

            wait("Khởi tạo remote", 1);
            insertSuccessLog("Đã start remote tool");
            getParentTask().updateMyMessage("Đang connect client tool");
            if (!getParentTask().getConnection().connectClient(60)) {
                insertErrorLog("Không thể kết nối thiết bị", "");
                return false;
            }
            insertSuccessLog("Đã kết nối với trình duyệt");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }

    @Override
    public void sendResultToParent() {

    }

    @Override
    public String setActionName() {
        return " phút | Đang mở và kết nối gologin";

    }

}
