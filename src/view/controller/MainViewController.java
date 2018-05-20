package view.controller;

import binary.BinaryNum;
import binary.BinaryNumOperation;
import binary.Operation;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.AppDataUtils;
import utils.Log;
import utils.NumberUtils;
import view.CalculateView;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private Button mBtnAdd,mBtnCut,mBtnMulti,mBtnDivision;//加减乘除四个按钮
    @FXML
    private ComboBox<String> mComboBoxBit;//位数
    @FXML
    private ComboBox<String> mComboBoxType;//计算的数的类型
    @FXML
    private ComboBox<String> mCBoxCalculateNumType;//计算的数 原码、补码
    @FXML
    private ComboBox<String> mCBoxCalculateType;//选择 一位、二位

    @FXML
    private TextField mEditNum1,mEditNum2;
    @FXML
    private Label mLabelONum1, mLabelONum2, mLabelCNum1, mLabelCNum2;

    private ObservableList<String> mComboBoxBitData;
    private ObservableList<String> mComboBoxTypeData;
    private ObservableList<String> mCBoxCalculateNumTypeData;
    private ObservableList<String> mCBoxCalculateTypeData;

    private int[] mBitLengthArys = {BinaryNum.TYPE_8_BIT, BinaryNum.TYPE_16_BIT};

    private String mNum1 = "00";
    private String mNum2 = "00";
    private int mBitLength = BinaryNum.TYPE_8_BIT;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initComboBox();
        initTextField();
        initButton();
    }

    /**
     * 初始化选择框
     */
    private void initComboBox(){
        mComboBoxBitData = FXCollections.observableArrayList("八位/8 bit", "十六位/16 bit");
        mComboBoxBit.setItems(mComboBoxBitData);
        mComboBoxBit.setValue(mComboBoxBitData.get(0));
        mComboBoxBit.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateView());

        mComboBoxTypeData = FXCollections.observableArrayList("整数", "纯小数");
        mComboBoxType.setItems(mComboBoxTypeData);
        mComboBoxType.setValue(mComboBoxTypeData.get(0));

        mCBoxCalculateNumTypeData = FXCollections.observableArrayList("原码运算", "补码运算");
        mCBoxCalculateNumType.setItems(mCBoxCalculateNumTypeData);
        mCBoxCalculateNumType.setValue(mCBoxCalculateNumTypeData.get(0));

        mCBoxCalculateTypeData = FXCollections.observableArrayList("一位运算", "二位运算");
        mCBoxCalculateType.setItems(mCBoxCalculateTypeData);
        mCBoxCalculateType.setValue(mCBoxCalculateTypeData.get(0));
    }

    private void initTextField(){

        mEditNum1.setText("0");
        mEditNum1.textProperty().addListener((observable, oldValue, newValue) -> {
            if (NumberUtils.isInteger(newValue)){
                int bitLength = mBitLengthArys[mComboBoxBitData.indexOf(mComboBoxBit.getValue())];
                int num = Integer.parseInt(newValue);
                mEditNum1.setText("" + num);
                mNum1 = NumberUtils.transBinNum(bitLength, newValue);
                mLabelCNum1.setText(mNum1);
                mLabelONum1.setText(NumberUtils.transComplementNum(bitLength, newValue));
            }else {
                mNum1 = "00";
                mEditNum1.setText("0");
            }

        });

        mEditNum2.setText("0");
        mEditNum2.textProperty().addListener((observable, oldValue, newValue) -> {
            if (NumberUtils.isInteger(newValue)){
                int bitLength = mBitLengthArys[mComboBoxBitData.indexOf(mComboBoxBit.getValue())];
                mEditNum2.setText("" + Integer.parseInt(newValue));
                mNum2 = NumberUtils.transBinNum(bitLength, newValue);
                mLabelCNum2.setText(mNum2);
                mLabelONum2.setText(NumberUtils.transComplementNum(bitLength, newValue));
            }else {
                mNum2 = "00";
                mEditNum2.setText("0");
            }

        });
    }

    private void initButton(){
        mBtnAdd.setOnMouseClicked(event -> calculate(Operation.OP_ADD));
        mBtnCut.setOnMouseClicked(event -> calculate(Operation.OP_CUT));
//        mBtnMulti.setOnMouseClicked(event -> calculate(Operation.OP_MUTIL));
//        mBtnDivision.setOnMouseClicked(event -> calculate(Operation.OP_DIVISION));
    }

    private void calculate(int calculateType){
        try {
            AppDataUtils.put("num1", mNum1);
            AppDataUtils.put("num2", mNum2);
            AppDataUtils.put(Operation.TAG, calculateType);
            AppDataUtils.put("isTwoBit", "二位计算".equals(mCBoxCalculateType.getValue()));
            AppDataUtils.put("bitLength", mBitLength);
            new CalculateView().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateView(){
        int bitLength = mBitLengthArys[mComboBoxBitData.indexOf(mComboBoxBit.getValue())];
        mBitLength = bitLength;
        mNum1 = NumberUtils.transBinNum(bitLength, mEditNum1.getText());
        mLabelCNum1.setText(mNum1);
        mLabelONum1.setText(NumberUtils.transComplementNum(bitLength, mEditNum1.getText()));
        mNum2 = NumberUtils.transBinNum(bitLength, mEditNum2.getText());
        mLabelCNum2.setText(mNum2);
        mLabelONum2.setText(NumberUtils.transComplementNum(bitLength, mEditNum2.getText()));
    }
}
