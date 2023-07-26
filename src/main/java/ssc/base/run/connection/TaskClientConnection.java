/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.run.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import ssc.base.run.ActionWithAccountBase;
import static ssc.base.ultil.ThreadUtils.Sleep;

/**
 * @author topman garbuja,It represents each new connection
 */
public class TaskClientConnection implements Runnable {

    Socket socket;
    private ActionWithAccountBase task;

    public ActionWithAccountBase getTask() {
        return task;
    }

    public void setTask(ActionWithAccountBase task) {
        this.task = task;
    }

    public boolean isShudown() {
        return socket.isClosed();
    }

    public void shutdown() {
        if (socket != null) {
            try {
                socket.close();
                Sleep(2000);
                if (socket.isClosed()) {
                    task.setConnectStatus("Đã disconnect");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    // Create data input and output streams
    DataInputStream input;
    DataOutputStream output;
    private String name = "";
    private String port = "";

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskClientConnection(Socket socket) {
        this.socket = socket;
    }
    String message = "";

    public MessageParser currentMessage() {
        if (message != null) {
            if (task != null) {
                //task.insertSuccessLog(message + " code " + messageObj.getCode() + " ");  
            }
        }
        return messageObj;
    }

    public void setCurrentMessage(MessageParser messageParser) {
        this.messageObj = messageParser;
        if (messageParser == null) {
            message = null;
        }
    }
    public MessageParser messageObj = null;

    @Override
    public void run() {
        try {

            // Create data input and output streams
            input = new DataInputStream(
                    socket.getInputStream());
            output = new DataOutputStream(
                    socket.getOutputStream());
            while (true) {
                if (!socket.isConnected()) {
                    System.out.println("Mất kết nối client ");
                    break;
                }
                if (socket.isInputShutdown()) {
                    System.out.println("isInputShutdown client ");
                    break;
                }
                if (socket.isOutputShutdown()) {
                    System.out.println("isOutputShutdown client ");
                    break;
                }
                if (socket.isClosed()) {
                    System.out.println("isClosed client ");
                    break;
                }
                // Get message from the client
                message = input.readLine();

                if (message != null) {
                    if (task != null) {

                    }
                    System.err.println("Message server nhận được " + message);
                    messageObj = new MessageParser(message);
                    if (messageObj.getName() != null) {
                        name = messageObj.getName();
                        ToolSocket.getInstance().showNotifi(name);
                    }
                    if (messageObj.getPort() != null) {
                        port = messageObj.getPort();
                    }
                }
            }
        } catch (IOException ex) {
            // ex.printStackTrace();
        } finally {

        }
    }

    //send message back to client
    public void sendMessage(String message) {
        try {
            //System.out.println("#" + "#" + message);
            this.message = null;
            this.messageObj = null;
            output.writeUTF(message);
            output.flush();
        } catch (IOException ex) {
            //ex.printStackTrace();
        }
    }
}
