/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.view;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import ssc.base.ui.components.ComboboxIcon;
import ssc.base.ui.components.LabelIcon;
import ssc.base.global.TC;
import ssc.base.task.CountRunTimeTask;
import ssc.base.task.StopRunningTask;
import ssc.base.ui.components.SSCLoadingButton;
import ssc.base.ultil.Graphics;

/**
 * @author PC
 */
public abstract class ProcessViewBase {

    public static ObservableList<String> CHANGEIPMODE
            = FXCollections.observableArrayList(
                    "Không reset",
                    "Reset modem",
                    "Reset 3g",
                    "Reset tinsoft",
                    "Proxy tài khoản",
                    "Proxy TM"
            );
    public static ObservableList<String> CHANGEIPMODEE
            = FXCollections.observableArrayList(
                    "NONE",
                    "ACCOUNT PROXY",
                    "TM PROXY"
            );
    private ObservableList<String> THREADS = FXCollections.observableArrayList();

    public abstract void runEventBody();

    public EventHandler<ActionEvent> runEvent() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                runEventBody();
            }
        };
    }

    public ProcessViewBase() {

        for (int i = 1; i <= 100; i++) {
            THREADS.add(i + " Luồng");
        }

    }

    public abstract void timerTaskBody();

    public TimerTask timerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                timerTaskBody();
            }
        };
    }
    
    public abstract boolean stopRunningTaskBody();

    public StopRunningTask stopRunningTask() {
        return new StopRunningTask() {
            @Override
            public boolean mainThread() {
                return stopRunningTaskBody();
            }
        };
    }
    ;
    private GridPane apStatus;

    public GridPane getApStatus() {
        return apStatus;
    }

    LabelIcon statusTime;
    public LabelIcon statusThread;
    public LabelIcon statusSuccess;
    public LabelIcon statusError;
    LabelIcon statusFreeSpace;
    LabelIcon statusCpu;
    ComboboxIcon statusRunThread;
    ComboboxIcon statusRunResetIpMode;

    public ComboboxIcon getStatusRunResetIpMode() {
        return statusRunResetIpMode;
    }

    private SSCLoadingButton btRuButton;

    public SSCLoadingButton getBtRuButton() {
        return btRuButton;
    }


    private int thread = 1;
    private int resetMode = 0;
    private int success = 0;
    private int error = 0;
    private int total = 0;

    public int getSuccess() {
        return success;
    }

    public int getError() {
        return error;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void increaseTotal() {
        this.total++;
    }

    public void increaseSuccess() {
        this.success++;
    }

    public void increaseError() {
        this.error++;
    }

    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    public int getResetMode() {
        return resetMode;
    }

    public void setResetMode(int resetMode) {
        this.resetMode = resetMode;
    }

    private String resetModeString;

    public String getResetModeString() {
        return resetModeString;
    }

    public void setResetModeString(String resetModeString) {
        this.resetModeString = resetModeString;
    }

    public void goToRunReadyMode() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (btRuButton != null) {
                    btRuButton.setText("  Thực Hiện ");
                    btRuButton.setOnAction(runEvent());
                }
            }
        });
    }

    public void goToStopReadyMode() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (btRuButton != null) {
                    btRuButton.setText("  Ngừng Ngay ");
                    btRuButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            stopTask();
                            goToRunReadyMode();
                            StopRunningTask st = stopRunningTask();
                            st.start();
                        }
                    });
                }
            }
        });
    }

    public void initView(AnchorPane rootDialog, double x, double y, double w, double h) {
        double statusGroupW=w/4;
        apStatus = new GridPane();
        apStatus.setHgap(5);
        apStatus.setVgap(5);
        apStatus.setPrefSize(w, h);
        apStatus.setLayoutX(x);
        apStatus.setLayoutY(y);
        rootDialog.getChildren().add(apStatus);
        apStatus.getStyleClass().setAll("statusPane");
        statusTime = new LabelIcon("CLOCK_ALT", "Thời gian chạy", statusGroupW, 50);
        apStatus.add(statusTime.getView(), 0, 0);
        statusThread = new LabelIcon("FORWARD", "Tổng luồng", statusGroupW, 50);
        apStatus.add(statusThread.getView(), 1, 0);
        statusSuccess = new LabelIcon("CHECK", "Thành Công", statusGroupW, 50);
        apStatus.add(statusSuccess.getView(), 2, 0);
        statusError = new LabelIcon("CLOSE", "Lỗi", statusGroupW, 50);
        apStatus.add(statusError.getView(), 3, 0);

        statusFreeSpace = new LabelIcon("DATABASE", "Dung lượng", statusGroupW, 50);
        apStatus.add(statusFreeSpace.getView(), 0, 1);
        statusRunThread = new ComboboxIcon("WINDOWS", "Số Luồng", THREADS, statusGroupW, 50);
        apStatus.add(statusRunThread.getView(), 1, 1);

        statusRunResetIpMode = new ComboboxIcon("WIFI", "Chế Độ Reset Ip", CHANGEIPMODE, statusGroupW, 50);
        apStatus.add(statusRunResetIpMode.getView(), 2, 1);
        btRuButton = new SSCLoadingButton("  Thực Hiện",statusGroupW,30);
        btRuButton.pinButton();

        apStatus.add(btRuButton.getView(), 3, 1);
        btRuButton.setOnAction(runEvent());


        btRuButton.setOnAction(runEvent());
        statusRunThread.valueProperties().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                thread = Integer.parseInt(statusRunThread.getLbValue().getSelectionModel().getSelectedItem().toString().replaceAll("\\D+", ""));
                updateProcess();
            }
        });
        statusRunResetIpMode.valueProperties().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                resetMode = newValue.intValue();
                resetModeString = statusRunResetIpMode.getLbValue().getSelectionModel().getSelectedItem().toString();
            }
        });

    }

    private String localRunPath = System.getProperty("user.dir");
    private int currentFreeSpace = 0;
    private CountRunTimeTask taskCountTime;
    private int currentRunningPosition = 0;
    private int countRunning = 0;
    public int timeDelay = 1000;

    public int getTimeDelay() {
        return timeDelay;
    }

    public void setTimeDelay(int timeDelay) {
        this.timeDelay = timeDelay;
    }

    public void startTask() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                updateProcess();
                currentRunningPosition = 0;
                countRunning = 0;
                success = 0;
                error = 0;
                taskCountTime = new CountRunTimeTask(localRunPath);
                if (statusTime != null) {
                    statusTime.textProperty().bind(taskCountTime.messageProperty());
                    if (statusCpu != null) {
                        statusCpu.textProperty().bind(taskCountTime.titleProperty());
                    }
                }
                taskCountTime.start();
                timer = new Timer();
                timer.schedule(timerTask(), 1000, timeDelay);
                goToStopReadyMode();
            }
        });
    }

    public void stopTask() {
        if (taskCountTime != null) {
            taskCountTime.stop();
            taskCountTime = null;
        }
        if (timer != null) {
            timer.cancel();
        }
        goToRunReadyMode();

    }

    public String getLocalRunPath() {
        return localRunPath;
    }

    public void setLocalRunPath(String localRunPath) {
        this.localRunPath = localRunPath;
    }

    public int getCurrentRunningPosition() {
        return currentRunningPosition;
    }

    public void increaseRunningPosition() {
        this.currentRunningPosition++;
    }

    public int getCountRunning() {
        return countRunning;
    }

    public void increaseRunning() {
        this.countRunning++;
    }

    public void decreaseRunning() {
        this.countRunning--;
    }

    public int getCurrentFreeSpace() {
        return currentFreeSpace;
    }

    public void setCurrentFreeSpace(int currentFreeSpace) {
        this.currentFreeSpace = currentFreeSpace;
    }
    Timer timer;

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public void updateProcess() {
        localRunPath = TC.getInts().gologin_folder;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (statusThread != null) {
                    statusSuccess.setText(getSuccess() + " ");
                    statusError.setText(getError() + " ");
                    statusFreeSpace.setText(getTotal() + "");
                    statusThread.setText(getCountRunning() + "/" + thread + " Luồng");
                }
            }
        });
    }
}
