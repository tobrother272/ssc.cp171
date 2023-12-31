/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.run.connection;

//import FormComponent.View.SSCMessage;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Scene;

/**
 *
 * @author PC
 */
public class ToolSocket {

    private ServerSocket serverSocket;
    private List<TaskClientConnection> arrClients;
    private Scene scene;
    public void start(){
        if(serverSocket!=null){
            return;
        }
        try {
            serverSocket = new ServerSocket(8001);
            //SSCMessage.showSuccess(scene,"Đã tạo được server socket");
            new ServerConnect().start();
        } catch (Exception e) {
            //SSCMessage.showError(scene,"Không tạo được socket");
        }
    }
   

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public List<TaskClientConnection> getArrClients() {
        return arrClients;
    }

    public void setArrClients(List<TaskClientConnection> arrClients) {
        this.arrClients = arrClients;
    }

    public void showNotifi(String name) {
        //SSCMessage.showSuccessInThread(scene, "Đã connect trình duyệt " + name);
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public ToolSocket() {
        arrClients = new ArrayList<>(); 
    }
    public static ToolSocket instance;
    public static ToolSocket getInstance() {
        if (instance == null) {
            instance = new ToolSocket();
        }
        return instance;
    }

}
