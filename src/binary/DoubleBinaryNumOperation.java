package binary;

import binary.base.BaseBinaryNumOperation;
import binary.base.IBinaryNumOperation;
import utils.Log;
import utils.NumberUtils;

import java.util.LinkedList;

public class DoubleBinaryNumOperation extends BaseBinaryNumOperation implements IBinaryNumOperation<DoubleBinaryNum> {

    public static void main(String[] args){

        IBinaryNumOperation operation = new DoubleBinaryNumOperation();
        DoubleBinaryNum num = new DoubleBinaryNum(0.321, 8);
        DoubleBinaryNum num1 = new DoubleBinaryNum(-0.192, 8);
        operation.cut(num, num1, false);

    }


    /**
     * 浮点数的加法
     * @param num1
     * @param num2
     * @return
     */
    @Override
    public Operation add(DoubleBinaryNum num1, DoubleBinaryNum num2, boolean isTwoBit){
        Log.d("num1: " + num1.getDoubleValue() + ", bin: " + num1.toString());
        Log.d("num2: " + num2.getDoubleValue() + ", bin: " + num2.toString());
        Log.d(num1.toString() + " " + num2.toString());

        Operation<String> operation = new Operation<>();
        operation.setDNum1(num1.createNewOne());
        operation.setDNum2(num2.createNewOne());
        LinkedList<String> calculateProcess = new LinkedList<>();//保存计算过程
        int count = 0;

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

        //记录第一步
        String p1 = "第" + count + "步：\n求Num1 和 Num2 的补码：Num1：" + operation.getDNum1().toString() + "(原码)->" + num1.toString() + "(补码), "
                + " Num2：" + operation.getDNum2().toString() + "(原码)->" + num2.toString() + "(补码)";
        calculateProcess.add(p1);

        String p2 = "第" + ++count + "步：\n进行符号位溢出处理，将符号位转为二位符号位：Num1：" + num1.getValues()[0] + num1.toString() + ", Num2：" + num2.getValues()[0] + num2.toString();
        calculateProcess.add(p2);

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

        String cutLine = "";
        for (int i = 0; i < num1.getValues().length + 16; ++i){
            cutLine += "-";
        }

        String p3 = "第" + ++count + "步：计算过程\n\t" + num1.toString() + "\n+\t" + num2.toString() + "\n"
                + cutLine
                +"\n\t" + NumberUtils.transString(result);
//        p4 = "   " + num1.toString() + "\n+" + num2.toString() + "\n------------------\n" + NumberUtils.transString(sum);
        calculateProcess.add(p3);

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

        String p4 = "第" + ++count + "步：\n";
        p4 += "判断符号位有没有有溢出，将最高位和次高位的数进行异或，如果符号位异或的结果为 0，则表示 溢出，如果符号位为11，表示负溢出，不处理；\n"
                + "如果符号位为00，表示正溢出，去掉最高为的符号位，从次高位开始作为符号位。\n"
                + "如果符号位异或的结果为 1，表示不溢出，直接取次高位的数最为符号位。";
        p4 += "处理的结果（补码）：" + NumberUtils.transString(result);
        calculateProcess.add(p4);

        doubleBinaryNumResult = new DoubleBinaryNum(result, num1.getBitLength());
        Log.d("num1: " + num1.toString());
        Log.d("num2: " + num2.toString());
        Log.d("result:" + doubleBinaryNumResult.toString());
        num1.transComplementNum();
        num2.transComplementNum();
        doubleBinaryNumResult.transComplementNum();

        String p5 = "第" + ++count + "步：\n" + "再将计算结果转为原码，得到最终的计算结果：result = " + doubleBinaryNumResult.toString();
        calculateProcess.add(p5);

        Log.d(num1.getDoubleValue() + " + " + num2.getDoubleValue() + " = " + doubleBinaryNumResult.getDoubleValue());

        String p6 = "结束：(" + operation.getDNum1().getDoubleValue() + ") + (" + operation.getDNum2().getDoubleValue() + ") = " + doubleBinaryNumResult.getDoubleValue();
        calculateProcess.add(p6);

        operation.setCalculateProcess(calculateProcess);
        operation.setDResult(doubleBinaryNumResult);
        return operation;
    }

    @Override
    public Operation cut(DoubleBinaryNum num1, DoubleBinaryNum num2, boolean isTwoBit){

        Operation<String> operation = new Operation<>();
        operation.setDNum1(num1.createNewOne());
        operation.setDNum2(num2.createNewOne());
        Log.d("num2 " + operation.getDNum2().getDoubleValue());

        LinkedList<String> calculateProcess = new LinkedList<>();
        int count = 0;
        Log.d("[num1 - num2](补) = [num1](补) - [num2](补) = [num1](补) + [-num2](补)");
        //求出 -num2
        int[] dNum2Values = num2.getValues();
        if (dNum2Values[0] == 0){
            dNum2Values[0] = 1;
        }else {
            dNum2Values[0] = 0;
        }
        DoubleBinaryNum dNum2 = new DoubleBinaryNum(dNum2Values, num2.getBitLength());
        Operation<String> addOperation = add(num1.createNewOne(), dNum2, isTwoBit);

        DoubleBinaryNum result = addOperation.getDResult();

        //为了显示效果，做出些处理
        num1.transComplementNum();
        num2.transComplementNum();
        calculateProcess.add("说明：\n求出num1(补)和[-num2](补)\nnum1: " + operation.getDNum1().toString() + "(原) -> " + num1.toString() + "(补)，-num2:" + operation.getDNum2().toString() + "(原) -> " + num2.toString() + "(补)");
        calculateProcess.add("第" + ++count + "步：\n计算 " + num1.toString() + " + " + num2.toString());
        calculateProcess.add(addOperation.getCalculateProcess().get(2));
        calculateProcess.add(addOperation.getCalculateProcess().get(3));
        String p7 = "结束：(" + operation.getDNum1().getDoubleValue() + ") - (" + operation.getDNum2().getDoubleValue() + ") = " + result.getDoubleValue();
        calculateProcess.add(p7);
        operation.setDResult(result);
        operation.setCalculateProcess(calculateProcess);
        return operation;
    }

    @Override
    public Operation multi(DoubleBinaryNum num1, DoubleBinaryNum num2, boolean isTwoBit) {
        Operation<Operation.MultiProcess> operation = new Operation<>();
        operation.setDNum1(num1.createNewOne());
        operation.setDNum2(num2.createNewOne());
        LinkedList<Operation.MultiProcess> processes = new LinkedList<>();

        int resultSign = num1.getValues()[0] ^ num2.getValues()[0];//num1的符号位异或num2的符号位得到计算结果的符号位
        int[] num1Values = new int[num1.getValues().length - 1];//用于保存num1数值部分
        int[] num2Values = new int[num2.getValues().length - 1];//用于保存num2数值部分

        // 将num1,num2数值部分的数组赋值到新数组中
        System.arraycopy(num1.getValues(), 1, num1Values, 0, num1Values.length);
        System.arraycopy(num2.getValues(), 1, num2Values, 0, num2Values.length);
        Log.d("num1Values: ", num1Values);
        Log.d("num2Values: ", num2Values);

        // TODO: 2018/6/5 还需要处理，具体和整数相识
        return operation;
    }

    @Override
    public Operation division(DoubleBinaryNum num1, DoubleBinaryNum num2, boolean isTwoBit) {
        return null;
    }

}
