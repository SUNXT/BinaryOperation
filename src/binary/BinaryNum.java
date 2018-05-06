package binary;


import utils.Log;

public class BinaryNum {

    public static final int TYPE_8_BIT = 8;//8字节
    public static final int TYPE_16_BIT = 16;//16字节

    private String text;//字符串的表示形式
    private int[] values;//存储每个字节的值 每个字节的值为0或1,第一位为符号位 0表示+ 1表示-

    /**
     * 十进制数
     * @param decimalNum
     */
    public BinaryNum(int decimalNum){
        if (decimalNum >= 0){
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

    /**
     * 更新values数组
     */
    private void transValues(){
        if (text != null && text.length() > 0){
            values = new int[text.length()];
            for (int i = 0; i < values.length; ++ i ){
                values[i] = text.charAt(i)-48;
            }
        }
    }

    /**
     * 将二进制数转换为指定的字节长度 8/16
     * @param bitLength
     */
    public void transBinaryNumBitLength(int bitLength){
        if (bitLength < values.length - 1){
            Log.d("转换的字节长度小于原本的字节长度！");
            return;
        }

        int[] newValues = new int[bitLength + 1];//多出一位是符号位

        newValues[0] = values[0];//符号位不变
        System.arraycopy(values, 1, newValues, newValues.length - values.length + 1, values.length - 1);
        Log.d("before trans");
        for (int i : values){
            System.out.print(i);
        }
        Log.println();
        Log.d("after trans");
        for (int i: newValues){
            System.out.print(i);
        }
        Log.println();
        values = newValues;//替换
        updateText();//更新text
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
        int rightOneIndex = text.lastIndexOf("1");//找从右边数过来第一个1 的位置
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
     * 更新text文本
     */
    private void updateText(){
        StringBuilder newText = new StringBuilder();
        for (int i : values){
            newText.append(i);
        }
        text = newText.toString();
    }

    public int[] getValues(){
        return values;
    }

    /**
     * 获取整个字节码的长度
     * @return
     */
    public int getLength(){
        return values.length;
    }

    /**
     * 获取十进制的值
     * @return
     */
    public int getDecimalValue(){
        if (values[0] == 0){
            return Integer.valueOf( text.substring(1, text.length()),2);
        }else {
            return -Integer.valueOf( text.substring(1, text.length()),2);
        }
    }

    @Override
    public String toString() {
        return text;
    }


    public static void main(String[] args){
//        Log.d(new BinaryNum(-10).toString());
//        Log.d(new BinaryNum(10).toString());
        BinaryNum binaryNum = new BinaryNum(-120);
//        Log.d(binaryNum.toString());
        binaryNum.transBinaryNumBitLength(TYPE_8_BIT);
//        Log.d(binaryNum.toString());
//        binaryNum.transBinaryNumBitLength(TYPE_16_BIT);
//        Log.d(binaryNum.toString());
        binaryNum.transComplementNum();
        Log.d(binaryNum.toString());
    }
}
