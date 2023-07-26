/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.task;

import java.lang.management.ManagementFactory;
import javafx.concurrent.Task;

import com.sun.management.OperatingSystemMXBean;
import java.io.File;
import static ssc.base.ultil.ThreadUtils.Sleep;

/**
 * @author inet
 */

public class CountRunTimeTask extends Task<Void> {

    private static int[] printUsage() {
        int pro[] = new int[2];
        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
                OperatingSystemMXBean.class);
        pro[0] = (int) (osBean.getSystemCpuLoad() * 100);
        long used = osBean.getTotalPhysicalMemorySize() - osBean.getFreePhysicalMemorySize();
        pro[1] = (int) ((double) used / (double) osBean.getTotalPhysicalMemorySize() * 100);
        return pro;
    }

    private String localRunPath;

    public CountRunTimeTask(String localRunPath) {
        this.localRunPath = localRunPath;
    }

    @Override
    protected Void call() throws Exception {
        int i = 0;
        while (true) {
            int s = i % 60;
            int m = i / 60 % 60;
            int h = i / 60 / 60;
            updateMessage(h + " h " + m + " m " + s + " s");
            //Telegram.getInstance().setTotalTimeRun(h + " h " + m + " m " + s + " s");
            int res[] = printUsage();
            
            //Telegram.getInstance().comResource = res[0] + " % / " + res[1] + " %";
            if (localRunPath != null) {
                int currentFreeSpace = (int) (new File(localRunPath).getFreeSpace() / 1024 / 1024 / 1024);
                //Telegram.getInstance().totalSpace = currentFreeSpace;
                updateTitle(" " + res[0] + " % / " + res[1] + " % / "+currentFreeSpace+" GB");
            }else{
                updateTitle(" " + res[0] + " % / " + res[1] + " %");
            }
            Sleep(1000);
            i++;
        }
    }
    Thread t;

    public void start() {
        t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

    public void stop() {
        t.stop();
    }
}
