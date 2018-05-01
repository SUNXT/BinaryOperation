package binary;

import utils.Log;

/**
 * 二进制转换类
 */
public class Trans {

    public static String decimalToBin(int num){
        return Integer.toBinaryString(num);
    }

    public static void main(String[] args){
//        Log.d(decimalToBin(65535));
//        Log.d(decimalToBin(-11));
        BinaryNum binaryNum = new BinaryNum("010101110");
        for (int i : binaryNum.getValues()){
            Log.d(i+"");
        }
        Log.d(binaryNum.toString());
    }
}
