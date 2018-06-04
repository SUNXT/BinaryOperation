package view.controller;

import binary.BinaryNum;
import binary.BinaryNumOperation;
import binary.Operation;
import com.sun.xml.internal.bind.v2.TODO;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    private boolean mIsDouble = false;

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
        mComboBoxType.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> updateView());

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

            String transBinNum = inputFilter(mEditNum1, newValue);
            if (!"00".equals(transBinNum)){
                mNum1 = transBinNum;
                mLabelCNum1.setText(mNum1);
                mLabelONum1.setText(NumberUtils.transComplementNum(mBitLength, newValue, mIsDouble));
            }

        });

        mEditNum2.setText("0");
        mEditNum2.textProperty().addListener((observable, oldValue, newValue) -> {
            String transBinNum = inputFilter(mEditNum2, newValue);
            if (!"00".equals(transBinNum)){
                mNum2 = transBinNum;
                mLabelCNum2.setText(mNum2);
                mLabelONum2.setText(NumberUtils.transComplementNum(mBitLength, newValue, mIsDouble));
            }

        });
    }

    /**
     * 对输入框的过滤
     * @param textField 对应的输入框控件
     * @param newValue 输入中的新值
     * @return 处理之后的二进制原码
     */
    private String inputFilter(TextField textField, String newValue){
        if (mIsDouble){
            //存在小数点
            if (newValue.contains(".")){
                if (newValue.indexOf(".") != newValue.lastIndexOf(".")){
                    textField.setText("0");
                    return "00";
                }else {
                    //当只有一个小数点并且小数点不在最后一位的时候，才可能是浮点数
                    if (!newValue.endsWith(".")){
                        if (NumberUtils.isDouble(newValue)){
                            textField.setText(newValue);
                            return NumberUtils.decimal2DoubleBinary(Double.valueOf(newValue), mBitLength);
                        }else {
                            textField.setText("0");
                            return "00";
                        }
                    }else {
                        textField.setText(newValue);
                        return "00";
                    }
                }
            }else {
                textField.setText("0");
                return "00";
            }
        }else {
            if (NumberUtils.isInteger(newValue)){
                textField.setText(newValue);
                return NumberUtils.transBinNum(mBitLength, newValue);
            }else {
                textField.setText("0");
                return "00";
            }
        }
    }

    private void initButton(){
        mBtnAdd.setOnMouseClicked(event -> calculate(Operation.OP_ADD));
        mBtnCut.setOnMouseClicked(event -> calculate(Operation.OP_CUT));
        mBtnMulti.setOnMouseClicked(event -> calculate(Operation.OP_MUTIL));
//        mBtnDivision.setOnMouseClicked(event -> calculate(Operation.OP_DIVISION));
    }

    private void calculate(int calculateType){
        try {
            AppDataUtils.put("num1", mNum1);
            AppDataUtils.put("num2", mNum2);
            AppDataUtils.put(Operation.TAG, calculateType);
            AppDataUtils.put("isTwoBit", "二位运算".equals(mCBoxCalculateType.getValue()));
            AppDataUtils.put("isDouble", mIsDouble);
            AppDataUtils.put("bitLength", mBitLength);
            new CalculateView().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
            showDialog("提示", "请检查输入是否正确！");
        }
    }

    private void updateView(){
        mIsDouble = mComboBoxTypeData.get(1).equals(mComboBoxType.getValue());
        int bitLength = mBitLengthArys[mComboBoxBitData.indexOf(mComboBoxBit.getValue())];
        mBitLength = bitLength;

        if (mIsDouble){
            mNum1 = NumberUtils.decimal2DoubleBinary(Double.valueOf(mEditNum1.getText()), mBitLength);
            mNum2 = NumberUtils.decimal2DoubleBinary(Double.valueOf(mEditNum2.getText()), mBitLength);

        }else {
            mNum1 = NumberUtils.transBinNum(bitLength, mEditNum1.getText());
            mNum2 = NumberUtils.transBinNum(bitLength, mEditNum2.getText());
        }

        mLabelCNum1.setText(mNum1);
        mLabelONum1.setText(NumberUtils.transComplementNum(bitLength, mEditNum1.getText(), mIsDouble));

        mLabelCNum2.setText(mNum2);
        mLabelONum2.setText(NumberUtils.transComplementNum(bitLength, mEditNum2.getText(), mIsDouble));

    }

    //    弹出一个信息对话框
    public void showDialog(String p_header, String p_message){
        Alert _alert = new Alert(Alert.AlertType.INFORMATION);
        _alert.setTitle("信息");
        _alert.setHeaderText(p_header);
        _alert.setContentText(p_message);
        _alert.show();
    }

}
