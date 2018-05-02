package binary;

import utils.Log;

public class BinaryNumOperation {

    public static void main(String[] args){
        BinaryNum num = new BinaryNum("10110111");
        BinaryNum num1 = new BinaryNum("10101011");
        num.transBinaryNumBitLength(BinaryNum.TYPE_8_BIT);
        num1.transBinaryNumBitLength(BinaryNum.TYPE_8_BIT);
        add(num, num1);
    }

    /**
     * 补码加 如果参数为负数的话，需要转为补码
     * 使用定点一位加 每次输出部分和
     * @param num1
     * @param num2
     * @return
     */
    public static BinaryNum add(BinaryNum num1, BinaryNum num2){
        Log.d("计算 " + num1.getDecimalValue() + " + " + num2.getDecimalValue());
        //取两个数的补码
        num1.transComplementNum();//转为补码
        num2.transComplementNum();//转为补码
        Log.d("num1: " + num1.toString());
        Log.d("num2: " + num2.toString());
        int[] sum = new int[num1.getLength() + 1];//声明多1位，sum为部分和
        //第一步，将num1的值赋给部分和sum
        System.arraycopy(num1.getValues(), 0, sum, 1, sum.length - 1);
        sum[0] = num1.getValues()[0];//双符号位
        Log.d("sum", sum);

        int[] num2Values = new int[num2.getLength() + 1];
        System.arraycopy(num2.getValues(), 0, num2Values, 1, num2.getLength());
        num2Values[0] = num2.getValues()[0];//双符号位
        //符号位也要加
        for (int i = num2Values.length - 1; i >= 0; i --){
            sum = add(sum, num2Values[i], i);
            Log.d("sum: " + "i: " + i + " ", sum);
        }
        //判断符号位有没有有溢出 将最高位和次高位的数进行异或，如果结果为 1，则表示 溢出，取符号位最高为作为新的符号位，如果结果为 0，表示不溢出，直接取次高位的数最为符号位

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


        Log.d("sum: ", result.getValues());
        Log.d("sum: " + result.getDecimalValue());
        return result;
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
