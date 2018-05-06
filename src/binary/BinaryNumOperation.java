package binary;

import utils.Log;
import utils.NumberUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class BinaryNumOperation {

    public static void main(String[] args){
        BinaryNum num = new BinaryNum(0);
        BinaryNum num1 = new BinaryNum(-10);
        num.transBinaryNumBitLength(BinaryNum.TYPE_8_BIT);
        num1.transBinaryNumBitLength(BinaryNum.TYPE_8_BIT);
        add(num, num1);
    }

    /**
     * 操作类
     */
    public static class Operation{

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

    /**
     * 补码加 如果参数为负数的话，需要转为补码
     * 使用定点一位加 每次输出部分和
     * @param num1
     * @param num2
     * @return
     */
    public static Operation add(BinaryNum num1, BinaryNum num2){

        Log.d("计算 " + num1.getDecimalValue() + " + " + num2.getDecimalValue());
        Log.d("原码：");
        Log.d("num1: " + num1.toString());
        Log.d("num2: " + num2.toString());
        Log.println();

        Operation operation = new Operation();
        operation.setNum1(new BinaryNum(num1.getValues()));
        operation.setNum2(new BinaryNum(num2.getValues()));
        operation.setOperationWay(Operation.OP_ADD);
        LinkedList<String> calculateProcess = new LinkedList<>();//保存计算过程

        //第一步 求补码
        int count = 1;
        String t1 = num1.toString();
        String t2 = num2.toString();
        //取两个数的补码
        num1.transComplementNum();//转为补码
        num2.transComplementNum();//转为补码
        operation.setCalculateExplanation("二进制定点一位加法(符号位需要参加计算)\n通过一个'部分和'sum来存储与被加数每一位加运算所得到的结果，从被加数的最右边开始加起，直到符号位。");

        //记录第一步
        String p1 = "第" + count + "步：\n求Num1 和 Num2 的补码：Num1：" + t1 + "(原码)->" + num1.toString() + "(补码), "
                + " Num2：" + t2 + "(原码)->" + num2.toString() + "(补码)";
        calculateProcess.add(p1);

        String p2 = "第" + ++count + "步：\n进行符号位溢出处理，将符号位转为二位符号位：Num1：" + num1.getValues()[0] + num1.toString() + ", Num2：" + num2.getValues()[0] + num2.toString();
        calculateProcess.add(p2);

        int[] sum = new int[num1.getLength() + 1];//声明多1位，sum为部分和
        //第一步，将num1的值赋给部分和sum
        System.arraycopy(num1.getValues(), 0, sum, 1, sum.length - 1);
        sum[0] = num1.getValues()[0];//双符号位
        Log.d("sum", sum);

        int[] num2Values = new int[num2.getLength() + 1];
        System.arraycopy(num2.getValues(), 0, num2Values, 1, num2.getLength());
        num2Values[0] = num2.getValues()[0];//双符号位

        String p3 = "第" + ++count + "步：\n初始化(部分和)sum，将Num1赋值给sum，sum = " + NumberUtils.transString(sum);
        calculateProcess.add(p3);

        //符号位也要加
        for (int i = num2Values.length - 1; i >= 0; i --){
            String p = "第" + ++count + "步：\n(部分和)sum = " + NumberUtils.transString(sum) + "，被加数：";
            if (num2Values[i] != 0){
                sum = add(sum, num2Values[i], i);
                p += "1" + NumberUtils.createZero(num2Values.length - i - 1) + "，计算结果 sum = " + NumberUtils.transString(sum);
            }else {
                p += "0，计算结果 sum = " + NumberUtils.transString(sum);
            }
            calculateProcess.add(p);
            Log.d("sum: " + "i: " + i + " ", sum);
        }

        //判断符号位有没有有溢出 将最高位和次高位的数进行异或，如果结果为 0，则表示 溢出，如果符号位为11，表示负溢出，不处理；
        //如果符号位为00，表示正溢出，去掉最高为的符号位，从次高位开始作为符号位。
        //如果符号位异或的结果为 1，表示不溢出，直接取次高位的数最为符号位
        BinaryNum result;
        if ((sum[0]^sum[1]) == 0){
            //如果是负溢出 11
            if (sum[0] == 1){
                result = new BinaryNum(sum);//不处理
            }else {
                //正溢出 00
                int[] newSum = new int[sum.length - 1];
                System.arraycopy(sum, 1, newSum, 0, newSum.length);
                result = new BinaryNum(newSum);
            }
        }else {
            result = new BinaryNum(sum);
        }

        String p4 = "第" + ++count + "步：\n";
        p4 += "判断符号位有没有有溢出，将最高位和次高位的数进行异或，如果符号位异或的结果为 0，则表示 溢出，如果符号位为11，表示负溢出，不处理；\n"
                + "如果符号位为00，表示正溢出，去掉最高为的符号位，从次高位开始作为符号位。\n"
                + "如果符号位异或的结果为 1，表示不溢出，直接取次高位的数最为符号位。";
        p4 += "处理的结果（补码）：" + result.toString();
        calculateProcess.add(p4);

        Log.d("计算结果：sum：", result.getValues());
        //因为是补码计算，计算结果需要再取补码
        result.transComplementNum();
        String p5 = "第" + ++count + "步：\n" + "再将计算结果转为原码，得到最终的计算结果：result = " + result.toString();
        calculateProcess.add(p5);
        Log.d("取其补码：sum：", result.getValues());
        Log.d("sum: " + result.getDecimalValue());

        String p6 = "结束：(" + operation.getNum1().getDecimalValue() + ") + (" + operation.getNum2().getDecimalValue() + ") = " + result.getDecimalValue();
        calculateProcess.add(p6);

        operation.setOperationWay(Operation.OP_ADD);
        operation.setResult(result);//设置计算结果
        operation.setCalculateProcess(calculateProcess);
        Log.d(operation.getCalculateExplanation());
        for (String p: operation.getCalculateProcess()){
            Log.d(p);
        }

        return operation;
    }

    /**
     *
     * @param oldValues 被加数的原来数组
     * @param value 要加的值
     * @param index 要加的值的位数
     * @return
     */
    private static int[] add(int[] oldValues, int value, int index){

        if (index < 0 ){
            return oldValues;//已经到第一位，不需要做处理
        }

        // 如果是0不需要做处理
        if (value == 0){
            return oldValues;
        }

        if (oldValues[index] + 1 == 2){
            //进1 将原来位设为0，同时调用递归 进行加1
            oldValues[index] = 0;
            return add(oldValues, 1, index - 1);
        }else {
            //不进位
            oldValues[index] = 1;
            return oldValues;
        }
    }

}
