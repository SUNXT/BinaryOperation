package binary.base;

import binary.Operation;

public interface IBinaryNumOperation<NumType> {
    Operation add(NumType num1, NumType num2, boolean isTwoBit);//加法
    Operation cut(NumType num1, NumType num2, boolean isTwoBit);//加法
    Operation multi(NumType num1, NumType num2, boolean isTwoBit);//加法
    Operation division(NumType num1, NumType num2, boolean isTwoBit);//加法
}
