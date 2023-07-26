/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ultil;

/**
 *
 * @author PC
 */
public class ThreadUtils {
    public static void Sleep(long time){
        try {
             Thread.sleep(time);
        } catch (Exception e) {
        }
    }
}
