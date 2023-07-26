/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.task;

import static ssc.base.ultil.ThreadUtils.Sleep;

/**
 * @author PC
 */
public class CheckHoverTask extends BaseTask {
    @Override
    public boolean mainFunction() {
        try {
            try {
                for (int i = 0; i < 3; i++) {
                    Sleep(500);
                }
            } catch (Exception e) {
            }
        } catch (Exception e) {
        }
        return true;
    }

}
