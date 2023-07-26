package ssc.theta.app;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import ssc.base.view.LoginView;
import ssc.base.task.InitViewTask;
public class LoginController implements Initializable {
  
   
    
    private LoginController mthis=this;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        InitViewTask task = new InitViewTask();
        task.setOnScheduled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                //new GoogleAccount(0);
                //new Profile(0);
                new LoginView(mthis);
            }
        });
        task.start();
 
    }
    
    public void goMain(){
        App.setRoot("main");
    }
}
