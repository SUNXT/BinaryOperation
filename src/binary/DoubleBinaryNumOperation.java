package binary;

import binary.base.BaseBinaryNumOperation;
import utils.Log;

public class DoubleBinaryNumOperation extends BaseBinaryNumOperation{

    public static void main(String[] args){

        DoubleBinaryNum num = new DoubleBinaryNum(-10.9, 8);
        DoubleBinaryNum num1 = new DoubleBinaryNum(-0.192, 8);
        cut(num, num1);

    }


    /**
     * 浮点数的加法
     * @param num1
     * @param num2
     * @return
     */
    public static void add(DoubleBinaryNum num1, DoubleBinaryNum num2){
        Log.d("num1: " + num1.getDoubleValue() + ", bin: " + num1.toString());
        Log.d("num2: " + num2.getDoubleValue() + ", bin: " + num2.toString());
        Log.d(num1.toString() + " " + num2.toString());

        int[] newNum1Values,newNum2Values;//处理完的数组
        //判断num1和num2是否长度一样，不一样的话，需要用0来补充使其一样
        if (num1.getValues().length != num2.getValues().length){
            //处理使其长度一样，去掉符号位，然后填充向前填充0

            if (num1.getValues().length > num2.getValues().length){
                //拓展num2的去符号数值部分
                newNum2Values = fillZeroPre(num2.getValues(), num1.getValues().length - num2.getValues().length, true);
                newNum1Values = num1.getValues();
                Log.d("num2", num2.getValues());
                Log.d("拓展了num2， newNum2Values", newNum2Values);
            }else {
                //拓展num1的去符号数值部分
                newNum1Values = fillZeroPre(num1.getValues(), num2.getValues().length - num1.getValues().length, true);
                newNum2Values = num2.getValues();
                Log.d("拓展了num1， newNum1Values", newNum1Values);
            }
        }else {
            newNum1Values = num1.getValues();
            newNum2Values = num2.getValues();
        }

        //拓展后再求补码
        DoubleBinaryNum newNum1 = new DoubleBinaryNum(newNum1Values, num1.getBitLength());
        DoubleBinaryNum newNum2 = new DoubleBinaryNum(newNum2Values, num2.getBitLength());

        newNum1.transComplementNum();
        newNum2.transComplementNum();

        //拓展num1，使其变成双符号位
        int[] num1Values = new int[newNum1Values.length + 1];
        System.arraycopy(newNum1.getValues(), 0, num1Values, 1, newNum1Values.length);
        num1Values[0] = newNum1Values[0];

        //拓展num2，使其变成双符号位
        int[] num2Values = new int[newNum2Values.length + 1];
        System.arraycopy(newNum2.getValues(), 0, num2Values, 1, newNum2Values.length);
        num2Values[0] = newNum2Values[0];

        int[] result = add(num1Values, num2Values);
        Log.d("num1", num1Values);
        Log.d("num2", num2Values);
        Log.d("sum", result);

        //去掉result的最高一位,不然会计算出错
        int[] temp = new int[result.length - 1];
        System.arraycopy(result, 1, temp, 0, temp.length);
        result = temp;
        Log.d("result", result);

        DoubleBinaryNum doubleBinaryNumResult;
        //进行溢出判断
        if ((result[0]^result[1]) == 0){
            //如果是负溢出或正溢出，取最高
            int[] newSum = new int[result.length - 1];
            System.arraycopy(result, 1, newSum, 0, newSum.length);

            if (result[0] == 1){
                //负溢出
                newSum[0] = 1;
            }else {
                //正溢出 01
                newSum[0] = 0;
            }
            result = newSum;
        }
        doubleBinaryNumResult = new DoubleBinaryNum(result, num1.getBitLength());
        Log.d("num1: " + num1.toString());
        Log.d("num2: " + num2.toString());
        Log.d("result:" + doubleBinaryNumResult.toString());
        num1.transComplementNum();
        num2.transComplementNum();
        doubleBinaryNumResult.transComplementNum();
        Log.d(num1.getDoubleValue() + " + " + num2.getDoubleValue() + " = " + doubleBinaryNumResult.getDoubleValue());
    }

    public static void cut(DoubleBinaryNum num1, DoubleBinaryNum num2){
        Log.d("[num1 - num2](补) = [num1](补) - [num2](补) = [num1](补) + [-num2](补)");
        //求出 -num2
        int[] dNum2Values = num2.getValues();
        if (dNum2Values[0] == 0){
            dNum2Values[0] = 1;
        }else {
            dNum2Values[1] = 0;
        }
        DoubleBinaryNum dNum2 = new DoubleBinaryNum(dNum2Values, num2.getBitLength());
        add(num1, dNum2);
    }

}
