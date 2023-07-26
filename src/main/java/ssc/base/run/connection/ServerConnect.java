/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.run.connection;

import java.net.Socket;
import javafx.concurrent.Task;
/**
 *
 * @author PC
 */
public class ServerConnect extends Task<Boolean> {

    @Override
    protected Boolean call() {
        try {
            while (true) {
                Socket socket = ToolSocket.getInstance().getServerSocket().accept();
                TaskClientConnection connection = new TaskClientConnection(socket);
                ToolSocket.getInstance().getArrClients().add(connection);
                Thread t = new Thread(connection);
                t.setDaemon(true);
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void start() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }

}
