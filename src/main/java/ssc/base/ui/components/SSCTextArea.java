/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FormComponent;


import java.io.File;
import java.util.List;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ssc.base.global.TC;
import ssc.base.ui.components.SSCFormControl;
import static ssc.base.ultil.Constains.DESKTOP_PATH;
import ssc.base.ultil.Graphics;
import ssc.base.ultil.MyFileUtils;
import ssc.base.view.SSCForm;

/**
 *
 * @author PC
 */
public class SSCTextArea  extends SSCFormControl {
    
    
    public SSCTextArea(SSCForm form, String userdata, String label, String defautValue, String placeHolder, List<String> validates) {
        super(form, userdata, label, defautValue, placeHolder, validates);
    }
    
    private TextArea text;
    private AnchorPane apInputContainer;
    
    @Override
    public Node getMainInput() {
        apInputContainer = new AnchorPane();
        apInputContainer.setPrefWidth(form.getListElement().getPrefWidth());
        text = new TextArea();
        text.setPrefWidth(apInputContainer.getPrefWidth());
        text.setPrefHeight(200);
        apInputContainer.getChildren().add(text);
        return apInputContainer;
    }
    
    public StringProperty textProperty(){
        return text.textProperty();
    }
    public void enableSelectFolder(String title,String fileType) {
        text.setEditable(false);
        text.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (event.getClickCount() == 2) {
                        DirectoryChooser chooser = new DirectoryChooser();
                        chooser.setTitle(title);
                        chooser.setInitialDirectory(new File(DESKTOP_PATH));
                        File selectedDirectory = chooser.showDialog(new Stage());
                        if (selectedDirectory != null) {
                            File list[] = selectedDirectory.listFiles();
                            for (File file : list) {
                                if (fileType.length() != 0 && !MyFileUtils.getFileExtension(file.getName()).equals(fileType)) {
                                    return;
                                }
                            }
                            setValue(selectedDirectory.getAbsolutePath());
                        }
                    }
                }
            }
        });
        Button btn=new Button();
        btn.setPrefSize(30, 30);
        btn.setLayoutY(5);
        btn.setLayoutX(apInputContainer.getPrefWidth()-35);
        Graphics.setIconLeft(btn, "FOLDER_OPEN", 1.0);
        btn.getStyleClass().setAll("btn-secondary");
        apInputContainer.getChildren().add(btn);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                MyFileUtils.openFile(getValue());
            }
        });
    }
    
    /*
    new FileChooser.ExtensionFilter[]{new FileChooser.ExtensionFilter("PNG", "*.png"), new FileChooser.ExtensionFilter("JPG", "*.jpg")}
    */
    public void enableSelectFile(String title,FileChooser.ExtensionFilter[] arrFilter) {
        text.setEditable(false);
        text.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    if (event.getClickCount() == 2) {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle(title);
                        fileChooser.setInitialDirectory(new File(DESKTOP_PATH));
                        fileChooser.getExtensionFilters().addAll(arrFilter);
                        File selectedDirectory = fileChooser.showOpenDialog(new Stage());
                        if (selectedDirectory != null) {
                            text.setText(selectedDirectory.getAbsolutePath());
                        }
                    }
                }
            }
        });
        Button btn=new Button();
        btn.setPrefSize(30, 30);
        btn.setLayoutY(5);
        btn.setLayoutX(apInputContainer.getPrefWidth()-35);
        Graphics.setIconLeft(btn, "FOLDER_OPEN", 1.0);
        btn.getStyleClass().setAll("btn-secondary");
        apInputContainer.getChildren().add(btn);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                MyFileUtils.openFile(getValue());
            }
        });
    }
    
    
    @Override
    public String getValue() {
        return text.getText();
    }
    
    @Override
    public void setValue(String value) {
        text.setText(value);
    }
    
    @Override
    public void loadOldValue() {
        text.setText(TC.getInts().getPre().get(userData, ""));
    }
    
    @Override
    public void initChangeValue() {
        text.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                clearMessage();
            }
        });
    }
}
