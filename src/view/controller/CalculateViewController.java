package view.controller;

import binary.BinaryNum;
import binary.BinaryNumOperation;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import utils.AppDataUtils;
import utils.Log;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class CalculateViewController implements Initializable {

    @FXML
    private AnchorPane contentPane;

    @FXML
    private Button mButton;

    private int mLastViewLayoutY = 30;
    private BinaryNum mNum1;
    private BinaryNum mNum2;
    private int operationWay;
    private boolean isTwoBit;//是不是二位计算
    private BinaryNumOperation.Operation mOperation;
    private LinkedList<String> mCalculateProcess;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initButton();
        mNum1 = new BinaryNum((String) AppDataUtils.get("num1"));
        mNum2 = new BinaryNum((String) AppDataUtils.get("num2"));
        operationWay = (int) AppDataUtils.get(BinaryNumOperation.Operation.TAG);
        isTwoBit = (boolean) AppDataUtils.get("isTwoBit");
        calculate();//计算
    }

    private void initButton(){
        mButton.setLayoutX((contentPane.getPrefWidth() - 100)/2);
        mButton.setOnMouseClicked((event -> {
            String process = mCalculateProcess.pollFirst();
            if (process != null && !"".equals(process)){
                Label label = new Label();
                label.setLayoutY(mLastViewLayoutY);
                label.setText(process);
                contentPane.getChildren().add(label);
                mLastViewLayoutY += 80;
            }else {
                mButton.setDisable(true);
            }
        }));
    }

    private void calculate(){
        switch (operationWay){
            case BinaryNumOperation.Operation.OP_ADD:
                mOperation = BinaryNumOperation.add(mNum1, mNum2, isTwoBit);
                break;
            default:
                mOperation = BinaryNumOperation.add(mNum1, mNum2, isTwoBit);
        }
        mCalculateProcess = mOperation.getCalculateProcess();
    }

}
