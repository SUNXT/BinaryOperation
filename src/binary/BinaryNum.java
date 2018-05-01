package binary;

import utils.Log;

public class BinaryNum {

    public static final int TYPE_8_BIT = 8;//
    public static final int TYPE_16_BIT = 16;//

    private String text;//字符串的表示形式
    private int[] values;//存储每个字节的值 每个字节的值为0或1,第一位为符号位 0表示+ 1表示-

    /**
     * 十进制数
     * @param decimalNum
     */
    public BinaryNum(int decimalNum){
        if (decimalNum > 0){
            text = "0" + Integer.toBinaryString(decimalNum);
        }else {
            text = "1" + Integer.toBinaryString(-decimalNum);
        }
        transValues();
    }

    public BinaryNum(int[] values){
        this.values = values;
        StringBuilder builder = new StringBuilder();
        for (int i : values){
            builder.append(i);
        }
        text = builder.toString();
    }

    public BinaryNum(String text){
        this.text = text;
        transValues();//将文本转换为数组
    }

    private void transValues(){
        if (text != null && text.length() > 0){
            values = new int[text.length()];
            for (int i = 0; i < values.length; ++ i ){
                values[i] = text.charAt(i)-48;
            }
        }
    }

    public int[] getValues(){
        return values;
    }

    @Override
    public String toString() {
        return text;
    }


    public static void main(String[] args){
        Log.d(new BinaryNum(-10).toString());
        Log.d(new BinaryNum(10).toString());
    }
}
