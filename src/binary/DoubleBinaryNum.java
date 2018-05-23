package binary;

import utils.NumberUtils;

/**
 * 纯小数的二进制表示
 */
public class DoubleBinaryNum {

    private int[] values;//保存二进制数据,只保存小数点后面的数字
    private String text;//二进制表示的字符串
    private int bitLength;//二进制的位数，指的是小数点后的位数

    public DoubleBinaryNum(double decimalNum, int bitLength) {
        text = NumberUtils.decimal2DoubleBinary(decimalNum, bitLength);
        this.bitLength = bitLength;
    }


}
