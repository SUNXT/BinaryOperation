package binary;

import binary.base.BaseBinaryNumOperation;
import binary.base.IBinaryNumOperation;
import utils.Log;
import utils.NumberUtils;

import java.util.LinkedList;

public class DoubleBinaryNumOperation extends BaseBinaryNumOperation implements IBinaryNumOperation<DoubleBinaryNum> {

    public static void main(String[] args){

        IBinaryNumOperation operation = new DoubleBinaryNumOperation();
        DoubleBinaryNum num = new DoubleBinaryNum(0.921, 8);
        DoubleBinaryNum num1 = new DoubleBinaryNum(-0.192, 8);
        operation.division(num, num1, true);

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

    /**
     * 只考虑纯小数，不考虑浮点数，所以默认绝对值小于1
     * 取出小数部分进行乘法运算，与整数不同的点在于，如果被乘数为0的时候，还需要加0进行进位操作，其他和整数的算法一样
     * @param num1
     * @param num2
     * @param isTwoBit
     * @return
     */
    @Override
    public Operation multi(DoubleBinaryNum num1, DoubleBinaryNum num2, boolean isTwoBit) {

        Log.d("num1: " + num1.toString());
        Log.d("num2: " + num2.toString());

        Operation<Operation.MultiProcess> operation = new Operation<>();
        operation.setDNum1(num1.createNewOne());
        operation.setDNum2(num2.createNewOne());
        LinkedList<Operation.MultiProcess> processes = new LinkedList<>();

        int resultSign = num1.getValues()[0] ^ num2.getValues()[0];//num1的符号位异或num2的符号位得到计算结果的符号位
        int resultBitLength = num1.getBitLength() + num1.getBitLength();//结果的小数点位数为两个乘数相加

        //取出小数点后面的部分，保存起来
        int[] num1Values = new int[num1.getValues().length - 2];//用于保存num1数值部分
        int[] num2Values = new int[num2.getValues().length - 2];//用于保存num2数值部分

        // 将num1,num2数值部分的数组赋值到新数组中
        System.arraycopy(num1.getValues(), 2, num1Values, 0, num1Values.length);
        System.arraycopy(num2.getValues(), 2, num2Values, 0, num2Values.length);
        Log.d("num1Values: ", num1Values);
        Log.d("num2Values: ", num2Values);

        //将小数点后面的部分进行数组乘法运算，然后再拼接个位数和符号位

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
                        beAddNum = NumberUtils.createZeroArray(tempValues.length + 1);
                        multiProcess.setBeAddNum(NumberUtils.transString(beAddNum));
                        tempValues = add(tempValues, beAddNum);
                        break;
                    case "01":
                        beAddNum = fillZeroBehind(num1Values, j);
                        multiProcess.setBeAddNum(NumberUtils.transString(beAddNum));
                        tempValues = add(tempValues, beAddNum);
                        break;
                    case "10":
                        Log.d("10");
                        beAddNum = fillZeroBehind(num1ValuesD2, j);
                        multiProcess.setBeAddNum(NumberUtils.transString(beAddNum));
                        tempValues = add(tempValues, beAddNum);
                        break;
                    case "11":
                        Log.d("11");
                        beAddNum = fillZeroBehind(num1ValuesD3, j);
                        multiProcess.setBeAddNum(NumberUtils.transString(beAddNum));
                        tempValues = add(tempValues, beAddNum);
                        break;
                }
                explanation = "num2第" + (j+1) + " " + (j + 2) + "位值为 " + num2Values[i-1] + "" + num2Values[i] + ", 此时的被加数为 " + multiProcess.getBeAddNum() + ", 部分积为" + multiProcess.getPartResult() + ", 计算完的部分积为" + NumberUtils.transString(tempValues);
                multiProcess.setExplanation(explanation);
                multiProcess.setProcess("\t" + multiProcess.getPartResult() + "(部分积)\n" + "+\t" + multiProcess.getBeAddNum() + "(被加数)\n" + createCutLine(multiProcess.getBeAddNum().length() + 30) + "\n\t" + NumberUtils.transString(tempValues));
                processes.add(multiProcess);
            }
            Log.d("result", tempValues);
        }else {
            //这是一位乘运算,j是补0的个数
            for (int i = num2Values.length - 1, j = 0; i >= 0; --i, ++j){
                multiProcess = new Operation.MultiProcess();//用于记录每一位乘的过程
                multiProcess.setPartResult(NumberUtils.transString(tempValues));//记录当前部分积
                if (num2Values[i] == 1){
                    beAddNum = fillZeroBehind(num1Values, j);//被加数
                    tempValues = add(tempValues, beAddNum);
                    multiProcess.setBeAddNum(NumberUtils.transString(beAddNum));
                }else {
                    beAddNum = NumberUtils.createZeroArray(tempValues.length + 1);
                    tempValues = add(tempValues, beAddNum);
                    multiProcess.setBeAddNum(NumberUtils.transString(beAddNum));
                }
                String explanation = "num2第" + (j+1) + "位为" + num2Values[i] +", 此时被加数为" + multiProcess.getBeAddNum()+ ", 部分积为" + multiProcess.getPartResult() + ", 计算完部分积为 " + NumberUtils.transString(tempValues);
                multiProcess.setExplanation(explanation);
                multiProcess.setProcess("\t" + multiProcess.getPartResult() + "(部分积)\n" + "+\t" + multiProcess.getBeAddNum() + "(被加数)\n" + createCutLine(multiProcess.getBeAddNum().length() + 30) + "\n\t" + NumberUtils.transString(tempValues));
                processes.add(multiProcess);
            }
        }

        Operation.MultiProcess multiProcess1 = new Operation.MultiProcess();

        multiProcess1.setProcess("计算结果：（二进制）" + resultSign + "0." + NumberUtils.transString(tempValues));
        multiProcess1.setExplanation("计算完小数部分，需要拼接上符号位和个位数部分！");
        processes.add(multiProcess1);

        DoubleBinaryNum result = new DoubleBinaryNum(resultSign + "0." + NumberUtils.transString(tempValues), resultBitLength);
        operation.setDResult(result);

        multiProcess1 = new Operation.MultiProcess();
        multiProcess1.setProcess(operation.getDNum1().getDoubleValue() + " * " + operation.getDNum2().getDoubleValue() + " = " + result.getDoubleValue());
        multiProcess1.setExplanation("说明下，纯小数的计算可能有误差~");
        processes.add(multiProcess1);

        operation.setCalculateProcess(processes);

        Log.d(result.toString());
        Log.d(operation.getDNum1().getDoubleValue() + " * " + operation.getDNum2().getDoubleValue() + " = " + result.getDoubleValue());
        return operation;
    }

    /**
     * 只做纯小数的除法，纯小数的除法，先把符号位求出来，然后让小数部分的数值数组进行做二进制除法运算即可
     * @param num1
     * @param num2
     * @param isTwoBit
     * @return
     */
    @Override
    public Operation division(DoubleBinaryNum num1, DoubleBinaryNum num2, boolean isTwoBit) {

        Log.d("num1: " + num1.toString());
        Log.d("num2: " + num2.toString());

        Operation<Operation.DivisionProcess> operation = new Operation<>();
        operation.setDNum1(num1.createNewOne());
        operation.setDNum2(num2.createNewOne());


        int resultSign = num1.getValues()[0] ^ num2.getValues()[0];//num1的符号位异或num2的符号位得到计算结果的符号位

        StringBuilder explanation = new StringBuilder();
        explanation.append("使用加减交替法进行除法运算\n将num1 和 num2 的符号位进行异或得到计算结果符号，取绝对值进行除法运算" + "\nnum1的符号位为：").append(num1.getValues()[0]).append("，num2的符号位为：").append(num2.getValues()[0]).append(" 异或结果为：").append(resultSign).append("\n");
        operation.setCalculateExplanation(explanation.toString());
        //取出小数点后面的部分，保存起来
        int[] num1Values = new int[num1.getValues().length - 1];//用于保存num1数值部分
        int[] num2Values = new int[num2.getValues().length - 1];//用于保存num2数值部分

        // 将num1,num2数值部分的数组赋值到新数组中,保留第一位作为符号位，都取大于0，所以符号位都是0
        System.arraycopy(num1.getValues(), 2, num1Values, 1, num1Values.length - 1);
        System.arraycopy(num2.getValues(), 2, num2Values, 1, num2Values.length - 1);

        int bitLength = num1.getBitLength() / 2;//记录做完除法之后商的小数点位数，要求少一半
        //消去前面的0
        Log.d("num1Values", num1Values);
        Log.d("num2Values", num2Values);

        int[] tempNum1,tempNum2;
        if (num1.getDoubleValue() > num2.getDoubleValue()){
            tempNum1 = NumberUtils.removePreZero(num1Values);
            tempNum2 = NumberUtils.removePreZero(num2Values);
        }else {
            String tempS = NumberUtils.transString(num2Values);
            int firstOneIndex = tempS.indexOf("1");//第一个1出现的位置
            tempNum2 = NumberUtils.removePreZero(num2Values);
            //把num1Values变成对应长度的数组
            tempNum1 = new int[tempNum2.length];
            System.arraycopy(num1Values, firstOneIndex, tempNum1, 0, tempNum1.length);
        }


        //需要加上符号位，所以需要拼接上符号位,都是0
        num1Values = new int[tempNum1.length + 1];
        num2Values = new int[tempNum2.length + 1];
        System.arraycopy(tempNum1, 0, num1Values, 1, tempNum1.length);
        System.arraycopy(tempNum2, 0, num2Values, 1, tempNum2.length);
        Log.d("num1Values 处理", num1Values);
        Log.d("num2Values 处理", num2Values);

        //需要处理num1Values，在后面填充 bitLength0
        num1Values = fillZeroBehind(num1Values, bitLength);

        num2Values[0] = 1;//改变符号
        int[] dNum2Values = NumberUtils.transComplementNum(num2Values);
        num2Values[0] = 0;//变回来
        Log.d("dNum2Values", dNum2Values);
        Log.d("num2Values", num2Values);
        int[] remainderValues = new int[num1Values.length];//用于保存余数
        //一开始的余数为被除数 num1(补)
        System.arraycopy(num1Values, 0 , remainderValues, 0, remainderValues.length);
        int count = num1Values.length - num2Values.length + 1;//需要计算的次数，最后需要把小数点保留在bitLength的位置
        LinkedList<Operation.DivisionProcess> processes = new LinkedList<>();
        StringBuilder resultBuilder = new StringBuilder();//保存商，把每一位商拼接起来
        Operation.DivisionProcess divisionProcess;//记录除法过程
        String p1,p2;//p1记录divisionProcess中的process，p2记录divisionProcess中的explanation
        Log.d("count :" + count);
        for (int i = 0; i < count; i ++){
            Log.d("index: " + (i+1));
            //下面，为什么是[-num2](补)需要向前填充1呢？因为 [-num2](补)一定是负号的，即第一位一定是1，根据书里的加减交替法，必须填充1实现右移动，同理，[num2](补)数组的第一位一定是0，所以要填充0
            //为什么说是“一定 dNum2Values[0] = 1, num2Values[0] = 0”因为上面已经对两个数进行了取绝对值的处理，所以，处理后的 num2 一定是正数，-num2一定是负数
            divisionProcess = new Operation.DivisionProcess();
            Log.d("remainderValues", remainderValues);
            p1 = "\t" + NumberUtils.transString(remainderValues) + "\n";
            if (remainderValues[0] == 0){
                // 余数大于0
                // + [-num2](补)
                int[] tempDNum2Values = fillOnePre(dNum2Values, i, false);//先向前填充1
                tempDNum2Values = fillZeroBehind(tempDNum2Values, remainderValues.length - tempDNum2Values.length);//再向后面填充0，使其与余数的长度相同，方便两个数组相加，这样才不会出错
                Log.d("tempDNum2Values", tempDNum2Values);
                p1 += "+\t" + NumberUtils.transString(tempDNum2Values) + " [-num2](补)\n";
                p1 += createCutLine(tempDNum2Values.length + 30) + "\n";
                remainderValues = addNotOverFlow(remainderValues, tempDNum2Values);
                p1 += "\t" + NumberUtils.transString(remainderValues);
                Log.d("new RemainderValues", remainderValues);
                Log.println();
            }else {
                // 余数小于0
                // + [num2](补)
                int[] tempNum2Values = fillZeroPre(num2Values, i, false);
                tempNum2Values = fillZeroBehind(tempNum2Values, remainderValues.length - tempNum2Values.length);//道理和上面的相同
                p1 += "+\t" + NumberUtils.transString(tempNum2Values) + " [num2](补)\n";
                p1 += createCutLine(tempNum2Values.length + 20) + "\n";
                Log.d("tempNum2Values", tempNum2Values);
                remainderValues = addNotOverFlow(remainderValues, tempNum2Values);
                p1 += "\t" + NumberUtils.transString(remainderValues);
                Log.d("new RemainderValues", remainderValues);
                Log.println();
            }

            //将商的结果拼接上去，如果余数大于0，则商拼接1，否则拼接0
            //即 当符号位为0的时候，该拼接1，否则拼接0
            p2 = "余数：" + NumberUtils.transString(remainderValues);
            if (remainderValues[0] == 0){
                resultBuilder.append(1);
                p2 += " 为正数，所以商拼接1，下次计算需要加上 [-num2](补)";
            }else {
                resultBuilder.append(0);
                p2 += " 为负数，所以商拼接0，下次计算需要加上 [num2](补)";
            }
            divisionProcess.setProcess(p1);
            divisionProcess.setExplanation(p2);
            processes.add(divisionProcess);
        }

        //处理符号位和小数点
        String resultStrTemp = resultBuilder.toString();
        Log.d("resultTemp: " + resultStrTemp);
        //拼接小数点
        String resultStr = resultSign + resultStrTemp.substring(0, resultStrTemp.length() - bitLength) + "." + resultStrTemp.substring(resultStrTemp.length() - bitLength, resultStrTemp.length());
        Log.d("result: " + resultStr);
        DoubleBinaryNum resultDoubleNum = new DoubleBinaryNum(resultStr, bitLength);

        Operation.DivisionProcess divisionProcess1 = new Operation.DivisionProcess();
        divisionProcess1.setProcess("计算结果：" + resultDoubleNum.toString() + " 余数：" + NumberUtils.transString(remainderValues));
        divisionProcess1.setExplanation("该计算结果需要拼接上符号位和小数点，然后取小数点后" + bitLength + "位");
        processes.add(divisionProcess1);

        divisionProcess1 = new Operation.DivisionProcess();
        double r = operation.getDNum1().getDoubleValue() - operation.getDNum2().getDoubleValue() * resultDoubleNum.getDoubleValue();//余数
        divisionProcess1.setProcess(operation.getDNum1().getDoubleValue() + " / " + operation.getDNum2().getDoubleValue() + " = " + resultDoubleNum.getDoubleValue() + " 余数：" + r);
        processes.add(divisionProcess1);


        Log.d(resultDoubleNum.toString());
        operation.setDResult(resultDoubleNum);
        operation.setCalculateProcess(processes);

        return operation;
    }

}
