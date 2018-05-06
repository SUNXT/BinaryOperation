package utils;

import java.util.HashMap;

public class AppDataUtils {

    private static HashMap<String, Object> mData = new HashMap<>();

    public static Object get(String key){
        return mData.get(key);
    }

    public static void put(String key, Object value){
        mData.put(key, value);
    }
}
