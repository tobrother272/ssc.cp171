/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.modal;

import ssc.base.modal.SSCBaseModal;
import ssc.base.view.SSCForm;
import ssc.base.global.TC;

/**
 *
// * @author PC
 */
public class SSCSettingModal extends SSCBaseModal{

    private SSCForm settingForm;
    public SSCSettingModal(int widthRate, int height, String title, String subTitle) {
        super(widthRate, height, title, subTitle);
        
        settingForm=new SSCForm(content,"Lưu Cấu Hình",true) {
            @Override
            public void setOnAction() {
                TC.getInts().saveSetting(settingForm);
                hide();
            }
        };
       
        //content.getChildren().add(formContainer);
        TC.getInts().showSettingView(settingForm);
        settingForm.loadStorageValue();
    }

   

}
