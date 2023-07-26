/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.gologin;

import java.io.File;
import ssc.theta.app.api.MetaApi;
import ssc.base.global.TC;
import ssc.theta.app.model.ProfileG;
import ssc.base.task.BaseLogTask;
import ssc.base.ultil.MyFileUtils;
import ssc.base.ultil.ThreadUtils;
import ssc.theta.app.model.GoogleAccount;

/**
 *
 * @author PC
 */
public class OpenGologinBrowserTask extends BaseLogTask {

    private GologinDriver driver;
    private GoogleAccount account;

    public OpenGologinBrowserTask(GoogleAccount account) {
        super();
        this.account = account;
    }

    @Override
    public boolean mainFunction() {
        try {

            ProfileG profile = null;
            try {
                if (account.getUsername().toLowerCase().length() == 0) {
                    insertErrorLog("Tài khoản này chưa có profile 1", "");
                    return false;
                } else {
                    updateMyMessage("Đang load profile cũ " + account.getUsername().toLowerCase());
                    profile = MetaApi.getProfileInfo(account.getUsername().toLowerCase());
                    if (profile == null) {
                        insertErrorLog("Tài khoản này chưa có profile trên web ", "");
                        return false;

                    } else {
                        account.setProfile(profile);
                        insertSuccessLog("Lấy được profile cũ");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (profile == null) {
                return false;
            }

            this.driver = new GologinDriver(account.getConnectionName(), profile.getId(), profile.getCanvas(), "", getArrLog());
            //String defaultProfileZip = System.getProperty("user.dir") + File.separator + "tool" + File.separator + "default_profile.zip";
            String defaultProfileZip = System.getProperty("user.dir") + File.separator + "tool" + File.separator + "brave_profile.zip";

            String folderFolder = TC.getInts().gologin_folder + File.separator + "gologin_profile_" + account.getProfile_id();
            //System.out.println("folderFolder "+folderFolder);
            String fakeData = folderFolder + File.separator + "dataFake.json";
            updateMessage("Đang check phiên bản profile");

            //MyFileUtils.deleteFile(profileZip);
            if (!new File(folderFolder).exists()) {
                MyFileUtils.extractZip(defaultProfileZip, folderFolder);
            }

            updateMessage("Tạo dataFake.json");
            MyFileUtils.deleteFile(fakeData);

            MyFileUtils.writeJsonToFile(profile.getFake_data(), fakeData);

            updateMessage("Đang mở trình duyệt");
            ThreadUtils.Sleep(500);
            updateMessage("Đang khởi tạo trình duyệt");
            driver.openBrowser(account.getProxy().length()!=0?"http:"+account.getProxy():"", profile.getScreen_solution());
            updateMessage("Đã mở trình duyệt");

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
