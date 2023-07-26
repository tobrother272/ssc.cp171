/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.task;

import ssc.base.ultil.ThreadUtils;
import ssc.theta.app.model.GoogleAccount;
import ssc.theta.app.model.ProfileG;
/**
 * @author PC
 */
public class InitViewTask extends BaseTask {

    @Override
    public boolean mainFunction() {
        try {
            new ProfileG(0);
            new GoogleAccount(0);
            ThreadUtils.Sleep(300);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

}
