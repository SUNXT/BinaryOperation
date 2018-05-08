package binary;

import java.util.LinkedList;

/**
* 操作类
*/
public class Operation{

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
        private LinkedList<String> calculateProcess;//计算过程

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

        public LinkedList<String> getCalculateProcess() {
            return calculateProcess;
        }

        public void setCalculateProcess(LinkedList<String> calculateProcess) {
            this.calculateProcess = calculateProcess;
        }

    }