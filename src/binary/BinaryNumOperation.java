package binary;

import utils.Log;
import utils.NumberUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;


public class BinaryNumOperation {

    public static void main(String[] args){
        DoubleBinaryNum num = new DoubleBinaryNum(-0.99, 8);
        DoubleBinaryNum num1 = new DoubleBinaryNum(0.999, 8);
        add(num, num1);
//
//        BinaryNum num = new BinaryNum(14);
//        BinaryNum num1 = new BinaryNum(8);
//        num.transBinaryNumBitLength(BinaryNum.TYPE_8_BIT);
//        num1.transBinaryNumBitLength(BinaryNum.TYPE_8_BIT);
//        multi(num, num1, true);
//
//        int[] num1 = {0,1};
//        int[] num2 = {1,0,0,1};
//        Log.d("num1", num1);
//        Log.d("num2", num2);
//        Log.d("result", add(num1, num2));
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

        Operation<String> operation = new Operation<>();
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

//        String p3 = "第" + ++count + "步：\n初始化(部分和)sum，将Num1赋值给sum，sum = " + NumberUtils.transString(sum);
//        calculateProcess.add(p3);

        String p4 = "";
        if (isTwoBit){
            //二位计算
            for (int i = num2Values.length - 1; i >= 0; i-=2){
//                p4 = "第" + ++count + "步：\n(部分和)sum = " + NumberUtils.transString(sum) + "，被加数：";
                if (num2Values[i] == 0 && num2Values[i-1] == 0){
//                    p4 += "0，计算结果 sum = " + NumberUtils.transString(sum);
                }else {
                    sum = add(sum, num2Values[i], i);
                    sum = add(sum, num2Values[i-1], i - 1);
//                    p4 += num2Values[i-1] + "" +  num2Values[i] + NumberUtils.createZero(num2Values.length - i - 1) + "，计算结果 sum = " + NumberUtils.transString(sum);
                }
//                calculateProcess.add(p4);
            }
        }else {
            //一位计算
            //符号位也要加
            for (int i = num2Values.length - 1; i >= 0; i --){
//                p4 = "第" + ++count + "步：\n(部分和)sum = " + NumberUtils.transString(sum) + "，被加数：";
                if (num2Values[i] != 0){
                    sum = add(sum, num2Values[i], i);
//                    p4 += "1" + NumberUtils.createZero(num2Values.length - i - 1) + "，计算结果 sum = " + NumberUtils.transString(sum);
                }else {
//                    p4 += "0，计算结果 sum = " + NumberUtils.transString(sum);
                }
//                calculateProcess.add(p4);
            }
        }

        String cutLine = "";
        if (num1.getBitType() == BinaryNum.TYPE_8_BIT){
            cutLine = "------------------";
        }else {
            cutLine = "---------------------------";
        }
        p4 = "第" + ++count + "步：计算过程\n\t" + num1.toString() + "\n+\t" + num2.toString() + "\n"
                + cutLine
                +"\n\t" + NumberUtils.transString(sum);
//        p4 = "   " + num1.toString() + "\n+" + num2.toString() + "\n------------------\n" + NumberUtils.transString(sum);
        calculateProcess.add(p4);

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

        String num1ValuesStr = num1.toString();//保存num1的原码
        num1.transComplementNum();
        Log.d("num1 转为补码：" + num1.toString());

        Log.d("[num1 - num2](补) = [num1](补) - [num2](补) = [num1](补) + [-num2](补)");
        calculateProcess.add("[num1 - num2](补) = [num1](补) - [num2](补) = [num1](补) + [-num2](补)");
        Log.d("num2: " + num2.toString());
        int num2Decimal = num2.getDecimalValue();
        int bitType = num2.getBitType();
        num2 = new BinaryNum(-num2Decimal);//num2 变为 -num2
        num2.transBinaryNumBitLength(bitType);
        Log.d("num2: " + num2.toString());
        String num2ValuesStr = num2.toString();//保存num2的原码
        num2.transComplementNum();//转为补码
        Log.d("求[-num2](补) = " + num2.toString());

        calculateProcess.add("第" + count + "步：\n求出num1(补)和[-num2](补)\nnum1: " + num1ValuesStr + "(原) -> " + num1.toString() + "(补)，-num2:" + num2ValuesStr + "(原) -> " + num2.toString() + "(补)");

        calculateProcess.add("第" + ++count + "步：\n计算 " + num1.toString() + " + " + num2.toString());
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
//                p4 = "第" + ++count + "步：\n(部分和)sum = " + NumberUtils.transString(sum) + "，被加数：";
                if (num2Values[i] == 0 && num2Values[i-1] == 0){
//                    p4 += "0，计算结果 sum = " + NumberUtils.transString(sum);
                }else {
                    sum = add(sum, num2Values[i], i);
                    sum = add(sum, num2Values[i-1], i - 1);
//                    p4 += num2Values[i-1] + "" +  num2Values[i] + NumberUtils.createZero(num2Values.length - i - 1) + "，计算结果 sum = " + NumberUtils.transString(sum);
                }
//                calculateProcess.add(p4);
            }
        }else {
            //一位计算
            //符号位也要加
            for (int i = num2Values.length - 1; i >= 0; i --){
//                p4 = "第" + ++count + "步：\n(部分和)sum = " + NumberUtils.transString(sum) + "，被加数：";
                if (num2Values[i] != 0){
                    sum = add(sum, num2Values[i], i);
//                    p4 += "1" + NumberUtils.createZero(num2Values.length - i - 1) + "，计算结果 sum = " + NumberUtils.transString(sum);
                }else {
//                    p4 += "0，计算结果 sum = " + NumberUtils.transString(sum);
                }
//                calculateProcess.add(p4);
            }
        }

        String cutLine = "";
        if (num1.getBitType() == BinaryNum.TYPE_8_BIT){
            cutLine = "------------------";
        }else {
            cutLine = "---------------------------";
        }
        p4 = "第" + ++count + "步：计算过程\n\t" + num1.toString() + "\n+\t" + num2.toString() + "\n"
                + cutLine
                +"\n\t" + NumberUtils.transString(sum);
//        p4 = "   " + num1.toString() + "\n+" + num2.toString() + "\n------------------\n" + NumberUtils.transString(sum);
        calculateProcess.add(p4);

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

        String p5 = "第" + ++count + "步：\n";
        p5 += "判断符号位有没有有溢出，将最高位和次高位的数进行异或，如果符号位异或的结果为 0，则表示 溢出，如果符号位为11，表示负溢出，不处理；\n"
                + "如果符号位为00，表示正溢出，去掉最高为的符号位，从次高位开始作为符号位。\n"
                + "如果符号位异或的结果为 1，表示不溢出，直接取次高位的数最为符号位。";
        p5 += "处理的结果（补码）：" + result.toString();
        calculateProcess.add(p5);

        //将结果进行转为原码
        result.transComplementNum();

        String p6 = "第" + ++count + "步：\n" + "再将计算结果转为原码，得到最终的计算结果：result = " + result.toString();
        calculateProcess.add(p6);

        operation.setOperationWay(Operation.OP_CUT);
        operation.setResult(result);
        operation.setCalculateProcess(calculateProcess);
        Log.d("结果：" + result.getDecimalValue());

        String p7 = "结束：(" + operation.getNum1().getDecimalValue() + ") - (" + operation.getNum2().getDecimalValue() + ") = " + result.getDecimalValue();
        calculateProcess.add(p7);

        return operation;
    }

    /**
     * 纯小数的加法
     * @param num1
     * @param num2
     * @return
     */
    public static void add(DoubleBinaryNum num1, DoubleBinaryNum num2){
        Log.d("num1: " + num1.getDoubleValue() + ", bin: " + num1.toString());
        Log.d("num2: " + num2.getDoubleValue() + ", bin: " + num2.toString());
        num1.transComplementNum();
        num2.transComplementNum();
        //拓展num1，使其变成双符号位
        int[] num1Values = new int[num1.getValues().length + 1];
        System.arraycopy(num1.getValues(), 0, num1Values, 1, num1.getValues().length);
        num1Values[0] = num1.getValues()[0];
        //拓展num2，使其变成双符号位
        int[] num2Values = new int[num2.getValues().length + 1];
        System.arraycopy(num2.getValues(), 0, num2Values, 1, num2.getValues().length);
        num2Values[0] = num2.getValues()[0];

        int[] result = add(num1Values, num2Values);
        Log.d("num1", num1.getValues());
        Log.d("num2", num2.getValues());
        Log.d("sum", result);

        //去掉第一位
        int[] temp = new int[result.length - 1];
        System.arraycopy(result, 1, temp, 0, temp.length);
        result = temp;
        //进行溢出判断
        if ((result[0]^result[1]) == 0){
            //如果是负溢出 11
            if (result[0] == 1){
                //不处理
            }else {
                //正溢出 00
                int[] newSum = new int[result.length - 1];
                System.arraycopy(result, 1, newSum, 0, newSum.length);
            }
        }


    }

    /**
     * 乘法
     * @param num1
     * @param num2
     * @return
     */
    public static Operation<Operation.MultiProcess> multi(BinaryNum num1, BinaryNum num2, boolean isTwoBit){
        Operation<Operation.MultiProcess> operation = new Operation<>();
        operation.setNum1(num1);
        operation.setNum2(num2);
        LinkedList<Operation.MultiProcess> processes = new LinkedList<>();//计算过程

        int resultSign = num1.getValues()[0] ^ num2.getValues()[0];//num1的符号位异或num2的符号位得到计算结果的符号位
        int[] num1Values = new int[num1.getLength() - 1];//用于保存num1数值部分
        int[] num2Values = new int[num2.getLength() - 1];//用于保存num2数值部分
        //将num1,num2数值部分的数组赋值到新数组中
        System.arraycopy(num1.getValues(), 1, num1Values, 0, num1Values.length);
        System.arraycopy(num2.getValues(), 1, num2Values, 0, num2Values.length);
        Log.d("num1Values: ", num1Values);
        Log.d("num2Values: ", num2Values);
        //num1Values作为乘法中的被加数
        int[] tempValues = new int[num1Values.length];//初始化部分积
        //从num2Values的最右边数开始乘起，如果是一，则直接加上num1Values
        int[] beAddNum;//每次的被加数
        Operation.MultiProcess multiProcess;//乘法计算步骤
        if (isTwoBit){
            //这是二位运算,每次计算num2的两位乘，被乘数两位的情况有 00 01 10 11
            //当00时，被乘数为0，此时被加数为0
            //当01时，被乘数为1，此时被加数为 num1(后面补零，使其对应上被乘数的位置)
            //当10时，被乘数为2，此时被加数为 num1*2(后面补零，使其对应上被乘数的位置)
            //当11时，被乘数为3，此时被加数为 num1*3(后面补零，使其对应上被乘数的位置)
            //先计算num1*2 和 num2*3
            int[] num1ValuesD2 = new int[num1Values.length + 1];// num1*2
            System.arraycopy(num1Values, 0, num1ValuesD2, 0, num1Values.length);//将num1向左移动一位
            Log.d("num1", num1Values);
            Log.d("num1*2", num1ValuesD2);
            int[] num1ValuesD3 = add(num1Values, num1ValuesD2);
            Log.d("num*3", num1ValuesD3);
            for (int i = num2Values.length - 1, j = 0; i >= 0;  i-=2, j +=2){
                multiProcess = new Operation.MultiProcess();//记录两位乘的过程
                multiProcess.setPartResult(NumberUtils.transString(tempValues));
                String explanation;
                switch (num2Values[i-1] + "" + num2Values[i]){
                    case "00":
                        Log.d("00");
                        multiProcess.setBeAddNum("0");
                        break;
                    case "01":
                        Log.d("01");
                        beAddNum = fillZero(num1Values, j);
                        multiProcess.setBeAddNum(NumberUtils.transString(beAddNum));
                        tempValues = add(tempValues, beAddNum);
                        break;
                    case "10":
                        Log.d("10");
                        beAddNum = fillZero(num1ValuesD2, j);
                        multiProcess.setBeAddNum(NumberUtils.transString(beAddNum));
                        tempValues = add(tempValues, beAddNum);
                        break;
                    case "11":
                        Log.d("11");
                        beAddNum = fillZero(num1ValuesD3, j);
                        multiProcess.setBeAddNum(NumberUtils.transString(beAddNum));
                        tempValues = add(tempValues, beAddNum);
                        break;
                }
                explanation = "num2第" + (j+1) + " " + (j + 2) + "位值为 " + num2Values[i-1] + "" + num2Values[i] + ", 此时的被加数为 " + multiProcess.getBeAddNum() + ", 部分积为" + multiProcess.getPartResult() + ", 计算完的部分积为" + NumberUtils.transString(tempValues);
                multiProcess.setExplanation(explanation);
                processes.add(multiProcess);
            }
            Log.d("result", tempValues);
        }else {
            //这是一位乘运算,j是补0的个数
            for (int i = num2Values.length - 1, j = 0; i >= 0; --i, ++j){
                multiProcess = new Operation.MultiProcess();//用于记录每一位乘的过程
                multiProcess.setPartResult(NumberUtils.transString(tempValues));//记录当前部分积
                if (num2Values[i] == 1){
                    beAddNum = fillZero(num1Values, j);//被加数
                    tempValues = add(tempValues, beAddNum);
                    Log.d("tempValues", tempValues);
                    multiProcess.setBeAddNum(NumberUtils.transString(beAddNum));
                }else {
                    multiProcess.setBeAddNum("0");
                }
                String explanation = "num2第" + (j+1) + "位为" + num2Values[i] +", 此时被加数为" + multiProcess.getBeAddNum()+ ", 部分积为" + multiProcess.getPartResult() + ", 计算完部分积为 " + NumberUtils.transString(tempValues);
                multiProcess.setExplanation(explanation);
                processes.add(multiProcess);
            }
        }

        operation.setCalculateProcess(processes);
        BinaryNum result = new BinaryNum(resultSign + NumberUtils.transString(tempValues));
        operation.setResult(result);
        Log.d(num1.getDecimalValue() + " * " + num2.getDecimalValue() + " = " + result.getDecimalValue());

        for (Operation.MultiProcess process: processes){
            Log.d(process.toString());
        }
        return operation;
    }

    /**
     * 两个0/1数组相加
     * num1 和 num2
     * @param num1 加数数组
     * @param num2 被加数数组
     * @return 计算结果
     */
    private static int[] add(int[] num1, int[] num2){
        int[] result = new int[Math.max(num1.length, num2.length) + 1];//计算结果为两个数组中最长数组+1，防止溢出
        int[] temp;
        if (num1.length < num2.length){
            System.arraycopy(num1, 0, result, result.length - num1.length, num1.length);//将num1从右边赋值到result中，例如result是4位，num1=101，则赋值后，result为 0101
            temp = num2;//被加数为num2
        }else {
            System.arraycopy(num2, 0, result, result.length - num2.length, num2.length);//将num1从右边赋值到result中，例如result是4位，num2=101，则赋值后，result为 0101
            temp = num1;//被加数为num1
        }
        for (int i = result.length - 1, j = temp.length - 1; i >= 0 && j >= 0; i --, j --){
            if (temp[j] == 1){
                result = add(result, 1, i);
            }
        }
        return result;
    }

    /**
     * 拓展数组，后面补0
     * @param src
     * @param zeroLength
     * @return
     */
    private static int[] fillZero(int[] src, int zeroLength){
        if (zeroLength == 0)
            return src;

        int[] result = new int[src.length + zeroLength];
        System.arraycopy(src, 0, result, 0, src.length);
        return result;
    }

}
