/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ultil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import ssc.theta.app.model.TaskLog;

/**
 *
 * @author PC
 */
public class CMDUtils {

    public static boolean cmdStartWithNewLine(List<String> queries, ObservableList<TaskLog> historyList) {
        Thread thread = new Thread(new Task() {
            @Override
            protected Object call() throws Exception {
                try {
                    String[] itemsArray = new String[queries.size()];
                    itemsArray = queries.toArray(itemsArray);
                    cmdWithNewLine(itemsArray, historyList);
                } catch (Exception e) {
                }
                return true;
            }
        });
        thread.setDaemon(false);
        thread.start();
        return true;
    }

    public static List<String> cmdWithNewLine(String cmd_array[], ObservableList<TaskLog> historyList) {
        List<String> outPut = new ArrayList<>();
        String s;
        Process process;
        // System.out.println("\n-----\n");
        for (String string : cmd_array) {
            //System.out.print(" "+string);
        }
        try {
            ProcessBuilder pb = new ProcessBuilder(cmd_array);
            pb.redirectErrorStream(true);
            process = pb.start();
            InputStreamReader isr = new InputStreamReader(process.getInputStream());
            BufferedReader input = new BufferedReader(isr);
            String line;
            while ((line = input.readLine()) != null) {
                try {
                    historyList.add(0,new TaskLog(System.currentTimeMillis(), line, "", "", 0));
                } catch (Exception e) {
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {

            } catch (Exception e) {
            }
        }
        return outPut;
    }


}
