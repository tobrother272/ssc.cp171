/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.theta.app.screens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;
import ssc.base.global.BrowserMapData;
import ssc.base.global.TC;
import ssc.base.global.ViewGlobal;
import ssc.base.modal.InputModal;
import ssc.base.proxy.tmproxy.ListTMKeyInstance;
import ssc.base.run.connection.ToolSocket;
import ssc.base.ui.components.ChildMenuButton;
import ssc.base.ui.components.SSCTextField;
import ssc.base.ultil.ApiHelper;
import ssc.base.view.Navigator;
import ssc.base.view.ScreenBase;
import ssc.base.view.ProcessTaskView;
import ssc.base.view.SSCMessage;
import ssc.task.FinishEventTask;
import ssc.theta.app.model.sqlQuery.GoogleAccountQuery;

/**
 *
 * @author PC
 */
public class ViewVideoScreen extends ScreenBase {

    public ViewVideoScreen(String title, int tabIndex, String menuIcon, Navigator navigator) {
        super(title, tabIndex, menuIcon, navigator);
    }
    
    private ProcessTaskView uploadTaskView;
    private List<String> arrRunning;
    private InputModal inputModal;
    private SSCTextField txtComputerName;

    @Override
    public void initView() {


        
    }

    @Override
    public void reloadView() {

    }

    @Override
    public void initArrBtn(List<ChildMenuButton> arrChildMenuBtn) {

    }

}
