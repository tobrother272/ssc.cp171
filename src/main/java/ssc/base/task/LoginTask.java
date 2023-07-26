/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.task;

import org.json.simple.JSONObject;
import ssc.theta.app.api.MetaApi;
import ssc.base.global.TC;
import ssc.base.ultil.ThreadUtils;

/**
 *
 * @author PC
 */
public class LoginTask extends BaseTask {

    private String username;
    private String password;

    public LoginTask(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean mainFunction() {
        try {
            updateMessage("Đang login ...");
            ThreadUtils.Sleep(500);
            //String md5P=StringUtils.getMd5(password);
            //System.out.println(md5P);
            JSONObject res = MetaApi.auth(username, password);

            if (res != null) {

                if (res.get("status").toString().contains("success")) {
                    TC.getInts().setAccessToken(res.get("token").toString());
                    return true;
                }else{
                    updateMessage(res.get("message").toString());
                    return false;
                }
            }
            
            return false;
  
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            updateMessage("Đăng nhập");
        }
        return false;
    }

}
