/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.modal;

import javafx.scene.layout.AnchorPane;
import ssc.base.task.BaseTask;


/**
 *
 * @author PC
 */
public abstract class TaskModal extends SSCBaseModal {

    public TaskModal(int widthRate, int height, String title,BaseTask task) {
        super(widthRate, height, title, "");
        AnchorPane apRoot = new AnchorPane();
        apRoot.setPrefSize(content.getPrefWidth(), 100);
        task.start();
        
        
        
        content.getChildren().add(apRoot);
        bindTask(task);


    }

}
