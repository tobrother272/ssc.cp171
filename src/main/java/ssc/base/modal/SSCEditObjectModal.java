/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.modal;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import ssc.base.view.SSCForm;
import ssc.base.database.ActionBaseModelTask;
import ssc.base.database.BaseModel;

/**
 *
 * @author PC
 */
public abstract class SSCEditObjectModal extends SSCBaseModal {

    public abstract BaseModel initAction(BaseModel object);
    private SSCForm objectForm;

    public SSCEditObjectModal(int widthRate, int height, String title, BaseModel model, ObservableList list) {
        super(widthRate, height, title, "");
        objectForm = new SSCForm(content, title, true) {
            @Override
            public void setOnAction() {
                ActionBaseModelTask abmt = new ActionBaseModelTask(model) {
                    @Override
                    public BaseModel getData(BaseModel object) {
                        return initAction(object);
                    }
                };
                abmt.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        if (abmt.getValue() != null) {
                            list.set(list.indexOf(model), abmt.getValue());
                        }
                        hide();
                    }
                });
                objectForm.getActionBtn().bindTask(abmt);
                abmt.start();
            }
        };
        model.initEditObjectView(objectForm);

    }

}
