package binary;

import utils.Log;
import utils.NumberUtils;

import java.util.ArrayList;
import java.util.LinkedList;


public class BinaryNumOperation {

    public static void main(String[] args){
        BinaryNum num = new BinaryNum(55);
        BinaryNum num1 = new BinaryNum(10);
        num.transBinaryNumBitLength(BinaryNum.TYPE_8_BIT);
        num1.transBinaryNumBitLength(BinaryNum.TYPE_8_BIT);
        cut(num, num1, false);
    }


    /**
     * 整数
     * 补码加 如果参数为负数的话，需要转为补码
     * 使用定点一位加、二位加 每次输出部分和
     * @param num1
     * @param num2
     * @return
     */
    public static Operation add(BinaryNum num1, BinaryNum num2, boolean isTwoBit){

        if (isTwoBit){
            Log.d("二位加法");
        }else{
            Log.d("一位加法");
        }

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

        String p4 = "";
        if (isTwoBit){
            //二位计算
            for (int i = num2Values.length - 1; i >= 0; i-=2){
                p4 = "第" + ++count + "步：\n(部分和)sum = " + NumberUtils.transString(sum) + "，被加数：";
                if (num2Values[i] == 0 && num2Values[i-1] == 0){
                    p4 += "0，计算结果 sum = " + NumberUtils.transString(sum);
                }else {
                    sum = add(sum, num2Values[i], i);
                    sum = add(sum, num2Values[i-1], i - 1);
                    p4 += num2Values[i-1] + "" +  num2Values[i] + NumberUtils.createZero(num2Values.length - i - 1) + "，计算结果 sum = " + NumberUtils.transString(sum);
                }
                calculateProcess.add(p4);
            }
        }else {
            //一位计算
            //符号位也要加
            for (int i = num2Values.length - 1; i >= 0; i --){
                p4 = "第" + ++count + "步：\n(部分和)sum = " + NumberUtils.transString(sum) + "，被加数：";
                if (num2Values[i] != 0){
                    sum = add(sum, num2Values[i], i);
                    p4 += "1" + NumberUtils.createZero(num2Values.length - i - 1) + "，计算结果 sum = " + NumberUtils.transString(sum);
                }else {
                    p4 += "0，计算结果 sum = " + NumberUtils.transString(sum);
                }
                calculateProcess.add(p4);
            }
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

        String p5 = "第" + ++count + "步：\n";
        p5 += "判断符号位有没有有溢出，将最高位和次高位的数进行异或，如果符号位异或的结果为 0，则表示 溢出，如果符号位为11，表示负溢出，不处理；\n"
                + "如果符号位为00，表示正溢出，去掉最高为的符号位，从次高位开始作为符号位。\n"
                + "如果符号位异或的结果为 1，表示不溢出，直接取次高位的数最为符号位。";
        p5 += "处理的结果（补码）：" + result.toString();
        calculateProcess.add(p5);

        Log.d("计算结果：sum：", result.getValues());
        //因为是补码计算，计算结果需要再取补码
        result.transComplementNum();
        String p6 = "第" + ++count + "步：\n" + "再将计算结果转为原码，得到最终的计算结果：result = " + result.toString();
        calculateProcess.add(p6);
        Log.d("取其补码：sum：", result.getValues());
        Log.d("sum: " + result.getDecimalValue());

        String p7 = "结束：(" + operation.getNum1().getDecimalValue() + ") + (" + operation.getNum2().getDecimalValue() + ") = " + result.getDecimalValue();
        calculateProcess.add(p7);

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

    /**
     * 一位、二位补码减法
     * @param num1
     * @param num2
     * @param isTwoBit
     * @return
     */
    public static Operation cut(BinaryNum num1, BinaryNum num2, boolean isTwoBit){
        Log.d("num1: " + num1.toString() + " 值：" + num1.getDecimalValue());
        Log.d("num2: " + num2.toString() + " 值：" + num2.getDecimalValue());

        Operation operation = new Operation();
        operation.setNum1(new BinaryNum(num1.getValues()));
        operation.setNum2(new BinaryNum(num2.getValues()));
        LinkedList<String> calculateProcess = new LinkedList<>();
        int count = 1;

        num1.transComplementNum();
        Log.d("num1 转为补码：" + num1.toString());

        Log.d("[num1 - num2](补) = [num1](补) - [num2](补) = [num1](补) + [-num2](补)");
        Log.d("num2: " + num2.toString());
        int num2Decimal = num2.getDecimalValue();
        int bitType = num2.getBitType();
        num2 = new BinaryNum(-num2Decimal);//num2 变为 -num2
        num2.transBinaryNumBitLength(bitType);
        Log.d("num2: " + num2.toString());
        num2.transComplementNum();//转为补码
        Log.d("求[-num2](补) = " + num2.toString());

        //处理符号位变成双符号位
        int[] sum = new int[num1.getLength() + 1];//声明多1位，sum为部分和
        //第一步，将num1的值赋给部分和sum
        System.arraycopy(num1.getValues(), 0, sum, 1, sum.length - 1);
        sum[0] = num1.getValues()[0];//双符号位
        Log.d("sum", sum);

        int[] num2Values = new int[num2.getLength() + 1];
        System.arraycopy(num2.getValues(), 0, num2Values, 1, num2.getLength());
        num2Values[0] = num2.getValues()[0];//双符号位
        Log.d("num2", num2Values);

        //分为二位和一位计算
        String p4 = "";
        if (isTwoBit){
            //二位计算
            for (int i = num2Values.length - 1; i >= 0; i-=2){
                p4 = "第" + ++count + "步：\n(部分和)sum = " + NumberUtils.transString(sum) + "，被加数：";
                if (num2Values[i] == 0 && num2Values[i-1] == 0){
                    p4 += "0，计算结果 sum = " + NumberUtils.transString(sum);
                }else {
                    sum = add(sum, num2Values[i], i);
                    sum = add(sum, num2Values[i-1], i - 1);
                    p4 += num2Values[i-1] + "" +  num2Values[i] + NumberUtils.createZero(num2Values.length - i - 1) + "，计算结果 sum = " + NumberUtils.transString(sum);
                }
                calculateProcess.add(p4);
            }
        }else {
            //一位计算
            //符号位也要加
            for (int i = num2Values.length - 1; i >= 0; i --){
                p4 = "第" + ++count + "步：\n(部分和)sum = " + NumberUtils.transString(sum) + "，被加数：";
                if (num2Values[i] != 0){
                    sum = add(sum, num2Values[i], i);
                    p4 += "1" + NumberUtils.createZero(num2Values.length - i - 1) + "，计算结果 sum = " + NumberUtils.transString(sum);
                }else {
                    p4 += "0，计算结果 sum = " + NumberUtils.transString(sum);
                }
                calculateProcess.add(p4);
            }
        }

        //进行符号位处理
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

        //将结果进行转为原码
        result.transComplementNum();
        operation.setResult(result);

        Log.d("结果：" + result.getDecimalValue());
        return operation;
    }

}
