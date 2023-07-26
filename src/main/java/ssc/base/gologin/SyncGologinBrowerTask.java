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

/**
 *
 * @author PC
 */
public class SyncGologinBrowerTask extends BaseLogTask{

    private ProfileG profile;
    public SyncGologinBrowerTask(ProfileG profile) {
        super();
        this.profile = profile;
      
    }
    @Override
    public boolean mainFunction() {
        try {
            String folderFolder = TC.getInts().gologin_folder + File.separator + "gologin_profile_" + profile.getId();
            String folderZip = TC.getInts().gologin_folder + File.separator + profile.getId() + ".zip";
            updateMessage("Đang nén profile thành zip ...");
            MyFileUtils.zipFolderToFile(new File(folderZip),new File(folderFolder));
            updateMessage("Đang đồng bộ profile lên server ...");
            MetaApi.uploadProfileToServer(folderZip,profile.getId());
            return true;
        } catch (Exception e) {
        }
        return false;
    }


    
}
