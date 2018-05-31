package binary.base;

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
     * @param saveFirstValues 保留第一位然后进行拓展，如果是true，则保留，效果为 1101 拓展3个零 -》1 000 101 如果为false 则不保留 效果为 1101 拓展3个零 -》000 1101
     * @return
     */
    protected static int[] fillZeroPre(int[] src, int zerLength, boolean saveFirstValues){
        if (zerLength == 0){
            return src;
        }

        int[] result = new int[src.length + zerLength];
        if (saveFirstValues){
            result[0] = src[0];
            System.arraycopy(src, 1, result, zerLength + 1, src.length - 1);
        }else {
            System.arraycopy(src, 0, result, zerLength, src.length);
        }
        return result;
    }
}
