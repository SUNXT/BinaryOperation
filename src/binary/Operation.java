package binary;

import javafx.beans.property.SimpleStringProperty;

import java.util.LinkedList;

/**
* 操作类
*/
public class Operation<Process>{

        public static final String TAG = "Operation";

        public static final int OP_ADD = 0;//加法
        public static final int OP_CUT = 1;//减法
        public static final int OP_MUTIL = 2;//乘法
        public static final int OP_DIVISION = 3;//除法

        private BinaryNum num1;
        private BinaryNum num2;
        private BinaryNum result;
        private String calculateExplanation;//计算说明
        private int operationWay;//计算方式
        private LinkedList<Process> calculateProcess;//计算过程

        public BinaryNum getNum1() {
            return num1;
        }

        public void setNum1(BinaryNum num1) {
            this.num1 = num1;
        }

        public BinaryNum getNum2() {
            return num2;
        }

        public void setNum2(BinaryNum num2) {
            this.num2 = num2;
        }

        public BinaryNum getResult() {
            return result;
        }

        public void setResult(BinaryNum result) {
            this.result = result;
        }

        public int getOperationWay() {
            return operationWay;
        }

        public void setOperationWay(int operationWay) {
            this.operationWay = operationWay;
        }

        public String getCalculateExplanation() {
            return calculateExplanation;
        }

        public void setCalculateExplanation(String calculateExplanation) {
            this.calculateExplanation = calculateExplanation;
        }

        public LinkedList<Process> getCalculateProcess() {
            return calculateProcess;
        }

        public void setCalculateProcess(LinkedList<Process> calculateProcess) {
            this.calculateProcess = calculateProcess;
        }


    /**
     * 乘法计算过程
     */
    public static class MultiProcess{
        private SimpleStringProperty explanation = new SimpleStringProperty();//乘法过程的说明
        private SimpleStringProperty partResult = new SimpleStringProperty();//部分积
        private SimpleStringProperty beAddNum = new SimpleStringProperty();//被加数

        public String getExplanation() {
            return explanation.get();
        }

        public SimpleStringProperty explanationProperty() {
            return explanation;
        }

        public void setExplanation(String explanation) {
            this.explanation.set(explanation);
        }

        public String getPartResult() {
            return partResult.get();
        }

        public SimpleStringProperty partResultProperty() {
            return partResult;
        }

        public void setPartResult(String partResult) {
            this.partResult.set(partResult);
        }

        public String getBeAddNum() {
            return beAddNum.get();
        }

        public SimpleStringProperty beAddNumProperty() {
            return beAddNum;
        }

        public void setBeAddNum(String beAddNum) {
            this.beAddNum.set(beAddNum);
        }

        @Override
        public String toString() {
            return "MultiProcess{" +
                    "explanation=" + explanation +
                    ", partResult=" + partResult +
                    ", beAddNum=" + beAddNum +
                    '}';
        }
    }

}