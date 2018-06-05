package binary.base;

import utils.Log;
import utils.NumberUtils;

public class BaseBinaryNumOperation {

    /**
     * 两个0/1数组相加
     * num1 和 num2
     * @param num1 加数数组
     * @param num2 被加数数组
     * @return 计算结果
     */
    protected static int[] add(int[] num1, int[] num2){
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
     * 两个整型数组相加，要求数组必须长度一样长，相加得到的数组，去掉最高位溢出的数
     * @param num1
     * @param num2
     * @return
     */
    protected static int[] addNotOverFlow(int[] num1, int[] num2){
        if (num1.length != num2.length){
            Log.d("num1 和 num2的长度不一致");
            return null;
        }

        int[] temp = add(num1, num2);
        int[] result = new int[temp.length - 1];
        System.arraycopy(temp, 1, result, 0, result.length);
        return result;
    }


    /**
     *
     * @param oldValues 被加数的原来数组
     * @param value 要加的值
     * @param index 要加的值的位数
     * @return
     */
    protected static int[] add(int[] oldValues, int value, int index){

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
     * 向后补零
     * 拓展数组，后面补0
     * @param src
     * @param zeroLength
     * @return
     */
    protected static int[] fillZeroBehind(int[] src, int zeroLength){
        if (zeroLength == 0)
            return src;

        int[] result = new int[src.length + zeroLength];
        System.arraycopy(src, 0, result, 0, src.length);
        return result;
    }

    /**
     * 向前补零
     * 拓展数组，前面补零
     * @param src
     * @param zerLength
     * @param saveFirstValue 保留第一位然后进行拓展，如果是true，则保留，效果为 1101 拓展3个零 -》1 000 101 如果为false 则不保留 效果为 1101 拓展3个零 -》000 1101
     * @return
     */
    protected static int[] fillZeroPre(int[] src, int zerLength, boolean saveFirstValue){
        if (zerLength == 0){
            return src;
        }

        int[] result = new int[src.length + zerLength];
        if (saveFirstValue){
            result[0] = src[0];
            System.arraycopy(src, 1, result, zerLength + 1, src.length - 1);
        }else {
            System.arraycopy(src, 0, result, zerLength, src.length);
        }
        return result;
    }

    /**
     * 向前补1
     * @param src
     * @param oneLength
     * @param saveFirstValue 是否保留第一个值，和补0一样的道理
     * @return
     */
    protected static int[] fillOnePre(int[] src, int oneLength, boolean saveFirstValue){
        if (oneLength == 0){
            return src;
        }

//        Log.d("src", src);
        int[] result = fillZeroPre(src, oneLength, saveFirstValue);//先填充0，然后再将填充的0赋值为1
        for (int i = 1; i <= oneLength; ++i){
            result[i] = 1;
        }
        if (!saveFirstValue){
            result[0] = 1;
        }
//        Log.d("result", result);
        return result;
    }

    /**
     * 数组右移
     * 假设 src：0110 move：2 fillValue：1 ———》return 1101
     * @param src 原数组
     * @param move 移动的位数
     * @param fillValue 左边填充的数值
     * @return 输出数组
     */
    protected static int[] moveRight(int[] src, int move, int fillValue){
        if (move < 1){
            return src;
        }
        int[] result = new int[src.length];
        System.arraycopy(src, 0, result, move, src.length - move);
        for (int i = 0; i < move; i ++){
            result[i] = fillValue;
        }
        Log.d("moveRight, src：" + NumberUtils.transString(src) + " move：" + move + " fillValue：" + fillValue + " result：" + NumberUtils.transString(result));
        return result;
    }

    /**
     * 数组座移
     * @param src
     * @param move
     * @param fillValue
     * @return
     */
    protected static int[] moveLeft(int[] src, int move, int fillValue){
        if (move < 1){
            return src;
        }
        int[] result = new int[src.length];
        System.arraycopy(src, move, result, 0, src.length - move);
        for (int i = src.length - move; i < src.length; i ++){
            result[i] = fillValue;
        }
        Log.d("moveLeft, src：" + NumberUtils.transString(src) + " move：" + move + " fillValue：" + fillValue + " result：" + NumberUtils.transString(result));
        return result;
    }


    public static void main(String[] args){
        int[] num = {1,0,1,0,1,1};
        moveRight(num, 2, 0);
        moveLeft(num, 2, 1);
        moveLeft(num, 3, 0);
    }

}
