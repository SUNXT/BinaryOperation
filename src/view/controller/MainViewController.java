package view.controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import utils.Log;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private Button mButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void buttonAction(ActionEvent event){
        Log.d("点击！");
    }

}
