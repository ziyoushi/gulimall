package com.atguigu.gulimall.commons.utils;

/**
 * @author Administrator
 * @create 2019-08-05 12:35
 */
public class AppUtils {
    public static String arrayToString(String[] array,String spe){

        StringBuffer stringBuffer = new StringBuffer();
        String str = "";
        if (array != null){
            for (String s : array) {
                stringBuffer.append(s);
                stringBuffer.append(spe);
            }
            str = stringBuffer.toString().substring(0,stringBuffer.length()-1);
        }

        return str;
    }
}
