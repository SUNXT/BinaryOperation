package view.controller;

import binary.*;
import binary.base.IBinaryNumOperation;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import utils.AppDataUtils;
import utils.Log;
import utils.NumberUtils;

import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class CalculateViewController implements Initializable {

    @FXML
    private VBox contentPane;

    private String mNum1;
    private String mNum2;
    private int operationWay;
    private boolean isTwoBit;//是不是二位计算
    private boolean isDouble;//是否为浮点数
    private Operation mOperation;
    private int bitLength;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        initButton();
        mNum1 =(String) AppDataUtils.get("num1");
        mNum2 = (String) AppDataUtils.get("num2");
        bitLength = (int) AppDataUtils.get("bitLength");
        operationWay = (int) AppDataUtils.get(Operation.TAG);
        isTwoBit = (boolean) AppDataUtils.get("isTwoBit");
        isDouble = (boolean) AppDataUtils.get("isDouble");
        calculate();//计算
    }

//    private void initButton(){
//        mButton.setLayoutX((contentPane.getPrefWidth() - 100)/2);
//        mButton.setOnMouseClicked((event -> {
//            String process = mCalculateProcess.pollFirst();
//            if (process != null && !"".equals(process)){
//                Label label = new Label();
//                label.setLayoutY(mLastViewLayoutY);
//                label.setText(process);
//                contentPane.getChildren().add(label);
//                mLastViewLayoutY += 80;
//            }else {
//                mButton.setDisable(true);
//            }
//        }));
//    }

    private void calculate(){
        IBinaryNumOperation binaryNumOperation;
        Object num1,num2;
        if (isDouble){
            binaryNumOperation = new DoubleBinaryNumOperation();
            num1 = new DoubleBinaryNum(mNum1, bitLength);
            num2 = new DoubleBinaryNum(mNum2, bitLength);
        }else {
            binaryNumOperation = new BinaryNumOperation();
            BinaryNum temp1 = new BinaryNum(mNum1);
            BinaryNum temp2  = new BinaryNum(mNum2);
            temp1.transBinaryNumBitLength(bitLength);
            temp2.transBinaryNumBitLength(bitLength);
            num1 = temp1;
            num2 = temp2;
        }
        switch (operationWay){
            case Operation.OP_ADD:
                mOperation = binaryNumOperation.add(num1, num2, isTwoBit);
                showAddCutProcess();
                break;

            case Operation.OP_CUT:
                mOperation = binaryNumOperation.cut(num1, num2, isTwoBit);
                showAddCutProcess();
                break;

            case Operation.OP_MUTIL:
                mOperation = binaryNumOperation.multi(num1, num2, isTwoBit);
                showMultiProcess();
                break;

             case Operation.OP_DIVISION:
                 mOperation = binaryNumOperation.division(num1, num2, isTwoBit);
                 showDivisionProcess();
                 break;
            default:
                mOperation = binaryNumOperation.add(num1, num2, isTwoBit);
                showAddCutProcess();
        }

    }

    private void showAddCutProcess(){
        LinkedList<String> mCalculateProcess = mOperation.getCalculateProcess();
        while (mCalculateProcess.size() > 0){
            String process = mCalculateProcess.pollFirst();
            if (process != null && !"".equals(process)){
                Label label = new Label();
                label.setText(process);
                contentPane.getChildren().add(label);
            }
        }
    }

    private void showMultiProcess(){
        LinkedList<Operation.MultiProcess> multiProcesses = mOperation.getCalculateProcess();
        //用表格显示
        TableView<Operation.MultiProcess> tableView = new TableView<>();
        tableView.setItems(FXCollections.observableArrayList(multiProcesses));
        TableColumn processC = new TableColumn("计算过程");
//        TableColumn beAddNumC = new TableColumn("被加数");
        TableColumn explanationC = new TableColumn("说明");
        processC.setMinWidth(400);
        processC.setCellValueFactory(new PropertyValueFactory<>("process"));
        explanationC.setMinWidth(600);
        explanationC.setCellValueFactory(new PropertyValueFactory<>("explanation"));
        tableView.getColumns().addAll(processC, explanationC);
        contentPane.setPrefWidth(1000);

        Label label = new Label();
        label.setText("Num1：" + mOperation.getNum1().getDecimalValue() + " 二进制表示为：" + NumberUtils.transString(mOperation.getNum1().getValues()));
        Label label1 = new Label();
        label1.setText("Num2：" + mOperation.getNum2().getDecimalValue() + " 二进制表示为：" + NumberUtils.transString(mOperation.getNum2().getValues()));
        Label label2 = new Label();
        label2.setText("计算结果 result = " + mOperation.getResult().getDecimalValue() + " 二进制表示为：" + NumberUtils.transString(mOperation.getResult().getValues()));
        contentPane.getChildren().addAll(label, label1, tableView, label2);
    }

    private void showDivisionProcess(){
        LinkedList<Operation.DivisionProcess> divisionProcesses = mOperation.getCalculateProcess();
        //用表格显示
        TableView<Operation.DivisionProcess> tableView = new TableView<>();
        tableView.setItems(FXCollections.observableArrayList(divisionProcesses));
        TableColumn processC = new TableColumn("计算过程");
        TableColumn explanationC = new TableColumn("说明");
        processC.setMinWidth(400);
        processC.setCellValueFactory(new PropertyValueFactory<>("process"));
        explanationC.setMinWidth(600);
        explanationC.setCellValueFactory(new PropertyValueFactory<>("explanation"));
        tableView.getColumns().addAll(processC, explanationC);
        contentPane.setPrefWidth(1000);

        Label label = new Label();
        label.setText(mOperation.getCalculateExplanation());
        Label label2 = new Label();
        int remainder = mOperation.getNum1().getDecimalValue() - mOperation.getResult().getDecimalValue() * mOperation.getNum2().getDecimalValue();
        label2.setText("计算结果的二进制表示：" + mOperation.getResult().toString() + " \n" + mOperation.getNum1().getDecimalValue() + ") / (" + mOperation.getNum2().getDecimalValue() + ") = " + mOperation.getResult().getDecimalValue() + " 余数：" + remainder);

        contentPane.getChildren().addAll(label, tableView, label2);

    }

}
