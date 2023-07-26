/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.task;

import org.json.simple.JSONObject;
import ssc.base.global.TC;
import ssc.base.ultil.ApiHelper;

/**
 *
 * @author PC
 */
public class CheckAccessTokenTask extends BaseTask {

    @Override
    public boolean mainFunction() {
        try {
            JSONObject res = ApiHelper.getDataWithAccessToken("/oauth/verify_token");
            if (res != null && res.get("user") != null) {
                TC.getInts().role=((JSONObject)res.get("user")).get("role").toString();
                TC.getInts().gologin_token=res.get("api_key").toString();
                return true;
            } else {
                TC.getInts().setAccessToken("");
            }
        } catch (Exception e) {
        }
        TC.getInts().setAccessToken("");
        return true;
    }

}
