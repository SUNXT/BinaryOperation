package utils;

import com.sun.org.apache.regexp.internal.RE;

import java.util.regex.Pattern;

public class NumberUtils {

    /**
     * 判断是否为整数
     * @param str
     * @return 是整数返回true,否则返回false
     */
    public static boolean isInteger(String str) {
        if (str == null || str.equals("")){
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 十进制转二进制原码，带符号
     * @param bitLength 位数
     * @param dText
     * @return
     */
    public static String transBinNum(int bitLength, String dText){
        int num = Integer.parseInt(dText);
        String binNum = Integer.toBinaryString(Math.abs(num));
        int zeroLength = bitLength - binNum.length();//需要补零的位数
        String zeroStr = "";
        for (int i = 0; i < zeroLength; ++ i){
            zeroStr += "0";
        }

        if (num >= 0 ){
            return "0" + zeroStr + binNum;
        }
        return "1" + zeroStr + binNum;
    }

    /**
     * 十进制转二进制补码，带符号
     * @param bitLength
     * @param dText
     * @return
     */
    public static String transComplementNum(int bitLength, String dText){
        int num = Integer.parseInt(dText);
        if (num > 0){
            return transBinNum(bitLength, dText);
        }

        String binNum = transBinNum(bitLength, dText);

        //为负数，需要转换，从右往左扫描，遇到第一位1后的字节，进行反码处理
        int rightOneIndex = binNum.lastIndexOf("1");//找从右边数过来第一个1 的位置
        Log.d("the right one index is " + rightOneIndex);

        int[] values = new int[binNum.length()];
        for (int i = 0; i < values.length; ++ i ){
            values[i] = binNum.charAt(i)-48;
        }
        int i = rightOneIndex - 1;
        while (i > 0){
            if (values[i] == 1){
                values[i] = 0;
            }else {
                values[i] = 1;
            }
            i--;
        }
        StringBuilder newText = new StringBuilder();
        for (int v : values){
            newText.append(v);
        }
        return newText.toString();
    }

    public static String transString(int[] values){
        StringBuilder newText = new StringBuilder();
        for (int i : values){
            newText.append(i);
        }
        return newText.toString();
    }

    /**
     * 创造一个长度为length的0字符串
     * @param length
     * @return
     */
    public static String createZero(int length){
        if (length < 1){
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; ++ i){
            builder.append(0);
        }
        return builder.toString();
    }

}
