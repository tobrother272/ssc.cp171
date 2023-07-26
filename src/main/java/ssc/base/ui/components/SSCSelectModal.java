/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ui.components;

import ssc.base.modal.SSCBaseModal;
import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

/**
 *
 * @author PC
 */
public abstract class SSCSelectModal extends SSCBaseModal {

    private VBox listSelect;
    private List<ListSelectItem> arrItem;
    public SSCSelectModal(int widthRate, int height, String title,List<ListSelectItem> arrItem) {
        super(widthRate, arrItem.size()*50, title, "");
        this.arrItem=arrItem;
        listSelect=new VBox();
        listSelect.getStyleClass().addAll("selectModal");
        listSelect.setPrefSize(content.getPrefWidth(), arrItem.size()*50);
        content.getChildren().add(listSelect);
        
        for (ListSelectItem listSelectItem : arrItem) {
            Button btn=new Button(listSelectItem.getTitle());
            btn.setPrefSize(content.getPrefWidth(), 30);
            btn.setOnAction(listSelectItem.getEvent());
            listSelect.getChildren().add(btn);
        }

      
    }

}
