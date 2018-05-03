package view.controller;

import binary.BinaryNum;
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
import utils.Log;
import utils.NumberUtils;

import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private Button mButton;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initComboBox();
        initTextField();
    }

    /**
     * 初始化选择框
     */
    private void initComboBox(){
        mComboBoxBitData = FXCollections.observableArrayList("八位/8 bit", "十六位/16 bit");
        mComboBoxBit.setItems(mComboBoxBitData);
        mComboBoxBit.setValue(mComboBoxBitData.get(0));

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
                mLabelCNum1.setText(NumberUtils.transBinNum(bitLength, newValue));
                mLabelONum1.setText(NumberUtils.transComplementNum(bitLength, newValue));
            }else {
                mEditNum1.setText(oldValue);
            }

        });

        mEditNum2.setText("0");
        mEditNum2.textProperty().addListener((observable, oldValue, newValue) -> {
            if (NumberUtils.isInteger(newValue)){
                int bitLength = mBitLengthArys[mComboBoxBitData.indexOf(mComboBoxBit.getValue())];
                mEditNum2.setText("" + Integer.parseInt(newValue));
                mLabelCNum2.setText(NumberUtils.transBinNum(bitLength, newValue));
                mLabelONum2.setText(NumberUtils.transComplementNum(bitLength, newValue));
            }else {
                mEditNum2.setText(oldValue);
            }

        });
    }

    public void buttonAction(ActionEvent event){

        Log.d("" + mComboBoxBitData.indexOf(mComboBoxBit.getValue()));
    }

}
