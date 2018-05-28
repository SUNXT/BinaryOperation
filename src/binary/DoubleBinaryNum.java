package binary;

import utils.Log;
import utils.NumberUtils;

/**
 * 纯小数的二进制表示
 */
public class DoubleBinaryNum {

    public static final int TYPE_8_BIT = 8;//8字节
    public static final int TYPE_16_BIT = 16;//16字节

    private int[] values;//保存二进制数据,只保存小数点后面的数字
    private String text;//二进制表示的字符串
    private int bitLength;//二进制的位数，指的是小数点后的位数

    public DoubleBinaryNum(double decimalNum, int bitLength) {
        text = NumberUtils.decimal2DoubleBinary(decimalNum, bitLength);
        this.bitLength = bitLength;
        updateValues();
    }

    /**
     * 更新数组，数组的第一位是符号位，第二位为整数位
     */
    private void updateValues(){
        if (text == null || "".equals(text)){
            System.err.println("the text is null! ");
            return;
        }
        String temp = text.replace(".", "");
        if (temp != null && temp.length() > 0){
            values = new int[temp.length()];
            for (int i = 0; i < values.length; ++ i ){
                values[i] = temp.charAt(i)-48;
            }
        }
    }

    /**
     * 根据values更新text
     */
    private void updateText() {
        if (values == null){
            return;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(values[0] );
        builder.append(values[1]);
        builder.append(".");
        for (int i = 2; i < values.length; ++ i){
            builder.append(values[i]);
        }
        text = builder.toString();
        Log.d("update text, text = " + text);
    }

    /**
     * 转换为补码
     * 正数的补码不变，负数的补码会变
     */
    public void transComplementNum(){
        //原码为正数，补码和原码一样
        if (values[0] == 0){
            return;
        }

        //为负数，需要转换，从右往左扫描，遇到第一位1后的字节，进行反码处理
        int rightOneIndex = text.replace(".", "").lastIndexOf("1");//找从右边数过来第一个1 的位置
        Log.d("the right one index is " + rightOneIndex);
        int i = rightOneIndex - 1;
        while (i > 0){
            if (values[i] == 0){
                values[i] = 1;
            }else {
                values[i] = 0;
            }
            i --;
        }
        updateText();
    }

    /**
     * 根据数据进行计算
     * @return
     */
    public double getDoubleValue(){
        double dv = 0.0d;
        double temp = 1/2d;
        for (int i = 2; i < values.length; ++i){
            if (values[i] == 1){
                dv += temp;
            }
            temp /= 2;
        }
        dv += values[1];//加上整数部分
        if (values[0] == 1){
            dv = -dv;//变符号
        }
        return dv;
    }

    public int[] getValues(){
        return values;
    }

    public int getBitLength(){
        return bitLength;
    }

    @Override
    public String toString() {
        return text;
    }

    public static void main(String[] strings){
        double n = -0.312;
        DoubleBinaryNum num = new DoubleBinaryNum(n, 16);
        Log.d(num.toString());
        num.transComplementNum();
        Log.d(num.toString());
        Log.d("d : " + num.getDoubleValue());

        num = new DoubleBinaryNum(-n, 16);
        Log.d(num.toString());
        num.transComplementNum();
        Log.d(num.toString());
        Log.d("d : " + num.getDoubleValue());
    }
}
